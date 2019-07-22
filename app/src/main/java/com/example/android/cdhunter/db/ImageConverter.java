package com.example.android.cdhunter.db;

import androidx.room.TypeConverter;

import com.example.android.cdhunter.model.common.Image;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ImageConverter {

    @TypeConverter
    public static String fromImageList(List<Image> imageList) {
        if (imageList == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Image>>() {}.getType();
        return gson.toJson(imageList, type);
    }

    @TypeConverter
    public static List<Image> toImageList(String imageListString) {
        if (imageListString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Image>>() {}.getType();
        return gson.fromJson(imageListString, type);
    }
}
