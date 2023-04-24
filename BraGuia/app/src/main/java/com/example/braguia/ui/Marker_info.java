package com.example.braguia.ui;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.braguia.R;
import com.example.braguia.model.Trail.Media;
import com.example.braguia.model.Trail.Point;
import com.squareup.picasso.Picasso;
import android.widget.MediaController;
import java.io.IOException;
import java.util.List;

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
        Boolean b=false;
        String audio_url = "";

        for (int i=0;i<media.size();i++) {
            Media aux = media.get(i);
            if(aux.getType().equals("I")) {
                Picasso.get().load(aux.getFile().replace("http", "https")).into(image);
            }
            if(aux.getType().equals("V")) {
                a=false;

                Uri uri = Uri.parse(aux.getFile().replace("http", "https"));
                videoView.setVideoURI(uri);

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
                b=true;
                audio_url = aux.getFile().replace("http", "https");
            }

        }
        if(a) {videoView.setVisibility(View.GONE);}
        if(b) {
            String finalAudio_url = audio_url;
            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playAudio(finalAudio_url);
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
        else {
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

