package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yusuf.gamereference.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.button) Button mButton;
    @Bind(R.id.editText) EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, GameSearchActivity.class);
        intent.putExtra("search",mEditText.getText().toString());
        startActivity(intent);
    }
}
