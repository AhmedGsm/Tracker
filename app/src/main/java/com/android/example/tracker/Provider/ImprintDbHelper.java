package com.android.example.tracker.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImprintDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "register.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 1;

    // Constructor
    ImprintDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold the places data
        final String SQL_CREATE_IMPRINTS_TABLE = "CREATE TABLE " + ImprintContract.ImprintEntry.TABLE_NAME + " (" +
                ImprintContract.ImprintEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ImprintContract.ImprintEntry.COLUMN_IMPRINT + " TEXT NOT NULL, " +
                ImprintContract.ImprintEntry.COLUMN_TIMESTAMP + " INTEGER NOT NULL, " +
                "UNIQUE (" + ImprintContract.ImprintEntry.COLUMN_IMPRINT + ") ON CONFLICT REPLACE" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_IMPRINTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // For now simply drop the table and create a new one.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ImprintContract.ImprintEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
