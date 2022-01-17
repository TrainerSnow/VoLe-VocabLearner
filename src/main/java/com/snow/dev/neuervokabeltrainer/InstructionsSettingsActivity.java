package com.snow.dev.neuervokabeltrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class InstructionsSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions_settings);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}