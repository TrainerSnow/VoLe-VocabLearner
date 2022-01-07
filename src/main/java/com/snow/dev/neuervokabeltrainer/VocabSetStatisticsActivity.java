package com.snow.dev.neuervokabeltrainer;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ListView;

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

public class VocabSetStatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_set_statistics);

        getSupportActionBar().setTitle("Statistik für " + Variables.currentVocabSetName);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#14397d")));

        ListView vocabSetStatsView = findViewById(R.id.vocabSetStats);
        ArrayList<VocabPair> vocabPairs = new ArrayList<>();
        File vocabsFile = setUpFile(Variables.currentVocabulary);
        JSONObject vocabsObject = loadJSONFile(vocabsFile);
        JSONArray vocabsArray = null;
        try{
            vocabsArray = vocabsObject.getJSONArray("vocabs");
        }catch(JSONException e){
            e.printStackTrace();
        }
        vocabPairs = JSONArrayToArrayList(vocabsArray);
        ListViewAdapterVocabSetStatistics adapter = new ListViewAdapterVocabSetStatistics(getBaseContext(), vocabPairs, R.layout.vocab_stat_row);
        vocabSetStatsView.setAdapter(adapter);
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

    private ArrayList<VocabPair> JSONArrayToArrayList(JSONArray arr){
        ArrayList<VocabPair> returnArray = new ArrayList<>();
        try{
            for (int i = 0; i < arr.length(); i++) {
                returnArray.add(new VocabPair(
                                                arr.getJSONArray(i).getString(0),
                                                arr.getJSONArray(i).getString(1),
                                                arr.getJSONArray(i).getInt(2),
                                                arr.getJSONArray(i).getInt(3)
                                            ));
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return returnArray;
    }
}