package com.example.mymemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SQLiteAdapter extends SQLiteOpenHelper {

    public SQLiteAdapter(Context context) {
        super(context, "Memories.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table Memorydetails(title TEXT primary key, description TEXT," +
                " location TEXT, uri TEXT, emoji TEXT, time TEXT)");
        sqLiteDatabase.execSQL("create Table Passwords(title TEXT primary key, password TEXT)");

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", "general");
        contentValues.put("password", "");

        sqLiteDatabase.insert("Passwords", null, contentValues);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists Memorydetails");
    }
    public Boolean insertGeneralPassword(String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", "general");
        contentValues.put("password", password);

        long result = sqLiteDatabase.insert("Passwords", null, contentValues);
        if (result == -1){
            sqLiteDatabase.update("Passwords", contentValues, "title= ?", new String[] {"general"});
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean insertMemoryPassword(String title, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("password", password);

        long result = sqLiteDatabase.insert("Passwords", null, contentValues);
        if (result == -1){
            sqLiteDatabase.update("Passwords", contentValues, "title= ?", new String[] {"general"});
            return false;
        }
        else {
            return true;
        }
    }


    public Boolean insertMemoryData(String title, String description, String location, String uri, String emoji, String time){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("location", location);
        contentValues.put("uri", uri);
        contentValues.put("emoji", emoji);
        contentValues.put("time", time);
        long result = sqLiteDatabase.insert("Memorydetails", null, contentValues);
        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public void updateMemoryData(String oldTitle, String title, String description, String location, String uri, String emoji, String time){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("location", location);
        contentValues.put("uri", uri);
        contentValues.put("emoji", emoji);
        contentValues.put("time", time);

        sqLiteDatabase.update("Memorydetails", contentValues, "title= ?", new String[] {oldTitle});
    }

    public Boolean deleteMemoryData(String title){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Memorydetails where title = ?", new String[]{title});
        if(cursor.getCount()>0)
        {
            long result = DB.delete("Memorydetails", "title=?", new String[]{title});
            if(result==-1){
                return  false;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

    public Cursor getData(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from Memorydetails", null);
        return cursor;
    }

    public Cursor getPassword(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from Passwords", null);
        return cursor;
    }
}
