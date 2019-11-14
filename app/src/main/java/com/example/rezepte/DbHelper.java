package com.example.rezepte;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.rezepte.Contract.RezeptEntry.TABLE_NAME;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME= "Rezepte.db";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME
                + " (" + Contract.RezeptEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.RezeptEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                Contract.RezeptEntry.COLUMN_ZUTATEN + " TEXT NOT NULL, " +
                Contract.RezeptEntry.COLUMN_KOMMENTAR + " TEXT" +
                ");";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
