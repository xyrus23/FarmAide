package com.example.redfoxoptimaiii.farmaide;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class Signup extends AppCompatActivity {
    private String farm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void onSignup(View view) {
        String farm_name = ((EditText) findViewById(R.id.farm_name)).getText().toString();
        String contact_no = ((EditText) findViewById(R.id.contact_no)).getText().toString();
        String farm_password = ((EditText) findViewById(R.id.farm_password)).getText().toString();
        String farm_confirm_password = ((EditText) findViewById(R.id.farm_confirm_password)).getText().toString();
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String confirm_password = ((EditText) findViewById(R.id.confirm_password)).getText().toString();
        if (checkSignup(farm_name, contact_no, farm_password, farm_confirm_password, username, password, confirm_password)) {
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean checkSignup(String farm_name, String contact_no, String farm_password, String farm_confirm_password, final String username, final String password, String confirm_password) {
        final boolean[] check = {true};
        final TextInputLayout inputFarmName = (TextInputLayout) findViewById(R.id.input_farm_name);
        TextInputLayout inputContactNo = (TextInputLayout) findViewById(R.id.input_contact_no);
        TextInputLayout inputFarmPass = (TextInputLayout) findViewById(R.id.input_farm_password);
        TextInputLayout inputFarmConf = (TextInputLayout) findViewById(R.id.input_farm_confirm_password);
        TextInputLayout inputUser = (TextInputLayout) findViewById(R.id.input_username);
        TextInputLayout inputPass = (TextInputLayout) findViewById(R.id.input_password);
        TextInputLayout inputConf = (TextInputLayout) findViewById(R.id.input_confirm_password);
        inputFarmName.setErrorEnabled(false);
        inputContactNo.setErrorEnabled(false);
        inputFarmPass.setErrorEnabled(false);
        inputFarmConf.setErrorEnabled(false);
        inputUser.setErrorEnabled(false);
        inputPass.setErrorEnabled(false);
        inputConf.setErrorEnabled(false);
        if (farm_name.isEmpty()) {
            inputFarmName.setError("This field is required");
            check[0] = false;
        }
        if (contact_no.isEmpty()) {
            inputContactNo.setError("This field is required");
            check[0] = false;
        }
        if (farm_password.isEmpty()) {
            inputFarmPass.setError("This field is required");
            check[0] = false;
        }
        if (farm_confirm_password.isEmpty()) {
            inputFarmConf.setError("This field is required");
            check[0] = false;
        }
        if (username.isEmpty()) {
            inputUser.setError("This field is required");
            check[0] = false;
        }
        if (password.isEmpty()) {
            inputPass.setError("This field is required");
            check[0] = false;
        }
        if (confirm_password.isEmpty()) {
            inputConf.setError("This field is required");
            check[0] = false;
        }
        if (check[0]) {
                RestClient.get("farms/", new RequestParams("farm_name", farm_name), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        if(response.length()>0) {
                            inputFarmName.setError("Farm name already exist");
                            check[0] = false;
                        }
                    }
                });
//            try {
//                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
//                db = FarmAideDBHelper.getReadableDatabase();
//                cursor = db.query("FARM",
//                        new String[]{"farm_name"},
//                        "farm_name = ?",
//                        new String[]{farm_name},
//                        null, null, null);
//                if (cursor.moveToFirst()) {
//                    inputFarmName.setError("Farm name already exist");
//                    check = false;
//                }
                if (!farm_password.equals(farm_confirm_password)) {
                    inputFarmConf.setError("Password does not match");
                    check[0] = false;
                }
                if (!password.equals(confirm_password)) {
                    inputConf.setError("Password does not match");
                    check[0] = false;
                }

                if (check[0]) {
                    if (((RadioButton) findViewById(R.id.radioButton_farm)).isChecked()) farm_name += " (Farm)";
                    else farm_name += " (Supplier)";

                    HashMap<String, String> map = new HashMap<>();
                    map.put("farm_name", farm_name);
                    map.put("password", farm_password);
                    map.put("contact_no", contact_no);
                    RequestParams params = new RequestParams(map);

                    RestClient.post("farms", params, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            super.onSuccess(statusCode, headers, response);
                        }
                    });
                    Toast.makeText(this, farm_name, Toast.LENGTH_LONG).show();
                    RestClient.get("farm/"+farm_name, null, new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            try {
                                if (response.length() > 0) {
                                    farm_id = ((JSONObject) response.get(0)).getString("farm_id");
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("farm_id", farm_id);
                                    map.put("user_type", "admin");
                                    map.put("username", username);
                                    map.put("password", password);
                                    RequestParams params = new RequestParams(map);
                                    RestClient.post("signup", params, new JsonHttpResponseHandler(){
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                            super.onSuccess(statusCode, headers, response);
                                        }
                                    });
                                }
                            } catch (JSONException e) {}
                        }
                    });
//                    ((FarmAideDatabaseHelper) FarmAideDBHelper).insertFarm(db, farm_name, farm_password, contact_no);
//                    cursor = db.query("FARM",
//                            new String[]{"farm_id"},
//                            "farm_name = ?",
//                            new String[]{farm_name},
//                            null, null, null);
//                    if (cursor.moveToFirst())
//                        ((FarmAideDatabaseHelper) FarmAideDBHelper).insertUser(db, cursor.getInt(0), "admin", username, password);
                }
//                cursor.close();
//                db.close();
//            } catch (SQLiteException e) {
//            }
        }
        return check[0];
    }
}
