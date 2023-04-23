package com.example.braguia.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.braguia.R;
import com.example.braguia.model.Trail;
import com.squareup.picasso.Picasso;

public class Trail_info extends AppCompatActivity {
    private TextView trail_name;
    private TextView trail_desc;
    private TextView trail_duration;
    private TextView trail_difficulty;
    private ImageView imageView;
    private Button Start;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_trail);
        Trail a = (Trail) getIntent().getSerializableExtra("trail_info");
        trail_name = findViewById(R.id.trail_name);
        trail_desc = findViewById(R.id.trail_desc);
        trail_duration = findViewById(R.id.trail_duration);
        trail_difficulty = findViewById(R.id.trail_difficulty);
        imageView = findViewById(R.id.image);
        trail_name.setText(a.getTrail_name());
        trail_desc.setText(a.getTrail_desc());
        trail_duration.setText("Duration: " + a.getTrail_duration() + " minutes");
        trail_difficulty.setText("Difficulty: "+a.getTrail_difficulty());
        Picasso.get().load(a.getUrl().replace("http", "https")).into(imageView);
        Start = findViewById(R.id.start);
        Start.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(Trail_info.this, MainActivity.class);
                intent.putExtra("trail_info", a);
                context.startActivity(intent);
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Trail_info.this, Trails_activity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
