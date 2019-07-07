package com.example.android.cdhunter.model.album;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AlbumResponse {

    @SerializedName("album")
    @Expose
    private AlbumInfo albumInfo;

    public AlbumInfo getAlbumInfo() {
        return albumInfo;
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        this.albumInfo = albumInfo;
    }
}
