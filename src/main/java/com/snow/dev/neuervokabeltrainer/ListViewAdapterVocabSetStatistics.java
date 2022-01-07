package com.snow.dev.neuervokabeltrainer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapterVocabSetStatistics extends ArrayAdapter<VocabPair> {

    ArrayList<VocabPair> vocabPairs;
    Context context;
    int layout;

    public ListViewAdapterVocabSetStatistics(Context context, ArrayList<VocabPair> vocabSets, int layout){
        super(context, layout, vocabSets);
        this.context = context;
        this.vocabPairs = vocabSets;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            TextView vocabName = convertView.findViewById(R.id.vocabName2);
            TextView vocabValue = convertView.findViewById(R.id.vocabValue2);
            TextView vocabNumRight = convertView.findViewById(R.id.vocabNumRight);
            TextView vocabNumWrong = convertView.findViewById(R.id.vocabNumWrong);
            TextView vocabRateView = convertView.findViewById(R.id.vocabRate);
            float vocabRate = (float) vocabPairs.get(position).getNumGuessedRight() / (vocabPairs.get(position).getNumGuessedWrong() + vocabPairs.get(position).getNumGuessedRight());
            if(Float.isNaN(vocabRate))
                vocabRate = 0;

            vocabName.setText(vocabPairs.get(position).getName());
            vocabValue.setText(vocabPairs.get(position).getValue());
            vocabNumRight.setText(String.valueOf(vocabPairs.get(position).getNumGuessedRight()));
            vocabNumWrong.setText(String.valueOf(vocabPairs.get(position).getNumGuessedWrong()));
            vocabRateView.setText(vocabRate * 100 + "%");
        }
        return convertView;
    }
}
