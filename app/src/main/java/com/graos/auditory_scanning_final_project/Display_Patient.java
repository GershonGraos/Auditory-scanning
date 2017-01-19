package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 18/01/2017.
 */
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Display_Patient extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    TextView _name;
    ListView _my_list;

    String my_patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__patient);
        setTitle("Display");

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


        public void onItemClick(AdapterView<?> parent, View view,
            int position, long id) {

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

}

