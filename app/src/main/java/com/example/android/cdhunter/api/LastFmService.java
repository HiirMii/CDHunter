package com.example.android.cdhunter.api;

import com.example.android.cdhunter.model.album.AlbumResponse;
import com.example.android.cdhunter.model.artist.ArtistResponse;
import com.example.android.cdhunter.model.chart.ChartResponse;
import com.example.android.cdhunter.model.search.SearchResponse;
import com.example.android.cdhunter.model.similar.SimilarResponse;
import com.example.android.cdhunter.model.tag.TagResponse;
import com.example.android.cdhunter.model.topalbums.TopAlbumsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFmService {

    @GET("?method=artist.search&format=json")
    Call<SearchResponse> searchArtists(
            @Query("artist") String artistName,
            @Query("api_key") String apiKey
    );

    @GET("?method=chart.gettopartists&format=json")
    Call<ChartResponse> getChartTopArtists(
            @Query("api_key") String apiKey
    );

    @GET("?method=tag.gettopartists&format=json")
    Call<TagResponse> getTagTopArtists(
            @Query("tag") String tag,
            @Query("api_key") String apiKey
    );

    @GET("?method=artist.getsimilar&format=json")
    Call<SimilarResponse> getSimilarArtists(
            @Query("artist") String artistName,
            @Query("api_key") String apiKey
    );

    @GET("?method=artist.getinfo&format=json")
    Call<ArtistResponse> getArtistInfo(
            @Query("artist") String artistName,
            @Query("api_key") String apiKey
    );

    @GET("?method=album.getinfo&format=json")
    Call<AlbumResponse> getAlbumInfo(
            @Query("artist") String artistName,
            @Query("album") String albumName,
            @Query("api_key") String apiKey
    );

    @GET("?method=artist.gettopalbums&format=json")
    Call<TopAlbumsResponse> getArtistTopAlbums(
            @Query("artist") String artistName,
            @Query("api_key") String apiKey
    );
}
