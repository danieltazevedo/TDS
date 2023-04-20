package com.example.braguia.ui;

import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

import com.example.braguia.R;

import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;
        import java.util.HashMap;

        import butterknife.BindView;
        import butterknife.ButterKnife;

public class Partners extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private static final String CACHE_KEY = "json_cache";
    private Button Back;


    @BindView(R.id.recycler_main)
    RecyclerView recyclerMain;

    LinearLayoutManager linearLayoutManager;
    ArrayList<HashMap<String,String>> dataList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reciclyview);
        ButterKnife.bind(this);

        Back = findViewById(R.id.back);
        Back.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Partners.this, pagina_inicial.class);
                startActivity(intent);
                finish();
            }
        });

        mSharedPreferences = getSharedPreferences("app_info", MODE_PRIVATE);
        String cachedJson = mSharedPreferences.getString(CACHE_KEY, null);

        try {
            process_Json(cachedJson);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerMain.setLayoutManager(linearLayoutManager);
        DataAdapter dataAdapter = new DataAdapter(this,dataList);
        recyclerMain.setAdapter(dataAdapter);

        RecyclerItemDecoration recyclerItemDecoration = new RecyclerItemDecoration(this,120,true,getSectionCallback(dataList));
        recyclerMain.addItemDecoration(recyclerItemDecoration);
    }


    private RecyclerItemDecoration.SectionCallback getSectionCallback(final ArrayList<HashMap<String,String>> list)
    {
        return new RecyclerItemDecoration.SectionCallback() {
            @Override
            public boolean isSection(int pos) {
                return pos==0 || list.get(pos).get("Title")!=list.get(pos-1).get("Title");
            }

            @Override
            public String getSectionHeaderName(int pos) {
                HashMap<String,String> dataMap = list.get(pos);
                return dataMap.get("Title");
            }
        };
    }

   private void process_Json(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONArray partnersArray = jsonObject.getJSONArray("partners");
        for (int i=0; i < partnersArray.length(); i++) {
            HashMap<String,String> dataMAp = new HashMap<>();
            JSONObject partner = partnersArray.getJSONObject(i);
            dataMAp.put("Title",partner.getString("partner_name"));
            dataMAp.put("Desc1","Phone Number: "+partner.getString("partner_phone")+"\n"+"E-Mail: "+partner.getString("partner_mail"));
            dataMAp.put("Desc2",partner.getString("partner_url"));
            dataList.add(dataMAp);
        }

    }
}
