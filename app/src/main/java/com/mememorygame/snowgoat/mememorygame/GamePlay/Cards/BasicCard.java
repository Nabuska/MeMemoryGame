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
public class BasicCard extends Observable {

    protected ImageButton imageButton;
    public volatile SingleRunScheduledTask scheduledTask;
    public BasicCard pare;
    public boolean pareFound;
    public Drawable image;
    public Collection<BasicCard> basicCards;
    public int x;
    public int y;
    Activity activity;

    public BasicCard(Collection<BasicCard> cards, Activity activity/* Handler cardFlipHandler*/) {
        //this.cardFlipHandler = cardFlipHandler;
        this.activity = activity;
        //this.animation = AnimationUtils.loadAnimation(activity, android.R.anim.slide_out_right);
        this.basicCards = cards;
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
            imageButton.setAlpha(0.1f);
            pare.imageButton.setAlpha(0.1f);
        };
        scheduledTask = new SingleRunScheduledTask(task, 900,activity);
    }

    public void hideImageSideAfterDelay(){
        Runnable task = () -> {
            imageButton.setImageDrawable(GameActivity.backImage);
        };
        scheduledTask = new SingleRunScheduledTask(task, 900, activity);
    }
}

