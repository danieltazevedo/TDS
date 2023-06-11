package com.example.braguia.model;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Audio_Request  extends AsyncTask<Void, Void, InputStream>  {
    private String cache_name = "audio";
    private Context context;
    private String url;

    public Audio_Request (Context context, String url) {
        this.context = context;
        this.url = url;
    }


     @Override
    protected InputStream doInBackground(Void... voids) {
         File cacheFile = new File(context.getCacheDir(), cache_name);
         int cacheSize = 10 * 1024 * 1024;
         Cache cache = new Cache(context.getCacheDir(), cacheSize);
         OkHttpClient client = new OkHttpClient.Builder()
                 .cache(cache)
                 .build();

         Request request = new Request.Builder()
                 .url(url)
                 .build();

         Call call = client.newCall(request);
         Response response = null;
         try {
             response = call.execute();
         } catch (IOException e) {
             throw new RuntimeException(e);
         }

         return response.body().byteStream();

    }


    @Override
    protected void onPostExecute(InputStream inputStream) {
        File cacheFile = new File(context.getCacheDir(), cache_name);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(cacheFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        byte[] buffer = new byte[1024];
        int bytesRead;
        while (true) {
            try {
                if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                outputStream.write(buffer, 0, bytesRead);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        }
    }


