package com.graos.auditory_scanning_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DBHelper_MongoDB_Data  extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "mpngodb_data.db";
    public static final String TABLE_NAME = "mongo_table";
    public static final String COL_1 = "last_update";
    public static final String COL_2 = "tbl_patients";
    public static final String COL_3 = "tbl_therapists";
    public static final String COL_4 = "tbl_requests";
    public static final String COL_5 = "tbl_patients_data";
    public static final String COL_6 = "id";

    public DBHelper_MongoDB_Data(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (last_update DATETIME, tbl_patients TEXT, tbl_therapists TEXT, tbl_requests TEXT, tbl_patients_data TEXT, id TEXT)");
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        onCreate(db);
    }
    public Cursor get_last_update(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT abs(julianday('now')-julianday(last_update)) FROM " + TABLE_NAME, null);
        //Cursor res = db.rawQuery("SELECT abs(julianday('now')-julianday('2017-05-26 20:30:45')) FROM " + TABLE_NAME, null);
        return res;
    }
    public Cursor getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }
    public long set_first_date(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();;
        contentValues.put(COL_1, getDateTime());
        contentValues.put(COL_2, "");
        contentValues.put(COL_3, "");
        contentValues.put(COL_4, "");
        contentValues.put(COL_5, "");
        contentValues.put(COL_6, "1");
        return db.insert(TABLE_NAME, null, contentValues); // add the db
    }
    // GET DATE TIME
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    public boolean delete_mongo_data(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,null,null)>0;
    }
    public boolean update_mongo_data(String tbl_patients, String tbl_therapists, String tbl_requests, String tbl_patients_data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, getDateTime());
        if(tbl_patients != "")
            contentValues.put(COL_2, tbl_patients);
        if(tbl_therapists != "")
            contentValues.put(COL_3, tbl_therapists);
        if(tbl_requests != "")
            contentValues.put(COL_4, tbl_requests);
        if(tbl_patients_data != "")
            contentValues.put(COL_5, tbl_patients_data);
        db.update(TABLE_NAME, contentValues, "id = ?", new String[] { "1" } );
        return true;
    }
    public boolean reset_mongo_data(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();;
        contentValues.put(COL_1, getDateTime());
        contentValues.put(COL_2, "");
        contentValues.put(COL_3, "");
        contentValues.put(COL_4, "");
        contentValues.put(COL_5, "");
        db.update(TABLE_NAME, contentValues, "id = ?", new String[] { "1" } );
        return true;
    }
    public void drop(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME + "");
    }
    public void create(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table " + TABLE_NAME + " (last_update DATETIME, tbl_patients TEXT, tbl_therapists TEXT, tbl_requests TEXT, tbl_patients_data TEXT, id TEXT)");
    }
}