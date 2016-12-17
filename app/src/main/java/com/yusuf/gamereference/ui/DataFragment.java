package com.yusuf.gamereference.ui;

import android.app.Fragment;
import android.os.Bundle;

import com.yusuf.gamereference.models.GameDetail;

public class DataFragment extends Fragment {

    // data object we want to retain
    private GameDetail game;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(GameDetail game) {
        this.game = game;
    }

    public GameDetail getData() {
        return game;
    }
}
