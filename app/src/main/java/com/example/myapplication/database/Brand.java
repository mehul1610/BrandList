package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.bean.BeanBrand;

import java.util.ArrayList;


public class Brand {

    static final String CREATE_STATEMENT = "CREATE TABLE " + Keys.TABLE_NAME.name + "("
            + Keys.PRIMARY_ID.name + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + Keys.ID.name + " INTEGER,"
            + Keys.NAME.name + " TEXT,"
            + Keys.DESCRIPTION.name + " TEXT,"
            + Keys.CREATED_TIME.name + " DATETIME,"
            + Keys.STATUS.getName() + " BOOLEAN DEFAULT 1"
            + ")";

    private static Brand brand;
    private SQLiteDatabase database;

    private Brand(Context context) {
        database = DataBaseHelper.getHelper(context).getWritableDatabase();
    }

    public static Brand getBrand(Context context) {
        if (brand == null)
            return brand = new Brand(context);
        return brand;
    }

    public boolean addBrand(BeanBrand brand) {
        ContentValues values = new ContentValues();
        values.put(Keys.NAME.name, brand.getName());
        values.put(Keys.DESCRIPTION.name, brand.getDescription());
        long value = database.insert(Keys.TABLE_NAME.name, null, values);
        return value != -1;
    }

    public void addBrands(ArrayList<BeanBrand> beanBrands) {
        ArrayList<Integer> brands = getAllIds();
        for (BeanBrand brand : beanBrands) {
            if (!brands.contains(brand.getId())) {
                ContentValues values = new ContentValues();
                values.put(Keys.ID.name, brand.getId());
                values.put(Keys.NAME.name, brand.getName());
                values.put(Keys.DESCRIPTION.name, brand.getDescription());
                values.put(Keys.CREATED_TIME.name, brand.getCreatedAt());
                values.put(Keys.STATUS.name, false);
                database.insert(Keys.TABLE_NAME.name, null, values);
            }
        }
    }

    public boolean deleteRecord(int id) {
        String where = Keys.PRIMARY_ID.name + "=" + id;
        int i = database.delete(Keys.TABLE_NAME.name, where, null);
        return i != 0;
    }

    public ArrayList<BeanBrand> getAllRecords() {
        ArrayList<BeanBrand> beanBrands = new ArrayList<>();
        Cursor cursor = database.query(Keys.TABLE_NAME.name,
                new String[]{Keys.ID.name, Keys.NAME.name, Keys.DESCRIPTION.name, Keys.CREATED_TIME.name, Keys.STATUS.name},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            BeanBrand brand = new BeanBrand();
            brand.setId(cursor.getInt(0));
            brand.setName(cursor.getString(1));
            brand.setDescription(cursor.getString(2));
            brand.setCreatedAt(cursor.getString(3));
            brand.setStatus(parseBoolean(cursor.getString(4)));
            beanBrands.add(brand);
        }
        cursor.close();
        return beanBrands;
    }

    private ArrayList<Integer> getAllIds() {
        ArrayList<Integer> beanBrands = new ArrayList<>();
        Cursor cursor = database.query(Keys.TABLE_NAME.name,
                new String[]{Keys.ID.name},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            beanBrands.add(cursor.getInt(0));
        }
        cursor.close();
        return beanBrands;
    }

    public ArrayList<BeanBrand> getAllIdsStatus() {
        String whereClause = Keys.STATUS.name + "=" + 1;
        ArrayList<BeanBrand> beanBrands = new ArrayList<>();
        Cursor cursor = database.query(Keys.TABLE_NAME.name,
                new String[]{Keys.PRIMARY_ID.name, Keys.NAME.name, Keys.DESCRIPTION.name},
                whereClause, null, null, null, null);

        while (cursor.moveToNext()) {
            BeanBrand brand = new BeanBrand();
            brand.setPrimaryId(cursor.getInt(0));
            brand.setName(cursor.getString(1));
            brand.setDescription(cursor.getString(2));
            beanBrands.add(brand);
        }
        cursor.close();
        return beanBrands;
    }

    private boolean parseBoolean(String value) {
        return "0".equals(value);
    }


    public enum Keys {
        TABLE_NAME("brand"), PRIMARY_ID("primary_table_id"),
        ID("id"), NAME("name"), CREATED_TIME("time"),
        DESCRIPTION("description"), STATUS("status");

        private String name;

        Keys(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
