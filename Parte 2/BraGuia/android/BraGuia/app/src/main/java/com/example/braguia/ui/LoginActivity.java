package com.example.braguia.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.braguia.R;
import com.example.braguia.model.Altera_tema;
import com.example.braguia.model.BotaoSOS;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Altera_tema a = new Altera_tema();
        a.applyTheme(this);
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

        back = findViewById(R.id.back);
        back.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, pagina_inicial.class);
                startActivity(intent);
                finish();
            }
        });

        Button botao = findViewById(R.id.botao_compartilhado);
        BotaoSOS botaoSOS = new BotaoSOS(this);
        botao.setOnClickListener(botaoSOS);
    }

    private class LoginTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String username = mUsernameEditText.getText().toString();
            String password = mPasswordEditText.getText().toString();
            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                return "Please fill in all fields!";
            }
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && ((NetworkInfo) networkInfo).isConnected()) {
                try {
                URL url = new URL("https://c5a2-193-137-92-29.eu.ngrok.io/login");
                String data = "username=" + username + "&password=" + password;
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                if (conn.getResponseCode() == 200) {
                    String csrftoken = conn.getHeaderField("Set-Cookie").split(";")[0];
                    String sessionid = conn.getHeaderField("Set-Cookie").split(";")[1];
                    SharedPreferences SharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
                    SharedPreferences.edit().putString("csrftoken", csrftoken).apply();//
                    SharedPreferences.edit().putString("sessionid", sessionid).apply();

                    Intent intent = new Intent(LoginActivity.this, Trails_activity.class);
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
        }
            else {
                return "Please connect to the internet";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            if (!result.equals("")) {
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        }
    }
}