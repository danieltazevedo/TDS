package com.example.braguia.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.braguia.R;
import com.example.braguia.model.Altera_tema;
import com.example.braguia.model.BotaoSOS;
import com.example.braguia.model.Trail;
import com.example.braguia.model.menu;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Trail_info extends AppCompatActivity {
    private TextView trail_name;
    private TextView trail_desc;
    private TextView trail_duration;
    private TextView trail_difficulty;
    private TextView locais;
    private ImageView imageView;
    private Button Start;
    private Button back;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Altera_tema tema = new Altera_tema();
        tema.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_trail);
        Trail a = (Trail) getIntent().getSerializableExtra("trail_info");
        trail_name = findViewById(R.id.trail_name);
        trail_desc = findViewById(R.id.trail_desc);
        trail_duration = findViewById(R.id.trail_duration);
        trail_difficulty = findViewById(R.id.trail_difficulty);
        locais = findViewById(R.id.locais);
        imageView = findViewById(R.id.image);

        Button botao = findViewById(R.id.botao_compartilhado);
        BotaoSOS botaoSOS = new BotaoSOS(this);
        botao.setOnClickListener(botaoSOS);

        trail_name.setText(a.getTrail_name());
        trail_desc.setText(a.getTrail_desc());
        trail_duration.setText("Duration: " + a.getTrail_duration() + " minutes");
        trail_difficulty.setText("Difficulty: "+a.getTrail_difficulty());
        String string="Points of Interest:\n\n";
        List<Trail.Edge> list = a.getEdges();
        for (int i = 0; i < list.size(); i++) {
            Trail.Point start = list.get(i).getpoint_start();
            string = string + start.getName() +"\n";
        }
        string = string + list.get(list.size()-1).getpoint_end().getName() + "\n\n\n\n\n";
        locais.setText(string);
        Picasso.get().load(a.getUrl().replace("http", "https")).into(imageView);
        Start = findViewById(R.id.start);
        Start.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(Trail_info.this, Maps_Activity.class);
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

        drawerLayout = findViewById(R.id.info_trail);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        menu.setupDrawer(getApplicationContext(),this, drawerLayout, actionBarDrawerToggle);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return menu.onOptionsItemSelected(item, actionBarDrawerToggle) || super.onOptionsItemSelected(item);
    }
}
