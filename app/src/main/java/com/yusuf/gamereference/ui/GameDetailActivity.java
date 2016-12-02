package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yusuf.gamereference.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameDetailActivity extends AppCompatActivity {
    @Bind(R.id.textView) TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String gameId = intent.getStringExtra("id");
        mTextView.setText(gameId);
    }
}
