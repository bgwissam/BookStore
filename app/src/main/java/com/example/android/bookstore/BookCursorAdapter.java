package com.example.android.bookstore;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bookstore.BookStoreContact.BookEntry;

public class BookCursorAdapter extends CursorAdapter {

    //create default constructor
    public BookCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.list_view, viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        //find which fields should be populated
        TextView bookName = (TextView) view.findViewById(R.id.column_book_name);
        TextView bookPrice = (TextView) view.findViewById(R.id.column_book_price);
        final TextView bookQuantity = (TextView) view.findViewById(R.id.column_book_quantity);
        TextView supplierName = (TextView) view.findViewById(R.id.column_book_supName);
        TextView supplierNumber = (TextView) view.findViewById(R.id.column_book_supNum);

        //find the columns of pets we're interested in
        final String mBookName = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME));
        String mBookPrice = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_PRICE));
        final String mBookQuantity = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_QUANTITY));
        String mSupplierName = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME));
        final String mSupplierNumber = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NUMBER));

        Button quantityButton = view.findViewById(R.id.sale_button_view);
        //get current item id
        int currentId = cursor.getInt(cursor.getColumnIndex(BookEntry._ID));
        //Create the content Uri for the current Id
        final Uri contentUri = Uri.withAppendedPath(BookEntry.CONTENT_URI, Integer.toString(currentId));
        //create sales button to sell the desired book
        quantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mBookQuantity);
                if (quantity > 0) {
                    quantity--;

                    bookQuantity.setText(String.valueOf(quantity));

                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, quantity);
                    context.getContentResolver().update(contentUri, values, null, null);

                } else {
                    Toast.makeText(view.getContext(), "No stock available at the moment!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //populate fields
        bookName.setText(mBookName);
        bookPrice.setText(mBookPrice);
        bookQuantity.setText(mBookQuantity);
        supplierName.setText(mSupplierName);
        supplierNumber.setText(mSupplierNumber);

    }
}
