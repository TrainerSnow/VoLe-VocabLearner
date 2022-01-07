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

public class ListViewAdapterVocabPair extends ArrayAdapter<VocabPair> {

    ArrayList<VocabPair> vocabPairs;
    Context context;
    int layout;

    public ListViewAdapterVocabPair(Context context, ArrayList<VocabPair> vocabPairs, int layout){
        super(context, layout, vocabPairs);
        this.context = context;
        this.vocabPairs = vocabPairs;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            TextView vocabName = convertView.findViewById(R.id.vocabName);
            vocabName.setText(vocabPairs.get(position).getName());

            TextView vocabValue = convertView.findViewById(R.id.vocabValue);
            vocabValue.setText(vocabPairs.get(position).getValue());

            View vocabDeleteButton = convertView.findViewById(R.id.deleteVocabButton3);

            vocabDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OverviewVocabActivity.removeItem(position);
                }
            });
        }
        return convertView;
    }
}
