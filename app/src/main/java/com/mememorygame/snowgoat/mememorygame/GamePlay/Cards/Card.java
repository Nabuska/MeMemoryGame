package com.mememorygame.snowgoat.mememorygame.GamePlay.Cards;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

import com.mememorygame.snowgoat.mememorygame.AppUtils;
import com.mememorygame.snowgoat.mememorygame.GamePlay.GameActivity;
import com.mememorygame.snowgoat.mememorygame.GamePlay.SingleRunScheduledTask;

import java.util.Collection;
import java.util.Observable;

/**
 * Created by WinNabuska on 11.11.2015.
 */
public class Card extends Observable {

    private ImageButton imageButton;
    public volatile SingleRunScheduledTask scheduledTask;
    public Card pare;
    public boolean pareFound;
    public Drawable image;
    public Collection<Card> cards;
    public int x;
    public int y;
    //protected Animation animation;
    Activity activity;

    public Card(Collection<Card> cards, Activity activity/* Handler cardFlipHandler*/) {
        //this.cardFlipHandler = cardFlipHandler;
        this.activity = activity;
        //this.animation = AnimationUtils.loadAnimation(activity, android.R.anim.slide_out_right);
        this.cards = cards;
        scheduledTask = new SingleRunScheduledTask(() -> {}, 0, activity); //empty task with zero delay
    }

    public ImageButton getImageButton() {
        return imageButton;
    }

    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }

    public boolean imageSideUpp() {
        return imageButton.getDrawable() == image;
    }

    public void hideImage() {
        imageButton.setImageDrawable(GameActivity.backImage);
    }

    public void showImage() {
        imageButton.setImageDrawable(image);
        //imageButton.startAnimation(animation);
        //pare.imageButton.startAnimation(animation);
    }

    public void setPareFound() {
        AppUtils.vibrate(100, 100, 100, 50);
        pareFound=true;
        pare.pareFound=true;
        Runnable task = () -> {
            afterPareFound();
        };
        scheduledTask = new SingleRunScheduledTask(task, 900,activity);
    }

    public void hideImageSideAfterDelay(){
        Runnable task = () -> {
            imageButton.setImageDrawable(GameActivity.backImage);
        };
        scheduledTask = new SingleRunScheduledTask(task, 900, activity);
    }

    protected void afterPareFound(){
        imageButton.setAlpha(0.1f);
        pare.imageButton.setAlpha(0.1f);
    }
}

