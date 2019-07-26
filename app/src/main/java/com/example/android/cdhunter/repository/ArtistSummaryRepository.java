package com.example.android.cdhunter.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.android.cdhunter.AppExecutors;
import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.db.SimilarArtistDao;
import com.example.android.cdhunter.db.TagDao;
import com.example.android.cdhunter.model.artist.SimilarArtist;
import com.example.android.cdhunter.model.artist.Tag;
import com.example.android.cdhunter.model.chart.ChartResponse;
import com.example.android.cdhunter.model.common.ArtistSummary;
import com.example.android.cdhunter.model.search.SearchResponse;
import com.example.android.cdhunter.model.similar.SimilarResponse;
import com.example.android.cdhunter.model.tag.TagResponse;
import com.example.android.cdhunter.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

@Singleton
public class ArtistSummaryRepository {

    // similar artist call
    private List<ArtistSummary> similarArtistSummaryList = new ArrayList<>();
    private MutableLiveData<List<ArtistSummary>> similarArtistSummaryLiveData = new MutableLiveData<>();

    // artist search call
    private List<ArtistSummary> searchArtistSummaryList = new ArrayList<>();
    private MutableLiveData<List<ArtistSummary>> searchArtistSummaryLiveData = new MutableLiveData<>();

    // artist chart call
    private List<ArtistSummary> chartArtistSummaryList = new ArrayList<>();
    private MutableLiveData<List<ArtistSummary>> chartArtistSummaryLiveData = new MutableLiveData<>();

    // tag artist call
    private List<ArtistSummary> tagArtistSummaryList = new ArrayList<>();
    private MutableLiveData<List<ArtistSummary>> tagArtistSummaryLiveData = new MutableLiveData<>();

    // user interest based tag call
    private List<ArtistSummary> userInterestTagArtistSummaryList = new ArrayList<>();
    private MutableLiveData<List<ArtistSummary>> userInterestTagArtistSummaryLiveData = new MutableLiveData<>();

    private final AppExecutors appExecutors;
    private final LastFmService lastFmService;
    private final SimilarArtistDao similarArtistDao;
    private final TagDao tagDao;


    @Inject
    public ArtistSummaryRepository(AppExecutors appExecutors, LastFmService lastFmService,
                                   SimilarArtistDao similarArtistDao, TagDao tagDao) {
        this.appExecutors = appExecutors;
        this.lastFmService = lastFmService;
        this.similarArtistDao = similarArtistDao;
        this.tagDao = tagDao;
    }


    // get artists similar to what artists was user interested in
    public MutableLiveData<List<ArtistSummary>> getSimilarArtists(String userId) {
        appExecutors.diskIO().execute(() -> {
            boolean similarArtistExists = (similarArtistDao.getAllSimilarArtists(userId) != null
            && !similarArtistDao.getAllSimilarArtists(userId).isEmpty());
            if (similarArtistExists) {
                String similarArtistName = getSimilarArtistName(similarArtistDao.getAllSimilarArtists(userId));
                appExecutors.networkIO().execute(() ->
                        lastFmService.getSimilarArtists(similarArtistName, Constants.API_KEY)
                                .enqueue(new Callback<SimilarResponse>() {
                                    @Override
                                    public void onResponse(Call<SimilarResponse> call, Response<SimilarResponse> response) {
                                        SimilarResponse similarResponse = response.body();
                                        if (similarResponse != null &&
                                                similarResponse.getArtistListObject().getArtistList() != null) {
                                            similarArtistSummaryList = similarResponse.getArtistListObject().getArtistList();
                                            similarArtistSummaryLiveData.setValue(similarArtistSummaryList);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<SimilarResponse> call, Throwable t) {
                                        Timber.d(t.getCause(), "error fetching ArtistSummary data from API");
                                    }
                                }));
            }
        });

        return similarArtistSummaryLiveData;
    }

    // get artists based on users query
    public MutableLiveData<List<ArtistSummary>> getArtistsForSearchQuery(String artistName) {
        appExecutors.networkIO().execute(() ->
                lastFmService.searchArtists(artistName, Constants.API_KEY)
                        .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        SearchResponse searchResponse = response.body();
                        if (searchResponse != null && searchResponse.getSearchResults()
                                        .getArtistListObject().getArtistList() != null) {
                            searchArtistSummaryList = searchResponse.getSearchResults()
                                    .getArtistListObject().getArtistList();
                            searchArtistSummaryLiveData.setValue(searchArtistSummaryList);
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Timber.d(t.getCause(), "error fetching ArtistSummary data from API");
                    }
                }));

