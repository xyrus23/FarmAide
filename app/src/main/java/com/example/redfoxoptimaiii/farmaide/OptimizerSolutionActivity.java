package com.example.redfoxoptimaiii.farmaide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class OptimizerSolutionActivity extends AppCompatActivity {

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

        TextView textView_animal = (TextView) findViewById(R.id.textView_animal);
        TextView textView_type = (TextView) findViewById(R.id.textView_type);
        TextView textView_subtype = (TextView) findViewById(R.id.textView_subType);
        textView_animal.setText(getIntent().getStringExtra("animal"));
        textView_type.setText(getIntent().getStringExtra("animal_type"));
        textView_subtype.setText(getIntent().getStringExtra("recipe_name"));
        float[] solution = getIntent().getFloatArrayExtra("solution");
        float[] costs = getIntent().getFloatArrayExtra("costs");
        ArrayList<String> concentrates = getIntent().getStringArrayListExtra("concentrates");

        TableLayout tableLayout = (TableLayout) findViewById(R.id.tableLayout);
        for (int i=0;i<concentrates.size();i+=1){
            TextView concentrate = new TextView(this);
            concentrate.setText(concentrates.get(i));
            TextView amount = new TextView(this);
            amount.setText(Float.toString(solution[i]));
            TextView cost = new TextView(this);
            cost.setText(Float.toString((costs[i]*solution[i])));
            TableRow row = new TableRow(this);
            row.addView(concentrate);
            row.addView(amount);
            row.addView(cost);
            tableLayout.addView(row);
        }
    }
}
