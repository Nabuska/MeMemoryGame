package com.mememorygame.snowgoat.mememorygame.GamePlay;

import com.annimon.stream.Optional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by joona on 04/12/2015.
 */
public class PlayDurationEvaluator {

    private long from;
    private Optional<Long> pauseStart;
    private Optional<Long> gameEnd;

    public PlayDurationEvaluator(){
        pauseStart = Optional.empty();
        gameEnd = Optional.empty();
    }

    //Kutsutaan Activityn onCreate metodista. Tämä metodi on kutsuttava kerranennenkuin mitään alemmista metodeista kutsutaan
    public void onPlayStart(){
        from = System.currentTimeMillis();
    }

    public long getPlayDuration(){
        if(from!=0) {
            if (gameEnd.isPresent())
                return gameEnd.get();
            else
                return calculatePlayDuration();
        }
        else
            return 0;
    }

    //Palauttaa tähän astisen kokonaispeliajan
    private long calculatePlayDuration() {
        return System.currentTimeMillis()-from;
    }

    //Kutsuttava joka kerta kun Activity kutsuu onPause metodia
    public void onPause(){
        pauseStart = Optional.of(System.currentTimeMillis());
    }

    //Kutsuttava joka kerta kun Activity kutsuu onResume metodia
    public void onResume(){
        if(pauseStart.isPresent()){
            long now = System.currentTimeMillis();
            long pauseLength = now-pauseStart.get();
            from +=pauseLength;
        }
    }

    //Kutsuttava heti kun peli on pelattu loppuun.
    public void onGameOver(){
        gameEnd = Optional.of(calculatePlayDuration());
    }

    //returns a list of numbers that represents elapsed, seconds, tens of seconds, minutes and tens of minutes
    public List<Integer> elapsedTimeToDigits(){
        long millis = getPlayDuration();
        List<Integer> units = Arrays.asList(1000, 10000, 60000, 600000);
        List<Integer> limits = Arrays.asList(10, 6, 10, 6);
        List<Integer> timeDigits = new ArrayList<>();

        for(int i = 0; i<4 && units.get(i)<=millis; i++){
            int reminder = (int) (millis/units.get(i));
            int value =  reminder%limits.get(i);
            timeDigits.add(value);
        }
        return timeDigits;
    }
}
