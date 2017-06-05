package com.example.redfoxoptimaiii.farmaide;

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
import android.widget.TextView;
import android.widget.Toast;

public class UserSignup extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private int farm_id;
    private String cFarm_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        farm_id = getIntent().getIntExtra("farm_id", 0);
        String farm_name = "";
        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            cursor = db.query("FARM",
                    new String[]{"farm_name", "password"},
                    "farm_id = ?",
                    new String[]{Integer.toString(farm_id)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                farm_name = cursor.getString(0);
                cFarm_pass = cursor.getString(1);
            }

            TextView textView_farm_name = (TextView) findViewById(R.id.textView_farm_name);
            textView_farm_name.setText("Sign-up for " + farm_name + ":");
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }
    }

    public void onSignup(View view) {
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        String confirm_password = ((EditText) findViewById(R.id.confirm_password)).getText().toString();
        String farm_password = ((EditText) findViewById(R.id.farm_password)).getText().toString();
        if (checkSignup(username, password, confirm_password, farm_password)) {
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean checkSignup(String username, String password, String confirm_password, String farm_password) {
        boolean check = true;
        TextInputLayout inputUser = (TextInputLayout) findViewById(R.id.input_username);
        TextInputLayout inputPass = (TextInputLayout) findViewById(R.id.input_password);
        TextInputLayout inputConf = (TextInputLayout) findViewById(R.id.input_confirm_password);
        TextInputLayout inputFarmPass = (TextInputLayout) findViewById(R.id.input_farm_password);
        inputUser.setErrorEnabled(false);
        inputPass.setErrorEnabled(false);
        inputConf.setErrorEnabled(false);
        inputFarmPass.setErrorEnabled(false);

        if (username.isEmpty()) {
            inputUser.setError("This field is required");
            check = false;
        }
        if (password.isEmpty()) {
            inputPass.setError("This field is required");
            check = false;
        }
        if (confirm_password.isEmpty()) {
            inputConf.setError("This field is required");
            check = false;
        }
        if (farm_password.isEmpty()) {
            inputFarmPass.setError("This field is required");
            check = false;
        }
        if (check) {
            try {
                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getReadableDatabase();
                cursor = db.query("USER",
                        new String[]{"username"},
                        "farm_id = ? AND username = ?",
                        new String[]{Integer.toString(farm_id), username},
                        null, null, null);
                if (cursor.moveToFirst()) {
                    inputUser.setError("Username already taken");
                    check = false;
                }
                if (!password.equals(confirm_password)) {
                    inputConf.setError("Password does not match");
                    check = false;
                }
                if (!cFarm_pass.equals(farm_password)) {
                    inputFarmPass.setError("Incorrect Password");
                    check = false;
                }
                if (check)
                    ((FarmAideDatabaseHelper) FarmAideDBHelper).insertUser(db, farm_id, "user", username, password);
                cursor.close();
                db.close();
            } catch (SQLiteException e) {
            }
        }
        return check;
    }
}
