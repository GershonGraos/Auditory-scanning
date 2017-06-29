package com.graos.auditory_scanning_final_project;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Display_Rama_1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // my parameters
    //ArrayAdapter<String> adapter;
    NavigationView navigationView;
    TextView _name;
    Button btn_yes;
    String my_id_patient;
    String my_only_id_therapist;
    String request_click;
    String id_parent_send;
    int counter = 0;

    DBHelper_Requests my_dbHelper_requests;
    MyListAdapter my_list_adapter;
    ListView _my_list;

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
        setContentView(R.layout.activity_display_rama1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        thisContext = this;
        mApp = ((global_variables)getApplicationContext());
        mApp.l = 1;
        STT_matches = mApp.getMatchesList();

        if(mApp.statistic_sort) {
            MenuItem t = navigationView.getMenu().getItem(3);
            t.setChecked(true);
            t.setTitle(R.string.option_sort_list_by_statistic_inactive);
        }

        if(mApp.auto_recognize) {
            MenuItem t = navigationView.getMenu().getItem(2);
            t.setChecked(true);
            t.setTitle(R.string.option_voice_recognize_inactive);
        }

        // my Functions
        setTitle(R.string.nameActivity_display_pattient);

        my_dbHelper_requests = new DBHelper_Requests(this);
        _name = (TextView) findViewById(R.id.textViewName);
        _my_list = (ListView) findViewById(R.id.listView);

        Intent i_result_patient;
        i_result_patient = getIntent();
        my_id_patient = i_result_patient.getStringExtra("ID_PATIENT");
        my_only_id_therapist = i_result_patient.getStringExtra("ID_ONLY");
        _name.setText(my_id_patient);
        //simple_list_item_checked , simple_list_item_single_choice
        /*adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_activated_1, android.R.id.text1, values);
        _my_list.setAdapter(adapter);
        _my_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        */
        populateListViews();

        // ONE-CLICK TO ENTRY TO INTERIOR LIST
        _my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item value
                request_click = (String)_my_list.getItemAtPosition(position);
                Cursor cursor = my_dbHelper_requests.show_requests();
                while(cursor.moveToNext()){
                    if(cursor.getString(3).equals(request_click)){
                        id_parent_send = cursor.getString(4);
                        counter = cursor.getInt(6);
                        counter++;
                        my_dbHelper_requests.update_counter(counter, id_parent_send);
                        break;
                    }
                }

                Intent i;
                i = new Intent(Display_Rama_1.this, Display_Rama_2.class);
                i.putExtra("THE_REQUEST",request_click);
                i.putExtra("ID_PT",my_id_patient);
                i.putExtra("MY_I_PARENT",id_parent_send);
                startActivity(i);
            }
        });

        items_count = _my_list.getCount();
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
/*                    if (STT_matches == null)
                        return;
                    ArrayList<String> matches = partialResults.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    for (int i = 0; i < matches.size(); i++) {
                        for (int j = 0; j < STT_matches.size(); j++) {
                            if (levenshtein_distance(STT_matches.get(j), matches.get(i).toLowerCase(),3)) {
                                _name.setText(matches.get(i));
                                patient_said_yes_or_no = true;
                                //_my_list.getSelectedView().setBackgroundColor(Color.GREEN);
                                //btn_no.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style));
                                mSpeechRecognizer.stopListening();
                                customHandler.removeCallbacks(updateTimerThread);
                                return;
                            }
                        }
                    }
                    if (matches.get(0).isEmpty()) {
                        ArrayList<String> unstableData = partialResults.getStringArrayList("android.speech.extra.UNSTABLE_TEXT");
                        for (int i = 0; i < unstableData.size(); i++) {
                            for (int j = 0; j < STT_matches.size(); j++) {
                                if (levenshtein_distance(STT_matches.get(j), unstableData.get(i).toLowerCase(),3)) {
                                    _name.setText(STT_matches.get(j));
                                    patient_said_yes_or_no = true;
                                    //_my_list.getSelectedView().setBackgroundColor(Color.GREEN);
                                    //btn_no.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style));
                                    mSpeechRecognizer.stopListening();
                                    customHandler.removeCallbacks(updateTimerThread);
                                    return;
                                }
                            }
                        }
                    }*/
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
                    String [] current_matchs;
                    String current_STT_match;
                    for (int i = 0; i < matches.size(); i++) {
                        for (int j = 0; j < STT_matches.size(); j++) {
                            current_matchs = matches.get(i).toLowerCase().split(" ");
                            for(int k=0;k<current_matchs.length;k++) {
                                current_match = current_matchs[k];
                                current_STT_match = STT_matches.get(j);
                                //if (levenshtein_distance(current_STT_match, current_match,3)){
                                if (((current_STT_match.contains(current_match) || current_match.contains(current_STT_match)) && ((((double) Math.min(current_match.length(), current_STT_match.length()) / (double) Math.max(current_match.length(), current_STT_match.length())) * 100) >= 50)) || levenshtein_distance(current_STT_match, matches.get(i).toLowerCase(), 3)) {
                                    //_name.setText(matches.get(i));
                                    patient_said_yes_or_no = true;
                                    //_my_list.getSelectedView().setBackgroundColor(Color.GREEN);
                                    //btn_no.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_style));
                                    mSpeechRecognizer.stopListening();
                                    customHandler.removeCallbacks(updateTimerThread);
                                    _my_list.performItemClick(_my_list.getAdapter().getView(selected_item_index, null, null), selected_item_index, _my_list.getAdapter().getItemId(selected_item_index));
                                    return;
                                }
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
                _my_list.setItemChecked(0,true);
                mApp.first_listening = true;
                mSpeechRecognizer.startListening(mSpeechRecognizerIntent);
                //start Timer
                customHandler.postDelayed(updateTimerThread, 2500);
            }

    }

    // default
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            mSpeechRecognizer.stopListening();
            customHandler.removeCallbacks(updateTimerThread);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.display_rama1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    // my function navigate
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_edit) {
            if(mApp.auto_recognize){
                mSpeechRecognizer.stopListening();
                customHandler.removeCallbacks(updateTimerThread);
            }
            Intent i = new Intent(this, Edit_Rama_1.class);
            i.putExtra("ID_PATIENT",my_id_patient);
            startActivity(i);
        }

        else if (id == R.id.nav_personal_area) {
            if(mApp.auto_recognize){
                mSpeechRecognizer.stopListening();
                customHandler.removeCallbacks(updateTimerThread);
            }
            Intent i = new Intent(this, AreaPersonalActivity.class);
            i.putExtra("ID_REGISTER",my_only_id_therapist);
            startActivity(i);
        }

        else if (id == R.id.nav_logout) {
            if(mApp.auto_recognize){
                mSpeechRecognizer.stopListening();
                customHandler.removeCallbacks(updateTimerThread);
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        else if (id == R.id.nav_voice_recognize) {
            MenuItem t = navigationView.getMenu().getItem(2);
            if(t.isChecked()) {
                t.setChecked(false);
                t.setTitle(R.string.option_voice_recognize_active);
                Toast.makeText(this,R.string.voice_stop,Toast.LENGTH_SHORT).show();
                mApp.auto_recognize = false;
            }else{
                t.setChecked(true);
                t.setTitle(R.string.option_voice_recognize_inactive);
                Toast.makeText(this,R.string.voice_start,Toast.LENGTH_SHORT).show();
                mApp.auto_recognize = true;
            }
            if(items_count>0) {
                mSpeechRecognizer.stopListening();
                customHandler.removeCallbacks(updateTimerThread);
                startActivity(getIntent());
                finish();
            }
            //super.recreate();
        }

        else if (id == R.id.nav_sort_list_by_statistic) {
            MenuItem t = navigationView.getMenu().getItem(3);
            MenuItem t2 = navigationView.getMenu().getItem(2);
            if(t.isChecked()||(t2.isChecked()&&mApp.statistic_sort)) {
                t.setChecked(false);
                t.setTitle(R.string.option_sort_list_by_statistic_active);
                Toast.makeText(this,R.string.statistic_stop,Toast.LENGTH_SHORT).show();
                mApp.statistic_sort = false;
            }else{
                t.setChecked(true);
                t.setTitle(R.string.option_sort_list_by_statistic_inactive);
                Toast.makeText(this,R.string.statistic_start,Toast.LENGTH_SHORT).show();
                mApp.statistic_sort = true;
            }
            if(items_count>0) {
                mSpeechRecognizer.stopListening();
                customHandler.removeCallbacks(updateTimerThread);
                startActivity(getIntent());
                finish();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // SHOW THE STRING ARRAY REQUESTS INTO LAYOUT //
    public void populateListViews(){
        Cursor cursor = null;
        if(mApp.statistic_sort)
            cursor = my_dbHelper_requests.show_requests_level_1_sorted_statistically(my_id_patient);
        else
            cursor = my_dbHelper_requests.show_requests_level_1(my_id_patient);

        if(cursor.getCount() != 0) {
            ArrayList<String> listRequests = new ArrayList<String >();
            while (cursor.moveToNext()) {
                listRequests.add(cursor.getString(0));
            }
            my_list_adapter = new MyListAdapter(this, listRequests);

            _my_list.setAdapter(my_list_adapter);
        }
        else
            _my_list.setAdapter(null);
    }

    //show algorithm level
    public void press_show_algo_level_btn(View v){
        startActivity(new Intent(Display_Rama_1.this,AlgorithmLevel.class));
    }
    public void onStop () {
        if(mApp.auto_recognize&&mApp.first_listening==false) {
            //to pause the Timer:
            mSpeechRecognizer.stopListening();
            customHandler.removeCallbacks(updateTimerThread);
        }
        mApp.first_listening = false;
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

    public void help_display_patient_activity(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Display_Rama_1.this);
        builder.setTitle(R.string.tittle_help_main);
        builder.setIcon(R.mipmap.ic_help3);
        builder.setMessage(R.string.help_display_patient_activity);
        builder.show();
    }
}
