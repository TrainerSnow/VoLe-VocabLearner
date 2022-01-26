package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class AddVocabSetActivity extends AppCompatActivity {

    private static final String TAG = "AddVocabSetActivity";

    @Override
    protected void onPause(){
        super.onPause();
        updateJSONFile(Variables.vocabsetJSONObject, Variables.VOCAB_SET_FILE_NAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vocab_set);

        Log.d(TAG, "onCreate: " + Variables.vocabsetJSONObject.toString());

        getSupportActionBar().setTitle(getResources().getString(R.string.create_new_set));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.colorOnSecondary, null)));

        EditText titleInput = findViewById(R.id.newVocabSetTitle);
        EditText descriptionInput = findViewById(R.id.newVocabSetDescription);
        View applyNewVocabSetButton = findViewById(R.id.applyNewVocabSetButton);
        View returnToOverviewVocabSetButton = findViewById(R.id.returnToOverviewVocabSetButton);

        applyNewVocabSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newVocSetTitle = titleInput.getText().toString();
                try {
                    if(vocabSetNameAlrExists(newVocSetTitle)){
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.voc_set_alr_exists), Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String newVocSetDesc = descriptionInput.getText().toString();
                titleInput.getBackground().mutate().setColorFilter((getResources().getColor(android.R.color.holo_green_light)), PorterDuff.Mode.SRC_ATOP);
                descriptionInput.getBackground().mutate().setColorFilter((getResources().getColor(android.R.color.holo_green_light)), PorterDuff.Mode.SRC_ATOP);
                if(newVocSetTitle.equals("")){
                    titleInput.getBackground().mutate().setColorFilter((getResources().getColor(android.R.color.holo_red_light)), PorterDuff.Mode.SRC_ATOP);
                }
                if(newVocSetDesc.equals("")){
                    descriptionInput.getBackground().mutate().setColorFilter((getResources().getColor(android.R.color.holo_red_light)), PorterDuff.Mode.SRC_ATOP);
                }
                if(newVocSetTitle.equals("")|| newVocSetDesc.equals("")){
                    return;
                }
                String[][] fakeArray = new String[0][0];
                VocabSet vocabSet = new VocabSet(fakeArray, newVocSetTitle, newVocSetDesc, 0);
                saveVocabSetToJSONObject(vocabSet);
                updateJSONFile(Variables.vocabsetJSONObject, Variables.VOCAB_SET_FILE_NAME);
                setUpFile(newVocSetTitle.replaceAll(Variables.FILE_REGEX, "_").toLowerCase(Locale.ROOT)+ ".json");
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });

        returnToOverviewVocabSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });


    }

    private void saveVocabSetToJSONObject(VocabSet vocabSet) {
        Variables.vocabSetTitles.put(vocabSet.getTitle());
        Variables.vocabSetDescriptions.put(vocabSet.getDescription());
        Variables.vocabSetVocabularyPath.put(vocabSet.getTitle().replaceAll(Variables.FILE_REGEX, "_").toLowerCase(Locale.ROOT));
        Variables.vocabSetStreak.put(vocabSet.getStreak());
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

    private void setUpFile(String fileName) {
        File f = new File(this.getBaseContext().getFilesDir(), fileName);
        if(!f.exists()) {
            try{
                f.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        try{
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(Variables.VOCAB_FILE_PRESET.getBytes());
            fos.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private boolean vocabSetNameAlrExists(String name)throws JSONException {
        JSONArray titles = Variables.vocabsetJSONObject.getJSONArray("title");
        for(int i = 0; i < titles.length(); i++){
            if(name.equals(titles.getString(i))){
                return true;
            }
        }
        return false;
    }
}