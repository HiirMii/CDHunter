package com.example.android.cdhunter.model.artist;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tags")
public class Tag {

    @PrimaryKey(autoGenerate = true)
    private int entityId;

    private String userId;

    private String artistName;

    @SerializedName("name")
    @Expose
    private String tagName;

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

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
