package com.graos.auditory_scanning_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SA on 29/05/2017.
 */

public class DBHelper_Patients extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "patients.db";
    public static final String TABLE_NAME = "patients_table";
    public static final String COL_1 = "id";
    public static final String COL_2 = "name";
    public static final String COL_3 = "therapist_id";

    public DBHelper_Patients(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( id TEXT, name TEXT, therapist_id TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public long add_patient(String id_pt, String name, String id_th){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();;
        contentValues.put(COL_1, id_pt);
        contentValues.put(COL_2, name);
        contentValues.put(COL_3, id_th);
        long result_add = db.insert(TABLE_NAME, null, contentValues); // add the db
        return result_add;
    }

    public Cursor show_patients(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer delete_patient(String id_patient){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id_pt = ?", new String[] { id_patient } );
    }
}
