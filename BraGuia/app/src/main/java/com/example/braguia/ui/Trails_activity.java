package com.example.braguia.ui;


import android.os.Bundle;
import com.example.braguia.model.menu;

import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.braguia.R;
import com.example.braguia.model.BotaoSOS;

public class Trails_activity extends AppCompatActivity {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_trails);

        Button botao = findViewById(R.id.botao_compartilhado);
        BotaoSOS botaoSOS = new BotaoSOS(this);
        botao.setOnClickListener(botaoSOS);

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        menu.setupDrawer(this, drawerLayout, actionBarDrawerToggle);
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
