package com.myself.product.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.myself.product.data.ProductContract.ProductEntry;

/**
 * Created by M1030452 on 6/7/2017.
 */

public class ProductProvider extends ContentProvider {
    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private ProductDBHelper productDBHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT, PRODUCTS);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCT + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        productDBHelper = new ProductDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteDatabase sqLiteDatabase;
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                sqLiteDatabase = productDBHelper.getReadableDatabase();
                cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, null);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                sqLiteDatabase = productDBHelper.getReadableDatabase();
                cursor = sqLiteDatabase.query(ProductEntry.TABLE_NAME, projection, selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            default:
                throw new IllegalArgumentException("Invalid content url");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                return insertProduct(uri, values);
            default:
                throw new IllegalArgumentException("Unknown content uri");
        }

    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase;
        int i;
        switch (uriMatcher.match(uri)) {
            case PRODUCTS:
                sqLiteDatabase = productDBHelper.getWritableDatabase();
                i = sqLiteDatabase.delete(ProductEntry.TABLE_NAME, null, null);
                break;
            case PRODUCT_ID:
                selection = ProductEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                sqLiteDatabase = productDBHelper.getWritableDatabase();
                i = sqLiteDatabase.delete(ProductEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid content url");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return i;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = productDBHelper.getWritableDatabase();
        selection = ProductEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        int id = sqLiteDatabase.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return id;
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        SQLiteDatabase sqLiteDatabase = productDBHelper.getWritableDatabase();
        long id = sqLiteDatabase.insert(ProductEntry.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(ProductEntry.CONTENT_URL, id);
    }
}
