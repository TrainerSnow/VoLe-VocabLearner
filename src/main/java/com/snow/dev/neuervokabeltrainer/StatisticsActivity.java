package com.snow.dev.neuervokabeltrainer;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        ListView statisticsListView = findViewById(R.id.statisticsListView);
        ArrayList<StatisticCategory> categories = new ArrayList<>();
        JSONObject categoriesObject = Variables.statisticsJSONObject;
        JSONObject generalStats;
        JSONObject normalStats;
        JSONObject writeStats;
        Statistic numAppOpened = null;
        Statistic numChanged = null;
        Statistic numRightWriteMode = null;
        Statistic numWrongWriteMode = null;
        Statistic numTotalWriteMode = null;
        Statistic rate = null;

        try{
            generalStats = categoriesObject.getJSONObject("general");
            normalStats = categoriesObject.getJSONObject("normalmode");
            writeStats = categoriesObject.getJSONObject("writemode");
            numAppOpened = new Statistic("Appöffnungen", generalStats.getInt("numAppOpened"));
            numChanged = new Statistic("Durchgänge", normalStats.getInt("numChanged"));
            numRightWriteMode = new Statistic("Richtige Lösungen", writeStats.getInt("numRight"));
            numWrongWriteMode = new Statistic("Falsche Lösungen", writeStats.getInt("numWrong"));
            numTotalWriteMode = new Statistic("Gesamte Lösungen", writeStats.getInt("numTotal"));
                float rateF = (float) writeStats.getInt("numRight") / writeStats.getInt("numTotal");
                rate = new Statistic("Rate in %", rateF * 100);
        }catch(JSONException e) {
            e.printStackTrace();
        }

        StatisticCategory generalCategory = new StatisticCategory("Generalle Statistik", numAppOpened);
        StatisticCategory normalCategory = new StatisticCategory("Normaler Modus", numChanged);
        StatisticCategory writeCategory = new StatisticCategory("Schreibmodus", numRightWriteMode, numWrongWriteMode, numTotalWriteMode, rate);

        categories.add(generalCategory);
        categories.add(normalCategory);
        categories.add(writeCategory);

        ListViewAdapterStatisticCategory adapter = new ListViewAdapterStatisticCategory(getBaseContext(), categories, R.layout.statistics_category_row);
        statisticsListView.setAdapter(adapter);
    }
}