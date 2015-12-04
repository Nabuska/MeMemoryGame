package com.mememorygame.snowgoat.mememorygame;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

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

        boolean backgroundMusicChecked = prefs.getBoolean(BACKGROUND_MUSIC, true);
        backgroundMusicSwitch.setChecked(backgroundMusicChecked);

        final SharedPreferences.Editor edit = prefs.edit();

        backgroundMusicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean(BACKGROUND_MUSIC, isChecked);
                edit.commit();
                AppUtils.vibrate(100, 50, 100);
            }
        });

        Switch vibratorOnSwitch = (Switch) findViewById(R.id.vibration_switch);

        boolean vibratorOnChecked = prefs.getBoolean(VIBRATOR, true);
        vibratorOnSwitch.setChecked(vibratorOnChecked);

        vibratorOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean(VIBRATOR, isChecked);
                edit.commit();
                AppUtils.vibrate(100, 50, 100);
            }
        });
    }
}
