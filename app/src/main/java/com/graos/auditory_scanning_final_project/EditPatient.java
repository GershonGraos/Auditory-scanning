package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 13/01/2017.
 */
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditPatient extends AppCompatActivity {

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

}


//     ArrayAdapter<String> adapter;
//// values = new String[]{"adasdasda","asdasdasda"};
//adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);
//        _my_list.setAdapter(adapter);
//
//        _my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//@Override
//public void onItemClick(AdapterView<?> parent, View view,
//        int position, long id) {
//
//        // ListView Clicked item index
//        int itemPosition     = position;
//
//        // ListView Clicked item value
//        String  itemValue    = (String)_my_list.getItemAtPosition(position);
//
//        // Show Alert
//        Toast.makeText(getApplicationContext(),
//        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
//        .show();
//
//        }
//
//        });



// ---------- to the screen DISPLAY --------------
// ********* add image button ********************************


//    // Construct the data source
//    ArrayList<User> arrayOfUsers = new ArrayList<User>();
//    // Create the adapter to convert the array to views
//    UsersAdapter adapter = new UsersAdapter(this, arrayOfUsers);
//    // Attach the adapter to a ListView
//    ListView listView = (ListView) findViewById(R.id.lvItems);
//listView.setAdapter(adapter);