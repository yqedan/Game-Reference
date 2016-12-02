package com.yusuf.gamereference.models;

public class Game {
    private String mTitle;
    private String mImageUrl;

    public Game(String title, String imageUrl){
        this.mTitle = title;
        this.mImageUrl = imageUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

}