        return searchArtistSummaryLiveData;
    }

    // get top artists from chart
    public MutableLiveData<List<ArtistSummary>> getChartTopArtists() {
        appExecutors.networkIO().execute(() ->
                lastFmService.getChartTopArtists(Constants.API_KEY).enqueue(new Callback<ChartResponse>() {
                    @Override
                    public void onResponse(Call<ChartResponse> call, Response<ChartResponse> response) {
                        ChartResponse chartResponse = response.body();
                        if (chartResponse != null &&
                                chartResponse.getArtistListObject().getArtistList() != null) {
                            chartArtistSummaryList = chartResponse.getArtistListObject().getArtistList();
                            chartArtistSummaryLiveData.setValue(chartArtistSummaryList);
                        }
                    }

                    @Override
                    public void onFailure(Call<ChartResponse> call, Throwable t) {
                        Timber.d(t.getCause(), "error fetching ArtistSummary data from API");
                    }
                }));

        return chartArtistSummaryLiveData;
    }

    // get artists by given tag
    public MutableLiveData<List<ArtistSummary>> getTagTopArtists(String tag) {
        appExecutors.networkIO().execute(() ->
                lastFmService.getTagTopArtists(tag, Constants.API_KEY).enqueue(new Callback<TagResponse>() {
                    @Override
                    public void onResponse(Call<TagResponse> call, Response<TagResponse> response) {
                        TagResponse tagResponse = response.body();
                        if (tagResponse != null && tagResponse.getArtistListObject().getArtistList() != null) {
                            tagArtistSummaryList = tagResponse.getArtistListObject().getArtistList();
                            tagArtistSummaryLiveData.setValue(tagArtistSummaryList);
                        }
                    }

                    @Override
                    public void onFailure(Call<TagResponse> call, Throwable t) {
                        Timber.d(t.getCause(), "error fetching ArtistSummary data from API");
                    }
                }));

        return tagArtistSummaryLiveData;
    }

    // get artists by tag saved from artists user was interested in
    public MutableLiveData<List<ArtistSummary>> getUserInterestTagTopArtists(String userId) {
        appExecutors.diskIO().execute(() -> {
            boolean tagExists = (tagDao.getAllTags(userId) != null
                    && !tagDao.getAllTags(userId).isEmpty());
            if (tagExists) {
                String tagName = getTagName(tagDao.getAllTags(userId));
                appExecutors.networkIO().execute(() ->
                        lastFmService.getTagTopArtists(tagName, Constants.API_KEY).enqueue(new Callback<TagResponse>() {
                            @Override
                            public void onResponse(Call<TagResponse> call, Response<TagResponse> response) {
                                TagResponse tagResponse = response.body();
                                if (tagResponse != null && tagResponse.getArtistListObject().getArtistList() != null) {
                                    userInterestTagArtistSummaryList = tagResponse.getArtistListObject().getArtistList();
                                    userInterestTagArtistSummaryLiveData.setValue(userInterestTagArtistSummaryList);
                                }
                            }

                            @Override
                            public void onFailure(Call<TagResponse> call, Throwable t) {
                                Timber.d(t.getCause(), "error fetching ArtistSummary data from API");
                            }
                        }));
            }
        });

        return userInterestTagArtistSummaryLiveData;
    }

    // get random item from similar artist list and return similar artist name
    public String getSimilarArtistName(List<SimilarArtist> similarArtistList) {
        Random random = new Random();
        return similarArtistList.get(random.nextInt(similarArtistList.size())).getArtistName();
    }

    // get random item from tag list and return tag name
    public String getTagName(List<Tag> tagList) {
        Random random = new Random();
        return tagList.get(random.nextInt(tagList.size())).getTagName();
    }
}
