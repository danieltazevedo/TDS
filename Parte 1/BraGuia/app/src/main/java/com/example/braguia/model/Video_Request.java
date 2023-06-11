package com.example.braguia.model;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Video_Request extends AsyncTask<Void, Void, InputStream> {

    private Context mContext;
    private String url;
    private VideoView videoView;

    public Video_Request(Context context, String url, VideoView videoView) {
        mContext = context;
        this.url = url;
        this.videoView = videoView;
    }

    @Override
    protected InputStream doInBackground(Void... voids) {
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(mContext.getCacheDir(), cacheSize);
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


        File cacheFile = new File(mContext.getCacheDir(), "video");
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

        Uri uri = Uri.fromFile(cacheFile);
        videoView.setVideoURI(uri);

    }
}

