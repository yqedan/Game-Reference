package com.yusuf.gamereference.services;

import com.yusuf.gamereference.Constants;
import com.yusuf.gamereference.models.Game;

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
    public static void findGames(String title, Callback callback){
        OkHttpClient client = new OkHttpClient.Builder().build();
        HttpUrl.Builder urlBuildier = HttpUrl.parse(Constants.GIANT_BOMB_SEARCH_BASE_URL).newBuilder();
        urlBuildier.addQueryParameter(Constants.API_KEY_QUERY_PARAMETER,Constants.API_KEY)
                   .addQueryParameter(Constants.SEARCH_QUERY_PARAMETER,title);
        String url = urlBuildier.build().toString();

        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public static ArrayList<Game> processSearch(Response response){
        ArrayList <Game> games = new ArrayList<>();
        try{
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject gamesJSON = new JSONObject(jsonData);
                JSONArray resultsJSON = gamesJSON.getJSONArray("results");
                for (int i = 0; i <resultsJSON.length() ; i++) {
                    JSONObject game = resultsJSON.getJSONObject(i);
                    String title = game.getString("name");
                    String imageUrl = game.getJSONObject("image").getString("super_url");
                    games.add(new Game(title,imageUrl));
                }
            }
        }catch (JSONException |IOException e){
            e.printStackTrace();
        }
        return games;
    }
}
