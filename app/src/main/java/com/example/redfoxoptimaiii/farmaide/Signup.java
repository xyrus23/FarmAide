package com.example.redfoxoptimaiii.farmaide;

import android.content.ContentValues;
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
        EditText editText_username = (EditText) findViewById(R.id.username);
        EditText editText_password = (EditText) findViewById(R.id.password);
        EditText editText_confirm_password = (EditText) findViewById(R.id.confirm_password);
        String username = editText_username.getText().toString();
        String password = editText_password.getText().toString();
        String confirm_password = editText_confirm_password.getText().toString();
        if(checkSignup(username, password, confirm_password)){
            Toast.makeText(this, "Signup successful!", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    public boolean checkSignup(String username, String password, String confirm_password){
        boolean check = true;
        TextInputLayout inputUser = (TextInputLayout) findViewById(R.id.input_username);
        TextInputLayout inputPass = (TextInputLayout) findViewById(R.id.input_password);
        TextInputLayout inputConf = (TextInputLayout) findViewById(R.id.input_confirm_password);
        inputUser.setErrorEnabled(false);
        inputPass.setErrorEnabled(false);
        inputConf.setErrorEnabled(false);
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
                cursor = db.query("USER",
                        new String[] {"username"},
                        "username = ?",
                        new String[] {username},
                        null, null, null);
                if(cursor.moveToFirst()){
                    inputUser.setError("Username already taken");
                    check = false;
                }
                if(!password.equals(confirm_password)){
                    inputConf.setError("Password does not match");
                    check = false;
                }
                if(check){
                    ContentValues userValues = new ContentValues();
                    userValues.put("username", username);
                    userValues.put("password", password);
                    db.insert("USER", null, userValues);
                }
            } catch (SQLiteException e) { }
        }
        cursor.close();
        db.close();
        return check;
    }
}
