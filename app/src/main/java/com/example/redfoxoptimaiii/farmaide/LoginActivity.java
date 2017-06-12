package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private String user_type, farm_name;
    private int farm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user_type = getIntent().getStringExtra("user_type");
        farm_name = getIntent().getStringExtra("farm_name");
        if (user_type.equals("admin")) {
            Button btn_signup = (Button) findViewById(R.id.signup);
            btn_signup.setVisibility(View.INVISIBLE);
        } else findViewById(R.id.radiogroup).setVisibility(View.GONE);
        RestClient.get("farm/"+farm_name, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    if (response.length() > 0)
                        farm_id = ((JSONObject) response.get(0)).getInt("farm_id");
                } catch(JSONException e) {}
            }
        });
        Toast.makeText(this, Integer.toString(farm_id), Toast.LENGTH_SHORT).show();
    }

    public void login(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);
        onSuccessLogin(username.getText().toString().trim(), password.getText().toString());
    }

    public boolean onSuccessLogin(final String username, final String password) {
        final ArrayList<String> check = new ArrayList<>();
        RequestParams params = new RequestParams();
        params.put("farm_id", farm_id);
        params.put("username", username);
        RestClient.get("login/"+farm_id+"/"+username, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    TextInputLayout inputUser = (TextInputLayout) findViewById(R.id.input_username);
                    TextInputLayout inputPass = (TextInputLayout) findViewById(R.id.input_password);
                    inputUser.setErrorEnabled(false);
                    inputPass.setErrorEnabled(false);
                    if (response.length() > 0) {
                        JSONObject object = (JSONObject)response.get(0);
                        String utype = object.getString("user_type");
                        int f_id = object.getInt("farm_id");
                        String pword = object.getString("password");

                        if (!user_type.equalsIgnoreCase(utype))
                            inputUser.setError("Invalid User Type");
                        else if (farm_id != f_id) inputUser.setError("Incorrect username");
                        else if (!password.equals(pword))
                            inputPass.setError("Incorrect Password");
                        else {
                            Toast.makeText(LoginActivity.this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            if (user_type.equals("admin")) {
                                RadioButton radio_farm = (RadioButton) findViewById(R.id.radioButton_farm);
                                RadioButton radio_supplier = (RadioButton) findViewById(R.id.radioButton_supplier);
                                if (radio_farm.isChecked()) intent = new Intent(LoginActivity.this, Admin.class);
                                else if (radio_supplier.isChecked()) intent = new Intent(LoginActivity.this, Supplier.class);
                            } else if (user_type.equals("user")) intent = new Intent(LoginActivity.this, User.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            intent.putExtra("username", username.trim());
                            intent.putExtra("farm_id", farm_id);
                            startActivity(intent);
                        }
                    } else inputUser.setError("User does not exist");
                } catch (JSONException e) {}
            }
        });
        if (check.size() > 0) return true;
        else return false;
    }
//        try {
//            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
//            db = FarmAideDBHelper.getReadableDatabase();
//            TextInputLayout inputUser = (TextInputLayout) findViewById(R.id.input_username);
//            TextInputLayout inputPass = (TextInputLayout) findViewById(R.id.input_password);
//            inputUser.setErrorEnabled(false);
//            inputPass.setErrorEnabled(false);
//            cursor = db.query("USER",
//                    new String[]{"farm_id", "user_type", "password"},
//                    "farm_id = ? AND username = ?",
//                    new String[]{Integer.toString(farm_id), username},
//                    null, null, null);
//
//            if (cursor.moveToFirst()) {
//                if (!user_type.equalsIgnoreCase(cursor.getString(1)))
//                    inputUser.setError("Invalid User Type");
//                else if (farm_id != cursor.getInt(0)) inputUser.setError("Incorrect username");
//                else if (!password.equals(cursor.getString(2)))
//                    inputPass.setError("Incorrect Password");
//                else {
//                    Toast.makeText(this, "Welcome " + username + "!", Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//                cursor.close();
//                db.close();
//            } else inputUser.setError("User does not exist");
//            cursor.close();
//            db.close();
//        } catch (SQLException e) {
//        }
//        return false;
//    }

    public void signup(View view) {
        Intent intent = new Intent(this, UserSignup.class);
        intent.putExtra("farm_id", farm_id);
        startActivity(intent);
    }
}
