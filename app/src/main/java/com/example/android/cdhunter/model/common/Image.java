package com.example.android.cdhunter.model.common;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {

    @SerializedName("#text")
    @Expose
    private String imageUrl;

    @SerializedName("size")
    @Expose
    private String imageSize;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public Image(String imageUrl, String imageSize) {
        this.imageUrl = imageUrl;
        this.imageSize = imageSize;
    }

    public Image() {
    }
}
