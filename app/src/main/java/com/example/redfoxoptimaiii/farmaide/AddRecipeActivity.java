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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddRecipeActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_recipe);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void addRecipe(View view){
        TextInputLayout input_recipe_name = (TextInputLayout) findViewById(R.id.input_recipe_name);
        String recipe_name = ((EditText)findViewById(R.id.recipe_name)).getText().toString();
        String animal = ((Spinner) findViewById(R.id.spinner_animal)).getSelectedItem().toString();
        String animal_type = ((Spinner) findViewById(R.id.spinner_animal_type)).getSelectedItem().toString();
        String dm = ((EditText)findViewById(R.id.recipe_dm)).getText().toString();
        String cp = ((EditText)findViewById(R.id.recipe_cp)).getText().toString();
        String me = ((EditText)findViewById(R.id.recipe_me)).getText().toString();
        String ca = ((EditText)findViewById(R.id.recipe_ca)).getText().toString();
        String p = ((EditText)findViewById(R.id.recipe_p)).getText().toString();
        input_recipe_name.setErrorEnabled(false);
        boolean check = true;
        if (Admin.farm_id<0 || recipe_name.isEmpty() || animal.isEmpty() || animal_type.isEmpty() || dm.isEmpty() || cp.isEmpty() ||
                me.isEmpty() || ca.isEmpty() || p.isEmpty()) check = false;
        if (!check) Toast.makeText(this, "Please fill out all the fields", Toast.LENGTH_SHORT).show();
        else{
            Double dm_req = Double.parseDouble(dm);
            Double cp_req = Double.parseDouble(cp);
            Double me_req = Double.parseDouble(me);
            Double ca_req = Double.parseDouble(ca);
            Double p_req = Double.parseDouble(p);
            try {
                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getReadableDatabase();

                cursor = db.query("RECIPE",
                        new String[] {"recipe_name"},
                        "farm_id=? AND recipe_name=?",
                        new String[] {Integer.toString(Admin.farm_id),recipe_name},
                        null, null, null);

                if(cursor.moveToFirst()){
                    input_recipe_name.setError("Recipe name already exists.");
                    check = false;
                }

                if(check){
                    ((FarmAideDatabaseHelper)FarmAideDBHelper).insertRecipe(db, Admin.farm_id, recipe_name, animal, animal_type, dm_req, cp_req,me_req, ca_req, p_req);
                    Intent intent = new Intent(this, Admin.class);
                    Toast.makeText(this, "Successfully added recipe", Toast.LENGTH_SHORT).show();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("username", Admin.username);
                    intent.putExtra("farm_id", Admin.farm_id);
                    startActivity(intent);
                    finish();
                }
                cursor.close();
                db.close();
            } catch (SQLiteException e) { }

        }
    }

}
