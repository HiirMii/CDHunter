package com.example.android.cdhunter.model.artist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistResponse {

    @SerializedName("artist")
    @Expose
    private ArtistInfo artistInfo;

    public ArtistInfo getArtistInfo() {
        return artistInfo;
    }

    public void setArtistInfo(ArtistInfo artistInfo) {
        this.artistInfo = artistInfo;
    }
}
