package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private String user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_type = getIntent().getStringExtra("user_type");
    }

    public void login(View view){
        Intent intent = user_type.equals("admin") ? new Intent(this, Admin.class) : new Intent(this, User.class);
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);
        if(onSuccessLogin(username.getText().toString(), password.getText().toString(),user_type)){
            intent.putExtra("username", username.getText().toString());
            startActivity(intent);
        }
    }

    public boolean onSuccessLogin(String username, String password, String user_type){
        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("USER",
                    new String[] {"user_type", "username", "password"},
                    "username = ?",
                    new String[] {username},
                    null, null, null);

            if(cursor.moveToFirst()) {
                boolean success = true;
                if (!user_type.equalsIgnoreCase(cursor.getString(0))) {
                    Toast.makeText(this, "Invalid user type", Toast.LENGTH_SHORT).show();
                    success = false;
                }
                if (!username.equalsIgnoreCase(cursor.getString(1))) {
                    Toast.makeText(this, "Invalid username", Toast.LENGTH_SHORT).show();
                    success = false;
                }
                if (!password.equals(cursor.getString(2))) {
                    Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
                    success = false;
                }
                cursor.close();
                db.close();
                if(success){
                    Toast.makeText(this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else return false;
            }
            else{
                Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e){
            System.out.println(e);
        }
        cursor.close();
        db.close();
        return false;
    }

}
