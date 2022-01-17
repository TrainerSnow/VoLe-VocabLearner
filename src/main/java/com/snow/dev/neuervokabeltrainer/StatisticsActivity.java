package com.snow.dev.neuervokabeltrainer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        getSupportActionBar().setTitle(getResources().getString(R.string.statistics));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        ListView statisticsListView = findViewById(R.id.statisticsListView);
        ArrayList<StatisticCategory> categories = new ArrayList<>();
        ListViewAdapterStatisticCategory adapter = new ListViewAdapterStatisticCategory(getBaseContext(), categories, R.layout.statistics_category_row);
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
        Statistic numHighestHighScore = null;
        Statistic bestSet = null;
        Button resetStatsButton = findViewById(R.id.resetStatsButton);
        int highestHighScore = getHighestHightscore();
        String bestVocSetName = getBestVocSetName();

        resetStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categories.get(0).getStatistics().get(0).setValue(0);
                categories.get(1).getStatistics().get(0).setValue(0);
                categories.get(2).getStatistics().get(0).setValue(0);
                categories.get(2).getStatistics().get(1).setValue(0);
                categories.get(2).getStatistics().get(2).setValue(0);

                try{
                    Variables.statisticsJSONObject.getJSONObject("general").put("numAppOpened", 0);
                    Variables.statisticsJSONObject.getJSONObject("normalmode").put("numChanged", 0);
                    Variables.statisticsJSONObject.getJSONObject("writemode").put("numRight", 0);
                    Variables.statisticsJSONObject.getJSONObject("writemode").put("numWrong", 0);
                    Variables.statisticsJSONObject.getJSONObject("writemode").put("numTotal", 0);
                }catch(JSONException e){
                    e.printStackTrace();
                }
                updateStatJSONFile(Variables.statisticsJSONObject);
                statisticsListView.setAdapter(adapter);
            }
        });

        try{
            generalStats = categoriesObject.getJSONObject("general");
            normalStats = categoriesObject.getJSONObject("normalmode");
            writeStats = categoriesObject.getJSONObject("writemode");
            numAppOpened = new Statistic(getResources().getString(R.string.app_open_number), generalStats.getInt("numAppOpened"));
            numHighestHighScore = new Statistic(getResources().getString(R.string.highest_highscore), highestHighScore);
            bestSet = new Statistic(getResources().getString(R.string.best_voc_set), bestVocSetName);
            numChanged = new Statistic(getResources().getString(R.string.iterations), normalStats.getInt("numChanged"));
            numRightWriteMode = new Statistic(getResources().getString(R.string.right_solutions), writeStats.getInt("numRight"));
            numWrongWriteMode = new Statistic(getResources().getString(R.string.wrong_solutions), writeStats.getInt("numWrong"));
            numTotalWriteMode = new Statistic(getResources().getString(R.string.total_solutions), writeStats.getInt("numTotal"));
                float rateF = (float) writeStats.getInt("numRight") / writeStats.getInt("numTotal");
                rate = new Statistic(getResources().getString(R.string.rate_in_percent), rateF * 100);
        }catch(JSONException e) {
            e.printStackTrace();
        }

        StatisticCategory generalCategory = new StatisticCategory(getResources().getString(R.string.general_stats), numAppOpened, numHighestHighScore, bestSet);
        StatisticCategory normalCategory = new StatisticCategory(getResources().getString(R.string.normal_mode), numChanged);
        StatisticCategory writeCategory = new StatisticCategory(getResources().getString(R.string.write_mode), numRightWriteMode, numWrongWriteMode, numTotalWriteMode, rate);

        categories.add(generalCategory);
        categories.add(normalCategory);
        categories.add(writeCategory);

        statisticsListView.setAdapter(adapter);
    }

    private String getBestVocSetName() {
        try{
            if(Variables.vocabsetJSONObject.getJSONArray("streak").length() == 0){
                return "-";
            }
            int currentHighestIndex = -1;
            int recentStreak = -1;
            int currentStreak;
            for(int i = 0; i < Variables.vocabsetJSONObject.getJSONArray("streak").length(); i++){
                currentStreak = Variables.vocabsetJSONObject.getJSONArray("streak").getInt(i);
                if(currentStreak > recentStreak){
                    recentStreak = currentStreak;
                    currentHighestIndex = i;
                }
            }
            return Variables.vocabsetJSONObject.getJSONArray("title").getString(currentHighestIndex);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return "An Error occured";
    }

    private int getHighestHightscore() {
        try{
            int ret = -1;
            int currentStreak;
            for (int i = 0; i < Variables.vocabsetJSONObject.getJSONArray("streak").length(); i++){
                currentStreak = Variables.vocabsetJSONObject.getJSONArray("streak").getInt(i);
                if(currentStreak > ret)
                    ret = currentStreak;
            }
            return ret;
        }catch(JSONException e){
            e.printStackTrace();
        }
        return -1;
    }

    private void updateStatJSONFile(JSONObject j){
        String jsonString = j.toString();
        FileOutputStream fos = null;

        try{
            fos = openFileOutput(Variables.STATISTICS_FILE_NAME, MODE_PRIVATE);
            fos.write(jsonString.getBytes());

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}