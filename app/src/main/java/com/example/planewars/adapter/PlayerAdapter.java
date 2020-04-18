package com.example.planewars.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.planewars.R;
import com.example.planewars.data.Player;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder>{
    private List<Player> mPlayerList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View playerView;
        TextView id;
        TextView goal;

        public ViewHolder(View v){
            super(v);
            playerView = v;
            id = v.findViewById(R.id.player_id);
            goal = v.findViewById(R.id.player_goal);
        }
    }

    public PlayerAdapter(List<Player> playerList){
        mPlayerList = playerList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Player player = mPlayerList.get(position);
        holder.id.setText(player.getName());
        holder.goal.setText(player.getGoal().toString());
    }

    @Override
    public int getItemCount() {
        return mPlayerList.size();
    }
}
