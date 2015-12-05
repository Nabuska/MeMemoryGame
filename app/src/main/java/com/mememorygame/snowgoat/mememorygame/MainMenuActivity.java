package com.mememorygame.snowgoat.mememorygame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mememorygame.snowgoat.mememorygame.GamePlay.GameActivity;
import com.mememorygame.snowgoat.mememorygame.TopList.TopListActivity;


public class MainMenuActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    //TODO By Miika Lisätty implements OnComple... tänne

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        AppUtils.init(this);
    }

    public void onNewGameButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        Intent uusipeli = new Intent(this, GameActivity.class);
        startActivity(uusipeli);
    }

    public void onHowToPlayButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        Intent kuinkaPelata = new Intent(this, HowToPlayActivity.class);
        startActivity(kuinkaPelata);
    }

    public void onSettingsButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        Intent asetukset = new Intent(this, SettingsActivity.class);
        startActivity(asetukset);
    }

    public void onTrophyButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        Intent highScore = new Intent(this, TopListActivity.class);
        startActivity(highScore);
    }

    //TODO Tästä alaspäin Miikan aivopieruja
    MediaPlayer mp;
    int pain1 = 0;

    //Easter egg
    private int nyanClickCount = 0;
    public void onNyanButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        this.nyanClickCount++;
        if (nyanClickCount == 5) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.nyan);
            mp.start();
            nyanClickCount = 0;
            mp.setOnCompletionListener(this);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();
    }

    //Easter egg
    private int trollClickCount = 0;
    public void onTrollButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        this.trollClickCount++;
        if (trollClickCount == 5) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.troll);
            mp.start();
            trollClickCount = 0;
            mp.setOnCompletionListener(this);
        }
    }
}