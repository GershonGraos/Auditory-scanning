package com.graos.auditory_scanning_final_project;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by GG on 16/01/2017.
 */

// *************** CREATE DATA BASE ****************
// *************************************************
public class AssignmentsDBHelper extends SQLiteOpenHelper
{
    // CREATE AND "DELETE"(UPGRADE)
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + Constants.Items.TABLE_NAME + " (" +
            Constants.Items._ID+ " INTEGER PRIMARY KEY," +
            Constants.Items.ITEM+ " TEXT" + ");";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + Constants.Items.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Items.db";

    public AssignmentsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // USE - @Override
    public void onCreate(SQLiteDatabase db) {
        //Log.v("MyContactApp", "query=" + SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

