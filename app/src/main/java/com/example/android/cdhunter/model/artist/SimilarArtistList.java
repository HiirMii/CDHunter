package com.example.android.cdhunter.model.artist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SimilarArtistList {

    @SerializedName("artist")
    @Expose
    private List<SimilarArtist> similarArtistList = null;

    public List<SimilarArtist> getSimilarArtistList() {
        return similarArtistList;
    }

    public void setSimilarArtistList(List<SimilarArtist> similarArtistList) {
        this.similarArtistList = similarArtistList;
    }
}
