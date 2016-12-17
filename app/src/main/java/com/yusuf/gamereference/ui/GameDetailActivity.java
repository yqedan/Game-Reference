package com.yusuf.gamereference.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yusuf.gamereference.R;
import com.yusuf.gamereference.models.GameDetail;

import org.parceler.Parcels;

public class GameDetailActivity extends AppCompatActivity {
//    private static final String TAG = GameDetailActivity.class.getSimpleName();
//    @Bind(R.id.gameDetailImage) ImageView mBoxArt;
//    @Bind(R.id.gameDetailPlatforms) TextView mPlatforms;
//    @Bind(R.id.gameDetailPublishers) TextView mPublishers;
//    @Bind(R.id.gameDetailDevelopers) TextView mDevelopers;
//    @Bind(R.id.gameDetaiSimilarGames) ListView mSimilarGamesListView;
//    @Bind(R.id.addToCollectionButton) Button mAddToCollection;
//    @Bind(R.id.giantbombLink) Button mTextViewLink;
//
//    GameDetail mGame;
//    private ArrayList<Game> similarGames;
//    private ArrayList<String> similarTitles = new ArrayList<>();
//
//    private ProgressDialog mLoadingProgressDialog;
//
//    private GestureDetector mGestureDetectorImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
//        ButterKnife.bind(this);

        Intent intent = getIntent();
        GameDetail game = Parcels.unwrap(intent.getParcelableExtra("game"));
        String gameId = intent.getStringExtra("id");

        GameDetailFragment.newInstance(game, gameId);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_logout,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case R.id.logout:
//                Log.d(TAG, "logging out");
//                logout();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void logout() {
//        FirebaseAuth.getInstance().signOut();
//        Intent intent = new Intent(GameDetailActivity.this, LoginActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == mTextViewLink) {
//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mGame.getGiantBombUrl())));
//        }
//        if (v == mAddToCollection) {
//            addToCollection();
//        }
//    }
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        if(v == mBoxArt){
//            mGestureDetectorImage.onTouchEvent(event);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Integer gameId = similarGames.get(position).getId();
//        if (gameId != -1) {
//            //one game was set to -1 for the message "No Games" when no similar games were found
//            Intent intent = new Intent(GameDetailActivity.this, GameDetailActivity.class);
//            //TODO limit the amount times this can be done before we run out of memory!
//            intent.putExtra("id",gameId.toString());
//            startActivity(intent);
//        }
//    }
//
//    private void getGameDetails(String id){
//        mLoadingProgressDialog.show();
//        GameService.findGameDetails(id, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                mLoadingProgressDialog.dismiss();
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                mGame = GameService.processGame(response);
//                GameDetailActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mLoadingProgressDialog.dismiss();
//                        if (mGame != null) {
//                            updateViews();
//                        }
//                    }
//                });
//            }
//        });
//    }
//
//    private void updateViews(){
//        setTitle(mGame.getTitle());
//        Picasso.with(getApplicationContext()).load(mGame.getImageUrl()).resize(400,400).centerInside().into(mBoxArt);
//        //TODO: dry this up below by using a method
//        ArrayList<String> platforms = (ArrayList<String>) mGame.getPlatforms();
//        String platform = "";
//        for (int i = 0; i < platforms.size() ; i++) {
//            if (i+1 == platforms.size())
//                platform += (platforms.get(i));
//            else
//                platform += (platforms.get(i) + ", ");
//        }
//        if (platforms.size() == 0) {
//            mPlatforms.setText("Platforms: (none)");
//        }else if (platforms.size() == 1){
//            mPlatforms.setText("Platform: " + platform);
//        }else{
//            mPlatforms.setText("Platforms: " + platform);
//        }
//        ArrayList<String> developers = (ArrayList<String>) mGame.getDevelopers();
//        String developer = "";
//        for (int i = 0; i < developers.size() ; i++) {
//            if (i+1 == developers.size())
//                developer += (developers.get(i));
//            else
//                developer += (developers.get(i) + ", ");
//        }
//        if (developers.size() == 0) {
//            mDevelopers.setText("Developers: (none)");
//        }else if (developers.size() == 1){
//            mDevelopers.setText("Developer: " + developer);
//        }else{
//            mDevelopers.setText("Developers: " + developer);
//        }
//        ArrayList<String> publishers = (ArrayList<String>) mGame.getPublishers();
//        String publisher = "";
//        for (int i = 0; i < publishers.size() ; i++) {
//            if (i+1 == publishers.size())
//                publisher += (publishers.get(i));
//            else
//                publisher += (publishers.get(i) + ", ");
//        }
//        if (publishers.size() == 0) {
//            mPublishers.setText("Publishers: (none)");
//        }else if (publishers.size() == 1){
//            mPublishers.setText("Publisher: " + publisher);
//        }else{
//            mPublishers.setText("Publishers: " + publisher);
//        }
//        similarGames = (ArrayList<Game>) mGame.getSimilarGames();
//        similarTitles.removeAll(similarTitles); //Just to be sure!
//        for (int i = 0; i < similarGames.size() ; i++) {
//            similarTitles.add(similarGames.get(i).getTitle());
//        }
//        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, similarTitles){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent) {
//                TextView textView = (TextView) super.getView(position, convertView, parent);
//                textView.setTextColor(Color.parseColor("#000000"));
//                return textView;
//            }
//        };
//        mSimilarGamesListView.setAdapter(adapter);
//    }
//
//    private void createLoadingProgressDialog(){
//        mLoadingProgressDialog = new ProgressDialog(this);
//        mLoadingProgressDialog.setTitle("Loading...");
//        mLoadingProgressDialog.setMessage("Fetching game details");
//        mLoadingProgressDialog.setCancelable(false);
//    }
//
//    private void addToCollection(){
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String uid = user.getUid();
//        final DatabaseReference gameRef = FirebaseDatabase
//                .getInstance()
//                .getReference(Constants.FIREBASE_CHILD_GAMES)
//                .child(uid);
//        ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                boolean match = false;
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    int currentId = snapshot.getValue(GameDetail.class).getId();
//                    if (currentId == mGame.getId()) {
//                        match = true;
//                        break;
//                    }
//                }
//                if (!match) {
//                    DatabaseReference pushRef = gameRef.push();
//                    String pushId = pushRef.getKey();
//                    mGame.setPushId(pushId);
//                    pushRef.setValue(mGame);
//                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
//                }
//                else Toast.makeText(getApplicationContext(), "You already have this game", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        };
//        gameRef.addListenerForSingleValueEvent(listener);
//        gameRef.removeEventListener(listener);
//    }
}
