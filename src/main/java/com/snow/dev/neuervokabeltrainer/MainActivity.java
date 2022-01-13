package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.loading_screen);
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
        setupVariables();
        try{
            int currentNumAppOpened = Variables.statisticsJSONObject.getJSONObject("general").getInt("numAppOpened");
            Variables.statisticsJSONObject.getJSONObject("general").put("numAppOpened", currentNumAppOpened + 1);
            updateJSONFile(Variables.statisticsJSONObject, Variables.STATISTICS_FILE_NAME);
        }catch(JSONException e){
            e.printStackTrace();
        }


        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            public void run(){
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        }, 3000);
    }


    private void setupVariables() {
        Variables.settingsExternalFile = setUpFile(Variables.SETTINGS_FILE_NAME);
        Variables.vocabsetsExternalFile = setUpFile(Variables.VOCAB_SET_FILE_NAME);
        Variables.statisticsExternalFile = setUpFile(Variables.STATISTICS_FILE_NAME);
        Variables.vocabsExternalFile = setUpFile(Variables.VOCAB_FILE_NAME);

        Variables.settingsJSONObject = loadJSONFile(Variables.settingsExternalFile, Variables.SETTINGS_FILE_PRESET);
        Variables.vocabsetJSONObject = loadJSONFile(Variables.vocabsetsExternalFile, Variables.VOCAB_SET_FILE_PRESET);
        Variables.statisticsJSONObject = loadJSONFile(Variables.statisticsExternalFile, Variables.STATISTICS_FILE_PRESET);
        Variables.vocabsJSONObject = loadJSONFile(Variables.vocabsExternalFile, Variables.VOCAB_FILE_PRESET);
        Variables.vocabsJSONObject = loadJSONObjectFromPreset(Variables.VOCAB_FILE_PRESET);


        updateJSONFile(Variables.settingsJSONObject, Variables.SETTINGS_FILE_NAME);
        updateJSONFile(Variables.vocabsJSONObject, Variables.VOCAB_FILE_NAME);
        updateJSONFile(Variables.vocabsetJSONObject, Variables.VOCAB_SET_FILE_NAME);
        updateJSONFile(Variables.statisticsJSONObject, Variables.STATISTICS_FILE_NAME);

        try {
            Log.d(TAG, Variables.vocabsJSONObject.toString());
            Variables.vocabsJSONArray = Variables.vocabsJSONObject.getJSONArray("vocabs");
            Variables.vocabSetTitles = Variables.vocabsetJSONObject.getJSONArray("title");
            Variables.vocabSetDescriptions = Variables.vocabsetJSONObject.getJSONArray("description");
            Variables.vocabSetVocabularyPath = Variables.vocabsetJSONObject.getJSONArray("vocabulary");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected static JSONObject loadJSONFile(File file, String jsonPreset) {

        String jsonFileAsString;
        try {
            InputStream is = new FileInputStream(file);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            jsonFileAsString = new String(buffer, StandardCharsets.UTF_8);

            if(!jsonFileAsString.isEmpty()) {
                Log.d(TAG, "Didn't Use Preset");
                return new JSONObject(jsonFileAsString);
            }
            else{
                Log.d(TAG, "Used Preset");
                return new JSONObject(jsonPreset);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject loadJSONObjectFromPreset(String preset){
        try {
            return new JSONObject(preset);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private File setUpFile(String fileName) {
        File f = new File(this.getBaseContext().getFilesDir(), fileName);
        if(f.exists()) {
            return f;
        }
        else {
            try{
                f.createNewFile();
                Log.d(TAG, "Manually created File");
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return f;
    }

    private void updateJSONFile(JSONObject j, String fileName){
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

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}