package com.mememorygame.snowgoat.mememorygame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;

/**
 * Created by WinNabuska on 18.11.2015.
 */
public class AppUtils {

    private static Vibrator vibrator;
    private static MediaPlayer mediaPlayer;
    private static boolean vibratorOn;
    private static boolean musicOn;
    private static SharedPreferences.OnSharedPreferenceChangeListener listener;
    //ANDROID BUGI Listener on pakko esitellä luokan muuttujana muuten se päätyy roskienkeruuseen

    private AppUtils(){}

    public static void setMediaPlayer(MediaPlayer mediaPlayer){
        if(AppUtils.mediaPlayer!=null)
            AppUtils.mediaPlayer.release();
        AppUtils.mediaPlayer = mediaPlayer;
        mediaPlayer.setLooping(true);
    }

    public static void init(Activity activity){
        vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
        SharedPreferences prefs = activity.getSharedPreferences(SettingsActivity.COMMON_SETTINGS, Context.MODE_PRIVATE);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i("prefChange", "change " + key);
                switch (key){
                    case SettingsActivity.BACKGROUND_MUSIC:
                        musicOn = sharedPreferences.getBoolean(key, true);
                        if (musicOn)
                            mediaPlayer.start();
                        else
                            pauseMediaPlayer();
                        break;
                    case SettingsActivity.VIBRATOR:
                        vibratorOn = sharedPreferences.getBoolean(key,true);
                        break;
                    default:
                        break;

                }
            }
        };
        prefs.registerOnSharedPreferenceChangeListener(listener);
        vibratorOn = prefs.getBoolean(SettingsActivity.VIBRATOR, true);
        musicOn = prefs.getBoolean(SettingsActivity.BACKGROUND_MUSIC,true);
    }

    public static void startMediaPlayer(){
        if(musicOn && !mediaPlayer.isPlaying())
            mediaPlayer.start();
    }

    public static void pauseMediaPlayer(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
    }

    public static void releaseMediaPlayer(){
        if(mediaPlayer!=null) {
            Log.i("myMediaPlayer", "release");
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void vibrate(long... pattern){
        if(vibratorOn)
            vibrator.vibrate(pattern, -1);
    }

    public static boolean musicIsPlaying(){
        return mediaPlayer!=null && mediaPlayer.isPlaying();
    }
}