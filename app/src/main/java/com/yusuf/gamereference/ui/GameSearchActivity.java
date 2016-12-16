package com.yusuf.gamereference.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.yusuf.gamereference.Constants;
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

    //fields used for implementing an "infinite" scroll
    private int numberOfPages = 1;
    private int page = 1;
    private int numberOfCurrentResults = 0;
    private boolean loading = true;
    private int pastVisibleItems, visibleItemCount, totalItemCount; //Used to determine if we are at the bottom of our current list
    private int previousTotal = 0;

    //fields used for searching and progress dialogs
    private static String search;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private ProgressDialog mLoadingProgressDialog;
    private String mRecentSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mRecentSearch = mSharedPreferences.getString(Constants.PREFERENCES_SEARCH_KEY, null);
        if (mRecentSearch != null) {
            search = mRecentSearch;
        }
        createLoadingProgressDialog();
        setTitle(search);
        getGames(search);

        mAdapter = new GameListAdapter(mGames);
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

                if ( (loading && page > 2) || (loading && numberOfCurrentResults == 10) ){
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }

                if ( !loading && (pastVisibleItems + visibleItemCount >= totalItemCount)) {
                    if (numberOfCurrentResults == 10 && page <= numberOfPages) {
                        Log.d(TAG, "End is reached! page " + page + " loading");
                        getGames(search);
                        loading = true;
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "No more results", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_logout,menu);
        ButterKnife.bind(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //TODO: consider starting another instance of game search activity instead of resetting all these values
                addToSharedPreferences(query);
                page = 1;
                setTitle(query);
                mGames.removeAll(mGames);
                numberOfPages = 1;
                mAdapter.notifyDataSetChanged();
                getGames(query);
                loading = true;
                previousTotal = 0;
                search = query;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
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

    private void addToSharedPreferences(String search) {
        mEditor.putString(Constants.PREFERENCES_SEARCH_KEY, search).apply();
    }

    private void createLoadingProgressDialog(){
        mLoadingProgressDialog = new ProgressDialog(this);
        mLoadingProgressDialog.setTitle("Loading...");
        mLoadingProgressDialog.setMessage("Searching for results");
        mLoadingProgressDialog.setCancelable(false);
    }

    private void getGames(String title){
        //TODO cache search
        mLoadingProgressDialog.show();
        GameService.findGames(title, page, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoadingProgressDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final ArrayList<Game> temp = GameService.processSearch(response);
                if (temp == null) {
                    numberOfCurrentResults = 0;
                }else{
                    numberOfCurrentResults = temp.size();
                }
                numberOfPages = GameService.getNumberOfPages();
                GameSearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (temp == null) {
                            Toast.makeText(GameSearchActivity.this, "No Results found", Toast.LENGTH_SHORT).show();
                        }else{
                            mGames.addAll(temp);
                        }
                        mAdapter.notifyDataSetChanged();
                        mLoadingProgressDialog.dismiss();
                        page++;
                    }
                });
            }
        });
    }
}
