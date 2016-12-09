package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
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
    private static final String TAG = GameSearchActivity.class.getSimpleName();
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private final LinearLayoutManager layoutManager = new LinearLayoutManager(GameSearchActivity.this);
    private GameListAdapter mAdapter;
    private ArrayList<Game> mGames = new ArrayList<>();
    private int numberOfPages; //TODO get page number from API to prevent the 1 extra not necessary call
    private int page = 1;
    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount;
    private int previousTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);
        final String search = getIntent().getStringExtra("search");
        setTitle(search);
        //TODO add loading progress dialog
        getGames(search);

        mAdapter = new GameListAdapter(getApplicationContext(), mGames);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if ( !loading && (pastVisibleItems + visibleItemCount >= totalItemCount) ) {
                    //TODO add loading progress dialog and prevent extraneous api call if there are no remaining pages
                    Log.d(TAG, "End is reached!");
                    getGames(search);
                    loading = true;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Log.d(TAG, "logging out");
                logout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(GameSearchActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void getGames(String title){
        GameService.findGames(title, page, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //TODO detect if the are no results on first page search
                mGames.addAll(GameService.processSearch(response));
                GameSearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        //TODO remove loading progress dialog
                    }
                });
            }
        });
        page++;
    }
}
