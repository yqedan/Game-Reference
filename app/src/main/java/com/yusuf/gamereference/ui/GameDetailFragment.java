package com.yusuf.gamereference.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yusuf.gamereference.Constants;
import com.yusuf.gamereference.R;
import com.yusuf.gamereference.models.Game;
import com.yusuf.gamereference.models.GameDetail;
import com.yusuf.gamereference.services.GameService;
import com.yusuf.gamereference.util.CustomGestureDetector;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class GameDetailFragment extends Fragment
        implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        View.OnTouchListener{
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

    private ProgressDialog mLoadingProgressDialog;

    private GestureDetector mGestureDetectorImage;

    public static GameDetailFragment newInstance(GameDetail game, String gameId) {
        GameDetailFragment gameDetailFragment = new GameDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("game", Parcels.wrap(game));
        args.putString("id", gameId);
        gameDetailFragment.setArguments(args);
        return gameDetailFragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        createLoadingProgressDialog();
        getGameDetails("11949");
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_detail, container, false);
        ButterKnife.bind(this,view);

//        GameDetail game = Parcels.unwrap(getArguments().getParcelable("game"));
//        Log.d(TAG, "onCreateView: " + game.getTitle());
//        if (game == null) {
            //String gameId = getArguments().getString("id");

//        }else{
//            mGame = game;
//            updateViews();
//        }
        mTextViewLink.setOnClickListener(this);
        mAddToCollection.setOnClickListener(this);
        mSimilarGamesListView.setOnItemClickListener(this);

        CustomGestureDetector customGestureDetectorImage = new CustomGestureDetector(){
            @Override
            public void onLongPress(MotionEvent e){
                addToCollection();
            }
        };
        mGestureDetectorImage = new GestureDetector(getActivity(), customGestureDetectorImage);
        mBoxArt.setOnTouchListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_logout,menu);
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

    @Override
    public void onClick(View v) {
        if (v == mTextViewLink) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGame.getGiantBombUrl())));
        }
        if (v == mAddToCollection) {
            addToCollection();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v == mBoxArt){
            mGestureDetectorImage.onTouchEvent(event);
            return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void getGameDetails(String id){
        mLoadingProgressDialog.show();
        GameService.findGameDetails(id, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mLoadingProgressDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                mGame = GameService.processGame(response);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingProgressDialog.dismiss();
                        if (mGame != null) {
                            updateViews();
                        }
                    }
                });
            }
        });
    }

    private void updateViews(){
        getActivity().setTitle(mGame.getTitle());
        Picasso.with(getActivity()).load(mGame.getImageUrl()).resize(400,400).centerInside().into(mBoxArt);
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
        similarGames = (ArrayList<Game>) mGame.getSimilarGames();
        similarTitles.removeAll(similarTitles); //Just to be sure!
        for (int i = 0; i < similarGames.size() ; i++) {
            similarTitles.add(similarGames.get(i).getTitle());
        }
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, similarTitles){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView textView = (TextView) super.getView(position, convertView, parent);
                textView.setTextColor(Color.parseColor("#000000"));
                return textView;
            }
        };
        mSimilarGamesListView.setAdapter(adapter);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        //finish();
    }

    private void createLoadingProgressDialog(){
        mLoadingProgressDialog = new ProgressDialog(getActivity());
        mLoadingProgressDialog.setTitle("Loading...");
        mLoadingProgressDialog.setMessage("Fetching game details");
        mLoadingProgressDialog.setCancelable(false);
    }

    private void addToCollection(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        final DatabaseReference gameRef = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_GAMES)
                .child(uid);
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean match = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    int currentId = snapshot.getValue(GameDetail.class).getId();
                    if (currentId == mGame.getId()) {
                        match = true;
                        break;
                    }
                }
                if (!match) {
                    DatabaseReference pushRef = gameRef.push();
                    String pushId = pushRef.getKey();
                    mGame.setPushId(pushId);
                    pushRef.setValue(mGame);
                    Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getActivity(), "You already have this game", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        gameRef.addListenerForSingleValueEvent(listener);
        gameRef.removeEventListener(listener);
    }
}
