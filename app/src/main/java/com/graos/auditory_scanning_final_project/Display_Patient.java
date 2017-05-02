package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 18/01/2017.
 */
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class Display_Patient extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    TextView _name;
    ListView _my_list;
    String my_patient;

    private TextView textViewName;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private static boolean exit_rec = false;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__patient);
        setTitle(R.string.nameActivity_display_pattient);

        _name = (TextView) findViewById(R.id.textViewName);
        _my_list = (ListView) findViewById(R.id.listView);

        Intent i_result_patient;
        i_result_patient = getIntent();
        if(i_result_patient.getStringExtra("PATIENT") != null){
            my_patient = i_result_patient.getStringExtra("PATIENT");
        }

        _name.setText(my_patient);


        String []values = new String[]{"משהו מפריע לי","משהו דחוף","לעשות משהו","עניין רפואי","לשוחח","רוצה מישהו","החפצים שלי","מקום בבית","מקום מחוץ לבית"};
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);
        _my_list.setAdapter(adapter);

        _my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                int itemPosition     = position;
                // ListView Clicked item value
                String  itemValue    = (String)_my_list.getItemAtPosition(position);
                // Show Alert
                Toast.makeText(getApplicationContext(), "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG).show();
                Intent i = new Intent(Display_Patient.this,Demo_internal_screen.class);
                i.putExtra("CHOOSE",itemValue);
                startActivity(i);
            }
        });


        textViewName = (TextView) findViewById(R.id.textViewName);
        //start STT
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "he");
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "he");
        /*
            EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS
            EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS
            EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS
         */
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onBeginningOfSpeech() {
                // TODO Auto-generated method stub
            }
            @Override
            public void onBufferReceived(byte[] arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onEndOfSpeech() {
                // TODO Auto-generated method stub
            }
            @Override
            public void onError(int arg0) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onEvent(int arg0, Bundle arg1) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onPartialResults(Bundle partialResults) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onReadyForSpeech(Bundle params) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onResults(Bundle results) {
                // TODO Auto-generated method stub
                if(exit_rec)
                    return;
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                textViewName.setText(matches.get(0));
                for(int i=0;i<matches.size();i++) {
                    if (matches.get(i).equals("כן") || matches.get(i).equals("לא")) {
                        exit_rec = true;
                        return;
                    }
                }
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            }
            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub

            }
        });
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        exit_rec = false;
    }


    // ---------------------------------------------------------------------
    // --------------------- Button Delete --------------------------------
    public void press_delete_demo(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Display_Patient.this);
        builder.setMessage("Are you sure?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        Toast.makeText(getApplicationContext(), "Delete the item" , Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
//                        Toast.makeText(getApplicationContext(), "NO Delete the item" , Toast.LENGTH_LONG).show();
                    }
                });
        // Create the AlertDialog object and return it
        builder.show();
    }

    public void editPatient(View view){
        Intent i;
        i = new Intent(this, EditPatient.class);
        i.putExtra("PATIENT",my_patient);
        startActivity(i);
    }
}

