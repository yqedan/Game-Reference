package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.yusuf.gamereference.Constants;
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

public class GameDetailActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemClickListener{
    private static final String TAG = GameDetailActivity.class.getSimpleName();
    @Bind(R.id.gameDetailImage) ImageView mBoxArt;
    @Bind(R.id.gameDetailPlatforms) TextView mPlatforms;
    @Bind(R.id.gameDetailPublishers) TextView mPublishers;
    @Bind(R.id.gameDetailDevelopers) TextView mDevelopers;
    @Bind(R.id.gameDetaiSimilarGames) ListView mSimilarGamesListView;
    @Bind(R.id.addToCollectionButton) Button mAddToCollection;
    @Bind(R.id.giantbombLink) Button mTextViewLink;

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
        mAddToCollection.setOnClickListener(this);
        mSimilarGamesListView.setOnItemClickListener(this);
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
                            ArrayList<String> platforms = (ArrayList<String>) mGame.getPlatforms();
                            String platform = "";
                            for (int i = 0; i < platforms.size() ; i++) {
                                if (i+1 == platforms.size())
                                    platform += (platforms.get(i));
                                else
                                    platform += (platforms.get(i) + ", ");
                            }
                            if (platforms.size() == 0) {
                                mPlatforms.setText("Platforms: (none)");
                            }else if (platforms.size() == 1){
                                mPlatforms.setText("Platform: " + platform);
                            }else{
                                mPlatforms.setText("Platforms: " + platform);
                            }
                            ArrayList<String> developers = (ArrayList<String>) mGame.getDevelopers();
                            String developer = "";
                            for (int i = 0; i < developers.size() ; i++) {
                                if (i+1 == developers.size())
                                    developer += (developers.get(i));
                                else
                                    developer += (developers.get(i) + ", ");
                            }
                            if (developers.size() == 0) {
                                mDevelopers.setText("Developers: (none)");
                            }else if (developers.size() == 1){
                                mDevelopers.setText("Developer: " + developer);
                            }else{
                                mDevelopers.setText("Developers: " + developer);
                            }
                            ArrayList<String> publishers = (ArrayList<String>) mGame.getPublishers();
                            String publisher = "";
                            for (int i = 0; i < publishers.size() ; i++) {
                                if (i+1 == publishers.size())
                                    publisher += (publishers.get(i));
                                else
                                    publisher += (publishers.get(i) + ", ");
                            }
                            if (publishers.size() == 0) {
                                mPublishers.setText("Publishers: (none)");
                            }else if (publishers.size() == 1){
                                mPublishers.setText("Publisher: " + publisher);
                            }else{
                                mPublishers.setText("Publishers: " + publisher);
                            }
                            //TODO: add logic to tell user no similar games were found
                            similarGames = (ArrayList<Game>) mGame.getSimilarGames();
                            similarTitles.removeAll(similarTitles); //Just to be sure!
                            for (int i = 0; i < similarGames.size() ; i++) {
                                similarTitles.add(similarGames.get(i).getTitle());
                            }
                            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, similarTitles){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    TextView textView = (TextView) super.getView(position, convertView, parent);
                                    textView.setTextColor(Color.parseColor("#000000"));
                                    return textView;
                                }
                            };
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
        if (v == mAddToCollection) {
            DatabaseReference gameRef = FirebaseDatabase
                    .getInstance()
                    .getReference(Constants.FIREBASE_CHILD_GAMES);
            gameRef.push().setValue(mGame);
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Integer gameId = similarGames.get(position).getId();
        if (gameId != -1) {
            //one game was set to -1 for the message "No Games" when no similar games were found
            Intent intent = new Intent(GameDetailActivity.this, GameDetailActivity.class);
            //TODO limit the amount times this can be done before we run out of memory!
            intent.putExtra("id",gameId.toString());
            startActivity(intent);
        }
    }
}
