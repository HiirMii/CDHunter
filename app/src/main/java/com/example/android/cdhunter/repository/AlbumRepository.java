package com.example.android.cdhunter.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.android.cdhunter.AppExecutors;
import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.db.AlbumDao;
import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.model.album.AlbumResponse;
import com.example.android.cdhunter.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
public class AlbumRepository {

    private final AppExecutors appExecutors;
    private final AlbumDao albumDao;
    private final LastFmService lastFmService;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference("albums");

    private String albumSummary;

    private List<Album> albumList = new ArrayList<>();
    private Album currentAlbum;

    @Inject
    public AlbumRepository(AppExecutors appExecutors, AlbumDao albumDao,
                           LastFmService lastFmService) {
        this.appExecutors = appExecutors;
        this.albumDao = albumDao;
        this.lastFmService = lastFmService;
    }

    public LiveData<Album> getAlbum(String userId, String artistName, String albumName) {
        refreshAlbum(userId, artistName, albumName);
        return albumDao.getSingleAlbum(userId, artistName, albumName);
    }

    private void refreshAlbum(String userId, String artistName, String albumName) {
        appExecutors.diskIO().execute(() -> {
            boolean albumExists = (albumDao.checkIfAlbumExists(userId, artistName, albumName) != null);
            if (albumExists) {
                // if album exists in db I have to maintain current ownership status manually
                String ownershipStatus = albumDao.checkIfAlbumExists(userId, artistName, albumName).getOwnershipStatus();
                appExecutors.networkIO().execute(() ->
                        lastFmService.getAlbumInfo(artistName, albumName, Constants.API_KEY)
                                .enqueue(new Callback<AlbumResponse>() {
                                    @Override
                                    public void onResponse(Call<AlbumResponse> call, Response<AlbumResponse> response) {
                                        appExecutors.diskIO().execute(() -> {
                                            AlbumResponse albumResponse = response.body();
                                            assert albumResponse != null;
                                            if (albumResponse.getAlbum().getWiki() == null) {
                                                albumSummary = "No album summary available";
                                            } else {
                                                albumSummary = albumResponse.getAlbum().getWiki().getSummary();
                                            }

                                            String query = userId + "_" +
                                                    albumResponse.getAlbum().getArtistName() + "_" +
                                                    albumResponse.getAlbum().getAlbumName();

                                            Album album = new Album(
                                                    userId,
                                                    albumResponse.getAlbum().getAlbumName(),
                                                    albumResponse.getAlbum().getArtistName(),
                                                    albumResponse.getAlbum().getAlbumId(),
                                                    albumResponse.getAlbum().getListOfImages(),
                                                    albumResponse.getAlbum().getTrackList().getTrackList(),
                                                    albumSummary,
                                                    query,
                                                    ownershipStatus
                                            );
                                            albumDao.insertSingleAlbum(album);
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<AlbumResponse> call, Throwable t) {
                                        Timber.d(t.getCause(), "error fetching Album data from API");
                                    }
                                }));
            } else {
                appExecutors.networkIO().execute(() ->
                        lastFmService.getAlbumInfo(artistName, albumName, Constants.API_KEY)
                                .enqueue(new Callback<AlbumResponse>() {
                                    @Override
                                    public void onResponse(Call<AlbumResponse> call, Response<AlbumResponse> response) {
                                        appExecutors.diskIO().execute(() -> {
                                            AlbumResponse albumResponse = response.body();
                                            assert albumResponse != null;
                                            if (albumResponse.getAlbum().getWiki() == null) {
                                                albumSummary = "No album summary available";
                                            } else {
                                                albumSummary = albumResponse.getAlbum().getWiki().getSummary();
                                            }

                                            String query = userId + "_" +
                                                    albumResponse.getAlbum().getArtistName() + "_" +
                                                    albumResponse.getAlbum().getAlbumName();

                                            Album album = new Album(
                                                    userId,
                                                    albumResponse.getAlbum().getAlbumName(),
                                                    albumResponse.getAlbum().getArtistName(),
                                                    albumResponse.getAlbum().getAlbumId(),
                                                    albumResponse.getAlbum().getListOfImages(),
                                                    albumResponse.getAlbum().getTrackList().getTrackList(),
                                                    albumSummary,
                                                    query,
                                                    ""
                                            );
                                            albumDao.insertSingleAlbum(album);
                                            appExecutors.networkIO().execute(() -> {
                                                String firebaseItemId = userId + "_" + artistName + "_" + albumName;
                                                databaseReference.child(firebaseItemId).setValue(album);
                                            });
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<AlbumResponse> call, Throwable t) {
                                        Timber.d(t.getCause(), "error fetching Album data from API");
                                    }
                                }));
            }
        });
    }

    public LiveData<List<Album>> getAllAlbums(String userId, String ownershipStatus) {
        return albumDao.getAllAlbums(userId, ownershipStatus);
    }

    public void insertAllAlbums() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot album : dataSnapshot.getChildren()) {
                    currentAlbum = album.getValue(Album.class);
                    albumList.add(currentAlbum);
                }
                appExecutors.diskIO().execute(() -> {
                    albumDao.insertAllAlbums(albumList);
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateAlbumsOwnershipStatus(String userId, String artistName,
                                            String albumName, String ownershipStatus) {
        appExecutors.diskIO().execute(() -> {
            albumDao.updateSingleAlbumOwnershipStatus(userId, artistName, albumName, ownershipStatus);
            appExecutors.networkIO().execute(() -> {
                String query = userId + "_" + artistName + "_" + albumName;
                databaseReference.orderByChild("firebaseQuery").equalTo(query);
                databaseReference.child(query).child("ownershipStatus").setValue(ownershipStatus);
            });
        });
    }
}
