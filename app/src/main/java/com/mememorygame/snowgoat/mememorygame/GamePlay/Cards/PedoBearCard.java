package com.mememorygame.snowgoat.mememorygame.GamePlay.Cards;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mememorygame.snowgoat.mememorygame.AppUtils;
import com.mememorygame.snowgoat.mememorygame.GamePlay.SingleRunScheduledTask;
import com.mememorygame.snowgoat.mememorygame.R;

import java.util.Collection;
import java.util.Set;

/**
 * Created by WinNabuska on 16.11.2015.
 */
public class PedoBearCard extends BasicCard {

    public static final int IMAGE_NUMBER = 9;
    private Animation pareFoundAnim;

    public PedoBearCard(Collection<BasicCard> cards, Activity activity) {
        super(cards, activity);
        pareFoundAnim = AnimationUtils.loadAnimation(activity, R.anim.heiluminen);
    }

    @Override
    public void setImageButton(ImageButton imageButton) {
        //Pedobear kutittaa takaisin kun sitä koskee pitkään
        imageButton.setOnLongClickListener(e -> {
            AppUtils.vibrate(100, 100, 100, 50);
            return true;
        });
        super.setImageButton(imageButton);
    }

    @Override
    public void setPareFound() {
        AppUtils.vibrate(100, 100, 100, 50);
        pareFound=true;
        pare.pareFound=true;
        imageButton.startAnimation(pareFoundAnim);
        pare.imageButton.startAnimation(pareFoundAnim);
        Set<BasicCard> adjacent = Stream.of(basicCards).filter(c -> this.adjacentTo(c) && !c.pareFound).collect(Collectors.toSet());
        Stream.of(adjacent).forEach(c -> activity.runOnUiThread(() -> c.showImage()));
        Runnable task = () -> {
            Stream.of(adjacent).forEach(c -> c.hideImage());
        };
        scheduledTask = new SingleRunScheduledTask(task, 900, activity);
    }

    //Katsoo onko tarkasteltava kortti kohtisuoraa kyseisen oikealla, vasenmalla, yläpuolella tai alapuolella
    private boolean adjacentTo(BasicCard other) {
        int xDif = Math.abs(x - other.x);
        int yDif = Math.abs(y - other.y);
        boolean adjacent = xDif == 1 && yDif == 0 || yDif == 1 && xDif == 0;
        return adjacent;
    }
}
