package com.yusuf.gamereference.services;

import com.yusuf.gamereference.Constants;
import com.yusuf.gamereference.models.Game;
import com.yusuf.gamereference.models.GameDetail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GameService {
    private static int numberOfPages;

    public static int getNumberOfPages() {
        return numberOfPages;
    }

    public static void findGames(String title, int page, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GIANT_BOMB_SEARCH_BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.API_KEY_QUERY_PARAMETER,Constants.API_KEY)
                .addQueryParameter(Constants.SEARCH_PAGE_QUERY_PARAMETER,page + "")
                .addQueryParameter(Constants.SEARCH_QUERY_PARAMETER,title);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static void findGameDetails(String id, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.GIANT_BOMB_GAME_DETAILS_BASE_URL).newBuilder();
        urlBuilder.addPathSegment(id);
        urlBuilder.addQueryParameter(Constants.API_KEY_QUERY_PARAMETER,Constants.API_KEY);
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public static ArrayList<Game> processSearch(Response response){
        ArrayList <Game> games = null;
        try{
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject gamesJSON = new JSONObject(jsonData);
                int numberOfResults = gamesJSON.getInt("number_of_total_results");
                numberOfPages = numberOfResults / 10;
                if (numberOfResults % 10 > 0) numberOfPages++;
                if (numberOfPages > 0) games = new ArrayList<>();
                else return games;
                JSONArray resultsJSON = gamesJSON.getJSONArray("results");
                for (int i = 0; i <resultsJSON.length() ; i++) {
                    JSONObject game = resultsJSON.getJSONObject(i);
                    String title = game.getString("name");
                    Integer id = game.getInt("id");
                    String imageUrl = game.getJSONObject("image").getString("super_url");
                    JSONArray platformsJSON = game.getJSONArray("platforms");
                    ArrayList<String> platforms = new ArrayList<>();
                    for (int j = 0; j < platformsJSON.length(); j++) {
                        platforms.add(platformsJSON.getJSONObject(j).getString("abbreviation"));
                    }
                    games.add(new Game(title, imageUrl, platforms, id));
                }
            }
        }catch (JSONException |IOException e){
            e.printStackTrace();
        }
        return games;
    }

    public static GameDetail processGame(Response response){
        GameDetail game = null;
        try{
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject gameJSON = new JSONObject(jsonData);
                JSONObject resultJSON = gameJSON.getJSONObject("results");
                String title = resultJSON.getString("name");
                String imageUrl = resultJSON.getJSONObject("image").getString("super_url");
                String url = resultJSON.getString("site_detail_url");
                Integer id = resultJSON.getInt("id");

                JSONArray platformsJSON = null;
                try{
                    platformsJSON = resultJSON.getJSONArray("platforms");
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                ArrayList<String> platforms = new ArrayList<>();
                if (platformsJSON != null) {
                    for (int j = 0; j < platformsJSON.length(); j++) {
                        platforms.add(platformsJSON.getJSONObject(j).getString("abbreviation"));
                    }
                }
                else platforms.add("None Listed");

                JSONArray developersJSON = null;
                try{
                    developersJSON = resultJSON.getJSONArray("developers");
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                ArrayList<String> developers = new ArrayList<>();
                if (developersJSON != null) {
                    for (int j = 0; j < developersJSON.length(); j++) {
                        developers.add(developersJSON.getJSONObject(j).getString("name"));
                    }
                }
                else developers.add("None Listed");

                JSONArray publishersJSON = null;
                try{
                    publishersJSON = resultJSON.getJSONArray("publishers");
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                ArrayList<String> publishers = new ArrayList<>();
                if (publishersJSON != null) {
                    for (int j = 0; j < publishersJSON.length(); j++) {
                        publishers.add(publishersJSON.getJSONObject(j).getString("name"));
                    }
                }
                else publishers.add("None Listed");


                JSONArray similarGamesJSON = null;
                try{
                    similarGamesJSON = resultJSON.getJSONArray("similar_games");}
                catch(JSONException e){
                    e.printStackTrace();
                }
                ArrayList<Game> similarGames = new ArrayList<>();
                if (similarGamesJSON != null) {
                    for (int j = 0; j < similarGamesJSON.length(); j++) {
                        JSONObject similarGame = similarGamesJSON.getJSONObject(j);
                        String similarTitle = similarGame.getString("name");
                        Integer similarId = similarGame.getInt("id");
                        similarGames.add(new Game(similarTitle, null, null, similarId));
                    }
                }
                else similarGames.add(new Game("No Games", null, null, -1));

                game = new GameDetail(title,url, imageUrl, platforms, developers, publishers, similarGames, id);
            }
        }catch (JSONException |IOException e){
            e.printStackTrace();
        }
        return game;
    }
}
