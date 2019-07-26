package com.example.android.cdhunter.model.topalbums;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopAlbums {

    @SerializedName("album")
    @Expose
    private List<AlbumSummary> albumList = null;

    public List<AlbumSummary> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<AlbumSummary> albumList) {
        this.albumList = albumList;
    }
}
