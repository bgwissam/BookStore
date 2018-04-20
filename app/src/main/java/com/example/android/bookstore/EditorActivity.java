package com.example.android.bookstore;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstore.BookStoreContact.BookEntry;


public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public final static String LOG_TAG = EditorActivity.class.getName();

    private static final int PET_LOADER = 0;
    private static final int BOOK_LOADER = 1;
    //edit text for book name
    private EditText mBookName;
    //edit text for book price
    private EditText mBookPrice;
    //edit text for book quantity
    private TextView mBookQuantity;
    //edit text for supplier name
    private EditText mSupplierName;
    //edit text for supplier number
    private EditText mSupplierNumber;
    //create an intent variable to populate another activity
    private Intent intent;
    //create a global Uri variable
    private Uri currentBookUri;
    //check for changes in the TextViews
    private boolean bookHasChanged = false;
    //book quantity
    int bookQuantity;
    //will check if data is saved
    public boolean dataSaved;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener(){
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent){
            bookHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //check for the listener from BookStoreActivity if it's calling to add a new item or edit one
        //change the page title according to the request performed
        intent = getIntent();
        currentBookUri = intent.getData();
        //first check the content of the uri
        if(currentBookUri == null){
            setTitle(R.string.add_new_book);
        }
        else {
            setTitle(R.string.edit_current_book);
            //initialize the loader
            getLoaderManager().initLoader(BOOK_LOADER, null, this);
        }
        final TextView quantity = findViewById(R.id.text_book_quantity);
        bookQuantity = Integer.parseInt(quantity.getText().toString());

        //set click listener to change quantity
        ImageButton addQuantity = findViewById(R.id.add_button);
        addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              bookQuantity++;
              quantity.setText(String.valueOf(bookQuantity));
            }
        });
        ImageButton minusQuantity = findViewById(R.id.minus_button);
        minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bookQuantity>0) {
                    bookQuantity--;
                    quantity.setText(String.valueOf(bookQuantity));
                }
                else {
                    return;
                }
            }
        });

        //find which fields the user will be editing
        mBookName = findViewById(R.id.edit_book_name);
        mBookPrice = findViewById(R.id.edit_book_price);
        mBookQuantity = findViewById(R.id.text_book_quantity);
        mSupplierName = findViewById(R.id.edit_supp_name_field);
        mSupplierNumber = findViewById(R.id.edit_supp_num_field);

        //set listener to know when the views were edited
        mBookName.setOnTouchListener(mTouchListener);
        mBookPrice.setOnTouchListener(mTouchListener);
        mSupplierNumber.setOnTouchListener(mTouchListener);
        mSupplierName.setOnTouchListener(mTouchListener);

    }


    private void saveBook (){
        String name = mBookName.getText().toString().trim();
        String price = mBookPrice.getText().toString().trim();

        String supName = mSupplierName.getText().toString().trim();
        String supNum = mSupplierNumber.getText().toString().trim();
        //check if the user is adding a new pet of editing a current one
        if(currentBookUri == null && TextUtils.isEmpty(name) && TextUtils.isEmpty(price)
                && TextUtils.isEmpty(supName)){
            return;
        }
        if(name.isEmpty() || price.isEmpty() || supName.isEmpty()){
            Toast.makeText(this, R.string.missing_entry_error, Toast.LENGTH_SHORT).show();
            dataSaved = false;
        }
        else {
            //start a content value instance
            ContentValues values = new ContentValues();
            values.put(BookEntry.COLUMN_PRODUCT_NAME, name);
            values.put(BookEntry.COLUMN_PRODUCT_PRICE, price);
            values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, bookQuantity);
            values.put(BookEntry.COLUMN_SUPPLIER_NAME, supName);
            values.put(BookEntry.COLUMN_SUPPLIER_NUMBER, supNum);

            //determine if this is a new book or an edited one
            if (currentBookUri == null) {
                //insert data into a new table row
                Uri newUri = getContentResolver().insert(BookEntry.CONTENT_URI, values);

                if (newUri == null) {
                    Toast.makeText(this, R.string.error_inserting_new_book, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.new_book_added, Toast.LENGTH_SHORT).show();
                    dataSaved = true;
                }
            } else {
                //update the columns of the current selected row
                int rowsAffected = getContentResolver().update(currentBookUri, values, null, null);

                if (rowsAffected == 0) {
                    Toast.makeText(this, R.string.error_updating_current_book, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.current_book_updated, Toast.LENGTH_SHORT).show();
                    dataSaved = true;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        //inflate the menu from the res/menu/menu_editor.xml
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        //user clicks on a selected item in the options menu
        switch (item.getItemId()){
            case R.id.action_save:
                saveBook();
                //check if the data was saved before closing the editor
                if(dataSaved) {
                    finish();}
                    return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                finish();
                return true;
            case R.id.home:
                //navigate back to main page
                if(!bookHasChanged){
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                else {
                    DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NavUtils.navigateUpFromSameTask(EditorActivity.this);
                        }
                    };
                    showUnsavedChnageDialog(discardButtonClickListener);
                    return true;
                }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu){
        super.onPrepareOptionsMenu(menu);
        //if this is a new pet then high the delete button
        if(currentBookUri == null){
            //Todo for delete button
        }
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        //define the projections that will be used
        String [] projections = {BookEntry._ID,
        BookEntry.COLUMN_PRODUCT_NAME,
        BookEntry.COLUMN_PRODUCT_PRICE,
        BookEntry.COLUMN_PRODUCT_QUANTITY,
        BookEntry.COLUMN_SUPPLIER_NAME,
        BookEntry.COLUMN_SUPPLIER_NUMBER};

        //this code will execute the content provided
        return new CursorLoader(this, currentBookUri, projections, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() <0){
            return;
        }
        //move cursor to first position
        if(cursor.moveToFirst()){
            //find the attributes that we require
            int name = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME);
            int price = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_PRICE);
            int quantity = cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_QUANTITY);
            int supName = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME);
            int supNum = cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NUMBER);
            //extract the value
            String bookName = cursor.getString(name);
            String bookPrice = cursor.getString(price);
            String bookQuantity = cursor.getString(quantity);
            String bookSupName = cursor.getString(supName);
            String bookSupNum = cursor.getString(supNum);
            //update the edit text views with the current values obtained
            mBookName.setText(bookName);
            mBookPrice.setText(bookPrice);
            mBookQuantity.setText(bookQuantity);
            mSupplierName.setText(bookSupName);
            mSupplierNumber.setText(bookSupNum);
        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

        //reset all values
        mBookName.setText("");
        mBookPrice.setText("");
        mBookPrice.setText("");
        mSupplierName.setText("");
        mSupplierNumber.setText("");
    }

    //show dialog to warn the user of unsaved changes
    private void showUnsavedChnageDialog(DialogInterface.OnClickListener discardButtonClickListener){
        //create an alert dialog box and set a message for the user
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_message);
        builder.setPositiveButton(R.string.discard_button, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //user has clicked the keep editing button
                if(dialogInterface!=null){
                    dialogInterface.dismiss();
                }
            }
        });

        //create dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed(){
        //in case book hasn't changed continue handing the back press
        if(!bookHasChanged){
            super.onBackPressed();
            return;
        }
        else {
            DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            };
            //show dialog that there are unsaved changes
            showUnsavedChnageDialog(discardButtonClickListener);
        }
    }

    private void showDeleteConfirmationDialog(){
        //create an alert dialog to warn the user before deleting any record
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_book_warning);
        builder.setPositiveButton(R.string.delete_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletebook();
            }
        });
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null){
                    dialogInterface.dismiss();
                }
            }
        });

        //show alert dialog upon deletion
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    //create delete method
    private void deletebook(){
        //delete current book
        if(currentBookUri != null){
            int rowsAffected = getContentResolver().delete(currentBookUri, null, null);

            if(rowsAffected == 0){
                Toast.makeText(this, R.string.error_deleting_book, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, R.string.current_book_deleted, Toast.LENGTH_SHORT).show();
            }
        }

    }

}
