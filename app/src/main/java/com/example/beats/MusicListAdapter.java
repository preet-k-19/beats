package com.example.beats;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.viewHloder> {
    ArrayList<AudioModel> songslist;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> songslist, Context context) {
        this.songslist = songslist;
        this.context = context;
    }

    @Override
    public viewHloder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MusicListAdapter.viewHloder(view);
    }

    @Override
    public void onBindViewHolder( MusicListAdapter.viewHloder holder, int position) {
        AudioModel songData=songslist.get(position);
        holder.titletext.setText(songData.getTitle());

        if(MymediaPlayer.currentIndex==position)
        {
            holder.titletext.setTextColor(Color.parseColor("#FF0000"));
        }
        else
        {
            holder.titletext.setTextColor(Color.parseColor("#000000"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MymediaPlayer.getInstance().reset();
                ///*/*//*/*
                MymediaPlayer.currentIndex = holder.getAdapterPosition();
                Intent intent=new Intent(context,MusicPlayer.class);
                intent.putExtra("list",songslist);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return songslist.size();
    }

    public class viewHloder extends RecyclerView.ViewHolder {
        TextView titletext;
        ImageView iconimage;
        public viewHloder( View itemView) {
            super(itemView);
            titletext=itemView.findViewById(R.id.music_title);
            iconimage=itemView.findViewById(R.id.icon_view);
        }
    }
}
