package com.snow.dev.neuervokabeltrainer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapterStatisticCategory extends ArrayAdapter<StatisticCategory> {

    ArrayList<StatisticCategory> categories;
    Context context;
    int layout;

    public ListViewAdapterStatisticCategory(Context context, ArrayList<StatisticCategory> categories, int layout){
        super(context, layout, categories);
        this.context = context;
        this.categories = categories;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            TextView categoryHeaderView = convertView.findViewById(R.id.statisticsCategoryHeader);
            ListView categoryStatisticsListView = convertView.findViewById(R.id.statisticsInCategoryListView);

            categoryHeaderView.setText(categories.get(position).getHeader());

            ListViewAdapterStatistic listViewAdapterStatistic = new ListViewAdapterStatistic(context, categories.get(position).getStatistics(), R.layout.statistic_row);
            categoryStatisticsListView.setAdapter(listViewAdapterStatistic);
        }
        return convertView;
    }
}
