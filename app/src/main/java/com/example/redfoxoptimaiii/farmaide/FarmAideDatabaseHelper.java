package com.example.redfoxoptimaiii.farmaide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by REDFOX™ OptimaIII on 2/23/2017.
 */

public class FarmAideDatabaseHelper extends SQLiteOpenHelper {

	Context context;
    private static final String DB_NAME = "FarmAideDB";
    private static final int DB_VERSION = 1;
	String[] userColNames = {"user_type","username","password"};
	String[] feedColNames = {"feed_type","feed_name","dry_matter","feed_price","supply_amount",
	"crude_protein","met_energy","calcium","phosphorus","pic_ref"};

    FarmAideDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    	this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE RECIPE ("
				+ "recipe_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "farm_id INTEGER,"
				+ "recipe_name TEXT, "
				+ "animal TEXT,"
				+ "animal_type TEXT, "
				+ "dm_req REAL, "
				+ "cp_req REAL, "
				+ "me_req REAL, "
				+ "ca_req REAL, "
				+ "p_req REAL, "
				+ "animal_weight REAL"
				+ ");");

        db.execSQL("CREATE TABLE FARM ("
                + "farm_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "farm_name TEXT,"
                + "password TEXT,"
                + "contact_no TEXT);");

        insertFarm(db, "Farm A (Farm)", "farm1", "09123456789");
        insertFarm(db, "Farm B (Farm)", "farm2", "09999999999");

        db.execSQL("CREATE TABLE USER ("
                + "user_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "farm_id INTEGER,"
				+ "user_type TEXT, "
				+ "username TEXT, "
                + "password TEXT);");
        insertUser(db, 1, "admin", "admin", "admin");
        insertUser(db, 1, "user", "user", "user123");
        insertUser(db, 2, "admin", "admin1", "admin");
        insertUser(db, 2, "user", "user2", "useruser");

