package com.mememorygame.snowgoat.mememorygame;

import android.content.Intent;
import android.media.MediaDataSource;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mememorygame.snowgoat.mememorygame.GamePlay.GameActivity;
import com.mememorygame.snowgoat.mememorygame.TopList.TopListActivity;


public class MainMenuActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{
    //TODO By Miika Lisätty implements OnComple... tänne
    public static MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        AppUtils.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!AppUtils.musicIsPlaying()){
            AppUtils.setMediaPlayer(MediaPlayer.create(getApplicationContext(), R.raw.theme));
            AppUtils.startMediaPlayer();
        }
    }

    public void onNewGameButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        playSound(R.raw.button_sound);
        Intent uusipeli = new Intent(this, GameActivity.class);
        startActivity(uusipeli);
    }

    public void onHowToPlayButtonClick(View view) {
        AppUtils.vibrate(100, 50, 100);
        Intent kuinkaPelata = new Intent(this, HowToPlayActivity.class);
        playSound(R.raw.button_sound);
        startActivity(kuinkaPelata);
    }

    public void onSettingsButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        Intent asetukset = new Intent(this, SettingsActivity.class);
        playSound(R.raw.button_sound);
        startActivity(asetukset);
    }

    public void onTrophyButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        Intent highScore = new Intent(this, TopListActivity.class);
        playSound(R.raw.pair_sound);
        startActivity(highScore);
    }

    private void playSound(int id){
        mp = MediaPlayer.create(getApplicationContext(), id);
        mp.setOnCompletionListener(this);
        mp.start();
    }

    //TODO Tästä alaspäin Miikan aivopieruja

    //Easter egg
    private int nyanClickCount = 0;
    public void onNyanButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        this.nyanClickCount++;
        if (nyanClickCount == 5) {
            playSound(R.raw.nyan);
            nyanClickCount = 0;
        }
    }

    //Easter egg
    private int trollClickCount = 0;
    public void onTrollButtonClick(View view) {
        AppUtils.vibrate(new long[]{100, 50, 100});
        this.trollClickCount++;
        if (trollClickCount == 5) {
            playSound(R.raw.troll);
            trollClickCount = 0;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.reset();
        mp.release();
    }

    @Override
    protected void onDestroy() {
        Log.i("mediaPlayer", "main activity onDestroy");
        AppUtils.releaseMediaPlayer();
        super.onDestroy();
    }
}