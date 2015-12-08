package com.mememorygame.snowgoat.mememorygame.TopList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.mememorygame.snowgoat.mememorygame.GamePlay.Play;
import com.mememorygame.snowgoat.mememorygame.R;

import java.util.Comparator;
import java.util.List;

/**
 * Created by WinNabuska on 5.12.2015.
 */
public class CustomTopListAdapter extends ArrayAdapter<Play> {
    private int layout;
    private Comparator<Play> comparator;
    public CustomTopListAdapter(Context context, int resource, List<Play> playObjects) {
        super(context, resource, playObjects);

        layout = resource;
    }

    public void setCompator(Comparator<Play> comparator){
        this.comparator = comparator;
    }

    @Override
    public void notifyDataSetChanged() {
        if (comparator != null){
            setNotifyOnChange(false);
            sort(comparator);
        }
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemHolder viewHolder = null;
        Play play = getItem(position);
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout,parent,false);
            ListItemHolder holder = new ListItemHolder();
            holder.row = (TextView) convertView.findViewById(R.id.row_number_listitem_textview);
            holder.name= (TextView) convertView.findViewById(R.id.name_listitem_textview);
            holder.scores=(TextView)convertView.findViewById(R.id.score_listitem_textview);
            holder.time = (TextView)convertView.findViewById(R.id.time_listitem_textview);
            holder.turns= (TextView)convertView.findViewById(R.id.turns_listitem_textview);
            holder.bonus= (TextView)convertView.findViewById(R.id.bonus_listitem_textview);
            convertView.setTag(holder);
        }
        viewHolder = (ListItemHolder) convertView.getTag();
        viewHolder.row.setText(String.valueOf(1+position));
        viewHolder.name.setText(play.name);
        viewHolder.scores.setText(String.valueOf(play.getScore()));
        viewHolder.time.setText(String.valueOf(play.gameDuration)); //TODO pitäisikö aika näyttöö sekuntteina
        viewHolder.turns.setText(String.valueOf(play.turnCount));
        viewHolder.bonus.setText(String.valueOf(play.bonus));

        return convertView;
    }

    private class ListItemHolder{
        TextView row;
        TextView name;
        TextView scores;
        TextView time;
        TextView turns;
        TextView bonus;
    }
}
