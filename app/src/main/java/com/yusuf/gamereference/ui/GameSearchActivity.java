package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

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
    public static final String TAG = GameSearchActivity.class.getSimpleName();
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    final LinearLayoutManager layoutManager = new LinearLayoutManager(GameSearchActivity.this);
    private GameListAdapter mAdapter;
    @Bind(R.id.textView) TextView mTextView;
    public ArrayList<Game> mGames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);
        String search = getIntent().getStringExtra("search");
        mTextView.setText("You searched for " + search);
        getGames(search);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    Log.d(TAG, "End is reached!");
                    ArrayList <String> platforms = new ArrayList<>();
                    platforms.add("N64");
                    mGames.add(new Game("","I scrolled to the bottom!",platforms,0));
                }
            }
            @Override
            public void onScrollStateChanged (RecyclerView recyclerView, int newState){
                mAdapter.notifyDataSetChanged();
            }
        });
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
                        mAdapter = new GameListAdapter(getApplicationContext(), mGames);
                        mRecyclerView.setAdapter(mAdapter);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }
                });
            }
        });
    }
}
