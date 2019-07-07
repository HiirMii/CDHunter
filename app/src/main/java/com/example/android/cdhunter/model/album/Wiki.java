package com.example.android.cdhunter.model.album;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Wiki {

    @SerializedName("summary")
    @Expose
    private String summary;

    @SerializedName("content")
    @Expose
    private String content;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
