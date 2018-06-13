package com.myself.product.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.myself.product.data.ProductContract.ProductEntry;

/**
 * Created by M1030452 on 6/7/2017.
 */

public class ProductDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "product.db";
    private static final int VERSION_NUMBER = 12;

    public ProductDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_PRODUCTS = "CREATE TABLE " + ProductEntry.TABLE_NAME + "" + "("
                + ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProductEntry.COLUMN_PRODUCT_NAME + " TEXT NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_DESCRIPTION + " TEXT, "
                + ProductEntry.COLUMN_PRODUCT_PRICE + " INTEGER NOT NULL, "
                + ProductEntry.COLUMN_PRODUCT_IMAGE_ID + " INTEGER"+
                ");";
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME);
        onCreate(db);
    }
}
