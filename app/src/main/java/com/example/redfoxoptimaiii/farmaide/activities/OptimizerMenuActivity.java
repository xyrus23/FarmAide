package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OptimizerMenuActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    HashMap<String, List<String>> listHashMap = new HashMap<>();
    HashMap<String, List<String>> checkedItems;
    float[] nutri_req;
    String animal;
    float animal_weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimizer_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        animal = getIntent().getStringExtra("animal");
        String animal_type = getIntent().getStringExtra("animal_type");
        String recipe_name = getIntent().getStringExtra("recipe_name");
        animal_weight = getIntent().getFloatExtra("animal_weight", 0);

        TextView textView_animal = (TextView) findViewById(R.id.textView_animal);
        TextView textView_type = (TextView) findViewById(R.id.textView_type);
        TextView textView_subtype = (TextView) findViewById(R.id.textView_subType);
        textView_animal.setText(animal);
        textView_type.setText(animal_type);
        textView_subtype.setText(recipe_name);
        nutri_req = getIntent().getFloatArrayExtra("nutri_req");

        List<String> headers = new ArrayList<>();
        HashMap<String, List<String>> hashSupply = new HashMap<>();
        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            if (animal.equals("Ruminants")) {
                cursor = db.query("FEED",
                        new String[]{"feed_name, supply_amount"},
                        "farm_id=? AND feed_type=?", new String[]{Integer.toString(Admin.farm_id), "Roughage"}, null, null, "feed_name ASC");
                if (cursor.moveToFirst()) {
                    headers.add("Roughage");
                    List<String> roughage = new ArrayList<>();
                    List<String> supply = new ArrayList<>();
                    for (int i = 0; i < cursor.getCount(); i += 1) {
                        if (cursor.getDouble(1) >= 0.01) {
                            roughage.add(cursor.getString(0));
                            supply.add(cursor.getString(1) + "kg");
                        }
                        cursor.moveToNext();
                    }
                    if (roughage.size() > 0) listHashMap.put(headers.get(0), roughage);
                    if (supply.size() > 0) hashSupply.put(headers.get(0), supply);
                }
            }
            cursor = db.query("FEED",
                    new String[]{"feed_name, supply_amount"},
                    "farm_id=? AND feed_type=?", new String[]{Integer.toString(Admin.farm_id), "Concentrate"}, null, null, "feed_name ASC");
            if (cursor.moveToFirst()) {
                headers.add("Concentrates");
                List<String> concentrate = new ArrayList<>();
                List<String> supply = new ArrayList<>();
                for (int i = 0; i < cursor.getCount(); i += 1) {
                    if (cursor.getDouble(1) >= 0.01) {
                        concentrate.add(cursor.getString(0));
                        supply.add(cursor.getString(1) + "kg");
                    }
                    cursor.moveToNext();
                }
                if (concentrate.size() > 0) listHashMap.put("Concentrates", concentrate);
                if (supply.size() > 0) hashSupply.put("Concentrates", supply);
            }
            cursor = db.query("FEED",
                    new String[]{"feed_name, supply_amount"},
                    "farm_id=? AND feed_type=?", new String[]{Integer.toString(Admin.farm_id), "Additive"}, null, null, "feed_name ASC");
            if (cursor.moveToFirst()) {
                headers.add("Additives");
                List<String> additive = new ArrayList<>();
                List<String> supply = new ArrayList<>();
                for (int i = 0; i < cursor.getCount(); i += 1) {
                    if (cursor.getDouble(1) >= 0.01) {
                        additive.add(cursor.getString(0));
                        supply.add(cursor.getString(1) + "kg");
                    }
                    cursor.moveToNext();
                }
                if (additive.size() > 0) listHashMap.put("Additives", additive);
                if (supply.size() > 0) hashSupply.put("Additives", supply);
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandablelist);
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, headers, listHashMap, hashSupply, null);
        listView.setAdapter(listAdapter);
        checkedItems = listAdapter.getCheckedItems();
    }

    public void setupMatrix(View view) {
//        if (checkedItems.keySet().contains("Roughage") && checkedItems.get("Roughage")!=null){
//            List<String> list = checkedItems.get("Roughage");
//            try{
//                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
//                db = FarmAideDBHelper.getReadableDatabase();
//                for (int i=0;i<list.size();i+=1) {
//                    cursor = db.query("FEED",
//                            new String[]{"crude_protein, met_energy, calcium, phosphorus"},
//                            "farm_id=? AND feed_name=?",
//                            new String[]{Integer.toString(Admin.farm_id),list.get(i)},
//                            null, null, null);
//                    if (cursor.moveToFirst()) {
//                        for (int j=0;j<nutri_req.length;j+=1){
//                            if (cursor.getFloat(j)>nutri_req[j]) nutri_req[j] = 0;
//                            else nutri_req[j] -= cursor.getFloat(j);
//                        }
//                    }
//                }
//            } catch (SQLiteException e) {}
//        }
        ArrayList<String> concentrates = (ArrayList) checkedItems.get("Concentrates");
        ArrayList<String> additives = (ArrayList) checkedItems.get("Additives");
        ArrayList<String> ingredients = (ArrayList<String>) concentrates.clone();
        if (additives != null && ingredients != null)
            for (int i = 0; i < additives.size(); i += 1) ingredients.add(additives.get(i));
        if (concentrates == null || concentrates.size() < 2) {
            Toast.makeText(this, "Please pick at least 2 concentrates", Toast.LENGTH_SHORT).show();
            return;
        }
        int row = ingredients.size() + 1, col = 10 + ingredients.size();
        float nutri_req_margin = .05f;
        float[][] tableau = new float[row][col];
        float[] costs = new float[ingredients.size()];
        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            for (int i = 0; i < row - 1; i += 1) {
                cursor = db.query("FEED",
                        new String[]{"crude_protein, met_energy, calcium, phosphorus, feed_price"},
                        "farm_id=? AND feed_name = ?", new String[]{Integer.toString(Admin.farm_id), ingredients.get(i)}, null, null, null);
                if (cursor.moveToFirst()) {
                    costs[i] = cursor.getFloat(4);
                    float[] nutris = new float[]{cursor.getFloat(0) / 100, -1 * (cursor.getFloat(0) / 100),
                            cursor.getFloat(1), -1 * (cursor.getFloat(1)),
                            cursor.getFloat(2) / 100, -1 * (cursor.getFloat(2) / 100),
                            cursor.getFloat(3) / 100, -1 * (cursor.getFloat(3) / 100)};
                    for (int j = 0; j < col; j += 1) {
                        if (j < 8) tableau[i][j] = nutris[j];
                        else if (j == 8) tableau[i][j] = -1f;
                        else if (j == 9 + i) tableau[i][j] = 1f;
                        else if (j == col - 1) tableau[i][j] = costs[i];
                    }
                }
            }
            for (int i = 0; i < col - 1; i += 1) {
                if (i < 8) {
                    if (i % 2 == 0)
                        tableau[row - 1][i] = -1 * (nutri_req[i / 2] - (nutri_req[i / 2] * nutri_req_margin));
                    else
                        tableau[row - 1][i] = nutri_req[i / 2] + (nutri_req[i / 2] * nutri_req_margin);
                } else if (i == 8) tableau[row - 1][i] = 1f;
                else tableau[row - 1][i] = -0.01f;
            }

            String matrix = "";
            for (int i = 0; i < row; i += 1) {
                for (int j = 0; j < col; j += 1) {
                    matrix += tableau[i][j];
                    matrix += "\t";
                }
                matrix += "\n";
            }
//            Toast.makeText(getApplicationContext(), matrix, Toast.LENGTH_LONG).show();

            Optimizer simplex = new Optimizer(row, col, tableau);
            ArrayList<Float> sol = new ArrayList<>();
            float[] solution = simplex.getSolution();
            for (int i = 0; i < solution.length; i += 1)
                sol.add(solution[i]);

            if (simplex.isOptimized())
                Toast.makeText(this, "Please add more ingredient", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(this, OptimizerSolutionActivity.class);
                intent.putExtra("recipe_name", getIntent().getStringExtra("recipe_name"));
                intent.putExtra("animal", getIntent().getStringExtra("animal"));
                intent.putExtra("animal_type", getIntent().getStringExtra("animal_type"));
                intent.putExtra("solution", solution);
                intent.putExtra("costs", costs);
                intent.putStringArrayListExtra("ingredients", ingredients);
                intent.putExtra("nutri_req", nutri_req);
                intent.putExtra("animal_weight", animal_weight);
                startActivity(intent);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }
    }
}
