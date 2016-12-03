package com.yusuf.gamereference.models;

public class GameDetail {
    private String mTitle;
    private String mUrl;
    private String mImageUrl;

    public GameDetail(String title, String url, String imageUrl){
        this.mTitle = title;
        this.mUrl = url;
        this.mImageUrl = imageUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
