package com.example.redfoxoptimaiii.farmaide;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by REDFOX™ OptimaIII on 2/23/2017.
 */

public class FarmAideDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FarmAideDB";
    private static final int DB_VERSION = 8;

    FarmAideDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USER ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_type TEXT, "
                + "username TEXT, "
                + "password TEXT);");
        insertUser(db, "admin", "admin", "admin123");
        insertUser(db, "user", "user", "user123");
        insertUser(db, "user", "user2", "useruser");

        db.execSQL("CREATE TABLE FEED("
                + "feed_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "feed_type TEXT, "
                + "feed_name TEXT, "
                + "feed_price REAL, "
                + "supply_amount REAL, "
                + "crude_protein REAL,"
                + "met_energy REAL, "
                + "calcium REAL, "
                + "phosphorus REAL, "
                + "pic_ref INTEGER);");
        insertFeed(db, "concentrate", "Yellow Corn", 16, 23, 7.8, 3350, .07, .25, R.drawable.conc_yellowcorn);
        insertFeed(db, "concentrate", "Cassava Meal", 8, 33.56, 1.8, 2800, .12, .1, R.drawable.conc_cassavameal);
        insertFeed(db, "roughage", "Napier Grass", 0, 500.64, 63, 10.31, .3, .25, R.drawable.roug_napier);

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion == 1){

        }
        if(oldVersion < 3){

        }
    }

    private void insertUser(SQLiteDatabase db, String user_type, String username, String password){
        ContentValues userValues = new ContentValues();

        userValues.put("user_type", user_type);
        userValues.put("username", username);
        userValues.put("password", password);
        db.insert("USER", null, userValues);
    }

    private void insertFeed(SQLiteDatabase db, String feed_type, String feed_name, double feed_price, double supply_amount, double crude_protein, double met_energy, double calcium, double phosphorus, int pic_ref){
        ContentValues feedValues = new ContentValues();

        feedValues.put("feed_type", feed_type);
        feedValues.put("feed_name", feed_name);
        feedValues.put("feed_price", feed_price);
        feedValues.put("supply_amount", supply_amount);
        feedValues.put("crude_protein", crude_protein);
        feedValues.put("met_energy", met_energy);
        feedValues.put("calcium", calcium);
        feedValues.put("phosphorus", phosphorus);
        feedValues.put("pic_ref", pic_ref);
        db.insert("FEED", null, feedValues);
    }
}
