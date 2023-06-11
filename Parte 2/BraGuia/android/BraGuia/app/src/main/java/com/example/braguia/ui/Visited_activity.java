package com.example.braguia.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.braguia.R;
import com.example.braguia.model.Altera_tema;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class Visited_activity extends AppCompatActivity {
    private Button Back;
    private Set<String> visited = new HashSet<>();
    private TextView points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Altera_tema tema = new Altera_tema();
        tema.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locals_visited);

        points = findViewById(R.id.places);
        Back = findViewById(R.id.back);
        Back.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Visited_activity.this, Trails_activity.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences sharedPreferences_visited = getSharedPreferences("visited", MODE_PRIVATE);
        String listaJSON = sharedPreferences_visited.getString("locals",null);
        String locals = "";
        if (listaJSON != null) {
            Type type = new TypeToken<Set<String>>(){}.getType();
            Gson gson = new Gson();
            visited = gson.fromJson(listaJSON, type);

            for (String elemento : visited) {
                locals = locals + elemento + "\n";
            }
        }

        points.setText(locals);


    }

}
