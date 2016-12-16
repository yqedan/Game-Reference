package com.yusuf.gamereference.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
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
import com.yusuf.gamereference.ui.GameDetailActivity;
import com.yusuf.gamereference.util.CustomGestureDetector;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FirebaseGameViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener{
    @Bind(R.id.gameTitle) TextView mGameTitleTextView;
    @Bind(R.id.gamePlatform) TextView mGamePlatformTextView;
    @Bind(R.id.gameTitleImageView) ImageView mGameTitleImageView;

    private GestureDetector mGestureDetectorListItem;

    Context mContext;

    public FirebaseGameViewHolder(View itemView){
        super(itemView);
        ButterKnife.bind(this, itemView);
        mContext = itemView.getContext();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_GAMES)
                .child(uid);

        CustomGestureDetector customGestureDetectorListItem = new CustomGestureDetector(){
            @Override
            public boolean onDoubleTap(MotionEvent e){
                confirmDialog();
                return true;
            }
            public boolean onSingleTapConfirmed(MotionEvent e){
                final ArrayList<GameDetail> games = new ArrayList<>();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            games.add(snapshot.getValue(GameDetail.class));
                        }
                        int itemPosition = getLayoutPosition();
                        Intent intent = new Intent(mContext, GameDetailActivity.class);
                        intent.putExtra("game", Parcels.wrap(games.get(itemPosition)));
                        mContext.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                return true;
            }
        };
        mGestureDetectorListItem = new GestureDetector(mContext, customGestureDetectorListItem);
        itemView.setOnTouchListener(this);
    }

    public void bindGame(Game game){
        Picasso.with(mContext).load(game.getImageUrl()).resize(200,200).centerInside().into(mGameTitleImageView);
        mGameTitleTextView.setText(game.getTitle());
        ArrayList<String> platforms = (ArrayList<String>) game.getPlatforms();
        String platform = "";
        for (int i = 0; i < platforms.size() ; i++) {
            platform += (platforms.get(i) + " ");
        }
        mGamePlatformTextView.setText("Platforms: " + platform);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == itemView) {
            mGestureDetectorListItem.onTouchEvent(event);
            return true;
        }
        return false;
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setMessage("Are you sure you delete this game?");
        builder.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference ref = FirebaseDatabase
                        .getInstance()
                        .getReference(Constants.FIREBASE_CHILD_GAMES)
                        .child(uid);
                final ArrayList<GameDetail> games = new ArrayList<>();
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            games.add(snapshot.getValue(GameDetail.class));
                        }
                        int itemPosition = getLayoutPosition();
                        ref.child(games.get(itemPosition).getPushId()).removeValue();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
