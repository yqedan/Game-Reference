package com.yusuf.gamereference.models;

public class Game {
    private String mTitle;
    private String mImageUrl;
    private String mPlatform;

    public Game(String title, String imageUrl, String platform){
        this.mTitle = title;
        this.mImageUrl = imageUrl;
        this.mPlatform = platform;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getPlatform() {
        return mPlatform;
    }

}
