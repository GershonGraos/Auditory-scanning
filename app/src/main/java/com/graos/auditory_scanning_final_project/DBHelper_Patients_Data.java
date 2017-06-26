package com.graos.auditory_scanning_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public long insert_patient_data(String patient_id, String yes_video, String yes_audio, String words_list){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();;
        contentValues.put(COL_1, patient_id);
        contentValues.put(COL_2, yes_video);
        contentValues.put(COL_3, yes_audio);
        contentValues.put(COL_4, words_list);
        long result_add = db.insert(TABLE_NAME, null, contentValues); // add the db
        return result_add;
    }

    public boolean update_patient_data(String patient_id, String yes_video, String yes_audio, String words_list){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, patient_id);
        contentValues.put(COL_2, yes_video);
        contentValues.put(COL_3, yes_audio);
        contentValues.put(COL_4, words_list);
        db.update(TABLE_NAME, contentValues, "patient_id = ?", new String[] { patient_id } );
        return true;
    }

    public Cursor get_patient_data_by_id(String patient_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select yes_video,yes_audio,words_list from " + TABLE_NAME + " where patient_id = '" + patient_id + "'", null);
        return res;
    }

    public boolean delete_patient_data_by_id(String patient_id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "patient_id = ?", new String[] { patient_id } )>0;
    }
}

