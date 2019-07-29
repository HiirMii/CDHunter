package com.example.android.cdhunter.model.artist;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "similar_artists", primaryKeys = {"userId", "artistName", "similarArtistName"})
public class SimilarArtist {

    @NonNull
    private String userId;

    @NonNull
    private String artistName;

    @NonNull
    @SerializedName("name")
    @Expose
    private String similarArtistName;

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

    public String getSimilarArtistName() {
        return similarArtistName;
    }

    public void setSimilarArtistName(String similarArtistName) {
        this.similarArtistName = similarArtistName;
    }

    public SimilarArtist(String userId, String artistName, String similarArtistName) {
        this.userId = userId;
        this.artistName = artistName;
        this.similarArtistName = similarArtistName;
    }
}
