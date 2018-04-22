package com.example.android.bookstore;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.bookstore.BookStoreContact.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = BookDbHelper.class.getName();
    //create database name and version
    public static final String DATABASE_NAME = "bookstore.db";
    public static final int DATABASE_VERSION = 1;

    //create a default constructor
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create and execute the Book Store sql table
        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + "(" +
                BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BookEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, " +
                BookEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, " +
                BookEntry.COLUMN_PRODUCT_QUANTITY + " INTEGER NOT NULL, " +
                BookEntry.COLUMN_SUPPLIER_NAME + " TEXT NOT NULL, " +
                BookEntry.COLUMN_SUPPLIER_NUMBER + " INTEGER);";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
