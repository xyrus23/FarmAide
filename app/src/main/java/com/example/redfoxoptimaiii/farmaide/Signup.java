package com.example.redfoxoptimaiii.farmaide;

import android.content.ContentValues;
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
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

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

    public void onSignup(View view){
        String farm_name = ((EditText) findViewById(R.id.farm_name)).getText().toString();
        String contact_no = ((EditText) findViewById(R.id.contact_no)).getText().toString();
        String farm_password = ((EditText) findViewById(R.id.farm_password)).getText().toString();
        String farm_confirm_password = ((EditText) findViewById(R.id.farm_confirm_password)).getText().toString();
        String username = ((EditText) findViewById(R.id.username)).getText().toString();
        String password =((EditText) findViewById(R.id.password)).getText().toString();
        String confirm_password = ((EditText) findViewById(R.id.confirm_password)).getText().toString();
        if(checkSignup(farm_name,contact_no,farm_password,farm_confirm_password,username,password,confirm_password)){
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public boolean checkSignup(String farm_name, String contact_no, String farm_password, String farm_confirm_password, String username, String password, String confirm_password){
        boolean check = true;
        TextInputLayout inputFarmName = (TextInputLayout) findViewById(R.id.input_farm_name);
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
        if(farm_name.isEmpty()){
            inputFarmName.setError("This field is required");
            check = false;
        }
        if(contact_no.isEmpty()){
            inputContactNo.setError("This field is required");
            check = false;
        }
        if(farm_password.isEmpty()){
            inputFarmPass.setError("This field is required");
            check = false;
        }
        if(farm_confirm_password.isEmpty()){
            inputFarmConf.setError("This field is required");
            check = false;
        }
        if(username.isEmpty()){
            inputUser.setError("This field is required");
            check = false;
        }
        if(password.isEmpty()){
            inputPass.setError("This field is required");
            check = false;
        }
        if(confirm_password.isEmpty()){
            inputConf.setError("This field is required");
            check = false;
        }
        if(check) {
            try {
                SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getReadableDatabase();
                cursor = db.query("FARM",
                        new String[] {"farm_name"},
                        "farm_name = ?",
                        new String[] {farm_name},
                        null, null, null);
                if(cursor.moveToFirst()){
                    inputFarmName.setError("Farm name already exist");
                    check = false;
                }
                if (!farm_password.equals(farm_confirm_password)){
                    inputFarmConf.setError("Password does not match");
                    check = false;
                }
                if(!password.equals(confirm_password)){
                    inputConf.setError("Password does not match");
                    check = false;
                }

                if(check){
                    ((FarmAideDatabaseHelper)FarmAideDBHelper).insertFarm(db, farm_name, farm_password, contact_no);
                    cursor = db.query("FARM",
                            new String[] {"farm_id"},
                            "farm_name = ?",
                            new String[] {farm_name},
                            null, null, null);
                    if (cursor.moveToFirst())
                        ((FarmAideDatabaseHelper)FarmAideDBHelper).insertUser(db, cursor.getInt(0), "admin", username, password);
                }
                cursor.close();
                db.close();
            } catch (SQLiteException e) { }
        }
        return check;
    }
}
