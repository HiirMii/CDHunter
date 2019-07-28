package com.example.android.cdhunter.model.toptags;

import com.example.android.cdhunter.model.common.TagList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopTagsResponse {


    @SerializedName("toptags")
    @Expose
    private TagList tagList;

    public TagList getTagList() {
        return tagList;
    }

    public void setTagList(TagList tagList) {
        this.tagList = tagList;
    }
}
