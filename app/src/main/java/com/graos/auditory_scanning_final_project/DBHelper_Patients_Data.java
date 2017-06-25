package com.graos.auditory_scanning_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_Patients_Data extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "patient_data.db";
    public static final String TABLE_NAME = "patient_data_table";
    public static final String COL_1 = "patient_id";
    public static final String COL_2 = "yes_video";
    public static final String COL_3 = "yes_audio";
    public static final String COL_4 = "words_list";

    public DBHelper_Patients_Data(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( patient_id TEXT, yes_video TEXT, yes_audio TEXT, words_list TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long insert_patient_data(String id_user, String name, String user_name, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();;
        contentValues.put(COL_1, id_user);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, user_name);
        contentValues.put(COL_4, password);
        long result_add = db.insert(TABLE_NAME, null, contentValues); // add the db
        return result_add;
    }

}

