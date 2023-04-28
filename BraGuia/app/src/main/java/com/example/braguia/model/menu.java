package com.example.braguia.model;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class menu {

    public static void setupDrawer(Activity activity, DrawerLayout drawerLayout, ActionBarDrawerToggle actionBarDrawerToggle) {

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ((AppCompatActivity) activity).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Adicionar o OnClickListener para o DrawerLayout
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
    }


    public static boolean onOptionsItemSelected(MenuItem item, ActionBarDrawerToggle actionBarDrawerToggle) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }
}