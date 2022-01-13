package com.snow.dev.neuervokabeltrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InstructionsVocabSetOverview extends AppCompatActivity {

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions_vocab_set_overview);

        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();

        findViewById(R.id.returnToMainButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });


    }
}