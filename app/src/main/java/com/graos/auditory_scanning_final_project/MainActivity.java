package com.graos.auditory_scanning_final_project;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    EditText _passLogIn;
    EditText _userLogin;
    View focusView = null;
    boolean cancel = false;
    private MongoDBJDBC mong;

    //boolean isRightToLeft = getResources().getBoolean(R.bool.is_right_to_left);}
    // ***************************************************
    // ************* ON CREATE **************************
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setTitle("Home Screen");
        //setTitleColor(ge);

        _passLogIn = (EditText) findViewById(R.id.editText_login_pass);
        _userLogin = (EditText) findViewById(R.id.editText_login_user);
        mong = new MongoDBJDBC();

        MongoDatabase db = mong.getDB();
        MongoCollection<BasicDBObject> table = db.getCollection("patients", BasicDBObject.class);
        BasicDBObject document = new BasicDBObject();
        document.put("id", "302530009");
        document.put("name", "aviel edri");
        document.put("register_date", new Date());
        //table.insertOne(document);

        /*
        ArrayList<String> col = new ArrayList<String>();
        ArrayList<String> op = new ArrayList<String>();
        ArrayList<Object> val = new ArrayList<Object>();
        col.add("id");
        op.add("$eq");
        val.add("302530002");
        FindIterable<Document> therapists_tbl  = mong.selectQuery("therapists",col,op,val);
        */
        /*MongoCursor<Document> cursor = therapists_tbl.iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
            }
        } finally {
            cursor.close();
        }*/

//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.showSoftInput(_userLogin, InputMethodManager.SHOW_IMPLICIT);
        //_userLogin.setText("" + mong.getAuth());
        //mong.getDB();
        _userLogin.requestFocus();
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
        int flag = 0;

        if(user.length() == 0){
            _userLogin.setError(getString(R.string.error_field_required));
            focusView = _userLogin;
            cancel = true;
            flag++;
        }

        else if(password.length() == 0){
            _passLogIn.setError(getString(R.string.error_field_required));
            focusView = _passLogIn;
            cancel = true;
            flag++;
        }

        if(flag == 0)
        {
            Intent i = new Intent(this, AreaPersonalActivity.class);
            i.putExtra("USER_SIGN_IN",user);
            startActivity(i);
        }

        else{
            Toast.makeText(this,R.string.error_field_pass_user,Toast.LENGTH_SHORT).show();
        }
    }

}
