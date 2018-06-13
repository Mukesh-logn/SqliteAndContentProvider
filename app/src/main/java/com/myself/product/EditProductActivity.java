package com.myself.product;

import android.app.ActivityManager;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mindtree.sqlite_product_catalog.R;
import com.myself.product.data.ProductContract.ProductEntry;

public class EditProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private EditText productName;
    private EditText productPrice;
    private EditText productDesc;
    private Uri contentProductIdURL;
    private boolean openActivityInEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        productName = (EditText) findViewById(R.id.et_product_name);
        productPrice = (EditText) findViewById(R.id.et_product_price);
        productDesc = (EditText) findViewById(R.id.et_product_desc);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            openActivityInEditMode = true;
            contentProductIdURL = Uri.parse(bundle.getString("CONTENT_URL_PRODUCT_ID"));
            Log.i("URL", bundle.getString("CONTENT_URL_PRODUCT_ID"));
            this.getSupportActionBar().setTitle("Edit Product");
            this.getLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!openActivityInEditMode) {
            MenuItem menuItem = menu.findItem(R.id.delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.save:
                if (openActivityInEditMode) {
                    updateProductDetails();
                } else {
                    addProduct();
                }
                finish();
                break;
            case R.id.delete:
                int i = getContentResolver().delete(contentProductIdURL, null, null);
                Log.i("deleted url", String.valueOf(i));
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    private ContentValues getContentValue() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, productName.getText().toString().trim());
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, productPrice.getText().toString().trim());
        contentValues.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, productDesc.getText().toString().trim());
        contentValues.put(ProductEntry.COLUMN_PRODUCT_IMAGE_ID, "200");
        return contentValues;
    }

    private void addProduct() {
        ContentValues contentValues = getContentValue();
        Uri uri = getContentResolver().insert(ProductEntry.CONTENT_URL, contentValues);
        Log.i("insert uri", String.valueOf(uri));
    }

    private void updateProductDetails() {
        ContentValues contentValues = getContentValue();
        int i = getContentResolver().update(contentProductIdURL, contentValues, null, null);
        Log.i("updated uri", String.valueOf(i));
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String projection[] = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_PRICE,
                ProductEntry.COLUMN_PRODUCT_IMAGE_ID,
                ProductEntry.COLUMN_PRODUCT_DESCRIPTION
        };
        return new CursorLoader(
                this,
                contentProductIdURL,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            productName.setText(data.getString(data.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_NAME)));
            productPrice.setText(data.getString(data.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_PRICE)));
            productDesc.setText(data.getString(data.getColumnIndexOrThrow(ProductEntry.COLUMN_PRODUCT_DESCRIPTION)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
