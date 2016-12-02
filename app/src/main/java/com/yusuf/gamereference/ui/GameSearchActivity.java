package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yusuf.gamereference.R;
import com.yusuf.gamereference.models.Game;
import com.yusuf.gamereference.services.GameService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GameSearchActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public static final String TAG = GameSearchActivity.class.getSimpleName();
    @Bind(R.id.listView) ListView mListView;
    @Bind(R.id.textView) TextView mTextView;
    public ArrayList<Game> mGames = new ArrayList<>();
    public ArrayAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String search = intent.getStringExtra("search");
        Log.d(TAG,search);
        mTextView.setText("You searched for " + search);
        mListView.setOnItemClickListener(this);
        getGames(search);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String game = ((TextView)view).getText().toString();
        Intent intent = new Intent(GameSearchActivity.this, GameDetailActivity.class);
        intent.putExtra("game",game);
        startActivity(intent);
    }

    private void getGames(String title){
        GameService.findGames(title, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //String jsonData = response.body().string();
                //Log.d(TAG,jsonData);
                mGames = GameService.processSearch(response);
                GameSearchActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] gameTitles = new String[mGames.size()];
                        for (int i = 0; i < gameTitles.length; i++) {
                            gameTitles[i] = mGames.get(i).getTitle();
                        }
                        Integer x = mGames.size();
                        Log.d(TAG,x.toString());
                        adapter = new ArrayAdapter(getApplication(), android.R.layout.simple_list_item_1, gameTitles){
                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                TextView textView = (TextView) super.getView(position, convertView, parent);
                                textView.setTextColor(Color.parseColor("#000000"));
                                return textView;
                            }
                        };
                        mListView.setAdapter(adapter);
                    }
                });
            }
        });
    }
}
