package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            Spinner spinner = (Spinner) findViewById(R.id.spinner_farm);
            ArrayList<String> list = new ArrayList<>();

            cursor = db.query("FARM",
                    new String[]{"farm_name"},
                    null, null, null, null, null);
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i += 1) {
                    list.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list));
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }
    }

    public void openLoginAdmin(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_farm);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("user_type", "admin");
        intent.putExtra("farm_id", spinner.getSelectedItemPosition() + 1);
        startActivity(intent);
    }

    public void openLoginUser(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_farm);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("user_type", "user");
        intent.putExtra("farm_id", spinner.getSelectedItemPosition() + 1);
        startActivity(intent);
    }

    public void signup(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}