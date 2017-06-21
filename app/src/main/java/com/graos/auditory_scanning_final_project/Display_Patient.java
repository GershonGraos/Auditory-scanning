package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 18/01/2017.
 */
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.speech.RecognitionListener;
import android.speech.RecognitionService;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

import android.os.Handler;
import android.os.SystemClock;


public class Display_Patient extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    TextView _name;
    ListView _my_list;
    String my_patient;
    Button btn_yes;
    Button btn_no;

    private global_variables mApp;
    private ArrayList<String> STT_matches;

    private Context thisContext;
    int selected_item_index;
    int items_count;

    private TextView textViewName;
    private String []values;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    static SpeechRecognizer mSpeechRecognizer;
    static Intent mSpeechRecognizerIntent;
    static boolean patient_said_yes_or_no = false;
    static boolean recognizer_stop = true;

    //Timer
    private Handler customHandler = new Handler();
    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            //Start next STT
            if(recognizer_stop) {
                mSpeechRecognizer.stopListening();
                recognizer_stop = false;
                customHandler.postDelayed(this, 1000);
                return;
            }
            if(!patient_said_yes_or_no) {
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                mark_next_item();
                recognizer_stop = true;
                /*
                adapter = new ArrayAdapter<String>(thisContext,android.R.layout.simple_list_item_activated_1, android.R.id.text1, values);
                _my_list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                _my_list.setItemChecked(selected_item_index,true);
                */
                customHandler.postDelayed(this, 2000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__patient);
        setTitle(R.string.nameActivity_display_pattient);
        thisContext = this;

        mApp = ((global_variables)getApplicationContext());
        STT_matches = mApp.getMatchesList();

        btn_yes = (Button) findViewById(R.id.button6);
        btn_no = (Button) findViewById(R.id.button7);
        _name = (TextView) findViewById(R.id.textViewName);
        _my_list = (ListView) findViewById(R.id.listView);

        Intent i_result_patient;
        i_result_patient = getIntent();
        if(i_result_patient.getStringExtra("PATIENT") != null){
            my_patient = i_result_patient.getStringExtra("PATIENT");
        }

        _name.setText(my_patient);

        values = new String[]{"משהו מפריע לי","משהו דחוף","לעשות משהו","עניין רפואי","לשוחח","רוצה מישהו","החפצים שלי","מקום בבית","מקום מחוץ לבית"};
        //simple_list_item_checked , simple_list_item_single_choice
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1, android.R.id.text1, values);
        _my_list.setAdapter(adapter);
        _my_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
                //startActivity(i);
            }
        });

        items_count = _my_list.getCount();
        textViewName = (TextView) findViewById(R.id.textViewName);
        adapter.notifyDataSetChanged();
        _my_list.setItemChecked(0,true);

        //start STT
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, "auto");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "auto");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 2500);
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "auto");

        /*
            EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS
            EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS
            EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS
         */
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
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
                if(STT_matches==null)
                    return;
                ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for(int i=0;i<matches.size();i++) {
                    for(int j=0;j<STT_matches.size();j++){
                        if(getSamePercent(STT_matches.get(j),matches.get(i).toLowerCase())>60){
                            textViewName.setText(matches.get(i));
                            patient_said_yes_or_no = true;
                            //_my_list.getSelectedView().setBackgroundColor(Color.GREEN);
                            btn_yes.setBackgroundColor(Color.GREEN);
                            //btn_no.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style));
                            mSpeechRecognizer.stopListening();
                            customHandler.removeCallbacks(updateTimerThread);
                            return;
                        }
                    }
                }
                if(matches.get(0).isEmpty()){
                    ArrayList<String> unstableData = partialResults.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
                    for(int i=0;i<unstableData.size();i++) {
                        for(int j=0;j<STT_matches.size();j++){
                            if(getSamePercent(STT_matches.get(j),unstableData.get(i).toLowerCase())>60){
                                textViewName.setText(STT_matches.get(j));
                                patient_said_yes_or_no = true;
                                //_my_list.getSelectedView().setBackgroundColor(Color.GREEN);
                                btn_yes.setBackgroundColor(Color.GREEN);
                                //btn_no.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style));
                                mSpeechRecognizer.stopListening();
                                customHandler.removeCallbacks(updateTimerThread);
                                return;
                            }
                        }
                    }
                }
            }
            @Override
            public void onReadyForSpeech(Bundle params) {
                // TODO Auto-generated method stub
            }
            @Override
            public void onResults(Bundle results) {
                // TODO Auto-generated method
                if(STT_matches==null)
                    return;
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                for(int i=0;i<matches.size();i++) {
                    for(int j=0;j<STT_matches.size();j++){
                        if(getSamePercent(STT_matches.get(j),matches.get(i).toLowerCase())>60){
                            textViewName.setText(matches.get(i));
                            patient_said_yes_or_no = true;
                            //_my_list.getSelectedView().setBackgroundColor(Color.GREEN);
                            btn_yes.setBackgroundColor(Color.GREEN);
                            //btn_no.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style));
                            mSpeechRecognizer.stopListening();
                            customHandler.removeCallbacks(updateTimerThread);
                            return;
                        }
                    }
                }
            }
            @Override
            public void onRmsChanged(float rmsdB) {
                // TODO Auto-generated method stub

            }
        });
        patient_said_yes_or_no = false;
        recognizer_stop = true;
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
        //start Timer
        customHandler.postDelayed(updateTimerThread, 2000);
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
    //show algorithm level
    public void press_show_algo_level_btn(View v){
        startActivity(new Intent(Display_Patient.this,AlgorithmLevel.class));
    }
    public void onStop () {
        //to pause the Timer:
        mSpeechRecognizer.stopListening();
        customHandler.removeCallbacks(updateTimerThread);
        super.onStop();
    }
    public void mark_next_item() {
        selected_item_index = _my_list.getCheckedItemPosition();
        if(selected_item_index==-1 || items_count==0)
            return;
        if(selected_item_index == (items_count-1))
            selected_item_index = -1;
        selected_item_index = selected_item_index + 1;
        _my_list.setItemChecked(selected_item_index,true);
        _my_list.smoothScrollToPosition(selected_item_index);
    }
    public int getSamePercent(String s1,String s2) {
        if(s1==null|| s2==null)
            return 0;
        if(s1.isEmpty() || s2.isEmpty())
            return 0;
        HashSet<Character> h1 = new HashSet<Character>(), h2 = new HashSet<Character>();
        for(int i = 0; i < s1.length(); i++)
        {
            h1.add(s1.charAt(i));
        }
        for(int i = 0; i < s2.length(); i++)
        {
            h2.add(s2.charAt(i));
        }
        h1.retainAll(h2);
        Character[] res = h1.toArray(new Character[0]);
        return ((res.length*2)/(s1.length()+s2.length()))*100;
    }
}

