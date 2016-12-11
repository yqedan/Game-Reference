package com.yusuf.gamereference.models;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class GameDetail {
    private String title;
    private String giantBombUrl;
    private String imageUrl;
    private List<String> platforms;
    private List<String> developers;
    private List<String> publishers;
    private List<Game> similarGames;
    private Integer id;
    private String pushId;

    public GameDetail(){}

    public GameDetail(String title, String giantBombUrl, String imageUrl,
                      ArrayList<String> platforms, ArrayList<String> developers,
                      ArrayList<String> publishers, ArrayList<Game> similarGames,
                      Integer id) {
        this.title = title;
        this.giantBombUrl = giantBombUrl;
        this.imageUrl = imageUrl;
        this.platforms = platforms;
        this.developers = developers;
        this.publishers = publishers;
        this.similarGames = similarGames;
        this.id = id;
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

    public List<String> getPlatforms() {
        return platforms;
    }

    public List<String> getDevelopers() {
        return developers;
    }

    public List<String> getPublishers() {
        return publishers;
    }

    public List<Game> getSimilarGames() {
        return similarGames;
    }

    public Integer getId() {
        return id;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
