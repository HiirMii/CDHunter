package com.example.android.cdhunter.repository;

import androidx.lifecycle.LiveData;

import com.example.android.cdhunter.AppExecutors;
import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.db.AlbumDao;
import com.example.android.cdhunter.model.album.Album;
import com.example.android.cdhunter.model.album.AlbumResponse;
import com.example.android.cdhunter.utils.Constants;

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


    @Inject
    public AlbumRepository(AppExecutors appExecutors, AlbumDao albumDao,
                           LastFmService lastFmService) {
        this.appExecutors = appExecutors;
        this.albumDao = albumDao;
        this.lastFmService = lastFmService;
    }

    public LiveData<Album> getAlbum(String artistName, String albumName, String userId, String albumId) {
        refreshAlbum(artistName, albumName, userId, albumId);
        return albumDao.getSingleAlbum(userId, albumId);
    }

    private void refreshAlbum(String artistName, String albumName, String userId, String albumId) {
        appExecutors.diskIO().execute(() -> {
            boolean albumExists = (albumDao.checkIfAlbumExists(userId, albumId) != null);
            if (albumExists) {
                // if album exists in db I have to maintain current ownership status manually
                String ownershipStatus = albumDao.checkIfAlbumExists(userId, albumId).getOwnershipStatus();
                appExecutors.networkIO().execute(() ->
                        lastFmService.getAlbumInfo(artistName, albumName, Constants.API_KEY)
                                .enqueue(new Callback<AlbumResponse>() {
                                    @Override
                                    public void onResponse(Call<AlbumResponse> call, Response<AlbumResponse> response) {
                                        appExecutors.diskIO().execute(() -> {
                                            AlbumResponse albumResponse = response.body();
                                            assert albumResponse != null;
                                            Album album = new Album(
                                                    userId,
                                                    albumResponse.getAlbum().getAlbumName(),
                                                    albumResponse.getAlbum().getArtistName(),
                                                    albumResponse.getAlbum().getAlbumId(),
                                                    albumResponse.getAlbum().getListOfImages(),
                                                    albumResponse.getAlbum().getTrackList().getTrackList(),
                                                    albumResponse.getAlbum().getWiki().getSummary(),
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
                                            Album album = new Album(
                                                    userId,
                                                    albumResponse.getAlbum().getAlbumName(),
                                                    albumResponse.getAlbum().getArtistName(),
                                                    albumResponse.getAlbum().getAlbumId(),
                                                    albumResponse.getAlbum().getListOfImages(),
                                                    albumResponse.getAlbum().getTrackList().getTrackList(),
                                                    albumResponse.getAlbum().getWiki().getSummary(),
                                                    ""
                                            );
                                            albumDao.insertSingleAlbum(album);
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
}
