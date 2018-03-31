package com.example.android.bookstore;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.android.bookstore.BookStoreContact.BookEntry;

public class BookStoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store);
        //display the rows of the table
       // insertData();
        //displayBooks();
    }
    private void displayBooks(){

        BookDbHelper mDbHelper = new BookDbHelper(this);

        //create and open an SQLite database
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        //place projections
        String [] projections = {BookEntry._ID, BookEntry.COLUMN_PRODUCT_NAME, BookEntry.COLUMN_PRODUCT_PRICE,
        BookEntry.COLUMN_PRODUCT_QUANTITY, BookEntry.COLUMN_SUPPLIER_NAME, BookEntry.COLUMN_SUPPLIER_NUMBER};
        //execute query call
        Cursor cursor = db.query(BookEntry.TABLE_NAME, projections, null, null, null, null, null);
        //display the data in the table
        TextView displayView = (TextView) findViewById(R.id.text_book_view);

        try {
            displayView.setText("Book Inventory Material\nNumber of rows: " + cursor.getCount() + "\n\n" );
            displayView.append(BookEntry._ID + " | "  + BookEntry.COLUMN_PRODUCT_NAME + " | " +
                    BookEntry.COLUMN_PRODUCT_PRICE + " | " +
                    BookEntry.COLUMN_PRODUCT_QUANTITY + " | " +
                    BookEntry.COLUMN_SUPPLIER_NAME + " | " +
                    BookEntry.COLUMN_SUPPLIER_NUMBER + "\n\n");

            int idColumnIndex = cursor.getColumnIndex(BookEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int supplierNumberIndex = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NUMBER);

            while (cursor.moveToNext()){
                //use the index to extract the items in the table
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentPrice = cursor.getString(priceColumnIndex);
                String currentQuantity = cursor.getString(quantityColumnIndex);
                String currentSupplier = cursor.getString(supplierNameIndex);
                String currentNumber = cursor.getString(supplierNumberIndex);

                displayView.append(currentId + " | " + currentName + " | " + currentPrice + " | " + currentQuantity + " | " +
                currentSupplier + " | " + currentNumber + "\n") ;
            }
        }
        finally {
            cursor.close();
        }

    }
    @Override
    public void onStart(){
        super.onStart();
        insertDataOne();
        insertDataTwo();
        displayBooks();
    }
    private void insertDataOne(){

        BookDbHelper mDbHelper = new BookDbHelper(this);

        //create and add data to the table
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //add demo data to the database in order to test the process
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "Persuade");
        values.put(BookEntry.COLUMN_PRODUCT_PRICE, "29");
        values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, "5");
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, "The National Distributor");
        values.put(BookEntry.COLUMN_SUPPLIER_NUMBER, "001 23232323");
        //insert a new row


        long newRowId;
        newRowId = db.insert(BookEntry.TABLE_NAME, BookEntry._ID, values);
        //Log.e("Table Name", BookEntry.TABLE_NAME);
        //display the data that is placed

    }
    private void insertDataTwo(){
        BookDbHelper mDbHelper = new BookDbHelper(this);

        //create and add data to the table
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //insert another value
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "Body Language");
        values.put(BookEntry.COLUMN_PRODUCT_PRICE, "19");
        values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, "10");
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, "The Main Library");
        values.put(BookEntry.COLUMN_SUPPLIER_NUMBER, "001 34343434");

        long newRowId;
        newRowId = db.insert(BookEntry.TABLE_NAME, BookEntry._ID, values);
    }

}
