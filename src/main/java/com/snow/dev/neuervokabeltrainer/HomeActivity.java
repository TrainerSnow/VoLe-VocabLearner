package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    static ListView vocabSetsView;
    public static ArrayList<VocabSet> vocabSetsArray;
    static ListViewAdapterVocabSet vocabSetsAdapter;
    private JSONObject vocabObject;
    private JSONArray vocabArray;
    private File vocabFile;
    private static final String TAG = "HomeActivity";

    @Override
    public void onPause(){
        super.onPause();
        updateJSONFile(Variables.vocabsetJSONObject, Variables.VOCAB_SET_FILE_NAME);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));

        vocabSetsView = findViewById(R.id.vocabSetsView);
        vocabSetsArray = new ArrayList<>();
        View addNewVocabSetButton = findViewById(R.id.addNewVocabSetButton);
        View startSettingsButton = findViewById(R.id.startSettingsButton);
        View startStatisticsButton = findViewById(R.id.startStatisticsButton);
        View showInstructionsHomeButton = findViewById(R.id.startInstructionsHomeButton);

        try {
            for (int i = 0; i < Variables.vocabSetTitles.length(); i++) {
                Log.d(TAG, "onCreate: " + i);
                vocabSetsArray.add(new VocabSet(Variables.VOCAB_ARRAY_PRESET, Variables.vocabSetTitles.getString(i), Variables.vocabSetDescriptions.getString(i), Variables.vocabSetStreak.getInt(i)));
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

        vocabSetsView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Variables.currentVocabSetIndex = position;
                try{

                    Variables.currentVocabulary = vocabSetsArray.get(position).getVocabFileName();
                    Variables.currentVocabSetName = vocabSetsArray.get(position).getTitle();
                    Variables.currentVocabSet = vocabSetsArray.get(position);
                    vocabFile = setUpFile(Variables.currentVocabulary);
                    vocabObject = loadJSONFile(vocabFile, Variables.VOCAB_FILE_PRESET);
                    vocabArray = vocabObject.getJSONArray("vocabs");

                    if(vocabArray.length() == 0){
                        Toast.makeText(getBaseContext(), R.string.no_vocabs_in_set, Toast.LENGTH_SHORT).show();
                        return;
                    }
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

        showInstructionsHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),InstructionsHomeActivity.class));
            }
        });
    }


    public static void removeItem (int remove){
            vocabSetsArray.remove(remove);
            vocabSetsView.setAdapter(vocabSetsAdapter);
            Variables.vocabSetTitles.remove(remove);
            Variables.vocabSetDescriptions.remove(remove);
            Variables.vocabSetVocabularyPath.remove(remove);
            Variables.vocabSetStreak.remove(remove);
    }

    private void onSettingsButton(int position){
        Variables.currentVocabulary = vocabSetsArray.get(position).getVocabFileName();
        startActivity(new Intent(getBaseContext(), OverviewVocabActivity.class));
    }

    private File setUpFile(String fileName) {
        File f = new File(this.getBaseContext().getFilesDir(), fileName);
        if(f.exists()) {
            return f;
        }
        else {
            try{
                f.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return f;
    }

    private void updateJSONFile(JSONObject j ,String fileName){
        String jsonString = j.toString();
        FileOutputStream fos = null;

        try{
            fos = openFileOutput(fileName, MODE_PRIVATE);
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

    private JSONObject loadJSONFile(File file, String preset) {

        String jsonFileAsString;
        try {
            InputStream is = new FileInputStream(file + ".json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            jsonFileAsString = new String(buffer, StandardCharsets.UTF_8);

            if(!jsonFileAsString.isEmpty())
                return new JSONObject(jsonFileAsString);
            else{
                return new JSONObject(preset);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
