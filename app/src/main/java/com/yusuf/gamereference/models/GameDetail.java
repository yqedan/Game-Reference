package com.yusuf.gamereference.models;

import java.util.ArrayList;

public class GameDetail {
    private String title;
    private String giantBombUrl;
    private String imageUrl;
    private ArrayList<String> platforms;
    private ArrayList<String> developers;
    private ArrayList<String> publishers;
    private ArrayList<Game> similarGames;

    public GameDetail(){}
    public GameDetail(String title, String giantBombUrl, String imageUrl,
                      ArrayList<String> platforms, ArrayList<String> developers,
                      ArrayList<String> publishers, ArrayList<Game> similarGames) {
        this.title = title;
        this.giantBombUrl = giantBombUrl;
        this.imageUrl = imageUrl;
        this.platforms = platforms;
        this.developers = developers;
        this.publishers = publishers;
        this.similarGames = similarGames;
    }

    public String getGiantBombUrl() {
        return giantBombUrl;
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

    public ArrayList<String> getDevelopers() {
        return developers;
    }

    public ArrayList<String> getPublishers() {
        return publishers;
    }

    public ArrayList<Game> getSimilarGames() {
        return similarGames;
    }
}
