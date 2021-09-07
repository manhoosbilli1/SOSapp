package com.example.sosapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public List<String> getEveryone(){
        List<String> myList = new ArrayList<String>();

        String queryString = "SELECT * FROM " + NUMBER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            //if there are results and first item
            do {
                int numberID = cursor.getInt(0);
                String number = cursor.getString(1);
                myList.add(number);


            } while (cursor.moveToNext());
        } else {
            //failure. do not add anything to the list.
        }
        //close the connections
        cursor.close();
        db.close();
        return myList;

    }
}
