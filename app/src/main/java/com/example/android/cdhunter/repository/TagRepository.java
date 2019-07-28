package com.example.android.cdhunter.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.android.cdhunter.AppExecutors;
import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.model.common.Tag;
import com.example.android.cdhunter.model.toptags.TopTagsResponse;
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
public class TagRepository {

    // top tags call
    private List<Tag> topTagList = new ArrayList<>();
    private MutableLiveData<List<Tag>> topTagLiveData = new MutableLiveData<>();

    private final AppExecutors appExecutors;
    private final LastFmService lastFmService;

    @Inject
    public TagRepository(AppExecutors appExecutors, LastFmService lastFmService) {
        this.appExecutors = appExecutors;
        this.lastFmService = lastFmService;
    }

    public MutableLiveData<List<Tag>> getTopTags() {
        appExecutors.networkIO().execute(() ->
                lastFmService.getTopTags(Constants.API_KEY).enqueue(new Callback<TopTagsResponse>() {
                    @Override
                    public void onResponse(Call<TopTagsResponse> call, Response<TopTagsResponse> response) {
                        TopTagsResponse topTagsResponse = response.body();
                        if (topTagsResponse != null &&
                                topTagsResponse.getTagList().getTagList() != null) {
                            topTagList = topTagsResponse.getTagList().getTagList();
                            topTagLiveData.setValue(topTagList);
                        }
                    }

                    @Override
                    public void onFailure(Call<TopTagsResponse> call, Throwable t) {
                        Timber.d(t.getCause(), "error fetching Tag data from API");
                    }
                }));

        return topTagLiveData;
    }
}
