package com.graos.auditory_scanning_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_App_Data  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "app_data.db";
    public static final String TABLE_NAME = "app_data_table";
    public static final String COL_1 = "mongo_last_update";

    public DBHelper_App_Data(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (mongo_last_update DATETIME default current_timestamp)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}