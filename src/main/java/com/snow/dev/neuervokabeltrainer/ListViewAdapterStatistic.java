package com.snow.dev.neuervokabeltrainer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapterStatistic extends ArrayAdapter<Statistic> {

    ArrayList<Statistic> statistics;
    Context context;
    int layout;
    private static final String TAG = "ListViewAdapterStatistic";

    public ListViewAdapterStatistic(Context context, ArrayList<Statistic> statistics, int layout){
        super(context, layout, statistics);
        this.context = context;
        this.statistics = statistics;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            TextView statisticNameView = convertView.findViewById(R.id.statisticsName);
            TextView statisticsValueView = convertView.findViewById(R.id.statisticsValue);

            statisticNameView.setText(statistics.get(position).getName());
            if(statistics.get(position).getValue() == -1){
                statisticsValueView.setText(String.valueOf(statistics.get(position).getValueF()));
                if(Float.isNaN(statistics.get(position).getValueF()))
                    statisticsValueView.setText(String.valueOf(0));
            }else{
                statisticsValueView.setText(String.valueOf(statistics.get(position).getValue()));
            }

        }
        return convertView;
    }
}
