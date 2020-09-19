package com.heremap.heremapfragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.heremap.heremapfragment.product.Product;

import java.util.ArrayList;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLHelper";

    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;
    Cursor cursor;

    public SQLHelper(@Nullable Context context) {
        super(context, Define.DB_NAME, null, Define.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryCreateTable = "CREATE TABLE Product ( " +
                "code INTEGER," +
                "latitude REAL," +
                "longitude REAL," +
                "name Text," +
                "address Text," +
                "voucher Text )";

        //Chạy câu lệnh tạo bảng product
        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table if exists " + Define.DB_NAME_PRODUCT);
            onCreate(db);
        }
    }

    public void insertProduct(int code, Product product) {
        sqLiteDatabase = getWritableDatabase();
        contentValues = new ContentValues();

        contentValues.put(Define.KEY_CODE, code);
        contentValues.put(Define.KEY_LATITUDE, product.getLatitude());
        contentValues.put(Define.KEY_LONGITUDE, product.getLongitude());
        contentValues.put(Define.KEY_NAME, product.getName());
        contentValues.put(Define.KEY_ADDRESS, product.getAddress());
        contentValues.put(Define.KEY_VOUCHER, product.getVoucher());

        sqLiteDatabase.insert(Define.DB_NAME_PRODUCT, null, contentValues);
    }

    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        cursor = sqLiteDatabase.query(false, Define.DB_NAME_PRODUCT, null, null, null
                , null, null, null, null);
        while (cursor.moveToNext()) {
            int code = cursor.getInt(cursor.getColumnIndex(Define.KEY_CODE));
            Double latitude = cursor.getDouble(cursor.getColumnIndex(Define.KEY_LATITUDE));
            Double longitude = cursor.getDouble(cursor.getColumnIndex(Define.KEY_LONGITUDE));
            String name = cursor.getString(cursor.getColumnIndex(Define.KEY_NAME));
            String address = cursor.getString(cursor.getColumnIndex(Define.KEY_ADDRESS));
            String voucher = cursor.getString(cursor.getColumnIndex(Define.KEY_VOUCHER));
            products.add(new Product(code, longitude, latitude, name, address, voucher));
        }
        closeDB();
        return products;
    }

    public void onDeleteAll() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(Define.DB_NAME_PRODUCT, null, null);
        closeDB();
    }

    private void closeDB() {
        if (sqLiteDatabase != null) sqLiteDatabase.close();
        if (contentValues != null) contentValues.clear();
        if (cursor != null) cursor.close();
    }
}
