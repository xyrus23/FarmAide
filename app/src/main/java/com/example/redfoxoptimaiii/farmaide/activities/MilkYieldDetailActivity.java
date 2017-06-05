package com.example.redfoxoptimaiii.farmaide;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MilkYieldDetailActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_milk_yield_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String cow_id = getIntent().getStringExtra("cow_id");
        String time_stamp = getIntent().getStringExtra("time_stamp");
        toolbar.setTitle(time_stamp);

        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            cursor = db.query("MILK_YIELD",
                    new String[]{"milk_yield, days, fats_yield, protein_yield, total_solids_yield"},
                    "farm_id=? AND cow_id=? AND time_stamp=?",
                    new String[]{Integer.toString(Admin.farm_id), cow_id, time_stamp},
                    null, null, null);
            if (cursor.moveToFirst()) {
                String value = String.format("%.2f", cursor.getFloat(0));
                ((TextView) findViewById(R.id.my)).setText(value);
                ((TextView) findViewById(R.id.ld)).setText(cursor.getString(1));
                value = String.format("%.2f", cursor.getFloat(2));
                ((TextView) findViewById(R.id.fy)).setText(value);
                value = String.format("%.2f", cursor.getFloat(3));
                ((TextView) findViewById(R.id.py)).setText(value);
                value = String.format("%.2f", cursor.getFloat(4));
                ((TextView) findViewById(R.id.tsy)).setText(value);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }
    }
}
