package com.mememorygame.snowgoat.mememorygame.GamePlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.annimon.stream.function.Function;
import com.mememorygame.snowgoat.mememorygame.AppUtils;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.AsianCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.BasicCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.ChallAcceptedCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.CryingCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.DogeCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.ForeverAloneCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.GrumpyCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.LolGuyCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.MeGustaCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.MotherOfGodCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.NyanCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.PedoBearCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.PepeCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.RageCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.ThinkingCard;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Cards.TrollCard;
import com.mememorygame.snowgoat.mememorygame.MainMenuActivity;
import com.mememorygame.snowgoat.mememorygame.R;
import com.mememorygame.snowgoat.mememorygame.TopList.TopListActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends Activity {

    /*TODO miikan tekemät muutokset ja uudet korttiluokat täytyy integroida*/

    private String LOG = "GameActivity";

    private Play play;
    private PlayDurationEvaluator durationEvaluator;
    private CardTable cardTable;

    private List<ImageView> timeImageViews;
    private List<ImageView> turnCountImageViews;
    private List<Drawable> numberImages;

    public static Drawable backImage;
    private int ROWS = 6, COLUMNS = 5;
    private ImageView minuteSecondSeparator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        durationEvaluator = new PlayDurationEvaluator();
        //MediaPlayer.create(this, R.raw.theme).start();
        backImage = getResources().getDrawable(R.drawable.memorycard_backimage);

        initDigitImageViews();

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);
        List<ImageButton> imageButtons = loadTablesViews(tableLayout);
        List<Drawable> images = getCardImages();
        Map<Integer, BasicCard> cards = createCards(images, imageButtons);
        cardTable = new CardTable(cards, this);
    }

    //Alustetaan säije päivittämään ajan näyttö sekunnin välein
    protected void onFirstClick(){
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                List<Integer> digits = durationEvaluator.elapsedTimeToDigits();
                List<ImageView> relevantViews = timeImageViews.subList(0, digits.size());
                Runnable digitUpdate = () -> {
                    if(digits.size()>2 && minuteSecondSeparator.getVisibility()== View.INVISIBLE)
                        minuteSecondSeparator.setVisibility(View.VISIBLE);
                    for(int i = 0; i<digits.size(); i++){
                        ImageView imgView = relevantViews.get(i);
                        if(imgView.getVisibility()==View.INVISIBLE)
                            imgView.setVisibility(View.VISIBLE);
                        imgView.setImageDrawable(numberImages.get(digits.get(i)));
                    }
                };
                GameActivity.this.runOnUiThread(digitUpdate);
            }
        };
        durationEvaluator.onPlayStart();
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }


    public void onGameOver(){
        durationEvaluator.onGameOver();

        Log.i("info", "game over");
        play = new Play(cardTable.turnCount,durationEvaluator.getPlayDuration(), 0 /*TODO implement bonus points*/);
        new AlertDialog.Builder(this).setTitle("Scores")
                .setMessage("Time: " + (play.gameDuration/1000) +" sec\nTurns: " +cardTable.turnCount+"\nPoints: "+play.getScore())
                .setCancelable(false)
                .setIcon(R.drawable.meme_grumpy_v1)
                .setPositiveButton("Continue", (d, id) -> {
                    d.dismiss();
                    Intent topListIntent = new Intent(this, TopListActivity.class);
                    topListIntent.putExtra(TopListActivity.PLAY_INTENT_EXTRA, play);
                    startActivity(topListIntent);
                    finish();
                })
                .create().show();
    }

    //kutsuttava CardTable:sta joka kerta kun kaksi korttia on käännetty
    public void setTurnCounter(int count){
        String strCount = String.valueOf(count);
        for(int i = 0; i<3 && i<strCount.length(); i++){
            ImageView imgV = turnCountImageViews.get(i);
            String digitChar = strCount.charAt(i)+"";
            Drawable digitImage = numberImages.get(Integer.valueOf(digitChar));
            if(imgV.getVisibility()==View.INVISIBLE)
                imgV.setVisibility(View.VISIBLE);
            turnCountImageViews.get(i).setImageDrawable(digitImage);
        }
    }

    @Override
    protected void onResume() {
        durationEvaluator.onResume();
        super.onResume();
        AppUtils.startMediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppUtils.pauseMediaPlayer();
        durationEvaluator.onPause();
    }

    private List<ImageButton> loadTablesViews(TableLayout tableLayout) {
        List<ImageButton> imageButtons = new ArrayList<>();

        int displayWidthPixels = getResources().getDisplayMetrics().widthPixels;

        int maxTilt = 15;

        int width = displayWidthPixels/COLUMNS-(displayWidthPixels/(COLUMNS*20));


        int height = (int)Math.round(width * 1.2);//(int)Math.round(Math.sqrt(Math.pow(width,2)+Math.pow(catetY,2)))+1;
        //tableLayout.setClipChildren(false);
        //tableLayout.setClipToPadding(false);

        Random random = new Random();
        for (int y = 0; y < ROWS; y++) {
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams tableRowLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            tableRow.setLayoutParams(tableRowLayoutParams);
            tableRow.setHorizontalGravity(Gravity.CENTER);
            //tableRow.setClipChildren(false);
            //tableRow.setClipToPadding(false);

            for (int x = 0; x < COLUMNS; x++) {
                ImageButton imageButton = new ImageButton(this, null, R.style.img_btn_5x6_style);
                TableRow.LayoutParams imgBtnLayoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                imageButton.setLayoutParams(imgBtnLayoutParams);
                //((ViewGroup.MarginLayoutParams) imageButton.getLayoutParams()).setMargins(0, 0, 0, 0);
                imgBtnLayoutParams.width = width;
                imgBtnLayoutParams.height = height;
                imageButton.setImageDrawable(backImage);
                imageButton.setRotation(random.nextInt(maxTilt * 2) - maxTilt);
                //imageButton.setAnimation(animation);
                //scale(imageButton);
                imageButton.setBackgroundColor(Color.TRANSPARENT);
                tableRow.addView(imageButton);
                int id = (y) * COLUMNS + x;
                imageButton.setId(id);
                imageButtons.add(imageButton);
            }
            tableLayout.addView(tableRow);
        }
        return imageButtons;
    }

    private void initDigitImageViews() {
        loadDigitImages();
        timeImageViews = new ArrayList<>();
        timeImageViews.add((ImageView) findViewById(R.id.time_imageview_singleseconds));
        timeImageViews.add((ImageView) findViewById(R.id.time_imageview_tensofseconds));
        timeImageViews.add((ImageView) findViewById(R.id.time_imageview_singleminutes));
        timeImageViews.add((ImageView) findViewById(R.id.time_imageview_tensofminutes));

        minuteSecondSeparator = (ImageView) findViewById(R.id.time_imageview_secondminute_separator);

        ImageView turnCount1 = (ImageView) findViewById(R.id.turncount_imageview1);
        ImageView turnCount2 = (ImageView) findViewById(R.id.turncount_imageview2);
        ImageView turnCount3 = (ImageView) findViewById(R.id.turncount_imageview3);

        turnCountImageViews = Arrays.asList(turnCount1, turnCount2, turnCount3);
    }

    private void loadDigitImages(){
        numberImages = new ArrayList<>();
        numberImages.add(getResources().getDrawable(R.drawable.number_zero_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_one_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_two_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_three_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_four_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_five_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_six_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_seven_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_eight_v1));
        numberImages.add(getResources().getDrawable(R.drawable.number_nine_v1));
    }

    private List<Drawable> getCardImages() {
        List<Drawable> images = new ArrayList<>();
        images.add(getResources().getDrawable(R.drawable.meme_crying_v1));  //0
        images.add(getResources().getDrawable(R.drawable.meme_doge_v1));   //1
        images.add(getResources().getDrawable(R.drawable.meme_forever_alone_v1));   //2
        images.add(getResources().getDrawable(R.drawable.meme_grumpy_v1));  //3
        images.add(getResources().getDrawable(R.drawable.meme_lolguy_v1));   //4
        images.add(getResources().getDrawable(R.drawable.meme_megusta_v1));   //5
        images.add(getResources().getDrawable(R.drawable.meme_challenge_accepted_v1));  //6
        images.add(getResources().getDrawable(R.drawable.meme_mother_of_god_v1));   //7
        images.add(getResources().getDrawable(R.drawable.meme_nyan_v1));    //8
        images.add(getResources().getDrawable(R.drawable.meme_bedobear_v1));    //9
        images.add(getResources().getDrawable(R.drawable.meme_pepe_v1));    //10
        images.add(getResources().getDrawable(R.drawable.meme_rage_v1));    //11
        images.add(getResources().getDrawable(R.drawable.meme_thinking_v1));    //12
        images.add(getResources().getDrawable(R.drawable.meme_troll_v1));   //13
        images.add(getResources().getDrawable(R.drawable.meme_asian_v1));   //14
        return images;
    }

    private Map<Integer, BasicCard> createCards(List<Drawable> images, List<ImageButton> imageButtons){
        List<ImageButton> unAssignedImageButtons = new ArrayList<>(imageButtons);

        Map<Integer, BasicCard> cards = new HashMap<>();
        int imageNumber = 0;

        Function<ImageButton, Integer> getRow = iBtn -> iBtn.getId()/COLUMNS;
        Function<ImageButton, Integer> getColumn = iBtn -> iBtn.getId()%COLUMNS;

        while(unAssignedImageButtons.size()>0){
            BasicCard[] pare = new BasicCard[2];
            for(int i = 0; i<2; i++){
                BasicCard card;
                switch (imageNumber){
                    case PedoBearCard.IMAGE_NUMBER:
                        card = new PedoBearCard(cards.values(), this);
                        break;
                    case ForeverAloneCard.IMAGE_NUMBER:
                        card = new ForeverAloneCard(cards.values(), this);
                        break;
                    case RageCard.IMAGE_NUMBER:
                        card = new RageCard(cards.values(), this);
                        break;
                    case ChallAcceptedCard.IMAGE_NUMBER:
                        card = new ChallAcceptedCard(cards.values(), this);
                        break;
                    case GrumpyCard.IMAGE_NUMBER:
                        card = new GrumpyCard(cards.values(), this);
                        break;
                    case MotherOfGodCard.IMAGE_NUMBER:
                        card = new MotherOfGodCard(cards.values(), this);
                        break;
                    case NyanCard.IMAGE_NUMBER:
                        card = new NyanCard(cards.values(), this);
                        break;
                    case PepeCard.IMAGE_NUMBER:
                        card = new PepeCard(cards.values(), this);
                        break;
                    case TrollCard.IMAGE_NUMBER:
                        card = new TrollCard(cards.values(), this);
                        break;
                    case AsianCard.IMAGE_NUMBER:
                        card = new AsianCard(cards.values(), this);
                        break;
                    case ThinkingCard.IMAGE_NUMBER:
                        card = new ThinkingCard(cards.values(), this);
                        break;
                    case DogeCard.IMAGE_NUMBER:
                        card = new DogeCard(cards.values(), this);
                        break;
                    case LolGuyCard.IMAGE_NUMBER:
                        card = new LolGuyCard(cards.values(), this);
                        break;
                    case MeGustaCard.IMAGE_NUMBER:
                        card = new MeGustaCard(cards.values(), this);
                        break;
                    case CryingCard.IMAGE_NUMBER:
                        card = new CryingCard(cards.values(), this);
                        break;
                    default:
                        card = new BasicCard(cards.values(), this);
                }
                pare[i] = card;

                ImageButton imgButton = unAssignedImageButtons.remove((int) (Math.random() * unAssignedImageButtons.size()));
                card.x = getColumn.apply(imgButton);
                card.y = getRow.apply(imgButton);
                card.setImageButton(imgButton);

                cards.put(imgButton.getId(), card);

                if(i>0){
                    pare[0].pare = pare[1];
                    pare[1].pare = pare[0];
                }
                card.image = images.get(imageNumber);
            }
            imageNumber++;
        }
        return cards;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}