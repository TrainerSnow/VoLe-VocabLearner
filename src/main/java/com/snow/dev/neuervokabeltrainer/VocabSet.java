package com.snow.dev.neuervokabeltrainer;

import java.util.Locale;

public class VocabSet {

    String[][] vocabPairs;
    String title;
    String description;
    String vocabFileName;
    int streak;

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public int getStreak() {
        return streak;
    }

    public VocabSet(String[][] vocabs, String name, String description, int streak) {
        this.vocabPairs = vocabs;
        this.title = name;
        this.description = description;
        this.vocabFileName = getTitle().replaceAll(Variables.FILE_REGEX, "_").toLowerCase(Locale.ROOT);
        this.streak = streak;
    }

    public String getVocabFileName() {
        return vocabFileName;
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
}
