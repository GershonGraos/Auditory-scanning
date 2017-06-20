package com.graos.auditory_scanning_final_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class Display_rama1 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // my parameters
    ArrayAdapter<String> adapter;
    TextView _name;
    String my_id_patient;
    String my_only_id_therapist;
    String request_click;
    String id_parent_send;
    int counter = 0;

    DBHelper_Requests my_dbHelper_requests;
    MyListAdapter my_list_adapter;
    ListView _my_list;


    // default
    private TextView textViewName;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private static boolean exit_rec = false;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;


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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // my Functions
        setTitle(R.string.nameActivity_display_pattient);

        my_dbHelper_requests = new DBHelper_Requests(this);
        _name = (TextView) findViewById(R.id.textViewName);
        _my_list = (ListView) findViewById(R.id.listView);

        Intent i_result_patient;
        i_result_patient = getIntent();
        my_id_patient = i_result_patient.getStringExtra("ID_PATIENT");
        my_only_id_therapist = i_result_patient.getStringExtra("ID_ONLY");
        Toast.makeText(this,"id: " + my_id_patient,Toast.LENGTH_SHORT).show();
        _name.setText(my_id_patient);

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
                i = new Intent(Display_rama1.this, Display_rama2.class);
                i.putExtra("THE_REQUEST",request_click);
                i.putExtra("ID_PT",my_id_patient);
                i.putExtra("MY_I_PARENT",id_parent_send);
                startActivity(i);
            }
        });

        //doron
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
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

    // default
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
            Intent i = new Intent(this, Edit_Rama_1.class);
            i.putExtra("ID_PATIENT",my_id_patient);
            startActivity(i);
        }
        else if (id == R.id.nav_personal_area) {
            Intent i = new Intent(this, AreaPersonalActivity.class);
            i.putExtra("ID_REGISTER",my_only_id_therapist);
            startActivity(i);
        }
        else if (id == R.id.nav_logout) {
            Toast.makeText(this,"logout",Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    // SHOW THE STRING ARRAY REQUESTS INTO LAYOUT //
    public void populateListViews(){
        Cursor cursor = my_dbHelper_requests.show_requests();
        if(cursor.getCount() != 0) {
            ArrayList<String> listRequests = new ArrayList<String >();
            while (cursor.moveToNext()) {
                if(cursor.getString(0).equals(my_id_patient) && cursor.getString(1).equals("-1") && cursor.getString(2).equals("1"))
                    listRequests.add(cursor.getString(3));
            }
            my_list_adapter = new MyListAdapter(this, listRequests);
            _my_list.setAdapter(my_list_adapter);
        }
        else
            _my_list.setAdapter(null);
    }


}
