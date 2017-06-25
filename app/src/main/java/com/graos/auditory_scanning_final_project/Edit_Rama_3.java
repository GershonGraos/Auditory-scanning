package com.graos.auditory_scanning_final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Edit_Rama_3 extends AppCompatActivity {
    TextView _my_lastClick_item;
    EditText _request;
    ListView _my_list_view;
    EditText a;

    String lastClick_request, id_patient, i_parent;
    String request_now;
    String request_click;
    String id_parent_send;

    DBHelper_Requests my_db_requests;
    MyListAdapter my_list_adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__rama_3);
        setTitle(R.string.nameActivity_edit_pattient);

        my_db_requests = new DBHelper_Requests(this);

        _my_lastClick_item = (TextView) findViewById(R.id.textView_request_click_rama3);
        _request = (EditText) findViewById(R.id.editText_request_rama3);
        _my_list_view = (ListView) findViewById(R.id.listItems_Rama3);

        Intent i;
        i = getIntent();
        lastClick_request = i.getStringExtra("THE_REQUEST_2");
        id_patient = i.getStringExtra("ID_PT_2");
        i_parent = i.getStringExtra("MY_I_PARENT_2");

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
                        break;
                    }
                }

                Intent i;
                i = new Intent(Edit_Rama_3.this, Edit_Rama_4.class);
                i.putExtra("THE_REQUEST_TO_4",request_click);
                i.putExtra("ID_PT_TO_4",id_patient);
                i.putExtra("MY_I_PARENT_TO_4",id_parent_send);
                startActivity(i);
            }
        });


        // LONG-CLICK TO DELETE SPECIFIC REQUEST
        _my_list_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l){
                request_click = (String)_my_list_view.getItemAtPosition(position);
                View view1 = LayoutInflater.from(Edit_Rama_3.this).inflate(R.layout.edit_or_delete_request, null);
                final AlertDialog.Builder builder_buttons = new AlertDialog.Builder(Edit_Rama_3.this);
                builder_buttons.setView(view1);
                builder_buttons.setTitle(R.string.edit_tittle_buttons);
                builder_buttons.setIcon(R.mipmap.ic_alert);
                builder_buttons.setNegativeButton(R.string.edit_exit_option, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder_buttons.show();
                return true;
            }
        });
    }




    // ----------------------------------------------------------------------------------
    // ------------------------ PRIVATE FUNCTIONS ---------------------------------------
    // ADD REQUEST TO DB
    public void press_addItem_Rama3(View view){
        request_now= _request.getText().toString();
        int count = 0;
        int flag = 0;

        if(!request_now.equals("")){
            Cursor cursor = my_db_requests.show_requests();
            while (cursor.moveToNext()){
                if(cursor.getString(0). equals(id_patient) && cursor.getString(3). equals(request_now)){
                    Toast.makeText(this, R.string.same_request, Toast.LENGTH_SHORT).show();
                    flag = 1;
                }
            }

            if(flag == 0){
                long temp = my_db_requests.add_request(id_patient, i_parent, "3", request_now, getDateTime(), count);
                if (temp == -1)
                    Toast.makeText(this, R.string.sign_up_error, Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, R.string.sign_up_successful, Toast.LENGTH_SHORT).show();

                _request.setText("");
                populateListViews();
            }
        }
        else
            Toast.makeText(this, R.string.insert_item, Toast.LENGTH_SHORT).show();
    }


    // SHOW THE STRING ARRAY REQUESTS INTO LAYOUT //
    public void populateListViews(){
        Cursor cursor = my_db_requests.show_requests();
        if(cursor.getCount() != 0) {
            ArrayList<String> listRequests = new ArrayList<String >();
            while (cursor.moveToNext()) {
                if(cursor.getString(0).equals(id_patient) && cursor.getString(1).equals(i_parent) && cursor.getString(2).equals("3"))
                    listRequests.add(cursor.getString(3));
            }
            my_list_adapter = new MyListAdapter(this, listRequests);
            _my_list_view.setAdapter(my_list_adapter);
        }
        else
            _my_list_view.setAdapter(null);
    }


    // GET DATE TIME
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    // EDIT REQUEST
    public void button_edit_request(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Rama_3.this);
        builder.setTitle(R.string.edit_tittle);
        builder.setIcon(R.mipmap.ic_edit_req);
        a = new EditText(Edit_Rama_3.this);
        builder.setView(a);
        builder.setMessage(R.string.edit_message)

                .setPositiveButton(R.string.button_get_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String in = a.getText().toString();
                        if( !in.equals("")){
                            Cursor cursor = my_db_requests.show_requests();
                            while(cursor.moveToNext()) {
                                if(cursor.getString(3).equals(request_click)){
                                    boolean edit_row = my_db_requests.update_data(in , cursor.getString(4));
                                    if(edit_row == true){
                                        populateListViews();
                                        Toast.makeText(Edit_Rama_3.this, R.string.edit_successfully, Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                        Toast.makeText(Edit_Rama_3.this, R.string.edit_not_successfully, Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }
                        else
                            Toast.makeText(Edit_Rama_3.this, R.string.edit_enter_request, Toast.LENGTH_SHORT).show();
                    }
                })

                .setNegativeButton(R.string.button_get_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }


    // DELETE REQUEST
    public void button_delete_request(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(Edit_Rama_3.this);
        builder.setTitle(R.string.delete_item_tittle);
        builder.setIcon(R.mipmap.ic_remove);
        builder.setMessage(R.string.delete_item_quetion)

                .setPositiveButton(R.string.button_get_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Cursor cursor = my_db_requests.show_requests();
                        if(cursor.getCount() != 0){
                            while(cursor.moveToNext()) {
                                if(cursor.getString(3).equals(request_click)){
                                    Integer delete_row = my_db_requests.delete_data(cursor.getString(4));
                                    if(delete_row > 0)
                                        populateListViews();
                                    Toast.makeText(getApplicationContext(), R.string.delete_the_item , Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                })

                .setNegativeButton(R.string.button_get_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        builder.show();
    }


}
