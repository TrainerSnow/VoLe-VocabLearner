package com.snow.dev.neuervokabeltrainer;

import java.util.Locale;

public class VocabSet {

    String[][] vocabPairs;
    String title;
    String description;
    int size;
    String vocabFileName;

    public String getVocabFileName() {
        return vocabFileName;
    }

    public VocabSet(String[][] vocabs, String name, String description) {
        this.vocabPairs = vocabs;
        this.title = name;
        this.description = description;
        this.vocabFileName = getTitle().replaceAll(Variables.FILE_REGEX, "_").toLowerCase(Locale.ROOT);
    }

    public String[] getVocab(int i){
        return this.vocabPairs[i];
    }

    public void addVocabPair(int i, String answer, String solution){
        this.vocabPairs[i][0] = answer;
        this.vocabPairs[i][1] = solution;
    }

    public String[][] getVocabPairs() {
        return vocabPairs;
    }

    public void setVocabPairs(String[][] vocabPairs) {
        this.vocabPairs = vocabPairs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
