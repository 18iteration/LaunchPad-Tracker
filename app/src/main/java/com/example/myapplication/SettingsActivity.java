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

public class SettingsActivity extends AppCompatActivity {
    Intent mainIntent;
    Switch switch_ThemeBtn;
    TextView requestText;
    Button request_Btn;
    View constraintLayout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_upcoming:
                    startActivity(mainIntent);
                    return true;
                case R.id.information:
                    return true;
                case R.id.settings:
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
        switch_ThemeBtn = findViewById(R.id.switch_Theme);
        requestText = findViewById(R.id.launchCardsText);
        request_Btn = findViewById(R.id.requestBtn);
        constraintLayout = findViewById(R.id.settingsConstraint);
        if (preferences.contains("darkMode")){
            if(preferences.getBoolean("darkMode",false)){
                napTime();
            }else {
                dayTime();
            }
        }
        switch_ThemeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (switch_ThemeBtn.isChecked()+""){
                    case "true":
                        napTime();
                        editor.putBoolean("darkMode", true);
                        break;
                    case "false":
                        dayTime();
                        editor.putBoolean("darkMode", false);
                        break;
                }
                editor.commit();
            }
        });
        mainIntent = new Intent(this, MainActivity.class);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void napTime(){
        constraintLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorDark));
        switch_ThemeBtn.setTextColor(getColor(R.color.colorOrange));
        switch_ThemeBtn.setChecked(true);
        requestText.setTextColor(getColor(R.color.colorOrange));
        request_Btn.setTextColor(getColor(R.color.colorOrange));
        request_Btn.setBackgroundColor(getColor(R.color.colorDark));
    }
    private void dayTime(){
        constraintLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLight));
        switch_ThemeBtn.setTextColor(getColor(R.color.colorPrimaryDark));
        switch_ThemeBtn.setChecked(false);
        requestText.setTextColor(getColor(R.color.colorPrimaryDark));
        request_Btn.setTextColor(getColor(R.color.colorPrimaryDark));
        request_Btn.setBackgroundColor(getColor(R.color.colorLight));
    }
}
