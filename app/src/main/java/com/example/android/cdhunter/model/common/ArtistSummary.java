package com.example.android.cdhunter.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistSummary {

    @SerializedName("name")
    @Expose
    private String artistName;

    @SerializedName("image")
    @Expose
    private List<Image> image = null;

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }
}
