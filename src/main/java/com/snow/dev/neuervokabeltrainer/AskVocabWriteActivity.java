package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class AskVocabWriteActivity extends AppCompatActivity {

    private boolean cheatButtonWasPushed = false;
    private TextView highScoreView;
    private int currentRandom = 0;
    private String currentSolution;
    private String currentQuestion;
    private JSONArray originalVocabsJSONArray;
    private JSONObject vocabsObject;
    private EditText vocabInputEditText;
    private TextView vocabShowTextView;
    private TextView currentVocabScoreView;
    private ArrayList<VocabPair> vocabsArray = new ArrayList<>();
    private int currentScore = 0;
    private static final String TAG = "AskVocabWriteActivity";

    @Override
    protected void onPause(){
        super.onPause();
        updateStatJSONFile(Variables.statisticsJSONObject);
        updateVocJSONFile(vocabsObject);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void finish(){
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_ask_vocab_write);
            getSupportActionBar().setTitle(Variables.currentVocabSetName);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.colorOnSecondary, null)));

            AdView adView = findViewById(R.id.adViewAskVocWrite);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            vocabShowTextView = findViewById(R.id.vocabShowView);
            vocabInputEditText = findViewById(R.id.vocabInputEditText);
            currentVocabScoreView = findViewById(R.id.currentScoreView);
            View showSolutionButton = findViewById(R.id.showSolutionButton);
            View deleteVocabButton = findViewById(R.id.deleteVocabButton);
            View returnToMain = findViewById(R.id.returnToMainButton);
            highScoreView = findViewById(R.id.highScoreView);
            View applyButton = findViewById(R.id.applyButton);
            File vocabsFile = setUpFile(Variables.currentVocabulary);
            vocabsObject = loadJSONFile(vocabsFile);
            originalVocabsJSONArray = vocabsObject.getJSONArray("vocabs");
            vocabsArray = JSONArrayToArrayList(originalVocabsJSONArray);

            assignVocab();
            showVocab();
            showStreak();
            showHighScore();


            showSolutionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cheatButtonWasPushed = true;
                    try {
                        if(Variables.settingsJSONObject.getBoolean("cheatMode"))
                            vocabInputEditText.setText(currentSolution);
                        else
                            Toast.makeText(getBaseContext(), R.string.cheatmode_is_off, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            deleteVocabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(vocabsArray.size() != 1){
                        originalVocabsJSONArray.remove(currentRandom);
                        vocabsArray = JSONArrayToArrayList(originalVocabsJSONArray);
                        Toast.makeText(App.getContext(), String.format(getResources().getString(R.string.deleted), currentQuestion, currentSolution), Toast.LENGTH_LONG).show();
                        updateVocJSONFile(vocabsObject);
                        assignVocab();
                        showVocab();
                    }else {
                        originalVocabsJSONArray.remove(currentRandom);
                        vocabsArray = JSONArrayToArrayList(originalVocabsJSONArray);
                        Toast.makeText(App.getContext(), String.format(getResources().getString(R.string.deleted), currentQuestion, currentSolution), Toast.LENGTH_LONG).show();
                        updateVocJSONFile(vocabsObject);
                        startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    }
                }
            });

            returnToMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        vocabsObject.put("vocabs", originalVocabsJSONArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    updateVocJSONFile(vocabsObject);
                    startActivity(new Intent(getBaseContext(), HomeActivity.class));
                    finish();
                }
            });

            applyButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    try{
                        if (Variables.settingsJSONObject.getBoolean("capitalization")) {
                            if (vocabInputEditText.getText().toString().equals(currentSolution)) {
                                guessedRight(v);
                            } else {
                                guessedWrong(v);
                            }

                        } else if (!Variables.settingsJSONObject.getBoolean("capitalization")) {
                            if (vocabInputEditText.getText().toString().toLowerCase(Locale.ROOT).equals(currentSolution.toLowerCase(Locale.ROOT))) {
                                guessedRight(v);
                            } else {
                                guessedWrong(v);
                            }
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    vocabInputEditText.setText("");
                    cheatButtonWasPushed = false;
                }
            });
        }catch(JSONException e){
            e.printStackTrace();
        }

    }

    private void guessedRight(View v){
        /*
        Statistics
         */
        try{
            if (!cheatButtonWasPushed) {
                int currentNumRight = Variables.statisticsJSONObject.getJSONObject("writemode").getInt("numRight");
                int currentNumTotal = Variables.statisticsJSONObject.getJSONObject("writemode").getInt("numTotal");
                Variables.statisticsJSONObject.getJSONObject("writemode").put("numRight", currentNumRight + 1);
                Variables.statisticsJSONObject.getJSONObject("writemode").put("numTotal", currentNumTotal + 1);
                updateStatJSONFile(Variables.statisticsJSONObject);
                currentScore++;
                if (currentScore > Variables.currentVocabSet.getStreak()) {
                    Variables.currentVocabSet.setStreak(Variables.currentVocabSet.getStreak() + 1);
                    Variables.vocabsetJSONObject.getJSONArray("streak").put(Variables.currentVocabSetIndex, currentScore);
                    updateSetJSONFile(Variables.vocabsetJSONObject);
                }
            } else {
                Toast.makeText(getBaseContext(), R.string.vocab_cheatmode_not_count_stats, Toast.LENGTH_SHORT).show();
            }
            showHighScore();
            showStreak();
            vocabsArray.get(currentRandom).setNumGuessedRight(vocabsArray.get(currentRandom).getNumGuessedRight() + 1);
            originalVocabsJSONArray = ArrayListToJSONArray(vocabsArray);
            updateVocJSONFile(vocabsObject);
            assignVocab();
            showVocab();
            vocabInputEditText.getBackground().mutate().setColorFilter((getResources().getColor(R.color.green)), PorterDuff.Mode.SRC_ATOP);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private void showHighScore() throws JSONException{
        highScoreView.setText(getResources().getString(R.string.high_score) + Variables.vocabsetJSONObject.getJSONArray("streak").getInt(Variables.currentVocabSetIndex));
    }

    private void guessedWrong(View v){
        /*
        Statistics
         */
        try{
            if (!cheatButtonWasPushed) {
                int currentNumWrong = Variables.statisticsJSONObject.getJSONObject("writemode").getInt("numWrong");
                int currentNumTotal = Variables.statisticsJSONObject.getJSONObject("writemode").getInt("numTotal");
                Variables.statisticsJSONObject.getJSONObject("writemode").put("numWrong", currentNumWrong + 1);
                Variables.statisticsJSONObject.getJSONObject("writemode").put("numTotal", currentNumTotal + 1);
                updateStatJSONFile(Variables.statisticsJSONObject);
            }else{
                Toast.makeText(getBaseContext(), R.string.vocab_cheatmode_not_count_stats, Toast.LENGTH_SHORT).show();
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        currentScore = 0;
        showStreak();
        vocabsArray.get(currentRandom).setNumGuessedWrong(vocabsArray.get(currentRandom).getNumGuessedWrong() + 1);
        originalVocabsJSONArray = ArrayListToJSONArray(vocabsArray);
        updateVocJSONFile(vocabsObject);

        showVocab();
        vocabInputEditText.getBackground().mutate().setColorFilter((getResources().getColor(R.color.red)), PorterDuff.Mode.SRC_ATOP);

    }


    private void assignVocab() {
        Random rand = new Random();
        int recentRandom = currentRandom;
        while (recentRandom == currentRandom) {
            currentRandom = rand.nextInt(vocabsArray.size());
            if (vocabsArray.size() == 1)
                break;
        }
        if(allVocabsHaveBeenAsked()){
            setBackAskedFlag();
        }else{
            while(vocabsArray.get(currentRandom).hasBeenAsked){
                if(currentRandom + 1 < vocabsArray.size()){
                    currentRandom++;
                }else{
                    currentRandom = 0;
                }
            }
        }
        currentQuestion = vocabsArray.get(currentRandom).getName();
        currentSolution = vocabsArray.get(currentRandom).getValue();
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

    private void updateSetJSONFile(JSONObject j){
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

    private void showVocab(){
        vocabShowTextView.setText(currentQuestion);
        vocabsArray.get(currentRandom).hasBeenAsked = true;
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

    private ArrayList<VocabPair> JSONArrayToArrayList(JSONArray arr){
        ArrayList<VocabPair> resultArray = new ArrayList<>();
        try{
            for (int i = 0; i < arr.length(); i++) {
                    resultArray.add( new VocabPair(arr.getJSONArray(i).getString(0), arr.getJSONArray(i).getString(1), arr.getJSONArray(i).getInt(2), arr.getJSONArray(i).getInt(3)));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return resultArray;
    }

    private JSONArray ArrayListToJSONArray(ArrayList<VocabPair> arr){
        JSONArray resultArray = new JSONArray();
        for(int i = 0; i < arr.size(); i++){
            JSONArray tempArray = new JSONArray();
            tempArray.put(arr.get(i).getName());
            tempArray.put(arr.get(i).getValue());
            tempArray.put(arr.get(i).getNumGuessedRight());
            tempArray.put(arr.get(i).getNumGuessedWrong());
            resultArray.put(tempArray);
        }
        return resultArray;
    }

    private boolean allVocabsHaveBeenAsked(){
        for(VocabPair pair : vocabsArray){
            if(!pair.hasBeenAsked)
                return false;
        }
        return true;
    }

    private void setBackAskedFlag(){
        for(VocabPair pair : vocabsArray){
            pair.hasBeenAsked = false;
        }
    }

    private void showStreak(){
        currentVocabScoreView.setText(getResources().getString(R.string.score) + " " +currentScore);
    }
}