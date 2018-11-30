package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    final String url = "https://api.spacexdata.com/v3/launches";

    private ArrayList<String> imgUrl = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> youtubeLink = new ArrayList<>();

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linkRequest(url);
        }

    private void initRecycleView(){
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(this,imgUrl,name,date,youtubeLink);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void linkRequest(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String responseData = response.body().string();
                            JSONArray json = new JSONArray(responseData);
                            for (int i = json.length()-1; i > 0; i--) {
                                imgUrl.add(json.getJSONObject(i).getJSONObject("links").getString("mission_patch"));
                                name.add(json.getJSONObject(i).getString("mission_name"));
                                date.add(json.getJSONObject(i).getString("launch_date_utc"));
                                youtubeLink.add(json.getJSONObject(i).getJSONObject("links").getString("video_link"));
                            }
                            initRecycleView();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    }

}
