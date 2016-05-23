package com.example.danijel.execomhackathon;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.danijel.execomhackathon.db.DatabaseHelper;
import com.example.danijel.execomhackathon.db.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.security.MessageDigest;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String ALGORITHM = "SHA-256";
    private static final String TAG = RegisterActivity.class.getName();

    private DatabaseHelper dbHelper;
    private static int userValid;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox rememberMe;

    private boolean keyUlogovan = false;
    private SharedPreferences loginPreferences;
    private static SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            keyUlogovan = extras.getBoolean("keyUlogovan");
        }

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        rememberMe = (CheckBox) findViewById(R.id.checkBoxRememberMe);
        loginPreferences = getPreferences(MODE_PRIVATE);
        loginPreferences.edit();
        int restoredText = loginPreferences.getInt("loginStatus", 0);
        if (restoredText == 1 && !keyUlogovan) {
            finish();
            Intent i = new Intent(getApplicationContext(), UlogovanActivity.class);
            startActivity(i);
            keyUlogovan = false;
        }

        if(keyUlogovan){
            loginPrefsEditor = getPreferences(MODE_PRIVATE).edit();
            loginPrefsEditor.putInt("loginStatus", 0);// removed ""
            loginPrefsEditor.commit();
        }

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(attemptLogin()){
                    if(rememberMe.isChecked()){
                        loginPrefsEditor = getPreferences(MODE_PRIVATE).edit();
                        loginPrefsEditor.putInt("loginStatus", 1);// removed ""
                        loginPrefsEditor.putString("email", mEmailView.getText().toString());// removed ""
                        loginPrefsEditor.commit();
                        keyUlogovan = false;
                    }else{
                        loginPrefsEditor.clear();
                        loginPrefsEditor.commit();
                    }
                    Intent i = new Intent(getApplicationContext(), UlogovanActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        /**
         * ukoliko se klikne registracija poziva se novi aktiviti
         */
        Button buttonRegister = (Button) findViewById(R.id.register_button);
        assert buttonRegister != null;
        buttonRegister.setOnClickListener(new OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                                                  startActivity(i);
                                              }
                                          });
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private boolean attemptLogin() {
        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        RuntimeExceptionDao userDao = dbHelper.getUserRuntimeDao();
        List<User> users;

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean work = true;
        View focusView = null;

        users = userDao.queryForEq("email", mEmailView.getText().toString());
        if (users.isEmpty()) {
            work = false;
            focusView = mEmailView;
        }else{
            String hex= getHex(mPasswordView.getText().toString());
            String hex1 = users.get(0).getPassword();
            if(hex.equals(hex1)){
                userValid = users.get(0).getId();
            }else{
                work = false;
                focusView = mPasswordView;
            }
        }

        if(work){
            User us = users.get(0);
            userDao.deleteById(us.getId());
            userDao.create(new User(us.getName(),us.getEmail(),us.getPassword(),1));
            return true;
        }else {
            mPasswordView.setError(getString(R.string.error_invalid_email_or_password));
            mEmailView.setError(getString(R.string.error_invalid_email_or_password));
            return false;
        }
    }

    /**
     * metoda sluzi za kreiranje hex koda pomocu SHA2 algoritma
     * njena svrha je da bi mogla da zastiti privatnost podataka
     * */
    private String getHex(String input){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM);
        } catch (Exception e) {
            Log.e(TAG, "No such algorithm.", e);
        }
        md.update(input.toString().getBytes());

        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }


}

