package com.example.braguia.ui;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.braguia.R;
import com.example.braguia.model.Audio_Request;
import com.example.braguia.model.Video_Request;
import com.example.braguia.model.Trail.Media;
import com.example.braguia.model.Trail.Point;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.widget.MediaController;


public class Marker_info extends AppCompatActivity {
    private Point point;
    private TextView name;
    private TextView desc;
    private ImageView image;
    private VideoView videoView;
    private Button play;
    private Button pause;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marker_info);
        point = (Point) getIntent().getSerializableExtra("point_info");

        name = findViewById(R.id.Name);
        desc = findViewById(R.id.Descricao);
        image = findViewById(R.id.image);
        videoView = findViewById(R.id.videoView);
        play = findViewById(R.id.Play);
        pause = findViewById(R.id.Pause);

        name.setText(point.getName());
        desc.setText(point.getDesc());
        List<Media> media = point.getMedia();
        Boolean a=true;
        Boolean b=true;

        for (int i=0;i<media.size();i++) {
            Media aux = media.get(i);
            if(aux.getType().equals("I")) {
                Picasso.get().load(aux.getFile().replace("http", "https")).into(image);
            }
            if(aux.getType().equals("V")) {
                a=false;
                Video_Request task = new Video_Request(getApplicationContext(),aux.getFile().replace("http", "https"),videoView);
                task.execute();

                MediaController mediaController = new MediaController(this);
                mediaController.setAnchorView(videoView);
                mediaController.setMediaPlayer(videoView);
                videoView.setMediaController(mediaController);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaController.setOnTouchListener(null);
                        View.OnClickListener clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                videoView.start();
                            }
                        };
                    }
                });
            }


            if(aux.getType().equals("R")) {
                b=false;
                File file = new File(Marker_info.this.getCacheDir(), "audio");
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if(!file.exists()) {
                if ((networkInfo != null) && ((NetworkInfo) networkInfo).isConnected()) {
                Audio_Request ar = new Audio_Request(Marker_info.this,aux.getFile().replace("http", "https"));
                ar.execute(); }}

                play.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        try {
                            mediaPlayer.setDataSource(file.getAbsolutePath());
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

                pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                            mediaPlayer.reset();
                            mediaPlayer.release();

                            Toast.makeText(Marker_info.this, "Audio has been paused", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Marker_info.this, "Audio has not played", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

        }
        if(a) {videoView.setVisibility(View.GONE);}
        if(b) {
            play.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);

        }
    }

    private void playAudio(String audioUrl) {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Audio started playing...", Toast.LENGTH_SHORT).show();
    }
}

