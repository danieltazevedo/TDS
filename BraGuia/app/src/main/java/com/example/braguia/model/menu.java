package com.example.braguia.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.braguia.R;
import com.example.braguia.ui.Setings;
import com.example.braguia.ui.pagina_inicial;
import com.example.braguia.ui.user_info;
import com.google.android.material.navigation.NavigationView;

public class menu {

    public static void setupDrawer(Context context, Activity activity, DrawerLayout drawerLayout, ActionBarDrawerToggle actionBarDrawerToggle) {

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

        // Adicionar o OnItemClickListener ao NavigationView
        NavigationView navigationView = activity.findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent = null;
                switch (id) {
                    case R.id.nav_account:
                        intent = new Intent(activity, user_info.class);
                        break;
                    case R.id.nav_settings:
                        intent = new Intent(activity, Setings.class);
                        break;
                    case R.id.nav_logout:
                        Logout_Request lr = new Logout_Request(context,activity);
                        lr.execute();
                        break;
                }
                if (intent != null) {
                    activity.startActivity(intent);
                    drawerLayout.closeDrawers();
                    return true;
                }
                return false;
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
