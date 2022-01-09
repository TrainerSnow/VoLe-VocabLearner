package com.snow.dev.neuervokabeltrainer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListViewAdapterVocabPair extends ArrayAdapter<VocabPair> {

    Context context;
    int layout;

    public ListViewAdapterVocabPair(Context context, ArrayList<VocabPair> vocabPairs, int layout){
        super(context, layout, vocabPairs);
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(layout,parent, false);
        View vocabDeleteButton = convertView.findViewById(R.id.deleteVocabButton3);
        String value = getItem(position).getValue();String name = getItem(position).getName();
        TextView vocabNameView = convertView.findViewById(R.id.vocabName);
        TextView vocabValueView = convertView.findViewById(R.id.vocabValue);

        vocabNameView.setText(name);
        vocabValueView.setText(value);

        vocabDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                OverviewVocabActivity.removeItem(position);
            }
        });
        return convertView;
    }
}
