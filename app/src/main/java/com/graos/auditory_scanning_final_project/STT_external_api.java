package com.graos.auditory_scanning_final_project;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class STT_external_api {
    // Name of the sound file (.flac)
    private String file;
    public STT_external_api(String audioFileFullPath){
        this.file = audioFileFullPath;
    }
    public ArrayList<String> transcript(){
        // doron api key: AIzaSyBYtGHFX7CXOfcBDnkEx67RSF4YMYq0pmc
        //other key AIzaSyBgnC5fljMTmCFeilkgLsOKBvvnx6CBS0M
        //more AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw
        Recognizer recognizer = new Recognizer (Recognizer.Languages.AUTO_DETECT, "AIzaSyBgnC5fljMTmCFeilkgLsOKBvvnx6CBS0M");
        int maxNumOfResponses = 10;
        JSONObject response = null;
        ArrayList<String> matches = new ArrayList<String>();
        try {
            response = recognizer.getRecognizedDataForFlac(file, maxNumOfResponses, 44100);
            if(response == null)
                return null;
            JSONArray jsonResultArray = response.getJSONArray("result");
            for(int i = 0; i < jsonResultArray.length(); i++) {
                JSONObject jsonAlternativeObject = jsonResultArray.getJSONObject(i);
                JSONArray jsonAlternativeArray = jsonAlternativeObject.getJSONArray("alternative");
                for(int j = 0; j < jsonAlternativeArray.length(); j++) {
                    JSONObject jsonTranscriptObject = jsonAlternativeArray.getJSONObject(j);
                    String transcript = jsonTranscriptObject.optString("transcript", "");
                    if(!transcript.isEmpty()) {
                        String [] tr = transcript.split(" ");
                        for(i=0;i<tr.length;i++)
                            matches.add(tr[i].toLowerCase());
                    }
                    //double confidence = jsonTranscriptObject.optDouble("confidence", 0.0);
                }
            }
            Log.d("Google Response: ", response.toString());
        }catch(IOException e) {
            Log.d("Error: ",e.getMessage());
        } catch (JSONException e) {
            Log.d("Error: ",e.getMessage());
        }
        /*
        RecognizerChunked recognizer = new RecognizerChunked("AIzaSyBgnC5fljMTmCFeilkgLsOKBvvnx6CBS0M",RecognizerChunked.Languages.ENGLISH_US.toString());
        try {
            recognizer.getRecognizedDataForFlac(file, 44100);
        }catch(IOException e) {
            Log.d("Error: ",e.getMessage());
        }
        */
        return matches;
    }
}
