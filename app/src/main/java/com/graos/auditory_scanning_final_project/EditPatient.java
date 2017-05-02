package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 13/01/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

public class EditPatient extends AppCompatActivity {

    static final int REQUEST_VIDEO_CAPTURE = 1;
    private boolean btn_rec_yes_mode = false;
    private boolean btn_rec_no_mode = false;
    private Uri UriYesVideo;
    private Uri UriNoVideo;

    AssignmentsDBHelper dbHelper;
    Cursor cursor;
    MyListAdapter mla;

    ListView _my_list;
    TextView _patient;
    EditText _item;

    String my_patient;
    String add_item;
    String [] projection = new String[]{Constants.Items._ID, Constants.Items.ITEM};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);
        setTitle(R.string.nameActivity_edit_pattient);

        // DB
        dbHelper = new AssignmentsDBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); //lock to write and read

        _patient = (TextView) findViewById(R.id.textViewHiPatient);
        _my_list = (ListView) findViewById(R.id.listItems);
        _item = (EditText) findViewById(R.id.editText);

        Intent i_result_patient;
        i_result_patient = getIntent();
        if(i_result_patient.getStringExtra("PATIENT") != null){
            my_patient = i_result_patient.getStringExtra("PATIENT");
        }

        _patient.setText(my_patient);

        // Cursor approaching to the TableDB-Contacts
        cursor = db.query(Constants.Items.TABLE_NAME, null, null, null, null, null, null);

        mla = new MyListAdapter(this, cursor);
        _my_list.setAdapter(mla);

        _my_list.invalidateViews();
        db.close();
    }



    // --------------- Add Item ----------------------------------------
    //-----------------------------------------------------------------
    public void press_addItem(View view){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values;

        if(_item.getText()!= null){
            add_item = _item.getText().toString();
        }
        else
            Toast.makeText(getApplicationContext(),"Please insert item" , Toast.LENGTH_LONG).show();

        if( !add_item.equals("") ){
            values = new ContentValues();
            values.put(Constants.Items.ITEM, add_item);
            db.insert(Constants.Items.TABLE_NAME, null, values);
            cursor = db.query(Constants.Items.TABLE_NAME, projection, null,null, null, null, null);
            mla = new MyListAdapter(this, cursor);
            _my_list.setAdapter(mla);
            _item.setText("");
        }
        else
            Toast.makeText(this, "Insert new item", Toast.LENGTH_SHORT).show();
    }


    // --------------- Delete Item ----------------------------------------
    //--------------------------------------------------------------------
    public void press_delete(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPatient.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            if(btn_rec_yes_mode)
                UriYesVideo = intent.getData();
            else
                UriNoVideo = intent.getData();
            //mVideoView.setVideoURI(videoUri);
        }
    }

    public void onClick_record_yes(View view){
        Button rec_yes_btn = (Button) view;
        if(!btn_rec_yes_mode) {
            btn_rec_yes_mode = true;
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
            rec_yes_btn.setText("Show YES recorder");
        }else{
            VideoView patient_video =  (VideoView) findViewById(R.id.PatientVideoView);
            patient_video.setVideoURI(UriYesVideo);
        }
    }

    public void onClick_delete_record_yes(View view){
        //rec_yes_btn.setText("Recorder YES");
        //btn_rec_yes_mode = false;
    }

    public void onClick_record_no(View view){
        Button rec_no_btn = (Button) view;
        if(!btn_rec_no_mode) {
            btn_rec_no_mode = true;
            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
            }
            rec_no_btn.setText("Show NO recorder");
        }else{
            VideoView patient_video = (VideoView)findViewById(R.id.PatientVideoView);
            patient_video.setVideoURI(UriNoVideo);
        }
    }

    public void onClick_delete_record_no(View view){
        //rec_yes_btn.setText("Recorder NO");
        //btn_rec_no_mode = false;
    }
}

