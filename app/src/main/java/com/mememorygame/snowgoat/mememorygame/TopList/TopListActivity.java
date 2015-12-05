package com.mememorygame.snowgoat.mememorygame.TopList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import com.mememorygame.snowgoat.mememorygame.GamePlay.Play;
import com.mememorygame.snowgoat.mememorygame.R;
import com.mememorygame.snowgoat.mememorygame.SavingAndLoading;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TopListActivity extends AppCompatActivity {
    private final String SCORES_TAB_ID = "SCORES_TAB", DURATIONS_TAB_ID = "DURATIONS_TAB", TURNS_TAB_ID = "TURNS_TAB";
    private final String WINS_TAB_ID = "WINS_TAB", FAILS_TAB_ID = "FAILS_TAB";
    private final int WINS_TAB_INDEX = 0, FAILS_TAB_INDEX = 1;
    private final int SCORES_TAB_INDEX = 0, DURATIONS_TAB_INDEX = 1, TURNS_TAB_INDEX = 2;
    private TabHost categoryTabHost, typeTabHost;
    private ListView listView;
    private Map<Integer, List<Play>> topPlays = null;
    private List<Play> currentContent;
    private CustomTopListAdapter listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_list);
        listView = (ListView) findViewById(R.id.leader_board_listview);
        SavingAndLoading dataHandling = new SavingAndLoading(this);
        if(topPlays == null){
            topPlays = dataHandling.load();

            Play latestPlay = (Play) getIntent().getSerializableExtra("PLAY");
            boolean changesMade = false;
            if(latestPlay!=null) {
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
                        loadListViewWith(Play.BEST_SCORE);
                    }
                });
                builder.show();
            }
        }

        categoryTabHost = (TabHost)findViewById(R.id.category_tab_host);
        categoryTabHost.setup();

        TabHost.TabSpec scoresTabSpec = categoryTabHost.newTabSpec(SCORES_TAB_ID);
        scoresTabSpec.setContent(R.id.scores_tab);
        scoresTabSpec.setIndicator("Scores");
        categoryTabHost.addTab(scoresTabSpec);

        TabHost.TabSpec gameDurationTabSpec = categoryTabHost.newTabSpec(DURATIONS_TAB_ID);
        gameDurationTabSpec.setContent(R.id.play_duration_tab);
        gameDurationTabSpec.setIndicator("Durations");
        categoryTabHost.addTab(gameDurationTabSpec);

        TabHost.TabSpec turnCountTabSpec = categoryTabHost.newTabSpec(TURNS_TAB_ID);
        turnCountTabSpec.setContent(R.id.play_duration_tab);
        turnCountTabSpec.setIndicator("Turns");
        categoryTabHost.addTab(turnCountTabSpec);

        categoryTabHost.setOnTabChangedListener(id -> {
            switch (id) {
                case SCORES_TAB_ID:
                    loadListViewWith(Play.BEST_SCORE);
                    break;
                case DURATIONS_TAB_ID:
                    if(typeTabHost.getCurrentTabTag().equals(WINS_TAB_ID))
                        loadListViewWith(Play.BEST_TIME);
                    else
                        loadListViewWith(Play.WORST_TIME);
                    break;
                case TURNS_TAB_ID:
                    if(typeTabHost.getCurrentTabTag().equals(WINS_TAB_ID))
                        loadListViewWith(Play.BEST_TURNS);
                    else
                        loadListViewWith(Play.WORST_TURNS);
                    break;
                default:
                    throw new Error("categoryTabHost default case error");
            }
        });

        typeTabHost = (TabHost)findViewById(R.id.type_tab_host);
        typeTabHost.setup();

        TabHost.TabSpec winTab = typeTabHost.newTabSpec(WINS_TAB_ID);
        winTab.setContent(R.id.wins_tab);
        winTab.setIndicator("Wins");
        typeTabHost.addTab(winTab);

        TabHost.TabSpec failTab = typeTabHost.newTabSpec(FAILS_TAB_ID);
        failTab.setContent(R.id.fails_tab);
        failTab.setIndicator("Fails");
        typeTabHost.addTab(failTab);
        typeTabHost.setOnTabChangedListener(id -> {
            if (id.equals(FAILS_TAB_ID)) {
                categoryTabHost.getTabWidget().getChildTabViewAt(SCORES_TAB_INDEX).setVisibility(View.GONE);
                if (categoryTabHost.getCurrentTabTag().equals(SCORES_TAB_ID))
                    categoryTabHost.setCurrentTabByTag(DURATIONS_TAB_ID);
            } else //Wins Tab
                categoryTabHost.getTabWidget().getChildTabViewAt(SCORES_TAB_INDEX).setVisibility(View.VISIBLE);
        });
        /*TODO REMOVE DEBUGLoad*/
        currentContent = new ArrayList<>(topPlays.get(Play.BEST_SCORE));
        listAdapter = new CustomTopListAdapter(this, R.layout.toplist_list_item_layout,currentContent);
        listView.setAdapter(listAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadListViewWith(final int playLeaderBoard){

        currentContent.clear();
        currentContent.addAll(topPlays.get(playLeaderBoard));
        switch (playLeaderBoard){

            case Play.BEST_SCORE:
                listAdapter.setCompator(Play.bestScoreComparator);
                break;

            case Play.BEST_TIME:
                listAdapter.setCompator(Play.bestTimeComparator);
                break;

            case Play.BEST_TURNS:
                listAdapter.setCompator(Play.bestTurnsComparator);
                break;

            case Play.WORST_TIME:
                listAdapter.setCompator(Play.worstTimeComparator);
                break;

            case Play.WORST_TURNS:
                listAdapter.setCompator(Play.worstTurnsComparator);
                break;

            default:break;
        }

        listAdapter.notifyDataSetChanged();
    }
}