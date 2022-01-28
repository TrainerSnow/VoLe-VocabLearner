package com.snow.dev.neuervokabeltrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

        getSupportActionBar().setTitle(getResources().getString(R.string.help));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.colorOnSecondary, null)));

        AdView adView = findViewById(R.id.adViewInstVoc);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        findViewById(R.id.returnToMainButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), HomeActivity.class));
            }
        });


    }
}