package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 05/01/2017.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AreaPersonalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView _user_view;
    TextView _hi;
    Spinner _spinner_patient;
    Spinner _spinner_choose;
    EditText input_patient;

    List<String> patients;
    ArrayAdapter<String> adapter;
    String user_signIn;
    String user_register;
    String new_patient;
    String s_patient;
    String s_state;
    int s_state_index;
    int flag_newUser=0;
    int flag_login=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_personal);
        //setTitle("Therapist Personal Area");
        setTitle("איזור אישי עבור מטפל");
        _hi = (TextView) findViewById(R.id.textViewHi);
        _user_view = (TextView) findViewById(R.id.textViewHiUser);
        _spinner_patient = (Spinner) findViewById(R.id.spinner_show);
        _spinner_choose = (Spinner) findViewById(R.id.spinner_state);

        Intent i_result = getIntent();
//        String pass = i_result.getStringExtra("PASS_REGISTER");
//        Toast.makeText(this,"pass: "+pass,Toast.LENGTH_SHORT).show();
        if(i_result.getStringExtra("USER_REGISTER") != null){
            user_register = i_result.getStringExtra("USER_REGISTER");
            flag_newUser = 1;
            _hi.setText("שלום: ");
            _user_view.setText(user_register);
            _user_view.setTextColor(Color.rgb(204,0,0));
        }

        else if(i_result.getStringExtra("USER_SIGN_IN") != null){
            user_signIn = i_result.getStringExtra("USER_SIGN_IN");
            flag_login = 1;
            _hi.setText("שלום: ");
            _user_view.setText(user_signIn);
            _user_view.setTextColor(Color.rgb(204,0,0));
        }
        _spinner_patient.setOnItemSelectedListener(this);

        if(flag_newUser == 1){
            patients = new ArrayList<>();
            flag_newUser = 0;
        }

        // temp
        if(flag_login == 1){
            patients = new ArrayList<>();
            flag_login = 0;
            patients.add("משה אשכנזי");
            patients.add("דוד מזרחי");
            patients.add("שלמה כהן");
        }


        if(patients.size() > 0){
            adapter = new ArrayAdapter<>(AreaPersonalActivity.this,android.R.layout.simple_spinner_item, patients);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            _spinner_patient.setAdapter(adapter);
            Toast.makeText(this,"size: " + String.valueOf(patients.size()),Toast.LENGTH_SHORT).show();
        }
    }

    // ------------ Select Patient --------------------------------------------
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){
        s_patient = adapterView.getItemAtPosition(i).toString();
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }



    // ---------------------------------------------------------------------
    // --------------------- Add Patient ----------------------------------
    public void addPatient(View view){
        // Dialog and show spinner
        AlertDialog.Builder builder = new AlertDialog.Builder(AreaPersonalActivity.this);
        //builder.setTitle("New Patient");
        builder.setTitle("מטופל חדש");
        builder.setIcon(android.R.drawable.ic_menu_edit);
        //builder.setMessage("Please enter Name and Last Name");
        builder.setMessage("אנא הקלד שם פרטי ושם משפחה עבור מטופל");
        input_patient = new EditText(AreaPersonalActivity.this);
        builder.setView(input_patient);

        //builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
        builder.setPositiveButton("הוסף", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input_patient.getText().toString()!=null ) {
                    new_patient = input_patient.getText().toString();
                    patients.add(new_patient);
                }
                adapter = new ArrayAdapter<>(AreaPersonalActivity.this,android.R.layout.simple_spinner_item, patients);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                _spinner_patient.setAdapter(adapter);
            }
        });

        //builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        builder.setNegativeButton("בטל", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }



    // ---------------------------------------------------------------------
    // --------------------- Share Patient --------------------------------
    public void sharePatient(View view){
        Toast.makeText(this,"Share Demo Patients",Toast.LENGTH_SHORT).show();
    }



    // ---------------------------------------------------------------------
    // --------------------- Button Next ----------------------------------
    public void press_next(View view){
        int flag = 1;
        if(_spinner_patient.getSelectedItem() == null){
            flag = 0;
            Toast.makeText(this,"Select/Add Patient",Toast.LENGTH_SHORT).show();
        }

        if(_spinner_choose.getSelectedItem() == null){
            Toast.makeText(this,"Select State",Toast.LENGTH_SHORT).show();
        }

        s_state = _spinner_choose.getSelectedItem().toString();
        s_state_index = _spinner_choose.getSelectedItemPosition();

        if(flag == 1){
            Toast.makeText(this,s_state,Toast.LENGTH_SHORT).show();

            if(s_state_index == 0){
                Intent i;
                i = new Intent(this, Display_Patient.class);
                i.putExtra("PATIENT",s_patient);
                startActivity(i);
            }
            else if(s_state_index == 1){
                Intent i;
                i = new Intent(this, EditPatient.class);
                i.putExtra("PATIENT",s_patient);
                startActivity(i);
            }

        }
        else
            Toast.makeText(this,"Select State/Patient",Toast.LENGTH_SHORT).show();

    }



}


