package com.example.redfoxoptimaiii.farmaide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = ProgressDialog.show(this, "", "Loading", true);
        getData();

//        try {
//            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
//            db = FarmAideDBHelper.getReadableDatabase();
//            Spinner spinner = (Spinner) findViewById(R.id.spinner_farm);
//            ArrayList<String> list = new ArrayList<>();
//
//            cursor = db.query("FARM",
//                    new String[]{"farm_name"},
//                    null, null, null, null, null);
//            if (cursor.moveToFirst()) {
//                for (int i = 0; i < cursor.getCount(); i += 1) {
//                    list.add(cursor.getString(0));
//                    cursor.moveToNext();
//                }
//                spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list));
//            }
//            cursor.close();
//            db.close();
//        } catch (SQLiteException e) {
//        }
    }

    public void getData(){
        final ArrayList<String> list = new ArrayList<>();

        RestClient.get("farms", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray farm) {
                for (int i = 0; i < farm.length(); i += 1) {
                    try {
                        JSONObject object = (JSONObject) farm.get(i);
                        list.add(object.getString("farm_name"));
                    } catch (JSONException e) {}
                }
                Spinner spinner = (Spinner) findViewById(R.id.spinner_farm);
                spinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, list));
            }
            @Override
            public void onFinish() {
                dialog.dismiss();
            }
        });
    }

    public void openLoginAdmin(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_farm);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("user_type", "admin");
        intent.putExtra("farm_name", spinner.getSelectedItem().toString());
        startActivity(intent);
    }

    public void openLoginUser(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner_farm);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("user_type", "user");
        intent.putExtra("farm_name", spinner.getSelectedItem().toString());
        startActivity(intent);
    }

    public void signup(View view) {
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }
}