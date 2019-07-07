package com.example.android.cdhunter.model.topalbums;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopAlbumsResponse {

    @SerializedName("topalbums")
    @Expose
    private TopAlbums topAlbums;

    public TopAlbums getTopAlbums() {
        return topAlbums;
    }

    public void setTopAlbums(TopAlbums topAlbums) {
        this.topAlbums = topAlbums;
    }
}
