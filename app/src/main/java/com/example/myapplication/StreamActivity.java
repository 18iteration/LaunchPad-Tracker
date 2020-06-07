package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StreamActivity extends AppCompatActivity {
    Intent main_intent;
    Intent settings_intent;
    View constraint_layout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_upcoming:
                    startActivity(main_intent);
                    return true;
                case R.id.navigation_stream:
                    return true;
                case R.id.navigation_settings:
                    startActivity(settings_intent);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        constraint_layout = findViewById(R.id.stream_constraint);

        if (preferences.contains("darkMode")) {
            if (preferences.getBoolean("darkMode", false)) {
                constraint_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark_background));
            }
        }
        main_intent = new Intent(getApplicationContext(), MainActivity.class);
        settings_intent = new Intent(getApplicationContext(), SettingsActivity.class);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
}