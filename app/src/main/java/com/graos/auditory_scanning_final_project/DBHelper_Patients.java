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

    private Context thisContext;

    public DBHelper_Patients(Context context) {
        super(context, DATABASE_NAME, null, 1);
        thisContext = context;
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
        log_this_action_for_mongo();
        return result_add;
    }

    public Cursor show_patients(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Cursor show_patients_by_id(String id_th){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where therapist_id = '" + id_th + "'", null);
        return res;
    }

    public Integer delete_patient(String id_patient){
        SQLiteDatabase db = this.getWritableDatabase();
        log_this_action_for_mongo();
        return db.delete(TABLE_NAME, "id_pt = ?", new String[] { id_patient } );
    }

    private void log_this_action_for_mongo(){
        DBHelper_MongoDB_Data dbHelper_mongoDB_data = new DBHelper_MongoDB_Data(thisContext);
        dbHelper_mongoDB_data.update_mongo_data("1","","","");
    }
}
