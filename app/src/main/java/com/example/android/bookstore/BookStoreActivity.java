package com.example.android.bookstore;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.bookstore.BookStoreContact.BookEntry;

public class BookStoreActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = BookStoreActivity.class.getName();
    //create an integer for the book loader
    private static final int BOOK_LOADER = 0;
    BookCursorAdapter mCursorAdapter;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        //define the projections that will be used
        String[] projections = {
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
    //create a variable for book cursor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_store);

        //display the data in the table
        ListView displayView = findViewById(R.id.book_list);
        View emptyView = findViewById(R.id.empty_view);
        displayView.setEmptyView(emptyView);
        //setup an adapter to create a list of items for each row
        mCursorAdapter = new BookCursorAdapter(this, null);
        displayView.setAdapter(mCursorAdapter);
        //create an onclick listener to inflate the editor view
        displayView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookStoreActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu from the res/menu/menu_editor.xml
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //user clicks on a selected item in the options menu
        switch (item.getItemId()) {

            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        //create an alert dialog to warn the user before deleting any record
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_book_warning);
        builder.setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteAll();
            }
        });
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        //show alert dialog upon deletion
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //create delete method
    private void deleteAll() {
        //delete current book
        int rowsAffected = getContentResolver().delete(BookEntry.CONTENT_URI, null, null);

        if (rowsAffected == 0) {
            Toast.makeText(this, R.string.error_deleting_books, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.current_books_deleted, Toast.LENGTH_SHORT).show();
        }
    }
}
