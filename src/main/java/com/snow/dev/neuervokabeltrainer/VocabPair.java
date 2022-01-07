package com.snow.dev.neuervokabeltrainer;

public class VocabPair {

    private String name;
    private String value;
    private int numGuessedRight;
    private int numGuessedWrong;
    public boolean hasBeenAsked = false;


    public VocabPair(String name, String value) {
        this.name = name;
        this.value = value;
        this.numGuessedRight = 0;
        this.numGuessedWrong = 0;
    }

    public VocabPair(String name, String value, int numGuessedRight, int numGuessedWrong) {
        this.name = name;
        this.value = value;
        this.numGuessedRight = numGuessedRight;
        this.numGuessedWrong = numGuessedWrong;
    }

    public int getNumGuessedRight() {
        return numGuessedRight;
    }

    public void setNumGuessedRight(int numGuessedRight) {
        this.numGuessedRight = numGuessedRight;
    }

    public int getNumGuessedWrong() {
        return numGuessedWrong;
    }

    public void setNumGuessedWrong(int numGuessedWrong) {
        this.numGuessedWrong = numGuessedWrong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
