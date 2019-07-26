package com.example.android.cdhunter.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.android.cdhunter.AppExecutors;
import com.example.android.cdhunter.api.LastFmService;
import com.example.android.cdhunter.db.SimilarArtistDao;
import com.example.android.cdhunter.db.TagDao;
import com.example.android.cdhunter.model.artist.Artist;
import com.example.android.cdhunter.model.artist.ArtistResponse;
import com.example.android.cdhunter.model.artist.SimilarArtist;
import com.example.android.cdhunter.model.artist.Tag;
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
public class ArtistRepository {

    private Artist artist = new Artist();
    private MutableLiveData<Artist> artistLiveData = new MutableLiveData<>();


    private List<SimilarArtist> similarArtistList = new ArrayList<>();
    private List<Tag> tagList = new ArrayList<>();

    private final AppExecutors appExecutors;
    private final LastFmService lastFmService;
    private final SimilarArtistDao similarArtistDao;
    private final TagDao tagDao;

    @Inject
    public ArtistRepository(AppExecutors appExecutors, LastFmService lastFmService,
                            SimilarArtistDao similarArtistDao, TagDao tagDao) {
        this.appExecutors = appExecutors;
        this.lastFmService = lastFmService;
        this.similarArtistDao = similarArtistDao;
        this.tagDao = tagDao;
    }

    public MutableLiveData<Artist> getArtist(String artistName, String userId) {
        appExecutors.networkIO().execute(() ->
                lastFmService.getArtistInfo(artistName, Constants.API_KEY).enqueue(new Callback<ArtistResponse>() {
                    @Override
                    public void onResponse(Call<ArtistResponse> call, Response<ArtistResponse> response) {
                        ArtistResponse artistResponse = response.body();
                        if (artistResponse != null && artistResponse.getArtist() != null) {
                            artist = artistResponse.getArtist();
                            artistLiveData.setValue(artist);
                            appExecutors.diskIO().execute(() -> {
                                similarArtistList = artist.getSimilarArtistListObject().getSimilarArtistList();
                                addUserIdAndArtistNameToSimilarArtist(userId, artistName, similarArtistList);
                                similarArtistDao.insertAllSimilarArtists(similarArtistList);

                                tagList = artist.getTagListObject().getTagList();
                                addUserIdToTag(userId, tagList);
                                tagDao.insertAllTags(tagList);
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<ArtistResponse> call, Throwable t) {
                        Timber.d(t.getCause(), "error fetching Artist data from API");
                    }
                }));

        return artistLiveData;
    }

    // add userId to every SimilarArtist object
    public void addUserIdAndArtistNameToSimilarArtist(String userId,String artistName, List<SimilarArtist> similarArtistList) {
        for (int i = 0; i < similarArtistList.size(); i++) {
            similarArtistList.get(i).setArtistName(artistName);
            similarArtistList.get(i).setUserId(userId);
        }
    }

    public void addUserIdToTag(String userId, List<Tag> tagList) {
        for (int i = 0; i < tagList.size(); i++) {
            tagList.get(i).setUserId(userId);
        }
    }
}
