package com.yusuf.gamereference.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yusuf.gamereference.R;
import com.yusuf.gamereference.adapters.GameListAdapter;
import com.yusuf.gamereference.models.Game;
import com.yusuf.gamereference.services.GameService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GameSearchActivity extends AppCompatActivity {
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    public static final String TAG = GameSearchActivity.class.getSimpleName();
    private GameListAdapter mAdapter;
    public ArrayList<Game> mGames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);
        String search = getIntent().getStringExtra("search");
        setTitle(search);
        getGames(search);
    }

    private void getGames(String title){
        GameService.findGames(title, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mGames = GameService.processSearch(response);
                GameSearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new GameListAdapter(mGames);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GameSearchActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }
}
