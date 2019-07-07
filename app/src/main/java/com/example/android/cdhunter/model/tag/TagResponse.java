package com.example.android.cdhunter.model.tag;

import com.example.android.cdhunter.model.common.ArtistList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TagResponse {

    @SerializedName("topartists")
    @Expose
    private ArtistList artistList;

    public ArtistList getArtistListObject() {
        return artistList;
    }

    public void setArtistListObject(ArtistList artistList) {
        this.artistList = artistList;
    }
}
