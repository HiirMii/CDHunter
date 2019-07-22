package com.example.android.cdhunter.db;

import androidx.room.TypeConverter;

import com.example.android.cdhunter.model.album.Track;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TrackConverter {

    @TypeConverter
    public static String fromTrackList(List<Track> trackList) {
        if (trackList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Track>>() {}.getType();
        return gson.toJson(trackList, type);
    }

    @TypeConverter
    public static List<Track> toTrackList(String trackListString) {
        if (trackListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Track>>() {}.getType();
        return gson.fromJson(trackListString, type);
    }
}
