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
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {
    Intent main_intent;
    Intent stream_intent;
    Switch switch_theme_button;
    TextView launch_cards_text;
    Button request_button;
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
                    startActivity(stream_intent);
                    return true;
                case R.id.navigation_settings:
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
        setContentView(R.layout.activity_settings);
        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();

        switch_theme_button = findViewById(R.id.switch_theme);
        launch_cards_text = findViewById(R.id.launch_cards_text);
        request_button = findViewById(R.id.request_button);
        constraint_layout = findViewById(R.id.settings_constraint);

        if (preferences.contains("darkMode")) {
            if (preferences.getBoolean("darkMode", false)) {
                napTime();
            } else {
                dayTime();
            }
        }
        switch_theme_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (switch_theme_button.isChecked() + "") {
                    case "true":
                        napTime();
                        editor.putBoolean("darkMode", true);
                        break;
                    case "false":
                        dayTime();
                        editor.putBoolean("darkMode", false);
                        break;
                }
                editor.apply();
            }
        });
        request_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "RENEWING DATA.", Toast.LENGTH_SHORT).show();
                editor.putBoolean("renew_data", true);
                editor.apply();
            }
        });
        main_intent = new Intent(getApplicationContext(), MainActivity.class);
        stream_intent = new Intent(getApplicationContext(), StreamActivity.class);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void napTime() {
        constraint_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark_background));
        switch_theme_button.setTextColor(getColor(R.color.colorLight));
        switch_theme_button.setChecked(true);
        launch_cards_text.setTextColor(getColor(R.color.colorLight));
        request_button.setTextColor(getColor(R.color.colorLight));
        request_button.setBackgroundColor(getColor(R.color.colorDark));
    }

    private void dayTime() {
        constraint_layout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLight_background));
        switch_theme_button.setTextColor(getColor(R.color.colorDark));
        switch_theme_button.setChecked(false);
        launch_cards_text.setTextColor(getColor(R.color.colorDark));
        request_button.setTextColor(getColor(R.color.colorDark));
        request_button.setBackgroundColor(getColor(R.color.colorLight));
    }
}
