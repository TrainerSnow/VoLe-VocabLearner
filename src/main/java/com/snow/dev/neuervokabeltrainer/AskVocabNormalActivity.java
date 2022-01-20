package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
import java.util.Random;

public class AskVocabNormalActivity extends AppCompatActivity {

    private int numClicked = -1;
    private TextView vocabGuessView;
    private JSONArray currentJSONArray;
    private int currentRandom = 0;
    JSONArray vocabsArray;
    JSONObject vocabsObject;

    @Override
    protected void onPause(){
        super.onPause();
        updateStatJSONFile(Variables.statisticsJSONObject);
        updateVocJSONFile(vocabsObject);
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_vocab_normal);

        File vocabFile = setUpFile(Variables.currentVocabulary);
        vocabsObject = loadJSONFile(vocabFile);
        try {
            vocabsArray = vocabsObject.getJSONArray("vocabs");
            currentJSONArray = getRandomVocabPair(vocabsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        View deleteVocabButton = findViewById(R.id.deleteVocabButton2);
        View returnToMainButton = findViewById(R.id.returnToMainButton2);
        vocabGuessView = findViewById(R.id.vocabGuessView);
        showNewVocab();

        getSupportActionBar().setTitle(Variables.currentVocabSetName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFFFFF")));



        vocabGuessView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(numClicked % 2 == 0)){
                    try{
                        int currentNumChanged = Variables.statisticsJSONObject.getJSONObject("normalmode").getInt("numChanged");
                        Variables.statisticsJSONObject.getJSONObject("normalmode").put("numChanged", currentNumChanged + 1);
                        updateStatJSONFile(Variables.statisticsJSONObject);
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                showNewVocab();
            }
        });


        deleteVocabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vocabsArray.length() != 1){
                    try {
                        Toast.makeText(getBaseContext(), String.format(getResources().getString(R.string.deleted) ,currentJSONArray.get(0), currentJSONArray.get(1)), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vocabsArray.remove(currentRandom);
                    updateVocJSONFile(vocabsObject);
                    showNewVocab();
                }else{
                    try {
                        Toast.makeText(getBaseContext(), String.format(getResources().getString(R.string.deleted) ,currentJSONArray.get(0), currentJSONArray.get(1)), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    vocabsArray.remove(currentRandom);
                    updateVocJSONFile(vocabsObject);
                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                }
            }
        });


        returnToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVocJSONFile(vocabsObject);
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
                finish();
            }
        });
    }


    private void showNewVocab(){
        numClicked++;
        if(numClicked % 2 == 0)
            showGermanVocab();
        else {
            showTranslatedVocab();
            currentJSONArray = getRandomVocabPair(vocabsArray);
        }
    }


    private void showTranslatedVocab() {
        try{
            vocabGuessView.setText(currentJSONArray.getString(1));
        }catch(JSONException e){
            e.printStackTrace();
        }
    }


    private void showGermanVocab() {
        try{
            vocabGuessView.setText(currentJSONArray.getString(0));
        }catch(JSONException e){
            e.printStackTrace();
        }

    }


    private JSONArray getRandomVocabPair(JSONArray jArr){
        try{
            Random rand = new Random();
            int recentRandom = currentRandom;
            while(recentRandom == currentRandom){
                currentRandom = rand.nextInt(jArr.length());
                if(vocabsArray.length() == 1)
                    break;
            }
            return jArr.getJSONArray(currentRandom);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }


    private void updateVocJSONFile(JSONObject j){
        String jsonString = j.toString();
        FileOutputStream fos = null;

        try{
            fos = openFileOutput(Variables.currentVocabulary + ".json", MODE_PRIVATE);
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

    protected static JSONObject loadJSONFile(File file) {

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
                return new JSONObject(Variables.VOCAB_FILE_PRESET);
            }

        } catch (JSONException | IOException e) {
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
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return f;
    }


}