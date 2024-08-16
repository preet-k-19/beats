package com.example.beats;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
 RecyclerView recyclerView;
 TextView nomusic;
 ArrayList<AudioModel> songslist=new ArrayList<AudioModel>();
    @SuppressLint({"MissingInflatedId", "SuspiciousIndentation"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_view);
        nomusic=findViewById(R.id.nosong);

        if(checkpermission()==false)
        {
            reqpermisson();
            return;
        }

        String [] Projection={
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION
        };

        String selection=MediaStore.Audio.Media.IS_MUSIC+"!=0";


        Cursor cursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,Projection,selection,null,null);
        while(cursor.moveToNext())
        {
            AudioModel songData=new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2));
            if(new File(songData.getPath()).exists())
            songslist.add(songData);
        }

        if(songslist.size()==0)
        {
            nomusic.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songslist,getApplicationContext()));
        }

    }

    boolean checkpermission()
    {
        int result= ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result== PackageManager.PERMISSION_GRANTED)
        {
            return  true;
        }
        else
        {
            return false;
        }
    }

    void reqpermisson()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
        {
            Toast.makeText(this, "Please allow permission to play songs", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerView!=null)
        {
            recyclerView.setAdapter(new MusicListAdapter(songslist,getApplicationContext()));
        }
    }
}