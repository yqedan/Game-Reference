package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.yusuf.gamereference.R;
import com.yusuf.gamereference.models.Game;
import com.yusuf.gamereference.models.GameDetail;
import com.yusuf.gamereference.services.GameService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GameDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = GameDetailActivity.class.getSimpleName();
    @Bind(R.id.textViewLink) TextView mTextViewLink;
    @Bind(R.id.gameDetailImage) ImageView mBoxArt;
    @Bind(R.id.gameDetailPlatforms) TextView mPlatforms;
    @Bind(R.id.gameDetailPublishers) TextView mPublishers;
    @Bind(R.id.gameDetailDevelopers) TextView mDevelopers;
    @Bind(R.id.gameDetaiSimilarGames) ListView mSimilarGamesListView;
    @Bind(R.id.addToCollectionButton) Button mAddToCollection;

    GameDetail mGame;
    private ArrayList<Game> similarGames;
    private ArrayList<String> similarTitles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String gameId = intent.getStringExtra("id");
        getGameDetails(gameId);
        mTextViewLink.setOnClickListener(this);
    }

    private void getGameDetails(String id){
        GameService.findGameDetails(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mGame = GameService.processGame(response);
                GameDetailActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mGame != null) {
                            setTitle(mGame.getTitle());
                            Picasso.with(getApplicationContext()).load(mGame.getImageUrl()).resize(400,400).centerInside().into(mBoxArt);
                            //TODO: dry this up below by using a method
                            ArrayList<String> platforms = mGame.getPlatforms();
                            String platform = "";
                            for (int i = 0; i < platforms.size() ; i++) {
                                platform += (platforms.get(i) + " ");
                            }
                            mPlatforms.setText("Platforms: " + platform);

                            ArrayList<String> developers = mGame.getDevelopers();
                            String developer = "";
                            for (int i = 0; i < developers.size() ; i++) {
                                developer += (developers.get(i) + " ");
                            }
                            mDevelopers.setText(developer);

                            ArrayList<String> publishers = mGame.getPublishers();
                            String publisher = "";
                            for (int i = 0; i < publishers.size() ; i++) {
                                publisher += (publishers.get(i) + " ");
                            }
                            mPublishers.setText(publisher);

                            similarGames = mGame.getSimilarGames();

                            for (int i = 0; i < similarGames.size() ; i++) {
                                similarTitles.add(similarGames.get(i).getTitle());
                            }

                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, similarTitles);
                            mSimilarGamesListView.setAdapter(adapter);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mTextViewLink) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGame.getGiantBombUrl())));
        }
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
        Intent intent = new Intent(GameDetailActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
