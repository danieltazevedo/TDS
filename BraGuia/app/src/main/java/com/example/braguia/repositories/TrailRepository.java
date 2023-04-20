package com.example.braguia.repositories;


import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.example.braguia.model.GuideDatabase;
import com.example.braguia.model.Trail;
import com.example.braguia.model.TrailAPI;
import com.example.braguia.model.TrailDAO;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrailRepository {

    public TrailDAO trailDAO;
    public LiveData<List<Trail>> allTrails;
    private GuideDatabase database;

    public TrailRepository(Application application){
        database = GuideDatabase.getInstance(application);
        trailDAO = database.trailDAO();
        init();
        allTrails = trailDAO.getTrails();
    }

    public void insert(List<Trail> trails){
        new InsertAsyncTask(trailDAO).execute(trails);
    }

    public void init(){
        // TODO add cache validation strategy
        if(allTrails == null || allTrails.getValue() == null || allTrails.getValue().isEmpty()){
            makeRequest();
        }
    }

    private void makeRequest() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://c5a2-193-137-92-29.eu.ngrok.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TrailAPI api=retrofit.create(TrailAPI.class);
        Call<List<Trail>> call=api.getTrails();
        call.enqueue(new retrofit2.Callback<List<Trail>>() {
            @Override
            public void onResponse(Call<List<Trail>> call, Response<List<Trail>> response) {
                if(response.isSuccessful()) {
                    insert(response.body());
                }
                else{
                    Log.e("main", "onFailure: "+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Trail>> call, Throwable t) {
                Log.e("main", "onFailure: " + t.getMessage());
            }
        });
    }


    public LiveData<List<Trail>> getAllTrails(){
        return allTrails;
    }
    private static class InsertAsyncTask extends AsyncTask<List<Trail>,Void,Void> {
        private TrailDAO trailDAO;

        public InsertAsyncTask(TrailDAO catDao) {
            this.trailDAO=catDao;
        }

        @Override
        protected Void doInBackground(List<Trail>... lists) {
            trailDAO.insert(lists[0]);
            return null;
        }
    }

}