        db.execSQL("CREATE TABLE FEED("
                + "feed_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "farm_id INTEGER,"
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
        insertFeed(db, 1, "Concentrate", "Yellow Corn", 87, 75.2, 16, 223, 7.8, 3350, .07, .25, convertDrawableToBye(R.drawable.concentrate_yellowcorn));
		insertFeed(db, 1, "Concentrate", "Rice Bran", 89, 77.7, 10, 325, 12.5, 3000, .08, 1.6, convertDrawableToBye(R.drawable.concentrate_ricebran));
        insertFeed(db, 1, "Concentrate", "Cassava Meal", 86, 84, 8, 356, 1.8, 2800, .12, .1, convertDrawableToBye(R.drawable.concentrate_cassavameal));
        insertFeed(db, 1, "Roughage", "Napier Grass", 22, 55, 0, 500.64, 63, 10.31, .3, .25, convertDrawableToBye(R.drawable.roughage_napier));
		insertFeed(db, 1, "Additive", "L-Lysine", 0, 0, 20, 200, 78.8, 0, 0, 0, convertDrawableToBye(R.drawable.additive_l_lysine));
		insertFeed(db, 1, "Additive", "Dicalcium Phosphate", 0, 0, 25, 250, 0, 0, 24.26, 19.89, convertDrawableToBye(R.drawable.additive_dicalcium_phosphate));
		insertFeed(db, 1, "Additive", "Limestone", 0, 0, 1.5, 250, 0, 0, 38, .16, convertDrawableToBye(R.drawable.additive_limestone));

		db.execSQL("CREATE TABLE TRANSACTIONS ("
				+ "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "user_id INTEGER,"
				+ "farm_id INTEGER,"
				+ "time_stamp TEXT, "
				+ "note TEXT);");

		db.execSQL("CREATE TABLE COW ("
				+ "cow_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "farm_id INTEGER,"
				+ "cow_name TEXT);");

		insertCow(db, 1, "Cow 1");
		insertCow(db, 1, "Cow 2");
		insertCow(db, 1, "Cow 3");

		db.execSQL("CREATE TABLE MILK_YIELD ("
				+ "milk_yield_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "cow_id INTEGER,"
				+ "farm_id INTEGER,"
				+ "milk_yield REAL,"
				+ "days INTEGER,"
				+ "fats_yield REAL,"
				+ "protein_yield REAL,"
				+ "total_solids_yield REAL,"
				+ "time_stamp TEXT);");

		insertMilkYield(db, 1, 1, 10.2, 35, 0.407, 0.35394, 0.99144, "Aug-24-2016");
		insertMilkYield(db, 1, 1, 12.4, 27, 0.3275, 0.33728, 1.22016, "Sep-20-2016");
		insertMilkYield(db, 1, 1, 9.4, 33, 0.4343, 0.33276, 1.26806, "Oct-23-2016");
		insertMilkYield(db, 1, 1, 7.4, 28, 0.205, 0.27232, 0.9287, "Nov-20-2016");
		insertMilkYield(db, 1, 1, 8.5, 21, 0.2397, 0.3094, 1.06505, "Dec-11-2016");
		insertMilkYield(db, 1, 1, 9.9, 43, 0.4574, 0.35244, 1.39095, "Jan-23-2017");
		insertMilkYield(db, 1, 1, 9.8, 30, 0.5331, 0.3724, 1.51802, "Feb-22-2017");
		insertMilkYield(db, 1, 1, 9.6, 25, 0.4378, 0.33312, 1.31904, "Mar-19-2017");

		insertMilkYield(db, 2, 1, 7.8, 31, 0.3221, 0.28314, 0.79092, "Aug-24-2016");
		insertMilkYield(db, 2, 1, 9.3, 27, 0.2641, 0.2325, 1.07973, "Sep-20-2016");
		insertMilkYield(db, 2, 1, 7.9, 33, 0.2781, 0.2844, 1.0981, "Oct-23-2016");
		insertMilkYield(db, 2, 1, 6.9, 28, 0.2415, 0.20424, 0.8832, "Nov-20-2016");
		insertMilkYield(db, 2, 1, 5.9, 21, 0.2584, 0.21358, 0.82423, "Dec-11-2016");
		insertMilkYield(db, 2, 1, 7.0, 43, 0.364, 0.2681, 1.0745, "Jan-23-2017");
		insertMilkYield(db, 2, 1, 6.9, 30, 0.4368, 0.25944, 1.12194, "Feb-22-2017");
		insertMilkYield(db, 2, 1, 8.3, 25, 0.3984, 0.28718, 1.15868, "Mar-19-2017");

		insertMilkYield(db, 3, 1, 5.1, 29, 0.1831, 0.17544, 0.47379, "Aug-24-2016");
		insertMilkYield(db, 3, 1, 9.8, 32, 0.4665, 0.34006, 1.3622, "Sep-20-2016");
		insertMilkYield(db, 3, 1, 9.7, 28, 0.4811, 0.33756, 1.37158, "Oct-23-2016");
		insertMilkYield(db, 3, 1, 6.6, 28, 0.2482, 0.2145, 0.9141, "Nov-20-2016");
		insertMilkYield(db, 3, 1, 7.7, 21, 0.288, 0.2233, 0.98714, "Dec-11-2016");
		insertMilkYield(db, 3, 1, 8.1, 43, 0.3629, 0.28998, 1.13157, "Jan-23-2017");
		insertMilkYield(db, 3, 1, 7.7, 30, 0.385, 0.27412, 1.11188, "Feb-22-2017");
		insertMilkYield(db, 3, 1, 8.8, 25, 0.4022, 0.3124, 1.23024, "Mar-19-2017");
	}
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion == 1){

        }
        if(oldVersion < 3){

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public byte[] convertDrawableToBye(int picref){
		Drawable drawable = context.getResources().getDrawable(picref);
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		byte[] buffer = out.toByteArray();
		return buffer;
	}

    public void insertFarm(SQLiteDatabase db, String farm_name, String password, String contact_no){
        ContentValues farmValues = new ContentValues();

        farmValues.put("farm_name", farm_name);
        farmValues.put("password", password);
        farmValues.put("contact_no", contact_no);
        db.insert("FARM", null, farmValues);

		Cursor cursor = db.query("FARM",
				new String[] {"farm_id"},
				"farm_name = ?",
				new String[] {farm_name},
				null, null, null);

		if (cursor.moveToFirst()) insertRecipes(db, cursor.getInt(0));
		cursor.close();

	}

    public void insertUser(SQLiteDatabase db, int farm_id, String user_type, String username, String password){
        ContentValues userValues = new ContentValues();

        userValues.put("farm_id", farm_id);
		userValues.put("user_type", user_type);
		userValues.put("username", username);
        userValues.put("password", password);
        db.insert("USER", null, userValues);
    }
	public void insertFeed(SQLiteDatabase db, int farm_id, String feed_type, String feed_name, double dry_matter, double total_digestible_nutrient, double feed_price, double supply_amount, double crude_protein, double met_energy, double calcium, double phosphorus, byte[] pic_ref){
        ContentValues feedValues = new ContentValues();

        feedValues.put("farm_id", farm_id);
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

    public void insertRecipe(SQLiteDatabase db, int farm_id, String recipe_name, String animal, String animal_type, double dm_req, double cp_req, double me_req, double ca_req, double p_req, double animal_weight){
        ContentValues recipeValues = new ContentValues();

        recipeValues.put("farm_id", farm_id);
        recipeValues.put("recipe_name", recipe_name);
        recipeValues.put("animal", animal);
        recipeValues.put("animal_type", animal_type);
        recipeValues.put("dm_req", dm_req);
        recipeValues.put("cp_req", cp_req);
        recipeValues.put("me_req", me_req);
        recipeValues.put("ca_req", ca_req);
        recipeValues.put("p_req", p_req);
		recipeValues.put("animal_weight", animal_weight);
        db.insert("RECIPE", null, recipeValues);
    }

    public void insertTransaction(SQLiteDatabase db, int user_id, int farm_id, String time_stamp, String note){
		ContentValues transactionValues = new ContentValues();

		transactionValues.put("user_id", user_id);
		transactionValues.put("farm_id", farm_id);
		transactionValues.put("time_stamp", time_stamp);
		transactionValues.put("note", note);
		db.insert("TRANSACTIONS", null, transactionValues);
	}

	public void insertCow(SQLiteDatabase db, int farm_id, String cow_name){
		ContentValues cowValues = new ContentValues();

		cowValues.put("farm_id", farm_id);
		cowValues.put("cow_name", cow_name);
		db.insert("COW", null, cowValues);
	}

	public void insertMilkYield(SQLiteDatabase db, int cow_id, int farm_id, double milk_yield, int days, double fats_yield, double protein_yield, double total_solids_yield, String time_stamp){
		ContentValues yieldValues = new ContentValues();

		yieldValues.put("cow_id", cow_id);
		yieldValues.put("farm_id", farm_id);
		yieldValues.put("milk_yield", milk_yield);
		yieldValues.put("days", days);
		yieldValues.put("fats_yield", fats_yield);
		yieldValues.put("protein_yield", protein_yield);
		yieldValues.put("total_solids_yield", total_solids_yield);
		yieldValues.put("time_stamp", time_stamp);
		db.insert("MILK_YIELD", null, yieldValues);
	}

	//update  user
	public void updateUser(SQLiteDatabase db, Integer id, String username, String password){
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
	public void updateFeed(SQLiteDatabase db, Integer id, String feed_type, String feed_name, double dry_matter,
						   double total_digestible_nutrient, double feed_price, double supply_amount, double crude_protein,
						   double met_energy, double calcium, double phosphorus, byte[] pic_ref){
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
		db.update("FEED", feedValues, "feed_id = ?", new String[] {Integer.toString(id)});
	}

	public void updateFeedSupply(SQLiteDatabase db, Integer id, Double supply_amount){
		ContentValues amount = new ContentValues();
		amount.put("supply_amount", supply_amount);
		db.update("FEED", amount, "feed_id = ?", new String[] {Integer.toString(id)});
	}

	//update recipe
	public void updateRecipe(SQLiteDatabase db, Integer id, String recipe_name, String animal, String animal_type, double dm_req, double cp_req, double me_req, double ca_req, double p_req, double animal_weight){
		ContentValues recipeValues = new ContentValues();

		recipeValues.put("recipe_name", recipe_name);
		recipeValues.put("animal", animal);
		recipeValues.put("animal_type", animal_type);
		recipeValues.put("dm_req", dm_req);
		recipeValues.put("cp_req", cp_req);
		recipeValues.put("me_req", me_req);
		recipeValues.put("ca_req", ca_req);
		recipeValues.put("p_req", p_req);
		recipeValues.put("animal_weight", animal_weight);

		db.update("RECIPE", recipeValues, "recipe_id = ?", new String[] {Integer.toString(id)} );
	}

	public void insertRecipes(SQLiteDatabase db, Integer farm_id){
		//Medium
		insertRecipe(db, farm_id, "Starter", "Swine", "Meat Type", 0, 17.2, 3150, .85, .52, 31);
		insertRecipe(db, farm_id, "Grower", "Swine", "Meat Type", 0, 15.8, 3000, .75, .5, 50);
		insertRecipe(db, farm_id, "Finisher", "Swine", "Meat Type", 0, 13.6, 3000, .75, .45, 72.5);
		insertRecipe(db, farm_id, "Gestating or Pregnant", "Swine", "Meat Type", 0, 14, 2850, .9, .5, 170);
		insertRecipe(db, farm_id, "Lactating", "Swine", "Meat Type", 0, 16, 3100, 1, .5, 170);
		insertRecipe(db, farm_id, "Booster", "Poultry", "Broiler", 0, 22.3, 2900, .87, .46, 2);
		insertRecipe(db, farm_id, "Starter", "Poultry", "Broiler", 0, 20, 2800, .84, .42, 2);
		insertRecipe(db, farm_id, "Finisher", "Poultry", "Broiler", 0, 18.7, 2800, .78, .39, 2);
		insertRecipe(db, farm_id, "Breeder", "Poultry", "Broiler", 0, 16, 2750, .9, .45, 2);         //Grower
//        insertRecipe(db, "Booster", "Poultry", "Egg-type");
		insertRecipe(db, farm_id, "Starter", "Poultry", "Egg-type", 0, 19.6, 2800, .98, .48, 2);
		insertRecipe(db, farm_id, "Grower", "Poultry", "Egg-type", 0, 16, 2750, 1, .46, 2);
		insertRecipe(db, farm_id, "Developer", "Poultry", "Egg-type", 0, 14.3, 2700, .95, .44, 2);
		insertRecipe(db, farm_id, "Calves", "Ruminants", "Dairy Type", 0, 18, 3110, .6, .4, 150);		//Calf starter
//        insertRecipe(db, 1, "Growers", "Ruminants", "Beef Type", 69, 16, 2600, .52, .31);
//        insertRecipe(db, "Pregnant", "Ruminants", "Beef Type");
//        insertRecipe(db, "Lactating", "Ruminants", "Beef Type");
//        insertRecipe(db, "Bulls", "Ruminants", "Beef Type");
//        insertRecipe(db, "Calves", "Ruminants", "Dairy Type");
//        insertRecipe(db, "Growers", "Ruminants", "Dairy Type");
//        insertRecipe(db, 1, "Pregnant", "Ruminants", "Dairy Type", );
		insertRecipe(db, farm_id, "Lactating", "Ruminants", "Dairy Type", 0, 18, 2800, 1.5, .8, 300);
//        insertRecipe(db, "Bulls", "Ruminants", "Dairy Type");
	}

	public void deleteRecipe(SQLiteDatabase db, Integer recipe_id){
		// db.delete(String table, String whereClause, String[] whereArgs);
		db.delete("RECIPE", "recipe_id=?", new String[]{Integer.toString(recipe_id)});
	}
}
