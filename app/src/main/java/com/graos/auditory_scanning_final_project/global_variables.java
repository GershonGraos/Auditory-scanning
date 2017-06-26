package com.graos.auditory_scanning_final_project;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;

import java.util.ArrayList;

public class global_variables extends Application {

    public int l=0;
    public String l1="";
    public String l2="";
    public String l3="";
    public boolean auto_recognize = false;
    public boolean statistic_sort = false;

    public boolean flag_no_dataPatient = false; //to refer it to activity appropriate

    private Uri UriYesVideo;
    public Uri getUriYesVideo() {
        return UriYesVideo;
    }
    public void setUriYesVideo(Uri str) {
        UriYesVideo = str;
    }

    private String AudioPath;
    public String getAudioPath() {
        return AudioPath;
    }
    public void setAudioPath(String str) {AudioPath = str;}

    private ArrayList<String> MatchesList;
    public ArrayList<String> getMatchesList() {return MatchesList;}
    public void setMatchesList(ArrayList<String> aList) {MatchesList = aList;}

    public void alertMessage(Context c,String head,String body){
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(c, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(c);
        }
        builder.setTitle(head)
                .setMessage(body)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}