package com.example.danijel.execomhackathon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.danijel.execomhackathon.db.DatabaseHelper;
import com.example.danijel.execomhackathon.db.Monuments;
import com.example.danijel.execomhackathon.db.TipMonumenta;
import com.example.danijel.execomhackathon.db.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.File;
import java.util.List;

public class MonumentActivity extends AppCompatActivity {

    //put do slike
    private static String path = "";

    private EditText type;
    private EditText name;
    private EditText description;
    private ImageView myImage;
    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //prima put do slike
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            path = extras.getString("path");
        }
        //podesavanje slike da se ucita

        path = path.substring(path.indexOf("/"),path.length());
        imgFile = new File(path);
        Log.d("juhu",path);
        Log.d("jasu",imgFile.getAbsolutePath());
        Bitmap myBitmap = BitmapFactory.decodeFile(path);
        myImage = (ImageView) findViewById(R.id.imgMonument);
        myImage.setImageBitmap(myBitmap);
       // myImage.setImageResource(R.drawable.arrow);



        type = (EditText) findViewById(R.id.typeMonument);
        description = (EditText) findViewById(R.id.description);
        name = (EditText) findViewById(R.id.nameMonument);

    }

    DatabaseHelper dbHelper;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if(!type.getText().toString().equals("") && !description.getText().toString().equals("") && !name.getText().toString().equals("")) {
                    dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

                    RuntimeExceptionDao<User, Integer> userDao = dbHelper.getUserRuntimeDao();
                    RuntimeExceptionDao<TipMonumenta, Integer> tipDao = dbHelper.getTipRuntimeDao();
                    RuntimeExceptionDao<Monuments, Integer> monumentsDao = dbHelper.getMonumentsRuntimeDao();

                    List<User> users;
                    List<TipMonumenta> tip;
                    List<Monuments> mon;

                    users = userDao.queryForEq("is", 1);

                    tip = tipDao.queryForEq("nazivtipa", type.getText().toString());
                    int u=0;
                    if(tip.isEmpty()){
                        tipDao.create(new TipMonumenta(type.getText().toString()));
                    }else {
                        u = tip.get(0).getId();
                    }
                    String user= users.get(0).getEmail();
                    Log.i("srenje",user);
                    monumentsDao.create(new Monuments(name.getText().toString(),path,description.getText().toString(),user,type.getText().toString()));

                    OpenHelperManager.releaseHelper();
                    Intent i = new Intent(getApplicationContext(), UlogovanActivity.class);
                    startActivity(i);
                    finish();

                }

                // treba sacuvati
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_actionbar_img, menu);
        return true;
    }

}
