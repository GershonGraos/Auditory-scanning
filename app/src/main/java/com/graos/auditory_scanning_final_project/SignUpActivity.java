package com.graos.auditory_scanning_final_project;
/**
 * Created by GG on 06/01/2017.
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class SignUpActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private UserLoginTask mAuthTask = null;

    // UI references //
    private AutoCompleteTextView userRegister;
    private EditText mPasswordRegister;
    private EditText idRegister;
    private EditText nameRegister;
    private View mProgressView;
    private View mLoginFormView;

    // To save the DB //
    DBHelper_Therapists my_db_new_therapist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        // ----- DB -------
        my_db_new_therapist = new DBHelper_Therapists(this);

        idRegister = (EditText) findViewById(R.id.id_register);
        nameRegister = (EditText) findViewById(R.id.name_register);

        // Set up the login form.
        userRegister = (AutoCompleteTextView) findViewById(R.id.user_register);
        idRegister.requestFocus();
        populateAutoComplete();

        mPasswordRegister= (EditText) findViewById(R.id.password_register);
        mPasswordRegister.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.button_register);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });
//
//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(userRegister, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    // ------- ATTEMPT TO REGISTER ---------- //
    public void attemptRegister() {
        Intent i = new Intent(this, AreaPersonalActivity.class);
        String id = idRegister.getText().toString();
        i.putExtra("ID_REGISTER", id);  // put only this data to next activity
        startActivity(i);


//        if (mAuthTask != null) {
//            return;
//        }
//
//        // Reset errors.
//        userRegister.setError(null);
//        mPasswordRegister.setError(null);
//
//        // Store values at the time of the login attempt.
//        String user = userRegister.getText().toString();
//        String password = mPasswordRegister.getText().toString();
//        String id = idRegister.getText().toString();
//        String name = nameRegister.getText().toString();
//
//
//
//        boolean cancel = false;
//        View focusView = null;
//
//        // Check for a valid password, if the user entered one.
//        if (!isPasswordValid(password)) {
//            mPasswordRegister.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordRegister;
//            cancel = true;
//        }
//
//        if(TextUtils.isEmpty(password)){
//            mPasswordRegister.setError(getString(R.string.error_field_required));
//            focusView = mPasswordRegister;
//            cancel = true;
//        }
//
//        // Check for a valid user
//        if (TextUtils.isEmpty(user)) {
//            userRegister.setError(getString(R.string.error_field_required));
//            focusView = userRegister;
//            cancel = true;
//        }
//
//        if (TextUtils.isEmpty(id)) {
//            idRegister.setError(getString(R.string.error_field_required));
//            focusView = idRegister;
//            cancel = true;
//        }
//
//        if (TextUtils.isEmpty(name)) {
//            nameRegister.setError(getString(R.string.error_field_required));
//            focusView = nameRegister;
//            cancel = true;
//        }
//
//
//        if (cancel)
//            focusView.requestFocus();
//
//        else {
//            if (id.matches("\\d+(?:\\.\\d+)?")){  // is number
//                // ***** Save the DB ***** //
//                long b = my_db_new_therapist.insert_data_therapist(id, name, user, password);
//                if (b == -1)
//                    Toast.makeText(this, R.string.sign_up_error, Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(this, R.string.sign_up_successful, Toast.LENGTH_SHORT).show();
//
//                userRegister.setText("");
//                mPasswordRegister.setText("");
//                idRegister.setText("");
//                nameRegister.setText("");
//
//                Intent i = new Intent(this, AreaPersonalActivity.class);
//                i.putExtra("ID_REGISTER", id);  // put only this data to next activity
//                i.putExtra("USER_REGISTER", user);
//                startActivity(i);
//            }
//            else
//                Toast.makeText(this, R.string.id_error_no_int, Toast.LENGTH_SHORT).show();
//        }
    }


    // DB - view all
    public void view_all_function(View view){
        Cursor res = my_db_new_therapist.show_data_therapists();
        if(res.getCount() == 0) {
            showMessage("Error", "No data found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()){
            buffer.append("Id: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("User: " + res.getString(2) + "\n");
            buffer.append("Password: " + res.getString(3)+ "\n\n");
        }
        showMessage("Data", buffer.toString());
    }

    public void showMessage(String tittle, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(tittle);
        builder.setMessage(Message);
        builder.show();
    }
    //--- END View all ----


    // DB - update data
    public void update_d(View view){
        String user = userRegister.getText().toString();
        String password = mPasswordRegister.getText().toString();
        String id = idRegister.getText().toString();
        String name = nameRegister.getText().toString();

        if (id.matches("\\d+(?:\\.\\d+)?")){
            boolean r_up = my_db_new_therapist.update_data(id, user, name, password);
            if(r_up == true)
                Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "NOT update", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, R.string.id_error_no_int, Toast.LENGTH_SHORT).show();

        userRegister.setText("");
        mPasswordRegister.setText("");
        idRegister.setText("");
        nameRegister.setText("");
    }

    // DB - delete user
    public void delete_d(View view){
        String id = idRegister.getText().toString();
        if (id.matches("\\d+(?:\\.\\d+)?")){
            Integer delete_rows = my_db_new_therapist.delete_data(id);
            if(delete_rows > 0 )
                Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, "NOT delete", Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(this, R.string.id_error_no_int, Toast.LENGTH_SHORT).show();

        idRegister.setText("");
    }


    // ---------------------------------------------------------------
    //----------------------------------------------------------------


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allowa
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(SignUpActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        userRegister.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }









    // *********************USERLOGINTASK**********************
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordRegister.setError(getString(R.string.error_incorrect_password));
                mPasswordRegister.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

}
