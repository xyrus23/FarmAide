package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditRecipeActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;
    private int recipe_id;
    String recipe_name, animal_name, animal_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recipe);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recipe_name = getIntent().getStringExtra("recipe_name");
        animal_name = getIntent().getStringExtra("animal");
        animal_type = getIntent().getStringExtra("animal_type");

        final Spinner spinner_animal = (Spinner) findViewById(R.id.spinner_animal);
        final Spinner spinner_animal_type = (Spinner) findViewById(R.id.spinner_animal_type);
        spinner_animal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> adapter;
                switch (spinner_animal.getSelectedItemPosition()){
                    case 0: adapter = ArrayAdapter.createFromResource(EditRecipeActivity.this, R.array.poultry_types, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_animal_type.setAdapter(adapter);
                        break;
                    case 1: adapter = ArrayAdapter.createFromResource(EditRecipeActivity.this, R.array.swine_types, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_animal_type.setAdapter(adapter);
                        break;
                    case 2: adapter = ArrayAdapter.createFromResource(EditRecipeActivity.this, R.array.ruminants_type, android.R.layout.simple_spinner_item);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_animal_type.setAdapter(adapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(EditRecipeActivity.this, R.array.poultry_types, android.R.layout.simple_spinner_dropdown_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_animal_type.setAdapter(adapter);
            }
        });

        EditText editText_recipe_name = (EditText)findViewById(R.id.recipe_name);
        EditText editText_dm = (EditText)findViewById(R.id.recipe_dm);
        EditText editText_cp = (EditText)findViewById(R.id.recipe_cp);
        EditText editText_me = (EditText)findViewById(R.id.recipe_me);
        EditText editText_ca = (EditText)findViewById(R.id.recipe_ca);
        EditText editText_p = (EditText)findViewById(R.id.recipe_p);
        EditText editText_aw = (EditText)findViewById(R.id.recipe_aw);

        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("RECIPE",
                    new String[]{"recipe_id","dm_req","cp_req","me_req","ca_req","p_req","animal_weight"},
                    "farm_id=? AND recipe_name=? AND animal=? AND animal_type=?",
                    new String[]{Integer.toString(Admin.farm_id),recipe_name,animal_name,animal_type},
                    null,null,null);
            if (cursor.moveToFirst()){
                recipe_id = cursor.getInt(0);
                editText_recipe_name.setText(recipe_name);
                editText_dm.setText(cursor.getString(1));
                editText_cp.setText(cursor.getString(2));
                editText_me.setText(cursor.getString(3));
                editText_ca.setText(cursor.getString(4));
                editText_p.setText(cursor.getString(5));
                editText_aw.setText(cursor.getString(6));
            }

        } catch (SQLiteException e){}
    }
    public void editRecipe(View view){
        TextInputLayout input_recipe_name = (TextInputLayout) findViewById(R.id.input_recipe_name);
        String recipe_name = ((EditText)findViewById(R.id.recipe_name)).getText().toString();
        String animal = ((Spinner) findViewById(R.id.spinner_animal)).getSelectedItem().toString();
        String animal_type = ((Spinner) findViewById(R.id.spinner_animal_type)).getSelectedItem().toString();
        String dm = ((EditText)findViewById(R.id.recipe_dm)).getText().toString();
        String cp = ((EditText)findViewById(R.id.recipe_cp)).getText().toString();
        String me = ((EditText)findViewById(R.id.recipe_me)).getText().toString();
        String ca = ((EditText)findViewById(R.id.recipe_ca)).getText().toString();
        String p = ((EditText)findViewById(R.id.recipe_p)).getText().toString();
        String aw = ((EditText)findViewById(R.id.recipe_aw)).getText().toString();
        input_recipe_name.setErrorEnabled(false);
        boolean check = true;
        if (Admin.farm_id<0 || recipe_name.isEmpty() || animal.isEmpty() || animal_type.isEmpty() || dm.isEmpty() || cp.isEmpty() ||
                me.isEmpty() || ca.isEmpty() || p.isEmpty() || aw.isEmpty()) check = false;
        if (!check) Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
        else{
            Double dm_req = Double.parseDouble(dm);
            Double cp_req = Double.parseDouble(cp);
            Double me_req = Double.parseDouble(me);
            Double ca_req = Double.parseDouble(ca);
            Double p_req = Double.parseDouble(p);
            Double animal_weight = Double.parseDouble(aw);
            try {
                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getReadableDatabase();

                cursor = db.query("RECIPE",
                        new String[] {"recipe_id"},
                        "farm_id=? AND recipe_name=? AND animal=? AND animal_type=?",
                        new String[]{Integer.toString(Admin.farm_id),recipe_name,animal,animal_type},
                        null, null, null);

                if(cursor.moveToFirst()){
                    if(recipe_id!=cursor.getInt(0)) {
                        input_recipe_name.setError("Recipe name already exists.");
                        check = false;
                    }
                }

                if(check){
                    ((FarmAideDatabaseHelper)FarmAideDBHelper).updateRecipe(db, recipe_id, recipe_name, animal, animal_type, dm_req, cp_req,me_req, ca_req, p_req, animal_weight);
                    Intent intent = new Intent(this, Admin.class);
                    Toast.makeText(this, "Successfully edited recipe", Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("username", Admin.username);
                    intent.putExtra("farm_id", Admin.farm_id);
                    intent.putExtra("position", 2);
                    startActivity(intent);
                    finish();
                }
                cursor.close();
                db.close();
            } catch (SQLiteException e) { }

        }
    }
}
