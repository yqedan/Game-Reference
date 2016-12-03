package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yusuf.gamereference.R;
import com.yusuf.gamereference.models.GameDetail;
import com.yusuf.gamereference.services.GameService;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GameDetailActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.textViewTitle) TextView mTextViewTitle;
    @Bind(R.id.textViewLink) TextView mTextViewLink;
    @Bind(R.id.imageView3) ImageView mBoxArt;
    public static final String TAG = GameDetailActivity.class.getSimpleName();
    GameDetail mGame;
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
                        mTextViewTitle.setText(mGame.getTitle());
                        Picasso.with(getApplicationContext()).load(mGame.getImageUrl()).resize(400,400).centerInside().into(mBoxArt);
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGame.getUrl())));
    }
}
