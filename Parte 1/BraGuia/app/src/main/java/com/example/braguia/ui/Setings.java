package com.example.braguia.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.example.braguia.R;

import io.opencensus.internal.StringUtils;

public class Setings extends AppCompatActivity {
    private Button save;
    private Button cancell;
    private EditText meters;
    private Switch Mode;
    private Switch Notifications;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = getSharedPreferences("app_settings", MODE_PRIVATE);
        boolean mode_app = sharedPreferences.getBoolean("Mode", true);

        if (!mode_app) {
            setTheme(R.style.Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);

        save = findViewById(R.id.save);
        cancell = findViewById(R.id.Cancel);
        meters = findViewById(R.id.meters);
        Mode = findViewById(R.id.Mode);
        Notifications = findViewById(R.id.Notifications);

        int distance = sharedPreferences.getInt("Distance", 1000);
        boolean notifications_app = sharedPreferences.getBoolean("Notifications", true);

        Mode.setChecked(mode_app);
        Notifications.setChecked(notifications_app);

        meters.setText("Current value: " + distance);


        meters.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    meters.setText("");
                }
            }
        });


        Mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("Mode", isChecked);
                editor.apply();

                if (!isChecked) {
                    setTheme(R.style.Light);
                }

                recreate();
            }
        });



        Notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("Notifications", isChecked);
                editor.apply();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = String.valueOf(meters.getText());
                if(value.matches("\\d+")) {
                    int newDistance = Integer.parseInt(value);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Distance", newDistance);
                    editor.apply();
                }
                Context context = view.getContext();
                Intent intent = new Intent(Setings.this, Trails_activity.class);
                context.startActivity(intent);
                finish();
            }
        });

        cancell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(Setings.this, Trails_activity.class);
                context.startActivity(intent);
                finish();
            }
        });
    }
}
