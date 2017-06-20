package com.graos.auditory_scanning_final_project;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Display_rama2 extends AppCompatActivity {

    TextView _my_lastClick_item;
    ListView _my_list_view;

    String lastClick_request, id_patient, i_parent;
    String request_click;
    String id_parent_send;
    int counter = 0;

    DBHelper_Requests my_db_requests;
    MyListAdapter my_list_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_rama2);

        setTitle(R.string.nameActivity_display_pattient);

        my_db_requests = new DBHelper_Requests(this);

        _my_lastClick_item = (TextView) findViewById(R.id.textViewName_rama2);
        _my_list_view = (ListView) findViewById(R.id.listView_rama2);

        Intent i;
        i = getIntent();
        lastClick_request = i.getStringExtra("THE_REQUEST");
        id_patient = i.getStringExtra("ID_PT");
        i_parent = i.getStringExtra("MY_I_PARENT");

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

                Intent i;
                i = new Intent(Display_rama2.this, Display_rama3.class);
                i.putExtra("THE_REQUEST_2",request_click);
                i.putExtra("ID_PT_2",id_patient);
                i.putExtra("MY_I_PARENT_2",id_parent_send);
                startActivity(i);
            }
        });
    }


    // SHOW THE STRING ARRAY REQUESTS INTO LAYOUT //
    public void populateListViews(){
        Cursor cursor = my_db_requests.show_requests();
        if(cursor.getCount() != 0) {
            ArrayList<String> listRequests = new ArrayList<String >();
            while (cursor.moveToNext()) {
                if(cursor.getString(0).equals(id_patient) && cursor.getString(1).equals(i_parent) && cursor.getString(2).equals("2"))
                    listRequests.add(cursor.getString(3));
            }
            my_list_adapter = new MyListAdapter(this, listRequests);
            _my_list_view.setAdapter(my_list_adapter);
        }
        else
            _my_list_view.setAdapter(null);
    }
}
