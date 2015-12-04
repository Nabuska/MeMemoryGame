package com.mememorygame.snowgoat.mememorygame.GamePlay;

import android.util.Log;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.mememorygame.snowgoat.mememorygame.AppUtils;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.BasicCard;

import java.util.List;
import java.util.Map;

/**
 * Created by joona on 04/12/2015.
 */
public class CardTable {
    public int turnCount;
    private Map<Integer, BasicCard> cards;
    private GameActivity activity;
    private boolean started = false;

    public CardTable(Map<Integer, BasicCard> cards, GameActivity activity) {
        this.activity = activity;
        this.cards = cards;
        Stream.of(cards.values()).forEach(c -> c.getImageButton().setOnClickListener(v -> onClick(v.getId())));
    }

    //Is called everytime a visible imagebutton is clicked
    private void onClick(int id) {
        if (!started) {
            started = true;
            activity.onFirstClick();
        }
        BasicCard card = cards.get(id);
        //Jos kortteja on asetettu sulkeutumaan tietyn aika viiveen päästä, niin kiirehditään suoritusta ja suoritetaan tehtävä välittömästi
        Stream.of(cards.values()).forEach(c -> c.scheduledTask.flush());

        //Opening a visible card is not possible (NOTE it might have just been closed while flushing any scheduledTask:s)
        if (!card.imageSideUpp()) {
            AppUtils.vibrate(50, 50, 50);
            card.showImage();
            List<BasicCard> visibleCards = Stream.of(cards.values()).filter(c -> !c.pareFound && c.imageSideUpp()).collect(Collectors.toList());
            if (visibleCards.size() == 2) {
                if (Stream.of(visibleCards).anyMatch(c -> card.pare == c)) {
                    card.setPareFound();
                } else {
                    //Stream.of(visibleCards).forEach(c -> c.hideImageSideAfterDelay());
                    for(BasicCard c : visibleCards)
                        c.hideImageSideAfterDelay();
                }
                activity.setTurnCounter(++turnCount);
            } else if(visibleCards.size()>2)
                Log.i("viduiks", "EI PITÄIS TAPAHTUA");

            if (Stream.of(cards.values()).allMatch(c -> c.pareFound))
                activity.onGameOver();
        }
    }
}
