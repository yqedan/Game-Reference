package com.yusuf.gamereference.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yusuf.gamereference.models.Game;
import com.yusuf.gamereference.R;
import com.yusuf.gamereference.ui.GameDetailActivity;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GameListAdapter extends RecyclerView.Adapter<GameListAdapter.GameViewHolder>{
    private ArrayList<Game> mGames = new ArrayList<>();
    private Context mContext;

    public GameListAdapter(Context context, ArrayList<Game> games) {
        mContext = context;
        mGames = games;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_list_item, parent, false);
        GameViewHolder viewHolder = new GameViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        holder.bindGame(mGames.get(position));
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @Bind(R.id.gameTitle) TextView mGameTitleTextView;
        @Bind(R.id.gamePlatform) TextView mGamePlatformTextView;
        @Bind(R.id.gameTitleImageView) ImageView mGameTitleImageView;

        private Context mContext;

        public GameViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            int itemPosition = getLayoutPosition();
            Intent intent = new Intent(mContext, GameDetailActivity.class);
            intent.putExtra("game",mGames.get(itemPosition).getTitle());
            mContext.startActivity(intent);
        }

        public void bindGame(Game game){
            Picasso.with(mContext).load(game.getImageUrl()).resize(200,200).centerInside().into(mGameTitleImageView);
            mGameTitleTextView.setText(game.getTitle());
            ArrayList<String> platforms = game.getPlatforms();
            String platform = "";
            for (int i = 0; i < platforms.size() ; i++) {
                platform += (platforms.get(i) + " ");
            }
            mGamePlatformTextView.setText("Platforms: " + platform);
        }
    }


}
