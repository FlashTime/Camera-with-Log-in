package com.example.danijel.execomhackathon;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danijel.execomhackathon.db.DatabaseHelper;
import com.example.danijel.execomhackathon.db.Monuments;
import com.example.danijel.execomhackathon.db.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

public class OnlyMonumentActivity extends AppCompatActivity {

    private int idMonument;

    private ImageView img;
    private TextView name;
    private TextView user;
    private TextView type;
    private TextView description;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_monument);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idMonument = extras.getInt("Idmonument");
        }

        img = (ImageView) findViewById(R.id.imagemonu);
        name = (TextView) findViewById(R.id.namemonu);
        user = (TextView) findViewById(R.id.usermonu);
        type = (TextView) findViewById(R.id.typemonu);
        description = (TextView) findViewById(R.id.descmonu);

        dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        RuntimeExceptionDao<User, Integer> userDao = dbHelper.getUserRuntimeDao();
        RuntimeExceptionDao<Monuments, Integer> monuDao = dbHelper.getMonumentsRuntimeDao();
        List<User> users;
        List<Monuments> monu;

        monu = monuDao.queryForEq("id",idMonument);
        users = userDao.queryForEq("email",monu.get(0).getUser()+"");

        name.setText("Name: "+monu.get(0).getNaziv());
        if(!users.isEmpty())
          user.setText("User: "+users.get(0).getEmail());
        description.setText("Description: "+monu.get(0).getDescription());
        type.setText("Type: "+monu.get(0).getTip());

        String path = monu.get(0).getImage();
        Bitmap original = BitmapFactory.decodeFile(path);
        img.setImageBitmap(original);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
