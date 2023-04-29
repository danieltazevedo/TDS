package com.example.braguia.model;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.braguia.R;

public class Altera_tema {

    public static void applyTheme(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("app_settings", MODE_PRIVATE);
        boolean mode_app = sharedPreferences.getBoolean("Mode", true);
        if (!mode_app) {
            activity.setTheme(R.style.Light);
        }

    }


}
