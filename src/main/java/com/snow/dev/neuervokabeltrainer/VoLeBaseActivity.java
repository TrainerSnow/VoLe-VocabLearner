package com.snow.dev.neuervokabeltrainer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Base Class for every Activity in this Application
 *
 */

public class VoLeBaseActivity extends AppCompatActivity {

    public static final String VOCAB_FILE_NAME = "vocabs.json";
    public static final String SETTINGS_FILE_NAME = "settings.json";
    public static final String VOCAB_SET_FILE_NAME = "vocabsets.json";
    public static final String STATISTICS_FILE_NAME = "statistics.json";
    public static final String VOCAB_FILE_PRESET = "{\n" +
            "  \"vocabs\":\n" +
            "  [\n" +
            "    \n" +
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
    public static final String FILE_REGEX = "[^a-zA-Z1-9]";



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
    }

    /**
     *
     * @param file The File from which the Content is going to be read
     * @param preset The Preset that the JSONObject will contain, incase the File is empty
     * @return The Contest of the JSON File as JSONObject
     * Used to read the Data from a File into JSONObject Format
     */

    public JSONObject loadJSONFile(File file, String preset) {

        String jsonFileAsString;
        try {
            InputStream is = new FileInputStream(file + ".json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            jsonFileAsString = new String(buffer, StandardCharsets.UTF_8);

            if(!jsonFileAsString.isEmpty())
                return new JSONObject(jsonFileAsString);
            else{
                return new JSONObject(preset);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param fileName Name of the File that is being assigned, or created
     * @return The File that has been found, or created
     * Used to search for a File, or create it, if it hasnt already
     */

    public File setUpFile(String fileName) {
        File f = new File(this.getBaseContext().getFilesDir(), fileName);
        if(f.exists()) {
            return f;
        }
        else {
            try{
                f.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return f;
    }

    /**
     *
     * @param j The JSONObject that is going to be saved to the File
     * @param fileName The File that the JSONObject is going to be saved to
     * Used to save a JSONObject into a File
     */


    public void updateJSONFile(JSONObject j ,String fileName){
        String jsonString = j.toString();
        FileOutputStream fos = null;

        try{
            fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(jsonString.getBytes());

        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
