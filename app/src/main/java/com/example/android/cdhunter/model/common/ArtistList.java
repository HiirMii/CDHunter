package com.example.android.cdhunter.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistList {

    @SerializedName("artist")
    @Expose
    private List<ArtistSummary> artistList = null;

    public List<ArtistSummary> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<ArtistSummary> artistList) {
        this.artistList = artistList;
    }
}
