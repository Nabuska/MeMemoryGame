package com.mememorygame.snowgoat.mememorygame;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HowToPlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);
    }

    public void backFromH2p(View view) {
        AppUtils.vibrate(100, 50, 100);
        MainMenuActivity.mp = MediaPlayer.create(getApplicationContext(), R.raw.button_sound);
        MainMenuActivity.mp.start();
        finish();
    }
}
