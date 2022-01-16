package com.snow.dev.neuervokabeltrainer;

public class Statistic {

    private String name;
    private int value = -1;
    private String valueS;
    private float valueF = -1;
    boolean valueIsString = false;

    public String getValueS() {
        return valueS;
    }

    public Statistic(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public Statistic(String name, String value){
        valueIsString = true;
        this.value = 0;
        this.name = name;
        this.valueS = value;
    }


    public Statistic(String name, float valueF) {
        this.name = name;
        this.valueF = valueF;
    }

    public float getValueF() {
        return valueF;
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
