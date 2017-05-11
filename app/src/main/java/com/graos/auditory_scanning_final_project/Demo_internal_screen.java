package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 19/01/2017.
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

public class Demo_internal_screen extends AppCompatActivity {

    ArrayAdapter<String> adapter;
    ListView _my_list;

    String my_choose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_internal_screen);

        _my_list = (ListView) findViewById(R.id.listView2);

        Intent i_result_choose;
        i_result_choose = getIntent();
        if(i_result_choose.getStringExtra("CHOOSE") != null){
            my_choose = i_result_choose.getStringExtra("CHOOSE");
        }

        setTitle(my_choose);

        String []values = new String[]{"לשבת","לקום","לשכב","להתהפך","להזיז","לקפל","לזוז","ליישר","לסדר","לחפש","להביא"};
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);
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
            }
        });
    }


    // ---------------------------------------------------------------------
    // --------------------- Button Delete --------------------------------
    public void press_delete_demo2(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(Demo_internal_screen.this);
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
