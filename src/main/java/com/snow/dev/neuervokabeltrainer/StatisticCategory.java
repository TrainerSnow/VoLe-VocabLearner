package com.snow.dev.neuervokabeltrainer;

import java.util.ArrayList;
import java.util.Arrays;

public class StatisticCategory {

    String header;
    ArrayList<Statistic> statistics = new ArrayList<>();
    int numStatistics;

    public StatisticCategory(String header, Statistic... statistics) {
        this.header = header;
        this.statistics.addAll(Arrays.asList(statistics));
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<Statistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(ArrayList<Statistic> statistics) {
        this.statistics = statistics;
    }

    public int getNumStatistics() {
        return numStatistics;
    }
}
