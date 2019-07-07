package com.example.android.cdhunter.model.topalbums;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopAlbums {

    @SerializedName("album")
    @Expose
    private List<Album> albumList = null;

    public List<Album> getAlbumList() {
        return albumList;
    }

    public void setAlbumList(List<Album> albumList) {
        this.albumList = albumList;
    }
}
