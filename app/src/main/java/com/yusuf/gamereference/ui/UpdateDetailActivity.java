package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yusuf.gamereference.R;
import com.yusuf.gamereference.models.GameDetail;

import org.parceler.Parcels;

public class UpdateDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_detail);
        Intent intent = getIntent();
        GameDetail game = Parcels.unwrap(intent.getParcelableExtra("game"));
        setTitle(game.getTitle());
    }
}
