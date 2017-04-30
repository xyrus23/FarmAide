package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class AdminInventoryDetailActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_inventory_detail);

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
        String feed_name = getIntent().getStringExtra("feed_name");

        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("FEED",
                    new String[] {"feed_price", "supply_amount", "dry_matter", "total_digestible_nutrient", "crude_protein", "met_energy", "calcium", "phosphorus", "pic_ref"},
                    "farm_id = ? AND feed_name = ?",
                    new String[] {Integer.toString(Admin.farm_id), feed_name},
                    null,null,null);
            if(cursor.moveToFirst()) {
                TextView textView_feed_name = (TextView) findViewById(R.id.feed_name);
                textView_feed_name.setText(feed_name);
                TextView textView_price = (TextView) findViewById(R.id.price_content);
                textView_price.setText("Php "+cursor.getString(0));
                TextView textView_availSupply = (TextView) findViewById(R.id.availsupply_content);
                textView_availSupply.setText(cursor.getString(1)+"kg");
                TextView dm = (TextView) findViewById(R.id.dm_content);
                dm.setText(cursor.getString(2)+"%");
                TextView textView_tdn = (TextView) findViewById(R.id.tdn_content);
                textView_tdn.setText(cursor.getString(3)+"%");
                TextView textView_cp = (TextView) findViewById(R.id.cp_content);
                textView_cp.setText(cursor.getString(4)+"%");
                TextView textView_me = (TextView) findViewById(R.id.me_content);
                textView_me.setText(cursor.getString(5)+"kcal/kg");
                TextView textView_ca = (TextView) findViewById(R.id.ca_content);
                textView_ca.setText(cursor.getString(6)+"%");
                TextView textView_p = (TextView) findViewById(R.id.p_content);
                textView_p.setText(cursor.getString(7)+"%");
                ImageView photo = (ImageView) findViewById(R.id.photo);
                photo.setImageResource(cursor.getInt(8));
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e){ }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_admin, menu);
//        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();
//                return false;
//            }
//        });
//        return true;
//    }
}
