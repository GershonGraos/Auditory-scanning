package com.graos.auditory_scanning_final_project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MainActivity extends AppCompatActivity {
    EditText _passLogIn;
    EditText _userLogin;

    DBHelper_Therapists my_dbHelper_Therapist;

    View focusView = null;
    boolean cancel = false;



    private MongoClient mongoClient;

    //boolean isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);}
    // ***************************************************
    // ************* ON CREATE **************************
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        my_dbHelper_Therapist = new DBHelper_Therapists(this);

        _passLogIn = (EditText) findViewById(R.id.editText_login_pass);
        _userLogin = (EditText) findViewById(R.id.editText_login_user);


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        MongoClientURI uri  = new MongoClientURI("mongodb://yerson28890:auditoryMongo1!@ds133398.mlab.com:33398/patients_db");
        MongoClient client = new MongoClient(uri);
        MongoDatabase db = client.getDatabase(uri.getDatabase());

        /*
        MongoCollection<BasicDBObject> collection = db.getCollection("patients", BasicDBObject.class);
        MongoCursor iterator = collection.find().iterator();
        while (iterator.hasNext()) {
            Log.d("data:",iterator.next().toString());
        }


        BasicDBObject document = new BasicDBObject();

        document.put("id", "302530001");
        document.put("name", "avi choen");
        document.put("register_date", new Date());
        document.put("therapist_id", "302530002");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", "302530003");
        document.put("name", "maor lavi");
        document.put("register_date", new Date());
        document.put("therapist_id", "302530004");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", "302530005");
        document.put("name", "david villa");
        document.put("register_date", new Date());
        document.put("therapist_id", "302530006");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", "302530007");
        document.put("name", "eli ovad");
        document.put("register_date", new Date());
        document.put("therapist_id", "302530008");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", "302530009");
        document.put("name", "aviel edri");
        document.put("register_date", new Date());
        document.put("therapist_id", "302530010");
        collection.insertOne(document);

        collection = db.getCollection("optionals_requests_of_patient", BasicDBObject.class);
        document = new BasicDBObject();

        document.put("id", 0);
        document.put("parent_id", -1);
        document.put("stage", 1);
        document.put("request", "משהו דחוף");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", 1);
        document.put("parent_id", -1);
        document.put("stage", 1);
        document.put("request", "משהו מפריע לי");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", 2);
        document.put("parent_id", -1);
        document.put("stage", 1);
        document.put("request", "לשוחח");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", 3);
        document.put("parent_id", -1);
        document.put("stage", 1);
        document.put("request", "נושאים");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", 4);
        document.put("parent_id", 3);
        document.put("stage", 2);
        document.put("request", "אוכל ושתיה");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", 5);
        document.put("parent_id", 3);
        document.put("stage", 2);
        document.put("request", "בגדים");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", 6);
        document.put("parent_id", 3);
        document.put("stage", 2);
        document.put("request", "כלי אוכל");
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", 7);
        document.put("parent_id", 3);
        document.put("stage", 2);
        document.put("request", "כלי שתיה");
        collection.insertOne(document);

        collection = db.getCollection("statistics_of_patient", BasicDBObject.class);

        document = new BasicDBObject();
        document.put("patient_id", "302530001");
        document.put("therapist_id", "302530002");
        document.put("request_id", 0);
        document.put("last_update", new Date());
        document.put("requests_counter", 2);
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("patient_id", "302530003");
        document.put("therapist_id", "302530004");
        document.put("request_id", 1);
        document.put("last_update", new Date());
        document.put("requests_counter", 1);
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("patient_id", "302530005");
        document.put("therapist_id", "302530006");
        document.put("request_id", 3);
        document.put("last_update", new Date());
        document.put("requests_counter", 8);
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("patient_id", "302530007");
        document.put("therapist_id", "302530008");
        document.put("request_id", 5);
        document.put("last_update", new Date());
        document.put("requests_counter", 4);
        collection.insertOne(document);

        collection = db.getCollection("therapists", BasicDBObject.class);

        document = new BasicDBObject();
        document.put("id", "302530002");
        document.put("name", "bani mizrchi");
        document.put("user_name", "avi123");
        document.put("password", "123456");
        document.put("register_date", new Date());
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", "302530004");
        document.put("name", "lior gor");
        document.put("user_name", "lior123");
        document.put("password", "123456");
        document.put("register_date", new Date());
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", "302530006");
        document.put("name", "gad choen");
        document.put("user_name", "gad123");
        document.put("password", "123456");
        document.put("register_date", new Date());
        collection.insertOne(document);

        document = new BasicDBObject();
        document.put("id", "302530008");
        document.put("name", "salom bar");
        document.put("user_name", "salom123");
        document.put("password", "123456");
        document.put("register_date", new Date());
        collection.insertOne(document);
        */

        //_userLogin.setText("" + mong.getAuth());
        //mong.getDB();
        _userLogin.requestFocus();
    }


    public void help_main_activity(View view){
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.tittle_help_main);
        builder.setIcon(R.mipmap.ic_help3);
        builder.setMessage(R.string.text_help_main);
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
            if(cursor.getCount() != 0){
                while (cursor.moveToNext()){
                    if(cursor.getString(2).equals(user)){
                        if(cursor.getString(3).equals(password)){
                            String id_the = cursor.getString(0);
                            Intent i = new Intent(this, AreaPersonalActivity.class);
                            i.putExtra("ID_REGISTER",id_the);
                            i.putExtra("USER_SIGN_IN",user);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(this,R.string.error_pass,Toast.LENGTH_SHORT).show();
                            _passLogIn.setText("");
                        }
                    }
                    else
                        Toast.makeText(this,R.string. error_user_not_exists,Toast.LENGTH_SHORT).show();
                }
            }
            else
                Toast.makeText(this,R.string.error_user_pass,Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(this,R.string.error_field_pass_user,Toast.LENGTH_SHORT).show();

    }


//        int flag = 0;
//
//        if(user.length() == 0){
//            _userLogin.setError(getString(R.string.error_field_required));
//            focusView = _userLogin;
//            cancel = true;
//            flag++;
//        }
//
//        else if(password.length() == 0){
//            _passLogIn.setError(getString(R.string.error_field_required));
//            focusView = _passLogIn;
//            cancel = true;
//            flag++;
//        }
//
//        if(flag == 0)
//        {
//            Intent i = new Intent(this, AreaPersonalActivity.class);
//            i.putExtra("USER_SIGN_IN",user);
//            startActivity(i);
//        }
//
//        else{
//            Toast.makeText(this,R.string.error_field_pass_user,Toast.LENGTH_SHORT).show();
//        }

}
