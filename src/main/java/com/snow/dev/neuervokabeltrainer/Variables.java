package com.snow.dev.neuervokabeltrainer;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class Variables {
    public static JSONObject vocabsJSONObject;
    public static JSONObject settingsJSONObject;
    public static JSONObject vocabsetJSONObject;
    public static JSONObject statisticsJSONObject;
    public static JSONArray vocabsJSONArray;
    public static JSONArray vocabSetTitles;
    public static JSONArray vocabSetDescriptions;
    public static JSONArray vocabSetVocabularyPath;
    public static File settingsExternalFile;
    public static File vocabsExternalFile;
    public static File vocabsetsExternalFile;
    public static File statisticsExternalFile;
    public static final String VOCAB_FILE_NAME = "vocabs.json";
    public static final String SETTINGS_FILE_NAME = "settings.json";
    public static final String VOCAB_SET_FILE_NAME = "vocabsets.json";
    public static final String STATISTICS_FILE_NAME = "statistics.json";
    public static final String VOCAB_FILE_PRESET = "{\n" +
            "  \"vocabs\": \n" +
            "  [\n" +
            "    [\"Beispielvokabel 1\" , \"Examplevocab 1\", 0, 0],\n" +
            "    [\"Beispielvokabel 2\" , \"Examplevocab 2\", 0, 0]\n" +
            "  ]\n" +
            "}";
    public static final String SETTINGS_FILE_PRESET = "{\n" +
            "  \"capitalization\" : false,\n" +
            "  \"writeMode\": false,\n" +
            "  \"cheatMode\": false\n" +
            "}";
    public static final String VOCAB_SET_FILE_PRESET = "{\n" +
            "    \"title\":\n" +
            "    [\"Beispielvokabeln 1\", \"Beispielvokabeln 2\", \"Beispielvokabeln 2\"],\n" +
            "    \"description\":\n" +
            "    [\"Dies ist eine Beispielbeschreibung\", \"Dies ist eine Beispielbeschreibung\", \"Dies ist eine Beispielbeschreibung\"],\n" +
            "    \"vocabulary\":[\"beispielvokabeln_1\", \"beispielvokabeln_2\", \"beispielvokabeln_3\"]\n" +
            "}";
    public static final String STATISTICS_FILE_PRESET = "{\n" +
            "  \"general\": {\n" +
            "    \"numAppOpened\": 0\n" +
            "  },\n" +
            "  \"normalmode\" : {\n" +
            "    \"numChanged\" : 0\n" +
            "  },\n" +
            "  \"writemode\": {\n" +
            "    \"numRight\" : 0,\n" +
            "    \"numWrong\": 0,\n" +
            "    \"numTotal\": 0\n" +
            "  }\n" +
            "}";
    public static final String[][] VOCAB_ARRAY_PRESET= {{"Beispielvokabel", "Examplevoakb"}};
    public static String currentVocabulary;
    public static final String FILE_REGEX = "[^a-zA-Z1-9]";
    public static String currentVocabSetName = "Default Name";
}
