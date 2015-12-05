package com.mememorygame.snowgoat.mememorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Thread nyThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Intent startMainMenu = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(startMainMenu);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        nyThread.start();
    }
}
