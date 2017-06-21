package com.graos.auditory_scanning_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by Doron on 18/05/2017.
 */


public class AlgorithmLevel extends AppCompatActivity {
    // ************* ON CREATE **************************
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_level_popup_activity);
        DisplayMetrics ms = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ms);
        int width = ms.widthPixels;
        int hieght = ms.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(hieght*.8));
    }
}
