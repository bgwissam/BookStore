package com.example.android.bookstore;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.bookstore.BookStoreContact.BookEntry;

public class BookStoreActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = BookStoreActivity.class.getName();
    BookCursorAdapter mCursorAdapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {


        //define the projections that will be used
        String [] projections = {
                BookEntry._ID,
                BookEntry.COLUMN_PRODUCT_NAME,
                BookEntry.COLUMN_PRODUCT_PRICE,
                BookEntry.COLUMN_PRODUCT_QUANTITY,
                BookEntry.COLUMN_SUPPLIER_NAME,
                BookEntry.COLUMN_SUPPLIER_NUMBER,
        };

        return new CursorLoader(this, BookEntry.CONTENT_URI, projections, null, null, null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //swap the new cursor in
        mCursorAdapter.swapCursor(cursor);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //set cursor to null
        mCursorAdapter.swapCursor(null);
    }
    //create an integer for the book loader
    private static final int BOOK_LOADER = 0;
    //create a variable for book cursor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store);

        //display the data in the table
        ListView displayView = (ListView) findViewById(R.id.book_list);
        View emptyView = findViewById(R.id.empty_view);
        displayView.setEmptyView(emptyView);
        //setup an adapter to create a list of items for each row
        mCursorAdapter = new BookCursorAdapter(this, null);
        displayView.setAdapter(mCursorAdapter);
        //create an onclick listener to inflate the editor view
        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id){
                //create intent to open the editor view
                Intent intent = new Intent(BookStoreActivity.this, EditorActivity.class);
                //create uri to pass its contents to the edit view
                Uri currentBook = ContentUris.withAppendedId(BookEntry.CONTENT_URI, id);
                intent.setData(currentBook);
                startActivity(intent);
            }
        });
        //start the loader
        getLoaderManager().initLoader(BOOK_LOADER, null, this);
        //setup the floating button
        FloatingActionButton fab =(FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent (BookStoreActivity.this, EditorActivity.class);
                    startActivity(intent);
                }
            });
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    private void insertDataOne() {

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
        //insert another value
        values.put(BookEntry.COLUMN_PRODUCT_NAME, "Body Language");
        values.put(BookEntry.COLUMN_PRODUCT_PRICE, "19");
        values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, "10");
        values.put(BookEntry.COLUMN_SUPPLIER_NAME, "The Main Library");
        values.put(BookEntry.COLUMN_SUPPLIER_NUMBER, "001 34343434");

        long newRowId;
        newRowId = db.insert(BookEntry.TABLE_NAME, BookEntry._ID, values);

    }

}
