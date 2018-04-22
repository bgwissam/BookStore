package com.example.android.bookstore;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;

public final class BookStoreContact implements BaseColumns {

    //set fixed Uri content authority
    public static final String CONTENT_AUTHORITY = "com.example.android.bookstore";
    public static final Uri BASE_CONTENT_AUTHORITY = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_BOOKS = "store_book";
    public static final String PATH_BOOKS_ID = "store_book/#";

    public static abstract class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "store_book";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "product_name";
        public static final String COLUMN_PRODUCT_PRICE = "product_price";
        public static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";
        public static final String COLUMN_SUPPLIER_NAME = "supplier_name";
        public static final String COLUMN_SUPPLIER_NUMBER = "supplier_number";


        //declaration of MIME types constants
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_BOOKS_ID;

        //Content authority will call the books table
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_AUTHORITY, PATH_BOOKS);


    }


}
