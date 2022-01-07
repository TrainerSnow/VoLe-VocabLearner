package com.snow.dev.neuervokabeltrainer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat activateCapitalizationModeSwitch;
    private SwitchCompat activateWriteModeSwitch;
    private SwitchCompat activateCheatModeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        activateCapitalizationModeSwitch = findViewById(R.id.activateCapitalizationmodeSwitch);
        activateWriteModeSwitch = findViewById(R.id.activateWriteModeSwitch);
        activateCheatModeSwitch = findViewById(R.id.activateCheatModeSwitch);
        View applySettingsButton = findViewById(R.id.applySettingsButton);

        loadSettings();
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        applySettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });
    }





    private void loadSettings() {
        try {
            activateCapitalizationModeSwitch.setChecked(Variables.settingsJSONObject.getBoolean("capitalization"));
            activateWriteModeSwitch.setChecked(Variables.settingsJSONObject.getBoolean("writeMode"));
            activateCheatModeSwitch.setChecked(Variables.settingsJSONObject.getBoolean("cheatMode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    protected void saveSettings() {
        try {
            Variables.settingsJSONObject.put("capitalization", activateCapitalizationModeSwitch.isChecked());
            Variables.settingsJSONObject.put("writeMode", activateWriteModeSwitch.isChecked());
            Variables.settingsJSONObject.put("cheatMode", activateCheatModeSwitch.isChecked());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        updateJSONFile(Variables.settingsJSONObject, Variables.SETTINGS_FILE_NAME);

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
}