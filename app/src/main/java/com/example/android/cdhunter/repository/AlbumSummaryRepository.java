package com.example.android.cdhunter.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.android.cdhunter.AppExecutors;
import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.db.SimilarArtistDao;
import com.example.android.cdhunter.model.artist.SimilarArtist;
import com.example.android.cdhunter.model.topalbums.AlbumSummary;
import com.example.android.cdhunter.model.topalbums.TopAlbumsResponse;
import com.example.android.cdhunter.utils.Constants;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
public class AlbumSummaryRepository {

    // artist top albums call
    private List<AlbumSummary> albumSummaryList = new ArrayList<>();
    private MutableLiveData<List<AlbumSummary>> albumSummaryLiveData = new MutableLiveData<>();

    // user interest based artist top albums call
    private List<AlbumSummary> userInterestAlbumSummaryList = new ArrayList<>();
    private MutableLiveData<List<AlbumSummary>> userInterestAlbumSummaryLiveData = new MutableLiveData<>();

    private final AppExecutors appExecutors;
    private final LastFmService lastFmService;
    private SimilarArtistDao similarArtistDao;


    @Inject
    public AlbumSummaryRepository(AppExecutors appExecutors, LastFmService lastFmService,
                                  SimilarArtistDao similarArtistDao) {
        this.appExecutors = appExecutors;
        this.lastFmService = lastFmService;
        this.similarArtistDao = similarArtistDao;
    }

    public MutableLiveData<List<AlbumSummary>> getArtistTopAlbums(String artistName) {
        appExecutors.networkIO().execute(() ->
                lastFmService.getArtistTopAlbums(artistName, Constants.API_KEY)
                        .enqueue(new Callback<TopAlbumsResponse>() {
            @Override
            public void onResponse(Call<TopAlbumsResponse> call, Response<TopAlbumsResponse> response) {
                TopAlbumsResponse topAlbumsResponse = response.body();
                if (topAlbumsResponse != null &&
                        topAlbumsResponse.getTopAlbums().getAlbumList() != null) {
                    albumSummaryList = topAlbumsResponse.getTopAlbums().getAlbumList();
                    removeNullAlbums(albumSummaryList);
                    albumSummaryLiveData.setValue(albumSummaryList);
                }
            }

            @Override
            public void onFailure(Call<TopAlbumsResponse> call, Throwable t) {
                Timber.d(t.getCause(), "error fetching AlbumSummary data from API");
            }
        }));

        return albumSummaryLiveData;
    }

    public MutableLiveData<List<AlbumSummary>> getUserInterestArtistTopAlbums(String userId) {
        appExecutors.diskIO().execute(() -> {
            boolean similarArtistExists = (similarArtistDao.getAllSimilarArtists(userId) != null
                    && !similarArtistDao.getAllSimilarArtists(userId).isEmpty());
            if (similarArtistExists) {
                String similarArtistName = getSimilarArtistName(similarArtistDao.getAllSimilarArtists(userId));
                appExecutors.networkIO().execute(() -> {
                    lastFmService.getArtistTopAlbums(similarArtistName, Constants.API_KEY)
                            .enqueue(new Callback<TopAlbumsResponse>() {
                                @Override
                                public void onResponse(Call<TopAlbumsResponse> call, Response<TopAlbumsResponse> response) {
                                    TopAlbumsResponse topAlbumsResponse = response.body();
                                    if (topAlbumsResponse != null &&
                                            topAlbumsResponse.getTopAlbums().getAlbumList() != null) {
                                        userInterestAlbumSummaryList =
                                                topAlbumsResponse.getTopAlbums().getAlbumList();
                                        removeNullAlbums(userInterestAlbumSummaryList);
                                        userInterestAlbumSummaryLiveData.setValue(userInterestAlbumSummaryList);
                                    }
                                }

                                @Override
                                public void onFailure(Call<TopAlbumsResponse> call, Throwable t) {

                                }
                            });
                });
            }
        });

        return userInterestAlbumSummaryLiveData;
    }

    // get random item from similar artist list and return similar artist name
    public String getSimilarArtistName(List<SimilarArtist> similarArtistList) {
        Random random = new Random();
        return similarArtistList.get(random.nextInt(similarArtistList.size())).getSimilarArtistName();
    }

    // remove albums with (null) name
    public void removeNullAlbums(List<AlbumSummary> albumSummaryList) {
        Iterator<AlbumSummary> iterator = albumSummaryList.iterator();
        while (iterator.hasNext()) {
            AlbumSummary albumSummary = iterator.next();
            if (albumSummary.getName().equals("(null)")) {
                iterator.remove();
                break;
            }
        }
    }
}
