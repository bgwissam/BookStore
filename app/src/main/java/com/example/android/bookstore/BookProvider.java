package com.example.android.bookstore;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.android.bookstore.BookStoreContact.BookEntry;


public class BookProvider extends ContentProvider {

    private final static String LOG_TAG = BookProvider.class.getName();
    public BookDbHelper mDbHelper;

    //create a Uri matcher integer
    private static final int BOOKS = 100;
    private static final int BOOKS_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    //this will run the first time anything is called from this class
    static {
        //adding content Uri
        sUriMatcher.addURI(BookStoreContact.CONTENT_AUTHORITY, BookStoreContact.PATH_BOOKS, BOOKS);
        sUriMatcher.addURI(BookStoreContact.CONTENT_AUTHORITY, BookStoreContact.PATH_BOOKS_ID, BOOKS_ID);
    }

    //initialize the provider and the database helper
    @Override
    public boolean onCreate() {

        mDbHelper = new BookDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projections, String selections, String[] selectionArgs, String sortOrder) {
        //get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Log.e(LOG_TAG, "this is the uri " + uri);
        //initialize cursor
        Cursor cursor;
        //create swtich statment to match Uri
        int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                cursor = database.query(BookEntry.TABLE_NAME, projections, selections, selectionArgs, null, null, sortOrder);
                break;
            case BOOKS_ID:
                //extract data depending on the selected ID
                selections = BookEntry._ID + "=?";
                selectionArgs = new String [] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(BookEntry.TABLE_NAME, projections, selections, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(R.string.cannnot_query_uri + " " + uri);
        }
        //set notification for the cursor to notify the query of any changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                return BookEntry.CONTENT_LIST_TYPE;
            case BOOKS_ID:
                return BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Uknown uri " + uri + "with match " + match);
        }

    }

    //insert new data to the table
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                return insertBooks(uri, contentValues);
            default:
                throw new IllegalArgumentException (R.string.insertion_not_supported + " " + uri);
        }

    }
    //create insertion helper
    public Uri insertBooks(Uri uri, ContentValues contentValues){

    //sanity check to avoid inserting null values
    String name = contentValues.getAsString(BookEntry.COLUMN_PRODUCT_NAME);
    if(name == null){
        throw new IllegalArgumentException(R.string.book_needs_name + "");
    }
    String price = contentValues.getAsString(BookEntry.COLUMN_PRODUCT_PRICE);
    if(price == null) {
        throw new IllegalArgumentException(R.string.book_needs_price + "");
    }
    String quantity = contentValues.getAsString(BookEntry.COLUMN_PRODUCT_QUANTITY);
    if (quantity == null) {
        throw new IllegalArgumentException(R.string.book_needs_quantity + "");
    }
    String supplier = contentValues.getAsString(BookEntry.COLUMN_SUPPLIER_NAME);
    if(supplier == null) {
        throw new IllegalArgumentException(R.string.book_needs_supplier + "");
    }

    SQLiteDatabase db = mDbHelper.getWritableDatabase();

    long id = db.insert(BookEntry.TABLE_NAME, null, contentValues);
    //if id is negative than insertion didn't happen
        if(id == -1){
            Log.e(LOG_TAG, R.string.failed_to_insert_data + " " + uri);
            return null;
        }
        //notify all listeners if any insertion has happened
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }
    //delete data for the given row or rows
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //get writable data
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //check deleted rows
        int deletedRows = 0;

        final int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                deletedRows = db.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOKS_ID:
                selection = BookEntry._ID + "=?";
                selectionArgs = new String []{ String.valueOf(ContentUris.parseId(uri))};
                deletedRows = db.delete(BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
        }
        //check if rows were deleted
        if(deletedRows != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        //match uri
        int match = sUriMatcher.match(uri);
        switch (match){
            case BOOKS:
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOKS_ID:
                //get id in order to update the right item
                selection = BookEntry._ID + "=?";
                selectionArgs = new String []{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException(R.string.failed_to_update_data + " " + uri);

        }
    }
    public int updateBook (Uri uri, ContentValues contentValues, String selection, String [] selectionArgs){
        //sanity check
        if (contentValues.containsKey(BookEntry.COLUMN_PRODUCT_NAME)){
            String name = contentValues.getAsString(BookEntry.COLUMN_PRODUCT_NAME);
            if(name == null){
                throw new IllegalArgumentException(R.string.book_needs_name + "");
            }
        }
        if(contentValues.containsKey(BookEntry.COLUMN_PRODUCT_PRICE)){
            String price = contentValues.getAsString(BookEntry.COLUMN_PRODUCT_PRICE);
            if(price == null){
                throw new IllegalArgumentException(R.string.book_needs_price + "");
            }
        }
        if(contentValues.containsKey(BookEntry.COLUMN_PRODUCT_QUANTITY)){
            int quantity = contentValues.getAsInteger(BookEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity == 0){
                throw new IllegalArgumentException(R.string.book_needs_quantity + "");
            }
        }
        if(contentValues.containsKey(BookEntry.COLUMN_SUPPLIER_NAME)){
            String supName = contentValues.getAsString(BookEntry.COLUMN_SUPPLIER_NAME);
            if(supName == null){
                throw new IllegalArgumentException(R.string.book_needs_supplier + "");
            }
        }
        if (contentValues.size() == 0){
            return 0;
        }
        //get writable sql database to update edited rows
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int affectedRows = db.update(BookEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        //notify incase of changes
        if(affectedRows !=0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return affectedRows;
    }

}
