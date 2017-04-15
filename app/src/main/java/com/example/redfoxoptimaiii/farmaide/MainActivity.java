
package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void login(View view){
        Intent intent = new Intent(this, Admin.class);
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);
        if(onSuccessLogin(username.getText().toString(), password.getText().toString())){
            intent.putExtra("username", username.getText().toString());
            startActivity(intent);
            finish();
        }
    }

    public void signup(View view){
        Intent intent = new Intent(this, Signup.class);
        startActivity(intent);
    }

    public boolean onSuccessLogin(String username, String password){
        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            TextInputLayout inputUser = (TextInputLayout) findViewById(R.id.input_username);
            TextInputLayout inputPass = (TextInputLayout) findViewById(R.id.input_password);
            inputUser.setErrorEnabled(false);
            inputPass.setErrorEnabled(false);
            cursor = db.query("USER",
                    new String[] {"password"},
                    "username = ?",
                    new String[] {username},
                    null, null, null);

            if(cursor.moveToFirst()) {
                if (!password.equals(cursor.getString(0))) {
                    inputPass.setError("Incorrect Password");
                }
                else{
                    Toast.makeText(this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                cursor.close();
                db.close();
            }
            else{
                inputUser.setError("Incorrect username");
            }
        } catch (SQLException e){
        }
        cursor.close();
        db.close();
        return false;
    }

}
