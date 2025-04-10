package com.edufun.music.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edufun.music.Activity.HomeActivity;
import com.edufun.music.Activity.PlaySongActivity;
import com.edufun.music.Model.InternalSongModel;
import com.edufun.music.MyMediaPlayer;
import com.edufun.music.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.Viewholder> {

    ArrayList<InternalSongModel>list ;
    Context context;



    public SongAdapter(ArrayList<InternalSongModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_song_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, @SuppressLint("RecyclerView") int position) {
        InternalSongModel songModel = list.get(position);
        holder.tvSongName.setText(songModel.getTitle());
        Uri uri = Uri.parse(songModel.getImageIcon());

        Picasso.get().load(uri).placeholder(R.drawable.player).into(holder.imgSongIcon);

        if (MyMediaPlayer.currentIndex==position){
            holder.tvSongName.setTextColor(Color.parseColor("#FF0000"));
        }else {
            holder.tvSongName.setTextColor(context.getResources().getColor(R.color.black));
        }


        holder.itemView.setOnClickListener(v -> {
            MyMediaPlayer.getInstance().reset();
            MyMediaPlayer.currentIndex=position;
            Intent in = new Intent(context, PlaySongActivity.class);
            in.putExtra("LIST",list);
            in.putExtra("fromActivity", "adapter");
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
          //  ((Activity)context).finish();

        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tvSongName;
        ImageView imgSongIcon;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            imgSongIcon = itemView.findViewById(R.id.imgSongIcon);
        }
    }
}
