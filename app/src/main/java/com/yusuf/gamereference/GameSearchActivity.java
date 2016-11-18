package com.yusuf.gamereference;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameSearchActivity extends AppCompatActivity {
    public static final String TAG = GameSearchActivity.class.getSimpleName();
    @Bind(R.id.listView) ListView mListView;
    @Bind(R.id.textView) TextView mTextView;
    String[] mGames = new String[] {
            "The Legend of Zelda",
            "The Legend of Zelda: The Wind Waker",
            "The Legend of Zelda: Link's Awakening",
            "The Legend of Zelda: Oracle of Ages",
            "The Legend of Zelda: Ocarina of Time",
            "Zelda II: The Adventure of Link",
            "The Legend of Zelda: A Link to the Past"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_search);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String search = intent.getStringExtra("search");
        Log.d(TAG,search);
        mTextView.setText("You searched for \"" + search + "\"\n Here is a hard coded list if the user searched \"Zelda\"" );
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mGames);
        mListView.setAdapter(adapter);
    }
}
