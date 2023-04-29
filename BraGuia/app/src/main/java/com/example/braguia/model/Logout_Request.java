package com.example.braguia.model;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.braguia.ui.pagina_inicial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Logout_Request extends AsyncTask<Void, Void, Boolean>  {

    private Context context;
    private Activity activity;

    public Logout_Request(Context context,Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    protected Boolean doInBackground(Void... voids) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user_info", MODE_PRIVATE);
        String csrftoken = sharedPreferences.getString("csrftoken", null);
        String sessionid = sharedPreferences.getString("sessionid", null);

        URL url = null;
        try {
            url = new URL("https://c5a2-193-137-92-29.eu.ngrok.io/logout");
            String data = csrftoken +"; " + sessionid;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            if (conn.getResponseCode() == 200) {
                return true;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return false;
    }

    @Override
    protected void onPostExecute(Boolean res) {
        if(res) {
            Intent intent = new Intent(activity, pagina_inicial.class);
            activity.startActivity(intent);
        }
    }

}