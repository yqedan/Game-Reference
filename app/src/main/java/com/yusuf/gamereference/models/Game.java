package com.yusuf.gamereference.models;

import java.util.ArrayList;

public class Game {
    private String mTitle;
    private String mImageUrl;
    private ArrayList<String> mPlatforms;
    private Integer mId;

    public Game(String title, String imageUrl, ArrayList<String> platforms, Integer id){
        this.mTitle = title;
        this.mImageUrl = imageUrl;
        this.mPlatforms = platforms;
        this.mId = id;
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

    public Integer getId() {
        return mId;
    }
}
