package com.yusuf.gamereference.services;

import com.yusuf.gamereference.Constants;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

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
}
