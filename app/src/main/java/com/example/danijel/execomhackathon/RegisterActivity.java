package com.example.danijel.execomhackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.danijel.execomhackathon.db.DatabaseHelper;
import com.example.danijel.execomhackathon.db.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.security.MessageDigest;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private static final String ALGORITHM = "SHA-256";
    private static final String TAG = RegisterActivity.class.getName();

    private DatabaseHelper dbHelper;

    private EditText username;
    private EditText email;
    private EditText password1;
    private EditText password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /**
         * kod za namestanje actionBar-a
         * */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        username = (EditText) findViewById(R.id.name_register);
        email = (EditText) findViewById(R.id.email_register);
        password1 = (EditText) findViewById(R.id.password_register1);
        password2 = (EditText) findViewById(R.id.password_register2);

        Button buttonNext = (Button) findViewById(R.id.button_register);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doUserDataStuff()){
                    Intent i = new Intent(getApplicationContext(), UlogovanActivity.class);
                    startActivity(i);
                    finish();
                }else{

                }
            }
        });
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

    private boolean doUserDataStuff() {
        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        RuntimeExceptionDao<User, Integer> userDao = dbHelper.getUserRuntimeDao();
        List<User> users;

        username.setError(null);
        email.setError(null);
        password1.setError(null);
        password2.setError(null);

        boolean work = true;
        View focusView = null;

        if(username.getText().toString().equals("")){
            work = false;
            username.setError(getString(R.string.error_invalid_username));
            focusView = username;
        }

        //proverava se da li su password-i jednaki
        if (password1.getText().toString().equals(password2.getText().toString()) && isPasswordValid(password1.getText().toString())) {

        } else {
            work = false;
            focusView = password1;
            focusView = password2;
            password1.setError(getString(R.string.error_incorrect_password));
            password2.setError(getString(R.string.error_incorrect_password));
        }

        //provera da li meil vec postoji
        users = userDao.queryForEq("email", email.getText().toString());
        if (!users.isEmpty()) {
            work = false;
            focusView = email;
            email.setError(getString(R.string.error_exist_email));
        }

        //proverava da li je meil
        if(!isEmailValid(email.getText().toString())){
            work = false;
            focusView = email;
            email.setError(getString(R.string.error_invalid_email));
        }

        if(work){
            //ako meil ne postoji korisnik se cuva u bazu
            String passwordA = getHex(password1.getText().toString());
            Boolean tr = true;
            userDao.create(new User(username.getText().toString(), email.getText().toString(), passwordA, 1));
            OpenHelperManager.releaseHelper();
            return true;
        }

        OpenHelperManager.releaseHelper();
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    // This activity is NOT part of this app's task, so create a new task
                    // when navigating up, with a synthesized back stack.
                    TaskStackBuilder.create(this)
                            // Add all of this activity's parents to the back stack
                            .addNextIntentWithParentStack(upIntent)
                            // Navigate up to the closest parent
                            .startActivities();
                } else {
                    // This activity is part of this app's task, so simply
                    // navigate up to the logical parent activity.
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
