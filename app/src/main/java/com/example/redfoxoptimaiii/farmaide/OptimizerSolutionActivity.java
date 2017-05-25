package com.example.redfoxoptimaiii.farmaide;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class OptimizerSolutionActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private ArrayList<String> ingredients;
    private float[] solution;
    private float animal_weight;
    private String animal;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimizer_solution);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String[] nutri = new String[]{"Crude Protein", "Metabolizable Energy", "Calcium", "Phosphorus"};
        TextView textView_animal = (TextView) findViewById(R.id.textView_animal);
        TextView textView_type = (TextView) findViewById(R.id.textView_type);
        TextView textView_subtype = (TextView) findViewById(R.id.textView_subType);
        textView_animal.setText(getIntent().getStringExtra("animal"));
        textView_type.setText(getIntent().getStringExtra("animal_type"));
        textView_subtype.setText(getIntent().getStringExtra("recipe_name"));
        animal = getIntent().getStringExtra("animal");
        solution = getIntent().getFloatArrayExtra("solution");
        float[] costs = getIntent().getFloatArrayExtra("costs");
        ingredients = getIntent().getStringArrayListExtra("ingredients");
        float[] nutri_req = getIntent().getFloatArrayExtra("nutri_req");
        animal_weight = getIntent().getFloatExtra("animal_weight",0);

        //for checking of nutri contents
        float[] values = new float[nutri_req.length];
        float[] amounts = new float[ingredients.size()];
        float[] supplies = new float[ingredients.size()];
        float total_cost_per_head = 0;
        boolean check = true;
        String lacks = "";

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        for (int i=0;i<ingredients.size();i+=1){
            try{
                FarmAideDatabaseHelper FarmAideDBHelper = new FarmAideDatabaseHelper(OptimizerSolutionActivity.this);
                db = FarmAideDBHelper.getReadableDatabase();
                cursor = db.query("FEED",
                        new String[]{"crude_protein","met_energy","calcium","phosphorus","supply_amount"},
                        "farm_id=? AND feed_name=?",
                        new String[]{Integer.toString(Admin.farm_id),ingredients.get(i)},
                        null,null,null);
                if (cursor.moveToFirst()){
                    for (int j=0;j<cursor.getColumnCount()-1;j+=1) {
                        values[j] += solution[j] * cursor.getFloat(j);
                    }
                    supplies[i] = cursor.getFloat(4);
                }
                cursor.close();
                db.close();
            } catch(SQLiteException e){}

            if (animal.equals("Ruminants")) amounts[i] = solution[i] * (animal_weight * .03f);
            else amounts[i] = solution[i] * 50f;
            total_cost_per_head+=amounts[i]*costs[i];
            TableRow row = new TableRow(this);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            row.setWeightSum(1);
            row.setPadding(5,5,5,5);
            TextView concentrate = newTextView(ingredients.get(i), .5f);
            String value = String.format("%.2f", amounts[i]);
            TextView amount = newTextView(value, .25f);
            value = String.format("%.2f", (costs[i]*amounts[i]));
            TextView cost = newTextView(value, .25f);
            row.addView(concentrate);
            row.addView(amount);
            row.addView(cost);
            tableLayout.addView(row);
        }
        if (animal.equals("Ruminants")) {
            String value = String.format("%.2f", total_cost_per_head);
            ((TextView) findViewById(R.id.textView_total_cost_per_head)).setText("Php " + value);
            value = String.format("%.0f", (supplies[getMinIndex(supplies)] / amounts[getMinIndex(supplies)]));
            ((TextView) findViewById(R.id.textView_total_animals)).setText(value + " heads");
            value = String.format("%.2f", (total_cost_per_head * (supplies[getMinIndex(supplies)] / amounts[getMinIndex(supplies)])));
            ((TextView) findViewById(R.id.textView_total_cost)).setText("Php " + value);
            findViewById(R.id.gridLayout).setVisibility(View.GONE);
        }
        else{
            String value = String.format("%.2f", total_cost_per_head);
            ((TextView) findViewById(R.id.textView_total_cost_per_formulate)).setText("Php " + value);
            value = String.format("%.0f", (supplies[getMinIndex(supplies)] / amounts[getMinIndex(supplies)]));
            ((TextView) findViewById(R.id.textView_total_capacity)).setText(value + " heads");
            value = String.format("%.2f", (total_cost_per_head * (supplies[getMinIndex(supplies)] / amounts[getMinIndex(supplies)])));
            ((TextView) findViewById(R.id.textView_total_cost_capacity)).setText("Php " + value);
            findViewById(R.id.gridLayout_ruminants).setVisibility(View.GONE);
        }

        for (int i=0;i<nutri_req.length;i+=1){
            if (values[i]<nutri_req[i]) {
                if (!check) lacks += ", ";
                lacks+=nutri[i];
                check = false;
            }
        }
        if (!check)
            Snackbar.make(findViewById(R.id.btn_use), "Formulated feed lacks by a considerable amount in: "+lacks, Snackbar.LENGTH_LONG).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public TextView newTextView(String text, float weight){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, weight));
        textView.setText(text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    public void useOutput(View view){
        final String[] items = new String[]{"Continue","Cancel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, items);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All computed amounts will be deducted to inventory. Proceed?");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    try {
                        FarmAideDatabaseHelper FarmAideDBHelper = new FarmAideDatabaseHelper(OptimizerSolutionActivity.this);
                        db = FarmAideDBHelper.getReadableDatabase();
                        for (int i=0;i<ingredients.size();i+=1){
                            cursor = db.query("FEED",
                                    new String[]{"feed_id, supply_amount"},
                                    "farm_id=? AND feed_name=?",
                                    new String[]{Integer.toString(Admin.farm_id), ingredients.get(i)},
                                    null, null,null);
                            if (cursor.moveToFirst()){
                                double supp = cursor.getDouble(1);
                                String value = String.format("%.2f", supp);
                                supp = Double.parseDouble(value);
                                FarmAideDBHelper.updateFeedSupply(db, cursor.getInt(0), (supp-solution[i]));
                                cursor = db.query("USER",
                                        new String[]{"user_id"},
                                        "farm_id=? AND username=?",
                                        new String[]{Integer.toString(Admin.farm_id), Admin.username},
                                        null, null, null);
                                if (cursor.moveToFirst()) {
                                    String time_stamp = new Date().toString();
                                    FarmAideDBHelper.insertTransaction(db, cursor.getInt(0), Admin.farm_id, time_stamp, "Consumed " + value + "kg supply from "+ingredients.get(i));
                                }
                            }
                        }
                        Toast.makeText(OptimizerSolutionActivity.this, "Updated Inventory", Toast.LENGTH_LONG).show();
                        cursor.close();
                        db.close();
                        Intent intent = new Intent(OptimizerSolutionActivity.this, Admin.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("username", Admin.username);
                        intent.putExtra("farm_id", Admin.farm_id);
                        startActivity(intent);
                        finish();
                    } catch (SQLiteException e) {}
                }
            }
        });
        final AlertDialog dialog = builder.show();
        dialog.show();
    }

    public int getMinIndex(float[] supplies){
        int index=0;
        float min=supplies[0];
        for (int i=0;i<supplies.length;i+=1) {
            if (supplies[i] < min){
                min = supplies[i];
                index = i;
            }
        }
        return index;
    }
}
