package com.example.android.cdhunter.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.android.cdhunter.AppExecutors;
import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.model.topalbums.AlbumSummary;
import com.example.android.cdhunter.model.topalbums.TopAlbumsResponse;
import com.example.android.cdhunter.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
public class AlbumSummaryRepository {

    private List<AlbumSummary> albumSummaryList = new ArrayList<>();
    private MutableLiveData<List<AlbumSummary>> albumSummaryLiveData = new MutableLiveData<>();

    private final AppExecutors appExecutors;
    private final LastFmService lastFmService;


    @Inject
    public AlbumSummaryRepository(AppExecutors appExecutors, LastFmService lastFmService) {
        this.appExecutors = appExecutors;
        this.lastFmService = lastFmService;
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
}
