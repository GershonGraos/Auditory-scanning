package com.graos.auditory_scanning_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText _passLogIn;
    EditText _userLogin;
    View focusView = null;
    boolean cancel = false;


    // ***************************************************
    // ************* ON CREATE **************************
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home Screen");
        //setTitleColor(ge);

        _passLogIn = (EditText) findViewById(R.id.editText_login_pass);
        _userLogin = (EditText) findViewById(R.id.editText_login_user);
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

        if(user.length() == 0){
            _passLogIn.setError(getString(R.string.error_invalid_email));
            focusView = _passLogIn;
            cancel = true;
        }

        if(password.length() == 0){
            _passLogIn.setError(getString(R.string.error_field_required));
            focusView = _passLogIn;
            cancel = true;
        }

        if(password.length() < 5){
            _passLogIn.setError(getString(R.string.error_invalid_password));
            focusView = _passLogIn;
            cancel = true;
        }

        else{
            Intent i = new Intent(this, AreaPersonalActivity.class);
            i.putExtra("USER_SIGN_IN",user);
            startActivity(i);
        }

    }

}
