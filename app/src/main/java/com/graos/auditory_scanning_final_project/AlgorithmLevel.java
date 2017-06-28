package com.graos.auditory_scanning_final_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AlgorithmLevel extends AppCompatActivity {
    // ************* ON CREATE **************************
    private global_variables mApp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.algorithm_level_popup_activity);
        DisplayMetrics ms = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ms);
        int width = ms.widthPixels;
        int hieght = ms.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (hieght * .8));

        mApp = ((global_variables) getApplicationContext());
        Drawable drawable_g = ResourcesCompat.getDrawable(getResources(), R.drawable.level_button_green, null);
        Drawable drawable_w = ResourcesCompat.getDrawable(getResources(), R.drawable.level_button_white, null);
        Button b;

        if (!mApp.l1.isEmpty()){
            b = (Button) findViewById(R.id.btn_level_1);
            b.setText(mApp.l1);
        }
        if (!mApp.l2.isEmpty()) {
            b = (Button) findViewById(R.id.btn_level_2);
            b.setText(mApp.l2);
        }
        if (!mApp.l3.isEmpty()) {
            b = (Button) findViewById(R.id.btn_level_3);
            b.setText(mApp.l3);
        }
        if (!mApp.l4.isEmpty()) {
            b = (Button) findViewById(R.id.btn_level_4);
            b.setText(mApp.l4);
        }

        switch(mApp.l) {
            case 1 :
                b = (Button) findViewById(R.id.btn_level_1);
                b.setBackground(drawable_g);
                b = (Button) findViewById(R.id.btn_level_2);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_3);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_4);
                b.setBackground(drawable_w);
                break; // optional
            case 2 :
                b = (Button) findViewById(R.id.btn_level_1);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_2);
                b.setBackground(drawable_g);
                b = (Button) findViewById(R.id.btn_level_3);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_4);
                b.setBackground(drawable_w);
                break; // optional
            case 3 :
                b = (Button) findViewById(R.id.btn_level_1);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_2);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_3);
                b.setBackground(drawable_g);
                b = (Button) findViewById(R.id.btn_level_4);
                b.setBackground(drawable_w);
                break; // optional
            case 4 :
                b = (Button) findViewById(R.id.btn_level_1);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_2);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_3);
                b.setBackground(drawable_w);
                b = (Button) findViewById(R.id.btn_level_4);
                b.setBackground(drawable_g);
                break; // optional
            // You can have any number of case statements.
            default : // Optional
                // Statements
        }

    }

    public void help_algorithm_level_activity(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(AlgorithmLevel.this);
        builder.setTitle(R.string.tittle_help_main);
        builder.setIcon(R.mipmap.ic_help3);
        builder.setMessage(R.string.help_algorithm_level_activity);
        builder.show();
    }
}
