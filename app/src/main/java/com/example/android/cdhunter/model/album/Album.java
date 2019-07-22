package com.example.android.cdhunter.model.album;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.android.cdhunter.db.ImageConverter;
import com.example.android.cdhunter.db.TrackConverter;
import com.example.android.cdhunter.model.common.Image;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "albums")
public class Album {

    @PrimaryKey(autoGenerate = true)
    private int entityId;

    private String userId;

    @SerializedName("name")
    @Expose
    private String albumName;

    @SerializedName("artist")
    @Expose
    private String artistName;

    @SerializedName("mbid")
    @Expose
    private String albumId;

    @SerializedName("image")
    @Expose
    @TypeConverters(ImageConverter.class)
    private List<Image> listOfImages = null;

    @TypeConverters(TrackConverter.class)
    private List<Track> listOfTracks = null;

    @Ignore
    @SerializedName("tracks")
    @Expose
    private TrackList trackList;

    private String albumSummary;

    @Ignore
    @SerializedName("wiki")
    @Expose
    private Wiki wiki;

    private String ownershipStatus;

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

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public List<Image> getListOfImages() {
        return listOfImages;
    }

    public void setListOfImages(List<Image> listOfImages) {
        this.listOfImages = listOfImages;
    }

    public List<Track> getListOfTracks() {
        return listOfTracks;
    }

    public void setListOfTracks(List<Track> listOfTracks) {
        this.listOfTracks = listOfTracks;
    }

    public TrackList getTrackList() {
        return trackList;
    }

    public void setTrackList(TrackList trackList) {
        this.trackList = trackList;
    }

    public String getAlbumSummary() {
        return albumSummary;
    }

    public void setAlbumSummary(String albumSummary) {
        this.albumSummary = albumSummary;
    }

    public Wiki getWiki() {
        return wiki;
    }

    public void setWiki(Wiki wiki) {
        this.wiki = wiki;
    }

    public String getOwnershipStatus() {
        return ownershipStatus;
    }

    public void setOwnershipStatus(String ownershipStatus) {
        this.ownershipStatus = ownershipStatus;
    }

    public Album(String userId, String albumName, String artistName, String albumId, List<Image> listOfImages,
                 List<Track> listOfTracks, String albumSummary, String ownershipStatus) {
        this.userId = userId;
        this.albumName = albumName;
        this.artistName = artistName;
        this.albumId = albumId;
        this.listOfImages = listOfImages;
        this.listOfTracks = listOfTracks;
        this.albumSummary = albumSummary;
        this.ownershipStatus = ownershipStatus;
    }
}
