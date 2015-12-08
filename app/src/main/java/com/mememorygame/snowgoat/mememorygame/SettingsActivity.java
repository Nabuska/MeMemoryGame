package com.mememorygame.snowgoat.mememorygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    public final static String BACKGROUND_MUSIC = "BACKGROUND_MUSIC";
    public final static String VIBRATOR = "VIBRATE_ON_CLICK";
    public final static String COMMON_SETTINGS = "APP_SETTINGS123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences prefs = getSharedPreferences(COMMON_SETTINGS, MODE_PRIVATE);

        Switch backgroundMusicSwitch = (Switch) findViewById(R.id.music_switch);

        final TextView backGroundMusicStateIndicator = (TextView) findViewById(R.id.music_switch_indicator_textview);

        boolean backgroundMusicChecked = prefs.getBoolean(BACKGROUND_MUSIC, true);
        backgroundMusicSwitch.setChecked(backgroundMusicChecked);
        if (backgroundMusicChecked)
            backGroundMusicStateIndicator.setText("ON");
        else
            backGroundMusicStateIndicator.setText("OFF");

        final SharedPreferences.Editor edit = prefs.edit();

        backgroundMusicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean(BACKGROUND_MUSIC, isChecked);
                edit.commit();
                AppUtils.vibrate(100, 50, 100);
                if (isChecked)
                    backGroundMusicStateIndicator.setText("ON");
                else
                    backGroundMusicStateIndicator.setText("OFF");
            }
        });

        Switch vibratorOnSwitch = (Switch) findViewById(R.id.vibration_switch);
        final TextView vibratorStateIndicator = (TextView) findViewById(R.id.vibrator_switch_indicator_textview);

        boolean vibratorOnChecked = prefs.getBoolean(VIBRATOR, true);
        vibratorOnSwitch.setChecked(vibratorOnChecked);

        if (vibratorOnChecked)
            vibratorStateIndicator.setText("ON");
        else
            vibratorStateIndicator.setText("OFF");

        vibratorOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean(VIBRATOR, isChecked);
                edit.commit();
                AppUtils.vibrate(100, 50, 100);
                if (isChecked)
                    vibratorStateIndicator.setText("ON");
                else
                    vibratorStateIndicator.setText("OFF");
            }
        });
    }

    public void onReturnButtonClick(View view) {
        AppUtils.vibrate(100, 50, 100);
        MainMenuActivity.mp = MediaPlayer.create(getApplicationContext(), R.raw.button_sound);
        MainMenuActivity.mp.start();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.startMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtils.pauseMediaPlayer();
    }
}
