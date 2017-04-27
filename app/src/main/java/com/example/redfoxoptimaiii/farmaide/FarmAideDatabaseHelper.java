package com.example.redfoxoptimaiii.farmaide;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 2/23/2017.
 */

public class FarmAideDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FarmAideDB";
    private static final int DB_VERSION = 1;
	String[] userColNames = {"user_type","username","password"};
	String[] feedColNames = {"feed_type","feed_name","dry_matter","feed_price","supply_amount",
	"crude_protein","met_energy","calcium","phosphorus","pic_ref"};
	
    FarmAideDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USER ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "user_type TEXT, "
				+ "username TEXT, "
                + "password TEXT);");
        insertUser(db, "admin", "admin", "admin");
        insertUser(db, "user", "user", "user123");
        insertUser(db, "user", "user2", "useruser");

        db.execSQL("CREATE TABLE FEED("
                + "feed_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "feed_type TEXT, "
                + "feed_name TEXT, "
                + "dry_matter TEXT,"
				+ "total_digestible_nutrient,"
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
		//Medium
        insertRecipe(db, "Starter", "Swine", "Meat Type", 0, 17.2, 3150, .85, .52);
        insertRecipe(db, "Grower", "Swine", "Meat Type", 0, 15.8, 3000, .75, .5);
        insertRecipe(db, "Finisher", "Swine", "Meat Type", 0, 13.6, 3000, .75, .45);
        insertRecipe(db, "Gestating or Pregnant", "Swine", "Meat Type", 0, 14, 2850, .9, .5);
        insertRecipe(db, "Lactating", "Swine", "Meat Type", 0, 16, 3100, 1, .5);
        insertRecipe(db, "Booster", "Poultry", "Broiler", 0, 22.3, 2900, .87, .46);
        insertRecipe(db, "Starter", "Poultry", "Broiler", 0, 20, 2800, .84, .42);
        insertRecipe(db, "Finisher", "Poultry", "Broiler", 0, 18.7, 2800, .78, .39);
        insertRecipe(db, "Breeder", "Poultry", "Broiler", 0, 16, 2750, .9, .45);			//Grower
//        insertRecipe(db, "Booster", "Poultry", "Egg-type");
//        insertRecipe(db, "Starter", "Poultry", "Egg-type");
//        insertRecipe(db, "Finisher", "Poultry", "Egg-type");
//        insertRecipe(db, "Breeder", "Poultry", "Egg-type");
//        insertRecipe(db, "Calves", "Ruminants", "Beef Type", 80, 18, 3110, .6, .4);
//        insertRecipe(db, "Growers", "Ruminants", "Beef Type", 69, 16, 2600, .52, .31);
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

    private void insertUser(SQLiteDatabase db, String user_type, String username, String password){
        ContentValues userValues = new ContentValues();

		userValues.put("user_type", user_type);
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
		
	//update  user
	private void updateUser(SQLiteDatabase db, Integer id, String username, String password){
		ContentValues userVal = new ContentValues();
		ArrayList<String> updateUserVal = new ArrayList<>();
		
		updateUserVal.add(username);
		updateUserVal.add(password);
		
		for(int i=0; i<2; i++){
			if(updateUserVal.get(i) != null){
				userVal.put(userColNames[i], updateUserVal.get(i));
			}
		}
		
		db.update("USER", userVal, "id = ?", new String[] {Integer.toString(id)} );
	}
	
	//update feed
	private void updateFeed(SQLiteDatabase db, Integer id, String feed_type, String feed_name, String dry_matter, double feed_price, double supply_amount, double crude_protein, double met_energy, double calcium, double phosphorus, int pic_ref){
		ContentValues feedVal = new ContentValues();
		ArrayList<String> updateFeedVal = new ArrayList<>();
		
		updateFeedVal.add(feed_type);
		updateFeedVal.add(feed_name);
		updateFeedVal.add(dry_matter);
		updateFeedVal.add(Double.toString(feed_price));
		updateFeedVal.add(Double.toString(supply_amount));
		updateFeedVal.add(Double.toString(crude_protein));
		updateFeedVal.add(Double.toString(met_energy));
		updateFeedVal.add(Double.toString(calcium));
		updateFeedVal.add(Double.toString(phosphorus));
		updateFeedVal.add(Integer.toString(pic_ref));
		
		for(int i=0; i<10; i++){
			if(updateFeedVal.get(i) != null){
				if(i >= 3){
					if(i == 9){
						//translate back to Integer
						feedVal.put(userColNames[i], updateFeedVal.get(i));
					}
					else{
						//translate back to Double
						feedVal.put(userColNames[i], updateFeedVal.get(i));
					}
				}
				else{
					feedVal.put(userColNames[i], updateFeedVal.get(i));
				}
			}
		}
		
		db.update("FEED", feedVal, "id = ?", new String[] {Integer.toString(id)} );
	}
	
	//update recipe
	private void updateRecipe(SQLiteDatabase db, Integer id, String recipe_name, String animal, String animal_type, double dm_req, double cp_req, double me_req, double ca_req, double p_req){
		ContentValues recipeVal = new ContentValues();
		ArrayList<String> updateRecipeVal = new ArrayList<>();
		
		updateRecipeVal.add(recipe_name);
		updateRecipeVal.add(animal);
		updateRecipeVal.add(animal_type);
		updateRecipeVal.add(Double.toString(dm_req));
		updateRecipeVal.add(Double.toString(cp_req));
		updateRecipeVal.add(Double.toString(me_req));
		updateRecipeVal.add(Double.toString(ca_req));
		updateRecipeVal.add(Double.toString(p_req));
		
		for(int i=0; i<8; i++){
			if(updateRecipeVal.get(i) != null){
				if(i >= 3){
					//translate back to Double
					recipeVal.put(userColNames[i], updateRecipeVal.get(i));
				}
				else{
					recipeVal.put(userColNames[i], updateRecipeVal.get(i));
				}
			}
		}
		
		db.update("RECIPE", recipeVal, "id = ?", new String[] {Integer.toString(id)} );
	}
}
