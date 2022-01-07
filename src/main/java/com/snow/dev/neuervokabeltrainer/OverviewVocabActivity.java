package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class OverviewVocabActivity extends AppCompatActivity {

    static ListView vocabView;
    static ListViewAdapterVocabPair vocabAdapter;
    static ArrayList<VocabPair> vocabArray;
    static JSONArray vocabArrayJSON;
    static JSONObject vocabsJSONObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_overview);

        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        vocabView  = (ListView) findViewById(R.id.vocabListView);
        vocabArray = new ArrayList<>();
        vocabAdapter = new ListViewAdapterVocabPair(getBaseContext(), vocabArray, R.layout.vocab_row);
        EditText vocabNameInput = (EditText) findViewById(R.id.newVocabInputName);
        EditText vocabValueInput = (EditText) findViewById(R.id.newVocabInputValue);
        View setNewVocabButton = findViewById(R.id.setNewVocabButton);
        View saveAndReturn = findViewById(R.id.saveAndReturn);
        View vocabSetStatsButton = findViewById(R.id.vocabSetStatsButton);

        vocabsJSONObject = loadJSONFile(new File(getBaseContext().getFilesDir() + "/" + Variables.currentVocabulary + ".json"));
        try {
            vocabArrayJSON = vocabsJSONObject.getJSONArray("vocabs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        loadJSONArrayToArrayList();

        vocabView.setAdapter(vocabAdapter);

        setNewVocabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //For ArrayList
                String newName = vocabNameInput.getText().toString();
                String newValue = vocabValueInput.getText().toString();
                VocabPair newVocabPair = new VocabPair(newName, newValue);
                vocabArray.add(newVocabPair);
                vocabView.setAdapter(vocabAdapter);

                /*
                For JSONArray
                 */
                JSONArray newVocabJSONArray = new JSONArray();
                newVocabJSONArray.put(newName);
                newVocabJSONArray.put(newValue);
                //amountRight & Wrong
                newVocabJSONArray.put(0);
                newVocabJSONArray.put(0);
                vocabArrayJSON.put(newVocabJSONArray);

                updateJSONFile(vocabsJSONObject, Variables.currentVocabulary);

                vocabNameInput.setText("");
                vocabValueInput.setText("");
            }
        });

        saveAndReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateJSONFile(vocabsJSONObject, Variables.currentVocabulary);
                vocabNameInput.setText("");
                vocabValueInput.setText("");
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });

        vocabSetStatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), VocabSetStatisticsActivity.class));
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        updateJSONFile(vocabsJSONObject, Variables.currentVocabulary);
    }

    private void loadJSONArrayToArrayList() {
        for(int i = 0; i < vocabArrayJSON.length(); i++){
            try {
                vocabArray.add(new VocabPair(vocabArrayJSON.getJSONArray(i).getString(0), vocabArrayJSON.getJSONArray(i).getString(1)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private JSONObject loadJSONFile(File file) {

        String jsonFileAsString;
        try {
            InputStream is = new FileInputStream(file);

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

    private void updateJSONFile(JSONObject j, String fileName){
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

    public static void removeItem(int position){
        if(vocabArray.size() == 1 || vocabArrayJSON.length() == 1){
            return;
        }
        vocabArrayJSON.remove(position);
        vocabArray.remove(position);
        vocabView.setAdapter(vocabAdapter);
    }
}