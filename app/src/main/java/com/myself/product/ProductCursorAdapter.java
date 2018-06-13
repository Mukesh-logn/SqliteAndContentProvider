package com.myself.product;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindtree.sqlite_product_catalog.R;
import com.myself.product.data.ProductContract;

/**
 * Created by M1030452 on 6/7/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {
    private static int[] headphones = {
            R.drawable.img_0,
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_0,
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_0,
            R.drawable.img_2,
            R.drawable.img_3
    };

    public ProductCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_grid_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView productIcon = (ImageView) view.findViewById(R.id.product_image);
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView productPrice = (TextView) view.findViewById(R.id.product_price);
        TextView productDesc= (TextView) view.findViewById(R.id.product_desc);
        int id = cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_IMAGE_ID);
        int nameId = cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceId = cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int productDescId = cursor.getColumnIndexOrThrow(ProductContract.ProductEntry.COLUMN_PRODUCT_DESCRIPTION);

        productIcon.setImageResource(headphones[cursor.getInt(id)%10]);
        productName.setText(cursor.getString(nameId));
        productPrice.setText(String.valueOf(cursor.getInt(priceId)));
        productDesc.setText(cursor.getString(productDescId));
    }
}
