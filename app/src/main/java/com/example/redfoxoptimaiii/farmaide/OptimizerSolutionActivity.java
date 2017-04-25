package com.example.redfoxoptimaiii.farmaide;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
            TableRow row = new TableRow(this);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            row.setWeightSum(1);
            row.setPadding(5,0,0,0);
            TextView concentrate = newTextView(concentrates.get(i), .5f);
            String value = String.format("%.2f", solution[i]);
            TextView amount = newTextView(value, .25f);
            value = String.format("%.2f", (costs[i]*solution[i]));
            TextView cost = newTextView(value, .25f);
            row.addView(concentrate);
            row.addView(amount);
            row.addView(cost);
            tableLayout.addView(row);
        }
        TextView total_cost = (TextView) findViewById(R.id.textView_total_cost);
        total_cost.setText("Php "+solution[solution.length-1]);
    }

    public TextView newTextView(String text, float weight){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, weight));
        textView.setText(text);

        return textView;
    }
}
