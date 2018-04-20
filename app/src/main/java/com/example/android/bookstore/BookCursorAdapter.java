package com.example.android.bookstore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.example.android.bookstore.BookStoreContact.BookEntry;

public class BookCursorAdapter extends CursorAdapter  {

    //create default constructor
    public BookCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(R.layout.list_view, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //find which fields should be populated
        TextView bookName = (TextView) view.findViewById(R.id.column_book_name);
        TextView bookPrice = (TextView) view.findViewById(R.id.column_book_price);
        final TextView bookQuantity = (TextView) view.findViewById(R.id.column_book_quantity);
        TextView supplierName = (TextView) view.findViewById(R.id.column_book_supName);
        TextView supplierNumber = (TextView) view.findViewById(R.id.column_book_supNum);

        //find the columns of pets we're interested in
        String mBookName = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_NAME));
        String mBookPrice = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_PRICE));
        final String mBookQuantity = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_PRODUCT_QUANTITY));
        String mSupplierName = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NAME));
        String mSupplierNumber = cursor.getString(cursor.getColumnIndex(BookEntry.COLUMN_SUPPLIER_NUMBER));

        Button quantityButton = view.findViewById(R.id.sale_button_view);
        //create sales button to sell the desired book
        quantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int quantity = Integer.parseInt(mBookQuantity);
                if(quantity > 0){
                    quantity--;

                    bookQuantity.setText(String.valueOf(quantity));

                    ContentValues values = new ContentValues();
                    values.put(BookEntry.COLUMN_PRODUCT_QUANTITY, quantity);


                }else{
                    throw new IllegalArgumentException("No stock available");
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
