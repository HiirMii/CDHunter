package com.example.android.cdhunter.model.artist;

import com.example.android.cdhunter.model.common.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArtistInfo {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("image")
    @Expose
    private List<Image> images = null;

    @SerializedName("similar")
    @Expose
    private SimilarArtistList similarArtistList;

    @SerializedName("tags")
    @Expose
    private TagList tagList;

    @SerializedName("bio")
    @Expose
    private Bio bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Image> getImageList() {
        return images;
    }

    public void setImageList(List<Image> image) {
        this.images = image;
    }

    public SimilarArtistList getSimilarArtistListObject() {
        return similarArtistList;
    }

    public void setSimilarArtistListObject(SimilarArtistList similarArtistList) {
        this.similarArtistList = similarArtistList;
    }

    public TagList getTagListObject() {
        return tagList;
    }

    public void setTagListObject(TagList tagList) {
        this.tagList = tagList;
    }

    public Bio getBio() {
        return bio;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }
}
