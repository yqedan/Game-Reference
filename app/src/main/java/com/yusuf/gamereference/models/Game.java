package com.yusuf.gamereference.models;

import java.util.ArrayList;

public class Game {
    private String mTitle;
    private String mImageUrl;
    private ArrayList<String> mPlatforms;

    public Game(String title, String imageUrl, ArrayList<String> platforms){
        this.mTitle = title;
        this.mImageUrl = imageUrl;
        this.mPlatforms = platforms;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public ArrayList<String> getPlatforms() {
        return mPlatforms;
    }

}
