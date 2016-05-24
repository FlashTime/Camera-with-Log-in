package com.example.danijel.execomhackathon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danijel.execomhackathon.FragmentsUlogovan.AllMonumentsFragment;
import com.example.danijel.execomhackathon.FragmentsUlogovan.Camera2BasicFragment;
import com.example.danijel.execomhackathon.FragmentsUlogovan.MyFragment;
import com.example.danijel.execomhackathon.db.DatabaseHelper;
import com.example.danijel.execomhackathon.db.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabClickListener;

import java.util.List;

public class UlogovanActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    private Camera2BasicFragment add;
    private AllMonumentsFragment all;
    private MyFragment my;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ulogovan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBottomBar = BottomBar.attach(this, savedInstanceState);
        mBottomBar.setItemsFromMenu(R.menu.bottombar_menu, new OnMenuTabClickListener() {
            @Override
            public void onMenuTabSelected(@IdRes int menuItemId) {
                final FragmentManager fragmentManager = getSupportFragmentManager();
                switch (menuItemId) {
                    case R.id.bb_menu_search:
                        all = new AllMonumentsFragment();
                        setTitle("All Monuments");
                        getFragmentManager().beginTransaction().replace(R.id.main_ulogovan, all.newInstance()).commit();
                        break;
                    case R.id.bb_menu_cammera:
                        add = new Camera2BasicFragment();
                        getFragmentManager().beginTransaction().replace(R.id.main_ulogovan, add.newInstance()).commit();
                        setTitle("Add Monuments");
                        break;
                    case R.id.bb_menu_me:
                        my = new MyFragment();
                        //fragmentManager.beginTransaction().remove(add);
                        getFragmentManager().beginTransaction().replace(R.id.main_ulogovan, my.newInstance()).commit();
                        setTitle("My Monuments");
                        break;
                }
            }

            @Override
            public void onMenuTabReSelected(@IdRes int menuItemId) {
                Toast.makeText(getApplicationContext(), getMessage(menuItemId, true), Toast.LENGTH_SHORT).show();
            }
        });

        // Setting colors for different tabs when there's more than three of them.
        // You can set colors for tabs in three different ways as shown below.
        mBottomBar.mapColorForTab(0, ContextCompat.getColor(this, R.color.colorAccent));
        mBottomBar.mapColorForTab(1, "#FF5252");
        mBottomBar.mapColorForTab(2, "#7B1FA2");

    }

    private String getMessage(int menuItemId, boolean isReselection) {
        String message = "Content for ";

        switch (menuItemId) {
            case R.id.bb_menu_search:
                message += "search";
                break;
            case R.id.bb_menu_cammera:
                message += "camera";
                break;
            case R.id.bb_menu_me:
                message += "me";
                break;
        }

        if (isReselection) {
            message += " WAS RESELECTED! YAY!";
        }

        return message;
    }

    DatabaseHelper dbHelper;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                finish();
                // User chose the "Settings" item, show the app settings UI...
                dbHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
                RuntimeExceptionDao<User, Integer> userDao = dbHelper.getUserRuntimeDao();
                List<User> users;
                users = userDao.queryForEq("is", 1);
                User us = users.get(0);
                userDao.deleteById(us.getId());
                userDao.create(new User(us.getName(),us.getEmail(),us.getPassword(),0));

                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                intent.putExtra("keyUlogovan",true);
                startActivity(intent);

                OpenHelperManager.releaseHelper();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_actionbar_settings, menu);
        return true;
    }

}
