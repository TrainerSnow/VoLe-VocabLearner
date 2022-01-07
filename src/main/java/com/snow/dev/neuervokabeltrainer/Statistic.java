package com.snow.dev.neuervokabeltrainer;

public class Statistic {

    private String name;
    private int value = -1;
    private float valueF = -1;

    public Statistic(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public float getValueF() {
        return valueF;
    }

    public Statistic(String name, float valueF) {
        this.name = name;
        this.valueF = valueF;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
