package com.example.beats;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayer extends AppCompatActivity {
TextView titletext,currentTv,totaltV;
SeekBar seekBar;
ImageView pause_play,nextbtn,previousbtn,iconmusic;

AudioModel currentsong;

ArrayList<AudioModel> songslist;
int x;

MediaPlayer mediaPlayer=MymediaPlayer.getInstance();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titletext=findViewById(R.id.songstitle);
        currentTv=findViewById(R.id.current_time);
        totaltV=findViewById(R.id.total_time);
        seekBar=findViewById(R.id.seekbar);
        pause_play=findViewById(R.id.ppause_play);
        nextbtn=findViewById(R.id.next);
        previousbtn=findViewById(R.id.previous);
        iconmusic=findViewById(R.id.music_icon);

        titletext.setSelected(true);

        songslist=(ArrayList<AudioModel>) getIntent().getSerializableExtra("list");

        setReasourcesWithmusic();

        MusicPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null)
                {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTv.setText(converttoMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying())
                    {
                        pause_play.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                        iconmusic.setRotation(x++);
                    }
                    else
                    {
                        pause_play.setImageResource(R.drawable.baseline_play_circle_outline_24);
                        iconmusic.setRotation(0);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if(mediaPlayer!=null && fromUser)
                {
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    void setReasourcesWithmusic(){
        currentsong=songslist.get(MymediaPlayer.currentIndex);
        titletext.setText(currentsong.getTitle());
        totaltV.setText(converttoMMSS(currentsong.getDuration()));

        pause_play.setOnClickListener(v->pausePlay());
        nextbtn.setOnClickListener(v->playNextSong());
        previousbtn.setOnClickListener(v->playPreviousSong());

        playMusic();
    }

    private void playMusic()
    {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentsong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    private void playNextSong()
    {
        if(MymediaPlayer.currentIndex==songslist.size()-1)
        {
            return;
        }
        MymediaPlayer.currentIndex+=1;
        mediaPlayer.reset();
        setReasourcesWithmusic();
    }

    private void playPreviousSong()
    {
        if(MymediaPlayer.currentIndex==0)
        {
            return;
        }
        MymediaPlayer.currentIndex-=1;
        mediaPlayer.reset();
        setReasourcesWithmusic();
    }

    private void pausePlay()
    {
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.pause();
        }
        else
        {
            mediaPlayer.start();

        }
    }
    public static String converttoMMSS(String duration)
    {
        Long millis=Long.parseLong(duration);
     return    String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis)% TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis)% TimeUnit.MINUTES.toSeconds(1));
    }
}