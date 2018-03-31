package com.example.android.bookstore;


import android.provider.BaseColumns;

public final class BookStoreContact {

    public static abstract class BookEntry implements BaseColumns {

        public static final String TABLE_NAME = "store_books";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "ProductName";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_SUPPLIER_NAME = "SupplierName";
        public static final String COLUMN_SUPPLIER_NUMBER = "SupplierNumber";

    }


}
