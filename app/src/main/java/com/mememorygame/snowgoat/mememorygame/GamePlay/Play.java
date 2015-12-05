package com.mememorygame.snowgoat.mememorygame.GamePlay;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Niko/Riku/Joona on 19.11.2015.
 */
public class Play implements Comparable<Play>, Serializable {

    public final static int BEST_SCORE = 1;
    public final static int BEST_TIME = 2;
    public final static int BEST_TURNS = 3;
    public final static int WORST_SCORE = 4;
    public final static int WORST_TIME = 5;
    public final static int WORST_TURNS = 6;

    public int turnCount;
    public long gameDuration;
    public int bonus;
    public String name = "";
    private boolean difference;

    public static Comparator<Play> bestScoreComparator = (s1, s2) -> {
        int dif = s2.getScore() - s1.getScore();
        if(dif == 0) {
            dif = s1.compareTo(s2);
        }
        return dif;
    };
    public static Comparator<Play> bestTimeComparator = (s1, s2) -> {
        int dif = (int) s1.gameDuration - (int) s2.gameDuration;
        if(dif == 0) {
            dif = s2.compareTo(s1);
        }
        return dif;
    };
    public static Comparator<Play> bestTurnsComparator = (s1, s2) -> {
        int dif = s1.turnCount - s2.turnCount;
        if(dif == 0){
            dif = s2.compareTo(s1);
        }
        return dif;
    };
    public static Comparator<Play> worstTimeComparator = (s1, s2) -> {
        int dif = (int) s2.gameDuration - (int) s1.gameDuration;
        if(dif == 0){
            dif= s2.compareTo(s1);
        }
        return dif;
    };
    public static Comparator<Play> worstTurnsComparator = (s1, s2) -> {
        int dif = s2.turnCount - s1.turnCount;
        if(dif == 0){
            dif = s2.compareTo(s1);
        }
        return dif;
    };

    public Play(int turnCount, long gameDuration, int bonus) {
        this.turnCount = turnCount;
        this.gameDuration = gameDuration;
        this.bonus = bonus;

    }

    /*TODO jos suoritus yltää leaders boardille (jokin belongsTo... metodi palauttaa true) täytyy setName jossain vaiheessa tulla kutsutuksi*/
    public void setName(String name) {
        this.name = name;
    }

    // public int getBonus(){
    //   return bonus;
    //}

    public String getName() {
        return name;
    }

    // public int getTurnCount() {
    //  return turnCount;
    // }

    // public long getGameDuration() {
    //    return gameDuration;
    // }

    public int getScore() {
        return (int) ((1.0 / (int) (gameDuration / 1000)) * 100000 - 10 * turnCount) + 1000;
    }

    @Override
    public String toString() {
        return "Score: \nName = " + name + "\nPlay = " + getScore() + "\nTurns = "
                + turnCount + "\nTime  = " + gameDuration + " sec";
    }

    /*TODO kaikki alla vaatii toteuttamista*/

    public boolean belongsToBestScorePlays(List<Play> bestScorePlays) {
        if(bestScorePlays.size() < 10){
            difference = true;
        }
        else if(bestScorePlays.get(bestScorePlays.size()-1).getScore() < this.getScore()) {

            difference = true;
        }
        else if(bestScorePlays.get(bestScorePlays.size()-1).getScore() == this.getScore()){

            if (this.compareTo(bestScorePlays.get(bestScorePlays.size()-1))>0){
                difference = true;
            }
        }
        else{
            difference = false;
        }
        return difference;
    }



    public boolean belongsToBestGameDurationPlays(List<Play> bestGameDurationPlays) {
        if(bestGameDurationPlays.size() < 10){
            difference = true;
        }
        else if(bestGameDurationPlays.get(bestGameDurationPlays.size()-1).gameDuration > this.gameDuration) {

            difference = true;
        }
        else if(bestGameDurationPlays.get(bestGameDurationPlays.size()-1).gameDuration == this.gameDuration){

            if (this.compareTo(bestGameDurationPlays.get(bestGameDurationPlays.size()-1))>0){
                difference = true;
            }
        }
        else{
            difference = false;
        }
        return difference;
    }



    public boolean belongsToBestTurnCountPlays(List<Play> bestTurnCountPlays) {
        if(bestTurnCountPlays.size() < 10){
            difference = true;
        }
        else if(bestTurnCountPlays.get(bestTurnCountPlays.size()-1).turnCount > this.turnCount) {

            difference = true;
        }
        else if(bestTurnCountPlays.get(bestTurnCountPlays.size()-1).turnCount == this.turnCount){

            if (this.compareTo(bestTurnCountPlays.get(bestTurnCountPlays.size()-1))<0){
                difference = true;
            }
        }
        else{
            difference = false;
        }
        return difference;
    }



    public boolean belongsToWorstPlayDurationPlays(List<Play> worstPlayDurationPlays) {
        if(worstPlayDurationPlays.size() < 10){
            difference = true;
        }
        else if(worstPlayDurationPlays.get(worstPlayDurationPlays.size()-1).gameDuration < this.gameDuration) {

            difference = true;
        }
        else if(worstPlayDurationPlays.get(worstPlayDurationPlays.size()-1).gameDuration == this.gameDuration){

            if (this.compareTo(worstPlayDurationPlays.get(worstPlayDurationPlays.size()-1))>0){
                difference = true;
            }
        }
        else{
            difference = false;
        }
        return difference;
    }


    public boolean belongsToWorstTurnCountPlays(List<Play> worstTurnCountPlays) {
        if(worstTurnCountPlays.size() < 10){
            difference = true;
        }
        else if(worstTurnCountPlays.get(worstTurnCountPlays.size()-1).turnCount < this.turnCount) {

            difference = true;
        }
        else if(worstTurnCountPlays.get(worstTurnCountPlays.size()-1).turnCount == this.turnCount){

            if (this.compareTo(worstTurnCountPlays.get(worstTurnCountPlays.size()-1))>0){
                difference = true;
            }
        }
        else{
            difference = false;
        }
        return difference;
    }





    /*TODO toteuttakaa myös staattiset metodit*/

    public static List<Play> bestTenScorePlaysOf(List<Play> topScorePlays) {
        return Stream.of(topScorePlays).sorted(bestScoreComparator).limit(10).collect(Collectors.toList());
    }

    public static List<Play> bestTenPlayDurationPlaysOf(List<Play> topDurationPlays) {

        return Stream.of(topDurationPlays).sorted(bestTimeComparator).limit(10).collect(Collectors.toList());
    }

    public static List<Play> bestTenTurnCountPlaysOf(List<Play> topTurnCountPlays) {

        return Stream.of(topTurnCountPlays).sorted(bestTurnsComparator).limit(10).collect(Collectors.toList());
    }

    public static List<Play> worstTenPlayDurationPlaysOf(List<Play> worstTimePlays) {

        return Stream.of(worstTimePlays).sorted(worstTimeComparator).limit(10).collect(Collectors.toList());
    }

    public static List<Play> worstTenTurnCountPlaysOf(List<Play> worstTurnsPlays) {

        return Stream.of(worstTurnsPlays).sorted(worstTurnsComparator).limit(10).collect(Collectors.toList());
    }

    @Override
    public int compareTo(Play variable) {
        int dif = variable.getScore() - variable.getScore();
        if (dif == 0) {
            dif = (int) variable.gameDuration - (int) gameDuration;
            if (dif == 0)
                dif = variable.turnCount - turnCount;
        }
        return dif;
    }
}