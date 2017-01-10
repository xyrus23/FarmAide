package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loginAdmin(View view){
        Intent intent = new Intent(this, Admin.class);
        EditText username = (EditText)findViewById(R.id.editText_username);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

    public void loginUser(View view){
        Intent intent = new Intent(this, User.class);
        EditText username = (EditText)findViewById(R.id.editText_username);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }

}
