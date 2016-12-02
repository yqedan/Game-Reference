package com.yusuf.gamereference.models;

public class GameDetail {
    private String mTitle;
    private String mUrl;

    public GameDetail(String title, String url){
        this.mTitle = title;
        this.mUrl = url;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }
}
