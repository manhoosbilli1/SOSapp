package com.example.sosapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String NUMBER = "NUMBER";
    public static final String NUMBER_TABLE = NUMBER + "_TABLE";
    public static final String ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context,"numbers.db", null , 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        final String number = NUMBER;
        String createTableStatement = "CREATE TABLE " + NUMBER_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NUMBER + " TEXT)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(String number){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NUMBER, number);

        long insert = db.insert(NUMBER_TABLE, null, cv);
        if (insert == -1) {
            return false;
        }else {
            return true;
        }
    }
}
