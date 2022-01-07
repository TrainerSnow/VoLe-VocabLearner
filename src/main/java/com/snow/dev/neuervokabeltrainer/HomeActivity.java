package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    static ListView vocabSetsView;
    public static ArrayList<VocabSet> vocabSetsArray;
    static ListViewAdapterVocabSet vocabSetsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        vocabSetsView = findViewById(R.id.vocabSetsView);
        vocabSetsArray = new ArrayList<>();
        View addNewVocabSetButton = findViewById(R.id.addNewVocabSetButton);
        View startSettingsButton = findViewById(R.id.startSettingsButton);
        View startStatisticsButton = findViewById(R.id.startStatisticsButton);

        try {
            for (int i = 0; i < Variables.vocabSetTitles.length(); i++) {
                vocabSetsArray.add(new VocabSet(Variables.VOCAB_ARRAY_PRESET, Variables.vocabSetTitles.getString(i), Variables.vocabSetDescriptions.getString(i)));
            }
        }catch(JSONException e) {
            e.printStackTrace();
        }

        vocabSetsAdapter = new ListViewAdapterVocabSet(getApplicationContext(), vocabSetsArray, R.layout.vocab_set_row);
        vocabSetsView.setAdapter(vocabSetsAdapter);


        addNewVocabSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), AddVocabSetActivity.class));
            }
        });

        vocabSetsView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Variables.currentVocabulary = vocabSetsArray.get(position).getVocabFileName();
                Variables.currentVocabSetName = vocabSetsArray.get(position).getTitle();
                onSettingsButton(position);
                return false;
            }
        });

        vocabSetsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Variables.currentVocabulary = vocabSetsArray.get(position).getVocabFileName();
                Variables.currentVocabSetName = vocabSetsArray.get(position).getTitle();
                try{
                    if (Variables.settingsJSONObject.getBoolean("writeMode"))
                        startActivity(new Intent(getBaseContext(), AskVocabWriteActivity.class));
                    else
                        startActivity(new Intent(getBaseContext(), AskVocabNormalActivity.class));
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });

        startSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SettingsActivity.class));
            }
        });

        startStatisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),StatisticsActivity.class));
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        updateJSONFile(Variables.vocabsetJSONObject);
    }


    public static void removeItem (int remove){
            vocabSetsArray.remove(remove);
            vocabSetsView.setAdapter(vocabSetsAdapter);
            Variables.vocabSetTitles.remove(remove);
            Variables.vocabSetDescriptions.remove(remove);
            Variables.vocabSetVocabularyPath.remove(remove);
    }

    private void updateJSONFile(JSONObject j){
        String jsonString = j.toString();
        FileOutputStream fos = null;

        try{
            fos = openFileOutput(Variables.VOCAB_SET_FILE_NAME, MODE_PRIVATE);
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

    private void onSettingsButton(int position){
        Variables.currentVocabulary = vocabSetsArray.get(position).getVocabFileName();
        startActivity(new Intent(getBaseContext(), OverviewVocabActivity.class));
    }
}
