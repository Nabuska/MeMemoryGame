package com.mememorygame.snowgoat.mememorygame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainMenuActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    //TODO By Miika Lisätty implements OnComple... tänne

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        AppUtils.init(this);
    }

    public void openNewGame(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        //Intent uusipeli = new Intent(this, GameActivity.class);//TODO kommentti pois
        //startActivity(uusipeli);
    }

    public void OpenHowToPlay(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        //Intent kuinkaPelata = new Intent(this, HowToPlay.class);//TODO kommentti pois
        //startActivity(kuinkaPelata);
    }

    public void openSettings(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        //Intent asetukset = new Intent(this, SettingsActivity.class);//TODO kommentti pois
        //startActivity(asetukset);
    }

    public void openHighScores(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        //Intent highScore = new Intent(this, TopListActivity.class);//TODO kommentti pois
        //startActivity(highScore);
    }

    //TODO Tästä alaspäin Miikan aivopieruja
    MediaPlayer mp;
    int pain1 = 0;

    public void playNyan(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        this.pain1++;
        if (pain1 == 5) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.nyan);
            mp.start();
            pain1 = 0;
            mp.setOnCompletionListener(this);
        }
    }

    int pain2 = 0;

    public void PlayTroll(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        this.pain2++;
        if (pain2 == 5) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.troll);
            mp.start();
            pain2 = 0;
            mp.setOnCompletionListener(this);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
    }
}