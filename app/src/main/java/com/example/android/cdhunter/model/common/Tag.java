package com.example.android.cdhunter.model.common;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tags", primaryKeys = {"userId", "artistName", "tagName"})
public class Tag {

    @NonNull
    private String userId;

    @NonNull
    private String artistName;

    @NonNull
    @SerializedName("name")
    @Expose
    private String tagName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Tag(String userId, String artistName, String tagName) {
        this.userId = userId;
        this.artistName = artistName;
        this.tagName = tagName;
    }
}
