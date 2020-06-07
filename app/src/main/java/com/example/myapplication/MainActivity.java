package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    final String url = "https://api.spacexdata.com/v3/launches";
    final String imgNotFoundUrl = "https://images.unsplash.com/photo-1460186136353-977e9d6085a1?ixlib=rb-0.3.5&s=d964dc1d289532170953994be94e89f6&auto=format&fit=crop&w=1950&q=80";
    private ArrayList<String> imgUrl = new ArrayList<>();
    private ArrayList<String> name = new ArrayList<>();
    private ArrayList<String> date = new ArrayList<>();
    private ArrayList<String> eta = new ArrayList<>();
    private ArrayList<String> youtubeLink = new ArrayList<>();
    private ArrayList<String> landing_intent = new ArrayList<>();
    Intent stream_intent;
    Intent settings_intent;
    View main_constraint;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_upcoming:
                    return true;
                case R.id.navigation_stream:
                    startActivity(stream_intent);
                    return true;
                case R.id.navigation_settings:
                    startActivity(settings_intent);
                    return true;
            }
            return false;
        }
    };
    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        RecyclerViewAdapter recyclerAdapter = new RecyclerViewAdapter(getApplicationContext(), imgUrl, name, date, eta, youtubeLink, landing_intent);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        main_constraint = findViewById(R.id.main_constraint);
        stream_intent = new Intent(getApplicationContext(), StreamActivity.class);
        settings_intent = new Intent(getApplicationContext(), SettingsActivity.class);

        if (preferences.contains("darkMode")) {
            if (preferences.getBoolean("darkMode", false)) {
                main_constraint.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark_background));
            }
        }
        /*Checks if preferences (persisting storage) contains name1 (any existing launch data)*/
        if (!preferences.contains("name1") | preferences.getBoolean("renew_data", true)) {
            editor.putBoolean("renew_data", true);
            linkRequest();
            editor.apply();
        }
        if (preferences.contains("name1")) {
            for (int i = preferences.getInt("jsonLength", -1); i > 0; i--) {
                imgUrl.add(preferences.getString("imgUrl" + i, null));
                name.add(preferences.getString("name" + i, null));
                date.add(preferences.getString("date" + i, null));
                eta.add(preferences.getString("eta" + i, null));
                youtubeLink.add(preferences.getString("youtubeLink" + i, null));
                landing_intent.add(preferences.getString("landing_intent" + i, null));
            }
            initRecycleView();
        }
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void linkRequest() {
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
                            for (int i = json.length() - 1; i > 0; i--) {
                                editor.putInt("jsonLength", json.length() - 1);
                                if (json.getJSONObject(i).getJSONObject("links").getString("mission_patch_small").equals("null")) {
                                    editor.putString("imgUrl" + i, imgNotFoundUrl);
                                } else {
                                    editor.putString("imgUrl" + i, json.getJSONObject(i).getJSONObject("links").getString("mission_patch_small"));
                                }
                                editor.putString("name" + i, json.getJSONObject(i).getString("mission_name"));
                                String mez_time = utc_to_local(json.getJSONObject(i).getString("launch_date_utc"));
                                editor.putString("date" + i, mez_time);

                                String eta_time = "";
                                long eta_long_time = getDateDiff(string_to_date(get_time()), string_to_date(mez_time), TimeUnit.HOURS);
                                if (eta_long_time > 0) {
                                    eta_time = String.valueOf(eta_long_time) + "h";
                                } else {
                                    eta_time = "Already occured";
                                }
                                editor.putString("eta" + i, "ETA: " + eta_time);

                                if (json.getJSONObject(i).getJSONObject("links").getString("video_link").equals("null")) {
                                    editor.putString("youtubeLink" + i, "Live Webcast  TBD!");
                                } else {
                                    editor.putString("youtubeLink" + i, json.getJSONObject(i).getJSONObject("links").getString("video_link"));
                                }
                                switch (json.getJSONObject(i).getJSONObject("rocket").getJSONObject("first_stage").getJSONArray("cores").getJSONObject(0).getString("landing_intent")) {
                                    case "true":
                                        editor.putString("landing_intent" + i, "Yes!");
                                        break;
                                    case "false":
                                        editor.putString("landing_intent" + i, "No!");
                                        break;
                                    case "null":
                                        editor.putString("landing_intent" + i, "TBD!");
                                        break;
                                }
                            }
                            editor.commit();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                        editor.putString("last_update", get_time());
                    }
                });
            }
        });
    }

    private String get_time() {
        Date date_time = Calendar.getInstance().getTime();
        String time_format = "dd.MM.yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(time_format);
        return df.format(date_time);
    }

    private Date string_to_date(String _str) {
        Date dt = null;
        try {
            dt = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(_str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }

    private String utc_to_local(String _str) {
        int offset = 2;
        return (_str.substring(8, 10) + "." + _str.substring(5, 7) + "." + _str.substring(0, 4) + " " + (((Integer.parseInt(_str.substring(11, 13))) < 10) ? ("0" + (Integer.parseInt(_str.substring(11, 13)) + offset)) : (Integer.toString(Integer.parseInt(_str.substring(11, 13)) + 2))) + ":" + _str.substring(14, 16) + ":" + _str.substring(17, 19));
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillie = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillie, TimeUnit.MILLISECONDS);
    }
}
