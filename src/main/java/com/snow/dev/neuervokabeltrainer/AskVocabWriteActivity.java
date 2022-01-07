package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import java.util.Locale;
import java.util.Random;

public class AskVocabWriteActivity extends AppCompatActivity {

    private boolean cheatButtonWasPushed = false;
    private int currentRandom = 0;
    private String currentSolution;
    private String currentQuestion;
    private JSONArray vocabsJSONArray;
    private JSONObject vocabsObject;
    private EditText vocabInputEditText;
    private TextView vocabShowTextView;
    private VocabPair[] vocabsArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_vocab_write);

        vocabShowTextView =  findViewById(R.id.vocabShowView);
        vocabInputEditText = findViewById(R.id.vocabInputEditText);
        View showSolutionButton = findViewById(R.id.showSolutionButton);
        View deleteVocabButton = findViewById(R.id.deleteVocabButton);
        View returnToMain = findViewById(R.id.returnToMainButton);
        View applyButton = findViewById(R.id.applyButton);
        File vocabsFile = setUpFile(Variables.currentVocabulary);
        vocabsObject = loadJSONFile(vocabsFile);
        try {
            vocabsJSONArray = vocabsObject.getJSONArray("vocabs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        vocabsArray = JSONArrayToVocabPairs(vocabsJSONArray);

        assignVocab();
        showVocab();

        getSupportActionBar().setTitle(Variables.currentVocabSetName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#14397d")));


        showSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cheatButtonWasPushed = true;
                try {
                    if(Variables.settingsJSONObject.getBoolean("cheatMode"))
                        vocabInputEditText.setText(currentSolution);
                    else
                        Toast.makeText(getBaseContext(), "Cheatmodus ist aus!", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        deleteVocabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocabsJSONArray.remove(currentRandom);
                vocabsArray = JSONArrayToVocabPairs(vocabsJSONArray);
                Toast.makeText(getBaseContext(), "Deleted " + currentQuestion + " - " + currentSolution, Toast.LENGTH_LONG).show();
                updateVocJSONFile(vocabsObject);
                assignVocab();
                showVocab();
            }
        });

        returnToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    vocabsObject.put("vocabs", vocabsJSONArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("JSONObject Vocabs", vocabsObject.toString());
                Log.d("JSONARRAY Vocabs", vocabsJSONArray.toString());
                updateVocJSONFile(vocabsObject);
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
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

                Log.d("JSONARRAY Vocabs", vocabsJSONArray.toString());
            }
        });

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
            }else{
                Toast.makeText(getBaseContext(), "Vokabeln die mit Cheatmodus eingegeben wurden zählen nicht zu den Statistiken", Toast.LENGTH_SHORT).show();
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        vocabsArray[currentRandom].setNumGuessedRight(vocabsArray[currentRandom].getNumGuessedRight() + 1);
        vocabsJSONArray = VocabPairArrayToJSONArray(vocabsArray);
        updateVocJSONFile(vocabsObject);
        assignVocab();
        showVocab();
        vocabInputEditText.getBackground().mutate().setColorFilter((getResources().getColor(android.R.color.holo_green_light)), PorterDuff.Mode.SRC_ATOP);
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
                Toast.makeText(getBaseContext(), "Vokabeln die mit Cheatmodus eingegeben wurden zählen nicht zu den Statistiken", Toast.LENGTH_SHORT).show();
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        vocabsArray[currentRandom].setNumGuessedWrong(vocabsArray[currentRandom].getNumGuessedWrong() + 1);
        vocabsJSONArray = VocabPairArrayToJSONArray(vocabsArray);
        updateVocJSONFile(vocabsObject);

        showVocab();
        vocabInputEditText.getBackground().mutate().setColorFilter((getResources().getColor(android.R.color.holo_red_light)), PorterDuff.Mode.SRC_ATOP);

    }


    private void assignVocab() {
        Random rand = new Random();
        int recentRandom = currentRandom;
        while (recentRandom == currentRandom) {
            currentRandom = rand.nextInt(vocabsArray.length);
            if (vocabsArray.length == 1)
                break;
        }
        currentQuestion = vocabsArray[currentRandom].getName();
        currentSolution = vocabsArray[currentRandom].getValue();
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

    private void showVocab(){
        vocabShowTextView.setText(currentQuestion);
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

    private VocabPair[] JSONArrayToVocabPairs(JSONArray arr){
        VocabPair[] resultArray = new VocabPair[arr.length()];
        Log.d("JSONARRAY Vocabs", arr.toString());
        try{
            for (int i = 0; i < arr.length(); i++) {
                    resultArray[i] = new VocabPair(arr.getJSONArray(i).getString(0), arr.getJSONArray(i).getString(1), arr.getJSONArray(i).getInt(2), arr.getJSONArray(i).getInt(3));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return resultArray;
    }

    private JSONArray VocabPairArrayToJSONArray(VocabPair[] arr){
        JSONArray resultArray = new JSONArray();
        for(int i = 0; i < arr.length; i++){
            JSONArray tempArray = new JSONArray();
            tempArray.put(arr[i].getName());
            tempArray.put(arr[i].getValue());
            tempArray.put(arr[i].getNumGuessedRight());
            tempArray.put(arr[i].getNumGuessedWrong());
            resultArray.put(tempArray);
        }
        return resultArray;
    }
}