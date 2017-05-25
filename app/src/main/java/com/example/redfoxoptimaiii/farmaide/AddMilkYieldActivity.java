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
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class AddMilkYieldActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private String cow_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_milk_yield);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cow_name = getIntent().getStringExtra("cow_name");
        toolbar.setTitle(cow_name);
    }

    public void add(View view){
        String my = ((EditText)findViewById(R.id.my)).getText().toString();
        String ld = ((EditText)findViewById(R.id.ld)).getText().toString();
        String fy = ((EditText)findViewById(R.id.fy)).getText().toString();
        String py = ((EditText)findViewById(R.id.py)).getText().toString();
        String tsy = ((EditText)findViewById(R.id.tsy)).getText().toString();
        if (my.isEmpty() || ld.isEmpty() || fy.isEmpty() || py.isEmpty() || tsy.isEmpty())
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
        else{
            try{
                double milk_yield = Double.parseDouble(my);
                int lactation_days = Integer.parseInt(ld);
                double fats_yield = Double.parseDouble(fy);
                double protein_yield = Double.parseDouble(py);
                double total_solids_yield = Double.parseDouble(tsy);
                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getReadableDatabase();
                cursor = db.query("COW",
                        new String[]{"cow_id"},
                        "farm_id=? AND cow_name=?",
                        new String[]{Integer.toString(Admin.farm_id),cow_name},
                        null, null, null
                        );
                Date date = new Date();
                String time_stamp = Admin.months[date.getMonth()] + "-" + date.getDate() + "-" + (date.getYear()+1900);
                if (cursor.moveToFirst()) {
                    ((FarmAideDatabaseHelper) FarmAideDBHelper).insertMilkYield(db, cursor.getInt(0), Admin.farm_id, milk_yield, lactation_days, fats_yield, protein_yield, total_solids_yield, time_stamp);
                    Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Admin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("username", Admin.username);
                    intent.putExtra("farm_id", Admin.farm_id);
                    intent.putExtra("position", 4);
                    startActivity(intent);
                    finish();
                }
                cursor.close();
                db.close();
            } catch (SQLiteException e){}
        }
    }
}
