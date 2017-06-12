package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;


public class SupplierInventoryDetailActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private String feed_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_inventory_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        TextView username = (TextView) findViewById(R.id.profile_name);
//        username.setText(Admin.username);
        int id = getIntent().getIntExtra("id", 0);
        ArrayList<String> list = getIntent().getStringArrayListExtra("feed_names");
        feed_name = list.get(id);

        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("FEED",
                    new String[]{"feed_price", "supply_amount", "dry_matter", "total_digestible_nutrient", "crude_protein", "met_energy", "calcium", "phosphorus", "pic_ref"},
                    "farm_id = ? AND feed_name = ?",
                    new String[]{Integer.toString(Supplier.farm_id), feed_name},
                    null, null, null);
            if (cursor.moveToFirst()) {
                TextView textView_feed_name = (TextView) findViewById(R.id.feed_name);
                textView_feed_name.setText(feed_name);
                TextView textView_price = (TextView) findViewById(R.id.price_content);
                textView_price.setText("Php " + cursor.getString(0));
                TextView textView_availSupply = (TextView) findViewById(R.id.availsupply_content);
                textView_availSupply.setText(cursor.getString(1) + "kg");
                TextView dm = (TextView) findViewById(R.id.dm_content);
                dm.setText(cursor.getString(2) + "%");
                TextView textView_tdn = (TextView) findViewById(R.id.tdn_content);
                textView_tdn.setText(cursor.getString(3) + "%");
                TextView textView_cp = (TextView) findViewById(R.id.cp_content);
                textView_cp.setText(cursor.getString(4) + "%");
                TextView textView_me = (TextView) findViewById(R.id.me_content);
                textView_me.setText(cursor.getString(5) + "kcal/kg");
                TextView textView_ca = (TextView) findViewById(R.id.ca_content);
                textView_ca.setText(cursor.getString(6) + "%");
                TextView textView_p = (TextView) findViewById(R.id.p_content);
                textView_p.setText(cursor.getString(7) + "%");
                ImageView photo = (ImageView) findViewById(R.id.photo);
                byte[] image = cursor.getBlob(8);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                photo.setImageBitmap(bitmap);
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }
    }

    public void onClickEditSupply(View view) {
        Button btn_edit_supply = (Button) findViewById(R.id.btn_edit_supply);
        Button btn_edit_all = (Button) findViewById(R.id.btn_edit_all);
        LinearLayout modifySupply = (LinearLayout) findViewById(R.id.modify_supply);
        btn_edit_supply.setVisibility(View.GONE);
        btn_edit_all.setVisibility(View.GONE);
        modifySupply.setVisibility(View.VISIBLE);
    }

    public void onClickEditAll(View view) {
        Intent intent = new Intent(this, EditInventoryActivity.class);
        intent.putExtra("feed_name", feed_name);
        startActivity(intent);
    }

    public void addSupply(View view) {
        String value_string = ((EditText) findViewById(R.id.editText_amount)).getText().toString();
        if (value_string.isEmpty())
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
        else {
            Double value = Double.parseDouble(value_string);
            try {
                FarmAideDatabaseHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getReadableDatabase();
                cursor = db.query("FEED",
                        new String[]{"feed_id, supply_amount"},
                        "farm_id=? AND feed_name=?",
                        new String[]{Integer.toString(Supplier.farm_id), feed_name},
                        null, null, null);
                if (cursor.moveToFirst()) {
                    FarmAideDBHelper.updateFeedSupply(db, cursor.getInt(0), (cursor.getDouble(1) + value));
                    Toast.makeText(this, "Successfully updated supply", Toast.LENGTH_SHORT).show();
                    cursor = db.query("USER",
                            new String[]{"user_id"},
                            "farm_id=? AND username=?",
                            new String[]{Integer.toString(Supplier.farm_id), Supplier.username},
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        String time_stamp = new Date().toString();
                        FarmAideDBHelper.insertTransaction(db, cursor.getInt(0), Supplier.farm_id, time_stamp, "Added " + value_string + "kg supply to " + feed_name);
                    }
                    finish();
                    startActivity(getIntent());
                }
                cursor.close();
                db.close();
            } catch (SQLiteException e) {
            }
        }
    }

    public void subSupply(View view) {
        String value_string = ((EditText) findViewById(R.id.editText_amount)).getText().toString();
        if (value_string.isEmpty())
            Toast.makeText(this, "Please enter amount", Toast.LENGTH_SHORT).show();
        else {
            Double value = Double.parseDouble(value_string);
            try {
                FarmAideDatabaseHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
                db = FarmAideDBHelper.getReadableDatabase();
                cursor = db.query("FEED",
                        new String[]{"feed_id, supply_amount"},
                        "farm_id=? AND feed_name=?",
                        new String[]{Integer.toString(Supplier.farm_id), feed_name},
                        null, null, null);
                if (cursor.moveToFirst()) {
                    if (cursor.getDouble(1) < value)
                        Toast.makeText(this, "Amount is greater than available supply", Toast.LENGTH_SHORT).show();
                    else {
                        FarmAideDBHelper.updateFeedSupply(db, cursor.getInt(0), (cursor.getDouble(1) - value));
                        Toast.makeText(this, "Successfully updated supply", Toast.LENGTH_SHORT).show();
                        cursor = db.query("USER",
                                new String[]{"user_id"},
                                "farm_id=? AND username=?",
                                new String[]{Integer.toString(Supplier.farm_id), Supplier.username},
                                null, null, null);
                        if (cursor.moveToFirst()) {
                            String time_stamp = new Date().toString();
                            FarmAideDBHelper.insertTransaction(db, cursor.getInt(0), Supplier.farm_id, time_stamp, "Consumed " + value_string + "kg supply from " + feed_name);
                        }
                        finish();
                        startActivity(getIntent());
                    }
                }
                cursor.close();
                db.close();
            } catch (SQLiteException e) {
            }
        }
    }
}
