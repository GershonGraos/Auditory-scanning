package com.graos.auditory_scanning_final_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Display_rama4 extends AppCompatActivity {
    TextView _my_lastClick_item;
    ListView _my_list_view;

    String lastClick_request, id_patient, i_parent;
    String request_click;
    String id_parent_send;
    int counter = 0;

    DBHelper_Requests my_db_requests;
    MyListAdapter my_list_adapter;

    private global_variables mApp;
    private ArrayList<String> STT_matches;
    private Context thisContext;
    int selected_item_index;
    int items_count=0;

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
                customHandler.postDelayed(this, 2000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_rama4);

        thisContext = this;
        mApp = ((global_variables)getApplicationContext());
        mApp.l = 4;
        STT_matches = mApp.getMatchesList();

        setTitle(R.string.nameActivity_display_pattient);

        my_db_requests = new DBHelper_Requests(this);

        _my_lastClick_item = (TextView) findViewById(R.id.textViewName_rama4);
        _my_list_view = (ListView) findViewById(R.id.listView_rama4);

        Intent i;
        i = getIntent();
        lastClick_request = i.getStringExtra("THE_REQUEST_3");
        id_patient = i.getStringExtra("ID_PT_3");
        i_parent = i.getStringExtra("MY_I_PARENT_3");
        mApp.l3 = lastClick_request;
        _my_lastClick_item.setText(lastClick_request);

        populateListViews();

        // ONE-CLICK TO ENTRY TO INTERIOR LIST
        _my_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item value
                request_click = (String)_my_list_view.getItemAtPosition(position);

                Cursor cursor = my_db_requests.show_requests();
                while(cursor.moveToNext()){
                    if(cursor.getString(3).equals(request_click)){
                        id_parent_send = cursor.getString(4);
                        counter = cursor.getInt(6);
                        counter++;
                        my_db_requests.update_counter(counter, id_parent_send);
                        break;
                    }
                }
            }
        });
        items_count = _my_list_view.getCount();
        //adapter.notifyDataSetChanged();

        //start STT
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

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
                // TODO Auto-generated method
                if (STT_matches == null)
                    return;
                ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String current_match;
                String current_STT_match;
                for (int i = 0; i < matches.size(); i++) {
                    for (int j = 0; j < STT_matches.size(); j++) {
                        current_match = matches.get(i).toLowerCase();
                        current_STT_match = STT_matches.get(j);
                        if (levenshtein_distance(current_STT_match, current_match,3)) {
                            _my_lastClick_item.setText(matches.get(i));
                            patient_said_yes_or_no = true;;
                            mSpeechRecognizer.stopListening();
                            customHandler.removeCallbacks(updateTimerThread);
                            _my_list_view.performItemClick(_my_list_view.getAdapter().getView(selected_item_index, null, null), selected_item_index, _my_list_view.getAdapter().getItemId(selected_item_index));
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
        if(mApp.auto_recognize&&items_count>0) {
            _my_list_view.setItemChecked(0,true);
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
            //start Timer
            customHandler.postDelayed(updateTimerThread, 2000);
        }
    }



    public void populateListViews(){
        Cursor cursor = my_db_requests.show_requests();
        if(cursor.getCount() != 0) {
            ArrayList<String> listRequests = new ArrayList<String >();
            while (cursor.moveToNext()) {
                if(cursor.getString(0).equals(id_patient) && cursor.getString(1).equals(i_parent) && cursor.getString(2).equals("4"))
                    listRequests.add(cursor.getString(3));
            }
            my_list_adapter = new MyListAdapter(this, listRequests);
            _my_list_view.setAdapter(my_list_adapter);
        }
        else
            _my_list_view.setAdapter(null);
    }
    public void press_show_algo_level_btn(View v){
        startActivity(new Intent(Display_rama4.this,AlgorithmLevel.class));
    }
    public void onStop () {
        if(mApp.auto_recognize) {
            //to pause the Timer:
            mSpeechRecognizer.stopListening();
            customHandler.removeCallbacks(updateTimerThread);
        }
        super.onStop();
    }
    public void mark_next_item() {
        selected_item_index = _my_list_view.getCheckedItemPosition();
        if(selected_item_index==-1 || items_count==0)
            return;
        if(selected_item_index == (items_count-1))
            selected_item_index = -1;
        selected_item_index = selected_item_index + 1;
        _my_list_view.setItemChecked(selected_item_index,true);
        _my_list_view.smoothScrollToPosition(selected_item_index);
    }

    public boolean levenshtein_distance(String a, String b, int threshold) {
        if(a==null|| b==null)
            return false;
        if(a.isEmpty() || b.isEmpty())
            return false;

        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        int the_distance  = costs[b.length()];
        int a_length = a.length();
        int b_length = b.length();
        if(the_distance >= a_length || the_distance >= b_length){
            return false;
        }
        if(the_distance>threshold)
            return false;
        return true;
    }
}
