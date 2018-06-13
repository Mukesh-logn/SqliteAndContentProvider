package com.myself.product;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.mindtree.sqlite_product_catalog.R;
import com.myself.product.data.ProductContract.ProductEntry;

public class ProductCatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ProductCursorAdapter productCursorAdapter;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_product_catalog);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductCatalogActivity.this, EditProductActivity.class);
                startActivity(intent);
            }
        });
        GridView gridView = (GridView) findViewById(R.id.product_grid_view);
        productCursorAdapter = new ProductCursorAdapter(this, null, 0);
        gridView.setAdapter(productCursorAdapter);
        getLoaderManager().initLoader(0, null, this);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                String contentURL = ProductEntry.CONTENT_URL + "/" + (id);
                bundle.putString("CONTENT_URL_PRODUCT_ID", contentURL);
                Intent intent = new Intent(getApplication(), EditProductActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.insert_dummy_record:
                insetDummyData();
                break;
            case R.id.delete_All:
                getContentResolver().delete(ProductEntry.CONTENT_URL, null, null);
                break;
            default:
                break;
        }
        return true;
    }

    private void insetDummyData() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRODUCT_NAME, "headphone" + counter++);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_DESCRIPTION, "Nice Headphone" +
                " With Above Good Sounf quality...Fully satisfied with these headphones." +
                "Before buying these headphones i thought on-ear headphones aren't made for me.");
        contentValues.put(ProductEntry.COLUMN_PRODUCT_IMAGE_ID, counter);
        contentValues.put(ProductEntry.COLUMN_PRODUCT_PRICE, 200 + counter);
        Uri uri =getContentResolver().insert(ProductEntry.CONTENT_URL, contentValues);
        Log.i("insert uri", String.valueOf(uri));
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductEntry._ID,
                ProductEntry.COLUMN_PRODUCT_NAME,
                ProductEntry.COLUMN_PRODUCT_DESCRIPTION,
                ProductEntry.COLUMN_PRODUCT_IMAGE_ID,
                ProductEntry.COLUMN_PRODUCT_PRICE,
        };
        return new android.content.CursorLoader(this, ProductEntry.CONTENT_URL,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        productCursorAdapter.swapCursor(data);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productCursorAdapter.swapCursor(null);

    }
}
