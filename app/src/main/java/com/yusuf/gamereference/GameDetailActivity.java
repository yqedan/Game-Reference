package com.yusuf.gamereference;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class GameDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        Intent intent = getIntent();
        String game = intent.getStringExtra("game");
        Toast.makeText(GameDetailActivity.this, game, Toast.LENGTH_LONG).show();
    }
}
