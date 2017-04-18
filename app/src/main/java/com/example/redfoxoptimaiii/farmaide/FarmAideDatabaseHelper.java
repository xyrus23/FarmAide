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
    private static final int DB_VERSION = 1;

    FarmAideDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USER ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "username TEXT, "
                + "password TEXT);");
        insertUser(db, "admin", "admin");
        insertUser(db, "user", "user123");
        insertUser(db, "user2", "useruser");

        db.execSQL("CREATE TABLE FEED("
                + "feed_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "feed_type TEXT, "
                + "feed_name TEXT, "
                + "dry_matter REAL,"
                + "total_digestible_nutrient REAL,"
                + "feed_price REAL, "
                + "supply_amount REAL, "
                + "crude_protein REAL,"
                + "met_energy REAL, "
                + "calcium REAL, "
                + "phosphorus REAL, "
                + "pic_ref INTEGER);");
        insertFeed(db, "Concentrate", "Yellow Corn", 87, 75.2, 16, 23, 7.8, 3350, .07, .25, R.drawable.concentrate_yellowcorn);
        insertFeed(db, "Concentrate", "Cassava Meal", 86, 84, 8, 33.56, 1.8, 2800, .12, .1, R.drawable.concentrate_cassavameal);
        insertFeed(db, "Roughage", "Napier Grass", 22, 55, 0, 500.64, 63, 10.31, .3, .25, R.drawable.roughage_napier);
        insertFeed(db, "Concentrate", "Rice Bran", 89, 77.7, 10, 25, 12.5, 3000, .08, 1.6, R.drawable.concentrate_ricebran);

        db.execSQL("CREATE TABLE RECIPE ("
                + "recipe_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "recipe_name TEXT, "
                + "animal TEXT,"
                + "animal_type TEXT, "
                + "dm_req REAL, "
                + "cp_req REAL, "
                + "me_req REAL, "
                + "ca_req REAL, "
                + "p_req REAL"
                + ");");
        insertRecipe(db, "Starter", "Swine", "Meat Type", 0, 17.2, 3150, .85, .52);
        insertRecipe(db, "Grower", "Swine", "Meat Type", 0, 15.8, 3000, .75, .5);
        insertRecipe(db, "Finisher", "Swine", "Meat Type", 0, 13.6, 3000, .75, .45);
        insertRecipe(db, "Gestating or Pregnant", "Swine", "Meat Type", 0, 14, 2850, .9, .5);
        insertRecipe(db, "Lactating", "Swine", "Meat Type", 0, 16, 3100, 1, .5);
//        insertRecipe(db, "Booster", "Poultry", "Broiler");
//        insertRecipe(db, "Starter", "Poultry", "Broiler");
//        insertRecipe(db, "Finisher", "Poultry", "Broiler");
//        insertRecipe(db, "Breeder", "Poultry", "Broiler");
//        insertRecipe(db, "Booster", "Poultry", "Egg-type");
//        insertRecipe(db, "Starter", "Poultry", "Egg-type");
//        insertRecipe(db, "Finisher", "Poultry", "Egg-type");
//        insertRecipe(db, "Breeder", "Poultry", "Egg-type");
//        insertRecipe(db, "Calves", "Ruminants", "Beef Type");
//        insertRecipe(db, "Growers", "Ruminants", "Beef Type");
//        insertRecipe(db, "Pregnant", "Ruminants", "Beef Type");
//        insertRecipe(db, "Lactating", "Ruminants", "Beef Type");
//        insertRecipe(db, "Bulls", "Ruminants", "Beef Type");
//        insertRecipe(db, "Calves", "Ruminants", "Dairy Type");
//        insertRecipe(db, "Growers", "Ruminants", "Dairy Type");
//        insertRecipe(db, "Pregnant", "Ruminants", "Dairy Type");
//        insertRecipe(db, "Lactating", "Ruminants", "Dairy Type");
//        insertRecipe(db, "Bulls", "Ruminants", "Dairy Type");

    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion == 1){

        }
        if(oldVersion < 3){

        }
    }

    private void insertUser(SQLiteDatabase db, String username, String password){
        ContentValues userValues = new ContentValues();

        userValues.put("username", username);
        userValues.put("password", password);
        db.insert("USER", null, userValues);
    }

    private void insertFeed(SQLiteDatabase db, String feed_type, String feed_name, double dry_matter, double total_digestible_nutrient, double feed_price, double supply_amount, double crude_protein, double met_energy, double calcium, double phosphorus, int pic_ref){
        ContentValues feedValues = new ContentValues();

        feedValues.put("feed_type", feed_type);
        feedValues.put("feed_name", feed_name);
        feedValues.put("dry_matter", dry_matter);
        feedValues.put("total_digestible_nutrient", total_digestible_nutrient);
        feedValues.put("feed_price", feed_price);
        feedValues.put("supply_amount", supply_amount);
        feedValues.put("crude_protein", crude_protein);
        feedValues.put("met_energy", met_energy);
        feedValues.put("calcium", calcium);
        feedValues.put("phosphorus", phosphorus);
        feedValues.put("pic_ref", pic_ref);
        db.insert("FEED", null, feedValues);
    }

    private void insertRecipe(SQLiteDatabase db, String recipe_name, String animal, String animal_type, double dm_req, double cp_req, double me_req, double ca_req, double p_req){
        ContentValues recipeValues = new ContentValues();

        recipeValues.put("recipe_name", recipe_name);
        recipeValues.put("animal", animal);
        recipeValues.put("animal_type", animal_type);
        recipeValues.put("dm_req", dm_req);
        recipeValues.put("cp_req", cp_req);
        recipeValues.put("me_req", me_req);
        recipeValues.put("ca_req", ca_req);
        recipeValues.put("p_req", p_req);
        db.insert("RECIPE", null, recipeValues);
    }
}
