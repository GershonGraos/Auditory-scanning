package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 05/01/2017.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AreaPersonalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView _user_view;
    Spinner _spinner_patient;
    Spinner _spinner_choose;
    EditText input_patient;

    ArrayList<String> patients;
    ArrayAdapter<String> adapter;
    String user_signIn;
    String user_register;
    String new_patient;
    String s_patient;
    String s_state;
    int s_state_index;
    int flag_newUser=0;
    int flag_login=0;
    int flag_add_one_user = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_personal);
        setTitle(R.string.nameActivity_area_personal);

        // -- connect with Xml --
        _spinner_patient = (Spinner) findViewById(R.id.spinner_show);

        // -- get intent --
        Intent i_result = getIntent();
//        String pass = i_result.getStringExtra("PASS_REGISTER");
//        Toast.makeText(this,"pass: "+pass,Toast.LENGTH_SHORT).show();
        if(i_result.getStringExtra("USER_REGISTER") != null){
            user_register = i_result.getStringExtra("USER_REGISTER");
            flag_newUser = 1;
        }

        else if(i_result.getStringExtra("USER_SIGN_IN") != null){
            user_signIn = i_result.getStringExtra("USER_SIGN_IN");
            flag_login = 1;
        }
        _spinner_patient.setOnItemSelectedListener(this);

        if(flag_newUser == 1){
            patients = new ArrayList<String>();
//            flag_newUser = 0;
            addPatient(_user_view);
        }

        if(flag_login == 1){
            patients = new ArrayList<String>();
            flag_login = 0;
            patients.add("משה אשכנזי");
            patients.add("דוד מזרחי");
            patients.add("שלמה כהן");
        }

        // -- show Spinner --
        if(patients.size() > 0){
            adapter = new ArrayAdapter<String>(AreaPersonalActivity.this,android.R.layout.simple_spinner_item, patients);
            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
            _spinner_patient.setAdapter(adapter);
            Toast.makeText(this,"size: " + String.valueOf(patients.size()),Toast.LENGTH_SHORT).show();
        }
    }

    // ------------ Select Patient --------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l){
        s_patient = adapterView.getItemAtPosition(position).toString();

        if(patients.size() == 1 && flag_newUser == 1){
            Intent i;
            i = new Intent(this, EditPatient.class);
            i.putExtra("PATIENT",s_patient);
            startActivity(i);
            flag_newUser = 0;
        }

       else{
            Intent i;
            i = new Intent(this, Display_Patient.class);
            i.putExtra("PATIENT",s_patient);
            startActivity(i);
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(this,"Select/Add Patient",Toast.LENGTH_SHORT).show();
    }



    // ---------------------------------------------------------------------
    // --------------------- Add Patient ----------------------------------
    public void addPatient(View view){
        // Dialog and show spinner
        AlertDialog.Builder builder = new AlertDialog.Builder(AreaPersonalActivity.this);
        builder.setTitle(R.string.newPtnt_tittle);
        builder.setIcon(android.R.drawable.ic_menu_edit);
        builder.setMessage(R.string.newPtnt_text);
        input_patient = new EditText(AreaPersonalActivity.this);
        builder.setView(input_patient);

        //builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
        builder.setPositiveButton(R.string.newPtnt_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input_patient.getText().toString()!=null ) {
                    new_patient = input_patient.getText().toString();
                    patients.add(new_patient);
                }
                adapter = new ArrayAdapter<String>(AreaPersonalActivity.this,android.R.layout.simple_spinner_item, patients);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spinner_patient.setAdapter(adapter);
            }
        });

        //builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        builder.setNegativeButton(R.string.newPtnt_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
        flag_add_one_user = 1;
    }



    // ---------------------------------------------------------------------
    // --------------------- Share Patient --------------------------------
    public void sharePatient(View view){
        //Toast.makeText(this,"Share Demo Patients",Toast.LENGTH_SHORT).show();

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
// set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"send_to@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
// the attachment
        /*String filename="contacts_sid.vcf";
        File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
        Uri path = Uri.fromFile(filelocation);
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);*/
// the mail subject
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, "Subject");
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }

    protected void sendEmail() {
        Log.d("Send email", "");

        String[] TO = {"someone@gmail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.d("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}


