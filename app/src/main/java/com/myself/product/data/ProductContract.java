package com.myself.product.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by M1030452 on 6/7/2017.
 */

public class ProductContract {
    public static final String CONTENT_AUTHORITY="com.mindtree.sqlite_product_catalog";
    public final static Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
    public final static String PATH_PRODUCT="products";

    private ProductContract() {

    }

    public static final class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME="products";
        public static final String _ID= BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME="name";
        public static final String COLUMN_PRODUCT_DESCRIPTION="description";
        public static final String COLUMN_PRODUCT_PRICE="price";
        public static final String COLUMN_PRODUCT_IMAGE_ID="image_id";
        public static final Uri CONTENT_URL = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_PRODUCT);
    }
}
