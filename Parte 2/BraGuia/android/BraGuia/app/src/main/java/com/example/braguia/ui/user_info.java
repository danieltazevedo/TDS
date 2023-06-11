package com.example.braguia.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.braguia.R;

import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.braguia.model.Altera_tema;
import com.example.braguia.model.User;
import com.example.braguia.model.UserAPI;

public class user_info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Altera_tema tema = new Altera_tema();
        tema.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        TextView username = findViewById(R.id.username);
        TextView first_name = findViewById(R.id.first_name);
        TextView last_name = findViewById(R.id.last_name);
        TextView email = findViewById(R.id.email);

        UserTask ut = new UserTask(username, first_name, last_name, email);
        ut.execute();

        Button Cancel = findViewById(R.id.Cancel);
        Cancel.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(user_info.this, Trails_activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    public  class UserTask extends AsyncTask<Void, Void, User> {
        TextView username;
        TextView first_name;
        TextView last_name;
        TextView email;

        public UserTask( TextView username, TextView first_name, TextView last_name, TextView email) {
            this.username = username;
            this.first_name = first_name;
            this.last_name = last_name;
            this.email = email;
        }

        @Override
        protected User doInBackground(Void... voids) {
            SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
            String csrftoken = sharedPreferences.getString("csrftoken", null);
            String sessionid = sharedPreferences.getString("sessionid", null);


            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request request = chain.request().newBuilder()
                                .addHeader("Cookie",  csrftoken +"; " + sessionid)
                                .build();
                        return chain.proceed(request);
                    }).build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://c5a2-193-137-92-29.eu.ngrok.io/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserAPI service = retrofit.create(UserAPI.class);
            Call<User> call = service.getUser();
            Response<User> response;

            try {
                response = call.execute();
                if (response.isSuccessful()) {
                    return response.body();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(User user) {

            if (user != null) {
                username.setText("Username: "+user.getUsername());
                first_name.setText("First Name: "+user.getFirstName());
                last_name.setText("Last Name: "+user.getLastName());
                email.setText("Email: "+user.getEmail());
            }
        }
    }
}