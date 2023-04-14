package com.example.braguia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class pagina_inicial extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String CACHE_KEY = "json_cache";
    private SharedPreferences mSharedPreferences;
    private Button LoginButton;
    private Button Contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagina_inicial);
        LoginButton = findViewById(R.id.login);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(pagina_inicial.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });

        Contacts = findViewById(R.id.contacts);
        Contacts.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(pagina_inicial.this, Contacts.class);
                startActivity(intent);
                finish();
            }
        });

        mSharedPreferences = getSharedPreferences("app_info", MODE_PRIVATE);
        String cachedJson = mSharedPreferences.getString(CACHE_KEY, null);

        if (cachedJson != null) {
            try {
                process_Json(cachedJson);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            new JsonTask().execute(getString(R.string.url_app_info));
        }
    }

    private void process_Json(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObject = jsonArray.getJSONObject(0);

        TextView textView = findViewById(R.id.appName);
        textView.setText(jsonObject.getString("app_name"));

        textView = findViewById(R.id.Descricao);
        textView.setText(jsonObject.getString("app_desc"));

        textView = findViewById(R.id.page_text);
        textView.setText(jsonObject.getString("app_landing_page_text"));
    }

    private class JsonTask extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Read the input stream into a String
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                return buffer.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    process_Json(result);
                    mSharedPreferences.edit().putString(CACHE_KEY, result).apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e(TAG, "Failed to get JSON from server");
            }
        }
    }
}

