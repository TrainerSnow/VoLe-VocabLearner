package com.snow.dev.neuervokabeltrainer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapterVocabSet extends ArrayAdapter<VocabSet> {

    ArrayList<VocabSet> vocabSets;
    Context context;
    int layout;

    public ListViewAdapterVocabSet(Context context, ArrayList<VocabSet> vocabSets, int layout){
        super(context, layout, vocabSets);
        this.context = context;
        this.vocabSets = vocabSets;
        this.layout = layout;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            TextView vocabSetIndex = convertView.findViewById(R.id.vocabSetIndex);
            vocabSetIndex.setText(position + 1 + ".");

            TextView vocabSetTitle = convertView.findViewById(R.id.vocabSetTitle);
            vocabSetTitle.setText(vocabSets.get(position).getTitle());

            TextView vocabSetDescription = convertView.findViewById(R.id.vocabSetDescription);
            vocabSetDescription.setText(vocabSets.get(position).getDescription());

            ImageView vocabSetDelete = convertView.findViewById(R.id.vocabSetDelete);
            vocabSetDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   HomeActivity.removeItem(position);
                }
            });
        }
        return convertView;
    }
}
