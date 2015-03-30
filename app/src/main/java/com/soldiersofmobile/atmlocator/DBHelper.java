package com.soldiersofmobile.atmlocator;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper{

    public static final String DATABASE_NAME = "atmlocator.db";
    public static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {
            TableUtils.createTable(connectionSource, Atm.class);
            TableUtils.createTable(connectionSource, Bank.class);

            Dao<Bank, String> banksDao = getDao(Bank.class);


            banksDao.create(new Bank("ING Bank Śląski", "500 600 700"));
            banksDao.create(new Bank("ING Bank Śląski2", "500 600 701"));
            banksDao.create(new Bank("ING Bank Śląski3", "500 600 702"));

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }
}
