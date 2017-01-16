package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 15/01/2017.
 */
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;



// ************* to ListView ************** //
class MyListAdapter extends CursorAdapter {

    private LayoutInflater inflater;
    private TextView name;

    public MyListAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        name = (TextView) view.findViewById(R.id.textViewItem);

        if(!cursor.getString(1).equals("")){
            name.setText(cursor.getString(1));
        }

        notifyDataSetChanged();
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        return inflater.inflate(R.layout.edit_list_items, viewGroup, false);
    }
}
