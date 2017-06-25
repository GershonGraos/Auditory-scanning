package com.graos.auditory_scanning_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SA on 15/06/2017.
 */

// *************** CREATE DATA BASE ****************
public class DBHelper_Requests extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "requests.db";
    public static final String TABLE_NAME = "requests_table";
    public static final String COL_1 = "id_patient";
    public static final String COL_2 = "parent_id";
    public static final String COL_3 = "stage";
    public static final String COL_4 = "request";
    public static final String COL_5 = "id";
    public static final String COL_6 = "last_update";
    public static final String COL_7 = "counter";

    public DBHelper_Requests(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " ( id_patient TEXT, parent_id TEXT, stage TEXT, request TEXT, id INTEGER PRIMARY KEY AUTOINCREMENT, last_update TEXT, counter INTEGER)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }

    public long add_request(String id_pt, String id_parent, String stage_rama, String request, String up, int count){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id_pt);
        contentValues.put(COL_2, id_parent);
        contentValues.put(COL_3, stage_rama);
        contentValues.put(COL_4, request);
        contentValues.put(COL_6, up);
        contentValues.put(COL_7, count);
        long result_add = db.insert(TABLE_NAME, null, contentValues); // add the db
        return result_add;
    }

    public Cursor show_requests(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public Integer delete_data(String id_counter){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ?", new String[] { id_counter } );
    }

    public boolean update_data(String req, String i){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4, req);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[] { i } );
        return true;
    }

    public boolean update_counter(int count, String i){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7, count);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[] { i } );
        return true;
    }
}