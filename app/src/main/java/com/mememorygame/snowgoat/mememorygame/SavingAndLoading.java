package com.mememorygame.snowgoat.mememorygame;

import android.content.Context;

import com.mememorygame.snowgoat.mememorygame.GamePlay.Play;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WinNabuska on 5.12.2015.
 */
public class SavingAndLoading {
    private Context context;
    public SavingAndLoading(Context context){
        this.context = context;
    }

    public void save(Map hScore){
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("savings", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(hScore);
            os.close();

        }
        catch (IOException e){
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Map<Integer, List<Play>> load() {
        FileInputStream fis = null;
        ObjectInputStream objIn = null;

        Map<Integer, List<Play>> hLista = null;
        try {
            fis = context.openFileInput("savings");
            objIn = new ObjectInputStream(fis);
            hLista = (Map<Integer, List<Play>>) objIn.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (objIn != null)
                    objIn.close();
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(hLista==null){
            hLista = new HashMap<>();
            hLista.put(Play.BEST_SCORE, new ArrayList<>());
            hLista.put(Play.BEST_TIME, new ArrayList<>());
            hLista.put(Play.BEST_TURNS, new ArrayList<>());
            hLista.put(Play.WORST_TIME, new ArrayList<>());
            hLista.put(Play.WORST_TURNS, new ArrayList<>());
        }


        return hLista;//sort(hLista);
    }
/*
    private Map<Integer, List<Play>> sort(Set<Play> hScores) {
        Map<Integer, List<Play>> sortedScores = new HashMap<>();
        Comparator<Play> bestScore = (s1, s2) -> {
            int dif = s1.getScore() - s2.getScore();
            if (dif == 0)
                dif = (int) s2.getTime() - (int) s1.getTime();
            return dif;
        };
        Comparator<Play> bestTime = (s1, s2) -> {
            int dif = (int) s2.getTime() - (int) s1.getTime();
            if (dif == 0)
                dif = s1.getScore() - s2.getScore();
            return dif;
        };
        Comparator<Play> bestTurns = (s1, s2) -> {
            int dif = s2.getTurns() - s1.getTurns();
            if (dif == 0)
                dif = s1.getScore() - s2.getScore();
            return dif;
        };


        Comparator<Play> worstScore = (s1, s2) -> {
            int dif = s2.getScore() - s1.getScore();
            if (dif == 0)
                dif = (int) s1.getTime() - (int) s2.getTime();
            return dif;
        };


        Comparator<Play> worstTime = (s1, s2) -> {
            int dif = (int) s1.getTime() - (int) s2.getTime();
            if (dif == 0)
                dif = s2.getScore() - s1.getScore();
            return dif;
        };


        Comparator<Play> worstTurns = (s1, s2) -> {
            int dif = s1.getTurns() - s2.getTurns();
            if (dif == 0)
                dif = s2.getScore() - s1.getScore();
            return dif;
        };



        List<Play> besthScoreList = Stream.of(hScores).sorted(bestScore).limit(10).collect(Collectors.toList());
        List<Play> bestTimeList = Stream.of(hScores).sorted(bestTime).limit(10).collect(Collectors.toList());
        List<Play> bestTurnsList = Stream.of(hScores).sorted(bestTurns).limit(10).collect(Collectors.toList());

        List<Play> worstTimeList = Stream.of(hScores).sorted(worstTime).limit(10).collect(Collectors.toList());
        List<Play> worstTurnsList = Stream.of(hScores).sorted(worstTurns).limit(10).collect(Collectors.toList());

        sortedScores.put(Play.BEST_SCORE, besthScoreList);
        sortedScores.put(Play.BEST_TIME, bestTimeList);
        sortedScores.put(Play.BEST_TURNS, bestTurnsList);
        sortedScores.put(Play.WORST_SCORE, worstScoreList);
        sortedScores.put(Play.WORST_TIME, worstTimeList);
        sortedScores.put(Play.WORST_TURNS, worstTurnsList);

        return sortedScores;
    }*/
}





