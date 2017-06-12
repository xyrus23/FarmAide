package com.example.redfoxoptimaiii.farmaide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String recipe_name = getIntent().getStringExtra("recipe_name");
        String animal = getIntent().getStringExtra("animal");
        String animal_type = getIntent().getStringExtra("animal_type");
        String dm = getIntent().getStringExtra("dm");
        String cp = getIntent().getStringExtra("cp");
        String me = getIntent().getStringExtra("me");
        String ca = getIntent().getStringExtra("ca");
        String p = getIntent().getStringExtra("p");
        String animal_weight = getIntent().getStringExtra("animal_weight");

        ((TextView) findViewById(R.id.textView_subType)).setText(recipe_name);
        ((TextView) findViewById(R.id.textView_animal)).setText(animal);
        ((TextView) findViewById(R.id.textView_type)).setText(animal_type);
        ((TextView) findViewById(R.id.dm_content)).setText(dm + "%");
        ((TextView) findViewById(R.id.cp_content)).setText(cp + "%");
        ((TextView) findViewById(R.id.me_content)).setText(me + "kcal/kg");
        ((TextView) findViewById(R.id.ca_content)).setText(ca + "%");
        ((TextView) findViewById(R.id.p_content)).setText(p + "%");
        ((TextView) findViewById(R.id.aw_content)).setText(animal_weight + "kg");
    }
}
