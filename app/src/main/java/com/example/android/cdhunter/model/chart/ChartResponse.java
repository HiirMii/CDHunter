package com.example.android.cdhunter.model.chart;

import com.example.android.cdhunter.model.common.ArtistList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChartResponse {

    @SerializedName("artists")
    @Expose
    private ArtistList artistList;

    public ArtistList getArtistListObject() {
        return artistList;
    }

    public void setArtistListObject(ArtistList artistList) {
        this.artistList = artistList;
    }
}
