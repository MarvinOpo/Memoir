package com.mvopo.memoir.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mvopo.memoir.R;
import com.sackcentury.shinebuttonlib.ShineButton;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ShineButton splashText = findViewById(R.id.splash_text);

        Handler splashHandler = new Handler();
        splashHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splashText.performClick();
            }
        }, 1000);

        Handler intentHandler = new Handler();
        intentHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences myPref = SplashActivity.this.getSharedPreferences("Memoir", MODE_PRIVATE);
                if(myPref.contains("theme")) {
                    int theme = myPref.getInt("theme", R.style.AppTheme_Pink);

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("theme", theme);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(SplashActivity.this, SettingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}
