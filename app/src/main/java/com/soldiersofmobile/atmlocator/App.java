package com.soldiersofmobile.atmlocator;

import android.app.Application;

import com.j256.ormlite.dao.Dao;

/**
 * Created by madejs on 30.03.15.
 */
public class App extends Application {

    private Dao<Bank, String> banksDao;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    private DBHelper dbHelper;

    public Dao<Bank, String> getBanksDao() {
        return banksDao;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dbHelper = new DBHelper(this);





    }
}
