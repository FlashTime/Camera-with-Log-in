package com.example.danijel.execomhackathon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.danijel.execomhackathon.R;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.query.In;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Danijel on 5/22/2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "date.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<User, Integer> userDao = null;
    private RuntimeExceptionDao<User, Integer> userRuntimeDao = null;

    private Dao<TipMonumenta, Integer> tipDao = null;
    private RuntimeExceptionDao<TipMonumenta, Integer> tipRuntimeDao = null;

    private Dao<Monuments, Integer> monumentsDao = null;
    private RuntimeExceptionDao<Monuments, Integer> monumentsRuntimeDao = null;

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            /**
             * creates the User database table
             */
            TableUtils.createTable(connectionSource,User.class);
            TableUtils.createTable(connectionSource,TipMonumenta.class);
            TableUtils.createTable(connectionSource,Monuments.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */
            TableUtils.dropTable(connectionSource,User.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns an instance of the data access object
     * @return
     * @throws SQLException
     */
    public Dao<User, Integer> getDao() throws SQLException {
        if(userDao == null){
            userDao = getDao(User.class);
        }
        return userDao;
    }
    public Dao<TipMonumenta, Integer> getTipDao() throws SQLException {
        if(tipDao == null){
            tipDao = getDao(TipMonumenta.class);
        }
        return tipDao;
    }


    public Dao<Monuments, Integer> getMonumentDao() throws SQLException {
        if(monumentsDao == null){
            monumentsDao = getDao(Monuments.class);
        }
        return monumentsDao;
    }

    public RuntimeExceptionDao<User, Integer> getUserRuntimeDao(){
        if(userRuntimeDao ==  null){
            userRuntimeDao = getRuntimeExceptionDao(User.class);
        }
        return userRuntimeDao;
    }

    public RuntimeExceptionDao<TipMonumenta, Integer> getTipRuntimeDao(){
        if(tipRuntimeDao ==  null){
            tipRuntimeDao = getRuntimeExceptionDao(TipMonumenta.class);
        }
        return tipRuntimeDao;
    }

    public RuntimeExceptionDao<Monuments, Integer> getMonumentsRuntimeDao(){
        if(monumentsRuntimeDao ==  null){
            monumentsRuntimeDao = getRuntimeExceptionDao(Monuments.class);
        }
        return monumentsRuntimeDao;
    }

}
