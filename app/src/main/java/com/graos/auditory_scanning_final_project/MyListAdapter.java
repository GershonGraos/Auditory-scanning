package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 15/01/2017.
 */
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;


// ************* to ListView ************** //
class MyListAdapter extends ArrayAdapter<String> {

    Context c;
    ArrayList<String> list_requests;
    LayoutInflater inflater;
    TextView item;

    public MyListAdapter(Context context, ArrayList<String> list_requests) {
        super(context, R.layout.list_items_rows_edit, list_requests);
        this.c = context;
        this.list_requests = list_requests;
    }

    public View getView(int position, View convertView, ViewGroup parent){

        //check if the view is null, if so then create this
        if(convertView == null){
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_items_rows_edit, null);
        }

        item = (TextView) convertView.findViewById(R.id.textViewItem_all_Ramas);
        item.setText(list_requests.get(position));

        return  convertView;
    }
}
