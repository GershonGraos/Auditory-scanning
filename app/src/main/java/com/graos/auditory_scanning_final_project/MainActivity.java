package com.graos.auditory_scanning_final_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    EditText _passLogIn;
    EditText _userLogin;

    DBHelper_Therapists my_dbHelper_Therapist;
    DBHelper_MongoDB_Data dbHelper_mongoDB_data;

    View focusView = null;
    boolean cancel = false;
    Context thisContext;


    private MongoClient mongoClient;

    //boolean isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);}
    // ***************************************************
    // ************* ON CREATE **************************
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisContext = this;
        my_dbHelper_Therapist = new DBHelper_Therapists(this);

        _passLogIn = (EditText) findViewById(R.id.editText_login_pass);
        _userLogin = (EditText) findViewById(R.id.editText_login_user);

//        //------Doron's Shortcut-------
//        _userLogin.setText("121");
//        _passLogIn.setText("123456");
        //---------------------------

//        ------Gershon's Shortcut-------
//        _userLogin.setText("moshe123");
//        _passLogIn.setText("12345");
//        ---------------------------

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        dbHelper_mongoDB_data = new DBHelper_MongoDB_Data(this);
        //dbHelper_mongoDB_data.drop();
        //dbHelper_mongoDB_data.create();
        //boolean t = dbHelper_mongoDB_data.delete_mongo_data();
        Cursor all_data_cursor = dbHelper_mongoDB_data.getAll();
        if(all_data_cursor.getCount()==0){
            dbHelper_mongoDB_data.set_first_date();
        }
        Cursor cursor = dbHelper_mongoDB_data.get_last_update();
        if(cursor.moveToFirst()){
            if(cursor.getInt(0)>7){
                all_data_cursor.moveToFirst();
                if(all_data_cursor.getString(1).equals("1") || all_data_cursor.getString(2).equals("1") || all_data_cursor.getString(3).equals("1") || all_data_cursor.getString(4).equals("1")) {
                    try{
                        MongoClientURI uri = new MongoClientURI("mongodb://yerson28890:auditoryMongo1!@ds133398.mlab.com:33398/patients_db");
                        MongoClient client = new MongoClient(uri);
                        MongoDatabase db = client.getDatabase(uri.getDatabase());

                        if(all_data_cursor.getString(1).equals("1")){
                            MongoCollection<BasicDBObject> collection = db.getCollection("patients", BasicDBObject.class);
                            collection.deleteMany(new BasicDBObject());
                            DBHelper_Patients dbHelper_patients = new DBHelper_Patients(this);
                            Cursor c = dbHelper_patients.show_patients();
                            BasicDBObject document;
                            while(c.moveToNext()){
                                document = new BasicDBObject();
                                document.put("id", c.getString(0));
                                document.put("name", c.getString(1));
                                document.put("therapist_id", c.getString(2));
                                collection.insertOne(document);
                            }
                        }
                        if(all_data_cursor.getString(2).equals("1")){
                            MongoCollection<BasicDBObject> collection = db.getCollection("therapists", BasicDBObject.class);
                            collection.deleteMany(new BasicDBObject());
                            DBHelper_Therapists dbHelper_therapists = new DBHelper_Therapists(this);
                            Cursor c = dbHelper_therapists.show_data_therapists();
                            BasicDBObject document;
                            while(c.moveToNext()){
                                document = new BasicDBObject();
                                document.put("id", c.getString(0));
                                document.put("name", c.getString(1));
                                document.put("user_name", c.getString(2));
                                document.put("password", c.getString(3));
                                collection.insertOne(document);
                            }
                        }
                        if(all_data_cursor.getString(3).equals("1")){
                            MongoCollection<BasicDBObject> collection = db.getCollection("tbl_requests", BasicDBObject.class);
                            collection.deleteMany(new BasicDBObject());
                            DBHelper_Requests dbHelper_requests = new DBHelper_Requests(this);
                            Cursor c = dbHelper_requests.show_requests();
                            BasicDBObject document;
                            while(c.moveToNext()){
                                document = new BasicDBObject();
                                document.put("id_patient", c.getString(0));
                                document.put("parent_id", c.getString(1));
                                document.put("stage", c.getString(2));
                                document.put("request", c.getString(3));
                                document.put("id", c.getInt(4));
                                document.put("last_update", c.getString(5));
                                document.put("counter", c.getInt(6));
                                collection.insertOne(document);
                            }
                        }
                        if(all_data_cursor.getString(4).equals("1")){
                            MongoCollection<BasicDBObject> collection = db.getCollection("tbl_patients_data", BasicDBObject.class);
                            collection.deleteMany(new BasicDBObject());
                            DBHelper_Patients_Data dbHelper_patients_data = new DBHelper_Patients_Data(this);
                            Cursor c = dbHelper_patients_data.get_patient_data();
                            BasicDBObject document;
                            while(c.moveToNext()){
                                document = new BasicDBObject();
                                document.put("patient_id", c.getString(0));
                                document.put("yes_video", c.getString(1));
                                document.put("yes_audio", c.getString(2));
                                document.put("words_list", c.getString(3));
                                collection.insertOne(document);
                            }
                        }
                        dbHelper_mongoDB_data.reset_mongo_data();
                    }catch (Exception e){
                    }
                }
            }
        }
        _userLogin.requestFocus();
    }


    public void help_main_activity(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.tittle_help_main);
        builder.setIcon(R.mipmap.ic_help3);
        builder.setMessage(R.string.help_main_activity);
        builder.show();
    }


    // ***************************************************
    // ************* SIGN UP ****************************
    public void press_signUp(View v){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }

    // ***************************************************
    // ************ SIGN IN *****************************
    public void press_signIn(View view) {
        String password = _passLogIn.getText().toString();
        String user = _userLogin.getText().toString();

        if(!password.equals("") && !user.equals("")){
            Cursor cursor = my_dbHelper_Therapist.show_data_therapists();
            boolean user_found = false;
            if(cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    if(cursor.getString(2).equals(user)){
                        if(cursor.getString(3).equals(password)){
                            String id_the = cursor.getString(0);
                            Intent i = new Intent(this, AreaPersonalActivity.class);
                            i.putExtra("ID_REGISTER",id_the);
                            i.putExtra("USER_SIGN_IN",user);
                            startActivity(i);
                            user_found = true;
                            break;
                        }
                        else{
                            Toast.makeText(this,R.string.error_pass,Toast.LENGTH_SHORT).show();
                            _passLogIn.setText("");
                            return;
                        }
                    }
                }
                if(!user_found)
                    Toast.makeText(this,R.string.error_therapist_not_exists,Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(this,R.string.error_user_pass,Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(this,R.string.error_field_pass_user,Toast.LENGTH_SHORT).show();

    }
    public void open_forgot_window(View v) {
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.forgot_password_activity, null);
        final EditText user_id = (EditText) view.findViewById(R.id.therapist_id);
        final EditText user_name = (EditText) view.findViewById(R.id.therapist_username);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(thisContext.getResources().getDrawable(R.mipmap.ic_forgot_password));
        builder.setTitle(R.string.app_textPassForgot);
        builder.setView(view);
        builder.setPositiveButton(R.string.therapist_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (user_id.getText().toString().isEmpty() || user_name.getText().toString().isEmpty())
                    Toast.makeText(MainActivity.this, R.string.error_fields_required_alert, Toast.LENGTH_SHORT).show();

                else {
                    String therapist_id = user_id.getText().toString();
                    String therapist_username = user_name.getText().toString();

                    if (therapist_id.matches("\\d+(?:\\.\\d+)?")) {  // is a number
                        Cursor cursor = my_dbHelper_Therapist.show_therapist_data_by_id_and_user_name(therapist_id,therapist_username);
                        if (cursor.getCount() == 0) {
                            Toast.makeText(MainActivity.this, R.string.error_therapist_not_exists, Toast.LENGTH_SHORT).show();
                        }else{
                            cursor.moveToFirst();
                            android.app.AlertDialog.Builder builder;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                builder = new android.app.AlertDialog.Builder(thisContext, android.R.style.Theme_Material_Dialog_Alert);
                            } else {
                                builder = new android.app.AlertDialog.Builder(thisContext);
                            }
                            builder.setTitle(thisContext.getResources().getString(R.string.therapist_your_password))
                                    .setMessage(thisContext.getResources().getString(R.string.therapist_your_password_is) + " " + cursor.getString(3))
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    })
                                    .setIcon(R.mipmap.ic_forgot_password)
                                    .show();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, R.string.id_error_no_int, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.newPtnt_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }
}
