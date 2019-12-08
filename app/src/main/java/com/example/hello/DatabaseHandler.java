package com.example.hello;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "newsManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "news";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_LINKRSS = "linkrss";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_datanewss_table = String.format("CREATE TABLE %s(%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_NAME, KEY_TYPE, KEY_LINKRSS);
        db.execSQL(create_datanewss_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_datanewss_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_datanewss_table);

        onCreate(db);
    }
    public void adddataNews(dataNews datanews) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, datanews.getName());
        values.put(KEY_TYPE, datanews.gettype());
        values.put(KEY_LINKRSS, datanews.getlinkrss());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public dataNews getdataNews(int datanewsId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[] { String.valueOf(datanewsId) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        dataNews datanews = new dataNews(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        return datanews;
    }
    public List<dataNews> getAlldataNewss() {
        List<dataNews>  datanewsList = new ArrayList<>();
        String query = "SELECT * from " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            dataNews datanews = new dataNews(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            datanewsList.add(datanews);
            cursor.moveToNext();
        }
        return datanewsList;
    }
    public void updatedataNews(dataNews datanews) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, datanews.getName());
        values.put(KEY_TYPE, datanews.gettype());
        values.put(KEY_LINKRSS, datanews.getlinkrss());

        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(datanews.getId()) });
        db.close();
    }
    public void deletedataNews(int datanewsId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(datanewsId) });
        db.close();
    }
    public void deletedataNews1(String datanewsLink) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_LINKRSS + " = ?", new String[] { String.valueOf(datanewsLink) });
        db.close();
    }
}