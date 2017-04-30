package com.example.redfoxoptimaiii.farmaide;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserInventoryDetailActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inventory_detail);
        int id = getIntent().getIntExtra("id", 0);
        ArrayList<String> list = getIntent().getStringArrayListExtra("feed_names");
        String feed_name = list.get(id);

        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("FEED",
                    new String[] {"feed_price", "supply_amount", "crude_protein", "met_energy", "calcium", "phosphorus", "pic_ref"},
                    "farm_id = ? AND feed_name = ?",
                    new String[] {Integer.toString(User.farm_id),feed_name},
                    null,null,null);
            if(cursor.moveToFirst()) {
                TextView textView_feed_name = (TextView) findViewById(R.id.feed_name);
                textView_feed_name.setText(feed_name);
                TextView textView_price = (TextView) findViewById(R.id.price_content);
                textView_price.setText("Php "+cursor.getString(0));
                TextView textView_availSupply = (TextView) findViewById(R.id.availsupply_content);
                textView_availSupply.setText(cursor.getString(1)+"kg");
                TextView textView_cp = (TextView) findViewById(R.id.cp_content);
                textView_cp.setText(cursor.getString(2)+"%");
                TextView textView_me = (TextView) findViewById(R.id.me_content);
                textView_me.setText(cursor.getString(3)+"kcal/kg");
                TextView textView_ca = (TextView) findViewById(R.id.ca_content);
                textView_ca.setText(cursor.getString(4)+"%");
                TextView textView_p = (TextView) findViewById(R.id.p_content);
                textView_p.setText(cursor.getString(5)+"%");
                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(cursor.getInt(6));
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e){ }
    }
}
