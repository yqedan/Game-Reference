package com.yusuf.gamereference.models;

import java.util.ArrayList;

public class Game {
    private String title;
    private String imageUrl;
    private ArrayList<String> platforms;
    private Integer id;

    public Game(){}
    public Game(String title, String imageUrl, ArrayList<String> platforms, Integer id){
        this.title = title;
        this.imageUrl = imageUrl;
        this.platforms = platforms;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public Integer getId() {
        return id;
    }
}
