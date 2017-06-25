package com.graos.auditory_scanning_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_App_Data  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "patients.db";

    public DBHelper_App_Data(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void show_tables() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ATTACH patients.db AS my_db");
        db.execSQL("SELECT name FROM my_db.sqlite_master WHERE type='table'");
    }
}