package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void openLoginAdmin(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("user_type", "admin");
        startActivity(intent);
        finish();

    }

    public void openLoginUser(View view){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("user_type", "user");
        startActivity(intent);
        finish();
    }
}