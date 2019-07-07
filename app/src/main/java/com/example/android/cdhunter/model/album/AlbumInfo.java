package com.example.android.cdhunter.model.album;

import com.example.android.cdhunter.model.common.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AlbumInfo {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("artist")
    @Expose
    private String artist;

    @SerializedName("mbid")
    @Expose
    private String albumId;

    @SerializedName("image")
    @Expose
    private List<Image> image = null;

    @SerializedName("tracks")
    @Expose
    private TrackList trackList;

    @SerializedName("wiki")
    @Expose
    private Wiki wiki;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public TrackList getTrackList() {
        return trackList;
    }

    public void setTrackList(TrackList trackList) {
        this.trackList = trackList;
    }

    public Wiki getWiki() {
        return wiki;
    }

    public void setWiki(Wiki wiki) {
        this.wiki = wiki;
    }
}
