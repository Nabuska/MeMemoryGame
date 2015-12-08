package com.mememorygame.snowgoat.mememorygame.TopList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import com.mememorygame.snowgoat.mememorygame.AppUtils;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Play;
import com.mememorygame.snowgoat.mememorygame.R;
import com.mememorygame.snowgoat.mememorygame.SavingAndLoading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TopListActivity extends AppCompatActivity {

    public static final String PLAY_INTENT_EXTRA = "PLAY_INTENT_EXTRA";

    private final String SCORES_TAB = "SCORES_TAB", DURATIONS_TAB = "DURATIONS_TAB", TURNS_TAB = "TURNS_TAB";
    private final String WINS_TAB = "WINS_TAB", FAILS_TAB = "FAILS_TAB";
    private final int SCORES_TAB_INDEX = 0, DURATIONS_TAB_INDEX = 1, TURNS_TAB_INDEX = 2;
    private TabHost categoryTabHost, typeTabHost;
    private ListView listView;
    private Map<Integer, List<Play>> topPlays = null;
    private List<Play> listviewContent;
    private CustomTopListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        listView = (ListView) findViewById(R.id.leader_board_listview);
        SavingAndLoading dataHandling = new SavingAndLoading(this);
        if(topPlays == null){
            topPlays = dataHandling.load();

            Play latestPlay = (Play) getIntent().getSerializableExtra(PLAY_INTENT_EXTRA);
            boolean changesMade = false;
            if(latestPlay!=null) {

                getIntent().removeExtra(PLAY_INTENT_EXTRA);

                if (latestPlay.belongsToBestScorePlays(topPlays.get(Play.BEST_SCORE))) {
                    topPlays.get(Play.BEST_SCORE).add(latestPlay);
                    List<Play> newTopTen = Play.bestTenScorePlaysOf(topPlays.get(Play.BEST_SCORE));
                    topPlays.put(Play.BEST_SCORE, newTopTen);
                    changesMade = true;
                }
                if (latestPlay.belongsToBestGameDurationPlays(topPlays.get(Play.BEST_TIME))) {
                    topPlays.get(Play.BEST_TIME).add(latestPlay);
                    List<Play> newTopTen = Play.bestTenPlayDurationPlaysOf(topPlays.get(Play.BEST_TIME));
                    topPlays.put(Play.BEST_TIME, newTopTen);
                    changesMade = true;
                }
                if (latestPlay.belongsToBestTurnCountPlays(topPlays.get(Play.BEST_TURNS))) {
                    topPlays.get(Play.BEST_TURNS).add(latestPlay);
                    List<Play> newTopTen = Play.bestTenTurnCountPlaysOf(topPlays.get(Play.BEST_TURNS));
                    topPlays.put(Play.BEST_TURNS, newTopTen);
                    changesMade = true;
                }
                if (latestPlay.belongsToWorstPlayDurationPlays(topPlays.get(Play.WORST_TIME))) {
                    topPlays.get(Play.WORST_TIME).add(latestPlay);
                    List<Play> newTopTen = Play.worstTenPlayDurationPlaysOf(topPlays.get(Play.WORST_TIME));
                    topPlays.put(Play.WORST_TIME, newTopTen);
                    changesMade = true;
                }
                if (latestPlay.belongsToWorstTurnCountPlays(topPlays.get(Play.WORST_TURNS))) {
                    topPlays.get(Play.WORST_TURNS).add(latestPlay);
                    List<Play> newTopTen = Play.worstTenTurnCountPlaysOf(topPlays.get(Play.WORST_TURNS));
                    topPlays.put(Play.WORST_TURNS, newTopTen);
                    changesMade = true;
                }
            }

            if(changesMade == true) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Your name: ");
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        latestPlay.name = input.getText().toString();
                        dataHandling.save(topPlays);
                        loadListViewWith(SCORES_TAB, WINS_TAB);
                    }
                });
                builder.show();
            }
        }

        initCategoryTabHost();
        initTypeTabHost();

        listviewContent = new ArrayList<>(topPlays.get(Play.BEST_SCORE));
        listAdapter = new CustomTopListAdapter(this, R.layout.toplist_list_item_layout, listviewContent);
        listView.setAdapter(listAdapter);
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

    private void initTypeTabHost() {
        typeTabHost = (TabHost)findViewById(R.id.type_tab_host);
/*        Intent intent;
        intent = new Intent().setClass(this, TopListActivity.class);*/
        typeTabHost.setup();

        TabHost.TabSpec winTab = typeTabHost.newTabSpec(WINS_TAB);
        winTab.setContent(R.id.wins_tab);
        winTab.setIndicator("", getResources().getDrawable(R.drawable.wins_selector));
        typeTabHost.addTab(winTab);

        TabHost.TabSpec failTab = typeTabHost.newTabSpec(FAILS_TAB);
        failTab.setContent(R.id.fails_tab);
        failTab.setIndicator("", getResources().getDrawable(R.drawable.fails_selector));
        typeTabHost.addTab(failTab);
        typeTabHost.setOnTabChangedListener(id -> {
            if (id.equals(FAILS_TAB)) {
                categoryTabHost.getTabWidget().getChildTabViewAt(SCORES_TAB_INDEX).setVisibility(View.GONE);
                if (categoryTabHost.getCurrentTabTag().equals(SCORES_TAB))
                    categoryTabHost.setCurrentTabByTag(DURATIONS_TAB);
            }
            else //Wins Tab
                categoryTabHost.getTabWidget().getChildTabViewAt(SCORES_TAB_INDEX).setVisibility(View.VISIBLE);

            loadListViewWith(categoryTabHost.getCurrentTabTag(), typeTabHost.getCurrentTabTag());
        });
    }

    private void initCategoryTabHost(){
        categoryTabHost = (TabHost)findViewById(R.id.category_tab_host);
        categoryTabHost.setup();

        TabHost.TabSpec scoresTabSpec = categoryTabHost.newTabSpec(SCORES_TAB);
        scoresTabSpec.setContent(R.id.scores_tab);
        scoresTabSpec.setIndicator("", getResources().getDrawable(R.drawable.score_selector));
        categoryTabHost.addTab(scoresTabSpec);

        TabHost.TabSpec gameDurationTabSpec = categoryTabHost.newTabSpec(DURATIONS_TAB);
        gameDurationTabSpec.setContent(R.id.play_duration_tab);
        gameDurationTabSpec.setIndicator("", getResources().getDrawable(R.drawable.time_selector));
        categoryTabHost.addTab(gameDurationTabSpec);

        TabHost.TabSpec turnCountTabSpec = categoryTabHost.newTabSpec(TURNS_TAB);
        turnCountTabSpec.setContent(R.id.play_duration_tab);
        turnCountTabSpec.setIndicator("", getResources().getDrawable(R.drawable.turns_selector));
        categoryTabHost.addTab(turnCountTabSpec);

        categoryTabHost.setOnTabChangedListener(id -> {
            loadListViewWith(categoryTabHost.getCurrentTabTag(), typeTabHost.getCurrentTabTag());
        });
    }

    private void loadListViewWith(final String CATEGORY, final String TYPE){
        listviewContent.clear();
        Comparator<Play> comparator;
        int contentType;
        switch (CATEGORY){
            case SCORES_TAB:
                comparator=Play.bestScoreComparator;
                contentType = Play.BEST_SCORE;
                break;
            case DURATIONS_TAB:
                if(TYPE.equals(WINS_TAB)){
                    comparator=Play.bestTimeComparator;
                    contentType=Play.BEST_TIME;
                }
                else{
                    comparator = Play.worstTimeComparator;
                    contentType = Play.WORST_TIME;
                }
                break;
            case TURNS_TAB:
                if(TYPE.equals(WINS_TAB)){
                    comparator = Play.bestTurnsComparator;
                    contentType = Play.BEST_TURNS;
                }
                else{
                    comparator = Play.worstTurnsComparator;
                    contentType = Play.WORST_TURNS;
                }
                break;
            default:
                throw new Error();
        }
        listAdapter.setCompator(comparator);
        listviewContent.addAll(topPlays.get(contentType));
        listAdapter.notifyDataSetChanged();
    }
}