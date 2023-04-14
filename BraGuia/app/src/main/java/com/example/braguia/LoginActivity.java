package com.example.braguia;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mUsernameEditText = findViewById(R.id.username_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mLoginButton = findViewById(R.id.login_button);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LoginTask().execute();
            }
        });
    }

    private class LoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                return "Please fill in all fields!";
            }
            try {
                URL url = new URL("https://c5a2-193-137-92-29.eu.ngrok.io/login");
                String data = "username="+username+"&password="+password;
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                if (conn.getResponseCode() == 200) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    return "Incorrect username or password!";
                }
                conn.disconnect();

            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("")) {
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                if (result.equals("Success")) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}