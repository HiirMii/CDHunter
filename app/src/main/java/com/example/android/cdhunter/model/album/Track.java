package com.example.android.cdhunter.model.album;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("name")
    @Expose
    private String trackName;

    @SerializedName("duration")
    @Expose
    private String trackDuration;

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackDuration() {
        return trackDuration;
    }

    public void setTrackDuration(String trackDuration) {
        this.trackDuration = trackDuration;
    }

    public Track(String trackName, String trackDuration) {
        this.trackName = trackName;
        this.trackDuration = trackDuration;
    }

    public Track() {
    }
}
