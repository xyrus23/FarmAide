package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.color.white;

public class OptimizerMenuActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    HashMap<String,List<String>> listHashMap = new HashMap<>();
    float[] nutri_req;

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

//        TextView username = (TextView) findViewById(R.id.profile_name);
//        username.setText(Admin.username);

        TextView textView_animal = (TextView) findViewById(R.id.textView_animal);
        TextView textView_type = (TextView) findViewById(R.id.textView_type);
        TextView textView_subtype = (TextView) findViewById(R.id.textView_subType);
        textView_animal.setText(getIntent().getStringExtra("animal"));
        textView_type.setText(getIntent().getStringExtra("animal_type"));
        textView_subtype.setText(getIntent().getStringExtra("recipe_name"));
        nutri_req = getIntent().getFloatArrayExtra("nutri_req");

        List<String> headers = new ArrayList<>();
        HashMap<String,List<String>> hashSupply = new HashMap<>();
        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            cursor = db.query("FEED",
                    new String[] {"feed_name, supply_amount"},
                    "farm_id=? AND feed_type=?",new String[] {Integer.toString(Admin.farm_id),"Roughage"},null,null,"feed_name ASC");
            if(cursor.moveToFirst()){
                headers.add("Roughage");
                List<String> roughage = new ArrayList<>();
                List<String> supply = new ArrayList<>();
                for(int i=0;i<cursor.getCount();i+=1){
                    roughage.add(cursor.getString(0));
                    supply.add(cursor.getString(1)+"kg");
                    cursor.moveToNext();
                }
                listHashMap.put(headers.get(0),roughage);
                hashSupply.put(headers.get(0),supply);
            }
            cursor = db.query("FEED",
                    new String[] {"feed_name, supply_amount"},
                    "farm_id=? AND feed_type=?",new String[] {Integer.toString(Admin.farm_id),"Concentrate"},null,null,"feed_name ASC");
            if(cursor.moveToFirst()){
                headers.add("Concentrates");
                List<String> concentrate = new ArrayList<>();
                List<String> supply = new ArrayList<>();
                for(int i=0;i<cursor.getCount();i+=1){
                    concentrate.add(cursor.getString(0));
                    supply.add(cursor.getString(1)+"kg");
                    cursor.moveToNext();
                }
                listHashMap.put(headers.get(1),concentrate);
                hashSupply.put(headers.get(1),supply);
            }
            cursor = db.query("FEED",
                    new String[] {"feed_name, supply_amount"},
                    "farm_id=? AND feed_type=?",new String[] {Integer.toString(Admin.farm_id),"Additive"},null,null,"feed_name ASC");
            if(cursor.moveToFirst()){
                headers.add("Additives");
                List<String> additive = new ArrayList<>();
                List<String> supply = new ArrayList<>();
                for(int i=0;i<cursor.getCount();i+=1){
                    additive.add(cursor.getString(0));
                    supply.add(cursor.getString(1)+"kg");
                    cursor.moveToNext();
                }
                listHashMap.put(headers.get(2),additive);
                hashSupply.put(headers.get(2),supply);
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e){ }

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandablelist);
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, headers, listHashMap, hashSupply, null);
        listView.setAdapter(listAdapter);
    }

    public void setupMatrix(View view){
//        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandablelist);
//        listView.getCheckedItemIds();
        ArrayList<String> concentrates = (ArrayList) listHashMap.get("Concentrates");
        int row = concentrates.size()+1, col = 7+(concentrates.size()*2);
        float req_amount = -50f, req_amount_margin = (-1*req_amount)+1;
        float[][] tableau = new float[row][col];
        float[] costs = new float[concentrates.size()];
        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            for(int i=0;i<row-1;i+=1){
                cursor = db.query("FEED",
                        new String[] {"feed_price, supply_amount, crude_protein, met_energy, calcium, phosphorus"},
                        "farm_id=? AND feed_name = ?",new String[]{Integer.toString(Admin.farm_id),concentrates.get(i)},null,null,null);
                if(cursor.moveToFirst()){
                    costs[i] = cursor.getFloat(0);
                    for (int j = 0; j < col; j += 1) {
                        if (j < 4) tableau[i][j] = cursor.getFloat(j + 2);
                        else if (j == 6+i) {
                            tableau[i][j] = -1f;
                            tableau[concentrates.size()][j] = cursor.getFloat(1);
                        }
                        else if (j == 6+i+concentrates.size()) tableau[i][j] = 1f;
                        else if (j == 4) {
                            tableau[i][j] = 1f;
                            tableau[concentrates.size()][j] = req_amount;
                        }
                        else if (j == 5) {
                            tableau[i][j] = -1f;
                            tableau[concentrates.size()][j] = req_amount_margin;
                        }
                        else if (j == col-1) tableau[i][j] = cursor.getFloat(0);
                    }
                }
            }
            for(int i=0;i<col;i+=1){
                if(i<4) tableau[concentrates.size()][i] = nutri_req[i];
                else if(i>=6+concentrates.size() && i<col-1) tableau[concentrates.size()][i] = -.1f;
            }
            String matrix = "";
            for (int i=0;i<row;i+=1){
                for (int j=0;j<col;j+=1){
                    matrix+=tableau[i][j];
                    matrix+="\t";
                }
                matrix+="\n";
            }
            Toast.makeText(getApplicationContext(), matrix, Toast.LENGTH_LONG).show();
            Optimizer simplex = new Optimizer(row, col, tableau);
            ArrayList<Float> sol = simplex.getSolution();
            float[] solution = new float[sol.size()];
            for (int i=0;i<sol.size();i+=1)
                solution[i] = sol.get(i);

            Intent intent = new Intent(this, OptimizerSolutionActivity.class);
            intent.putExtra("recipe_name",getIntent().getStringExtra("recipe_name"));
            intent.putExtra("animal",getIntent().getStringExtra("animal"));
            intent.putExtra("animal_type",getIntent().getStringExtra("animal_type"));
            intent.putExtra("solution", solution);
            intent.putExtra("costs", costs);
            intent.putStringArrayListExtra("concentrates", concentrates);
            startActivity(intent);

            cursor.close();
            db.close();
        } catch (SQLiteException e){ }
    }
}
