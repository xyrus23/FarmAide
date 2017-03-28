package com.example.redfoxoptimaiii.farmaide;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.R.color.white;

public class OptimizerMenuActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimizer_menu);

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

        TextView textView_animal = (TextView) findViewById(R.id.textView_animal);
        TextView textView_type = (TextView) findViewById(R.id.textView_type);
        TextView textView_subtype = (TextView) findViewById(R.id.textView_subType);
        textView_animal.setText(getIntent().getStringExtra("animal"));
        textView_type.setText(getIntent().getStringExtra("animal_type"));
        textView_subtype.setText(getIntent().getStringExtra("recipe_name"));
        float[] nutri_req = getIntent().getFloatArrayExtra("nutri_req");

        List<String> headers = new ArrayList<>();
        HashMap<String,List<String>> listHashMap = new HashMap<>();
        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();
            cursor = db.query("FEED",
                    new String[] {"feed_name, supply_amount"},
                    "feed_type=?",new String[] {"Roughage"},null,null,"feed_name ASC");
            if(cursor.moveToFirst()){
                headers.add("Roughage");
                List<String> roughage = new ArrayList<>();
                for(int i=0;i<cursor.getCount();i+=1){
                    roughage.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                listHashMap.put(headers.get(0),roughage);
            }
            cursor = db.query("FEED",
                    new String[] {"feed_name, supply_amount"},
                    "feed_type=?",new String[] {"Concentrate"},null,null,"feed_name ASC");
            if(cursor.moveToFirst()){
                headers.add("Concentrates");
                List<String> concentrate = new ArrayList<>();
                for(int i=0;i<cursor.getCount();i+=1){
                    concentrate.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                listHashMap.put(headers.get(1),concentrate);
            }
            cursor = db.query("FEED",
                    new String[] {"feed_name, supply_amount"},
                    "feed_type=?",new String[] {"Additive"},null,null,"feed_name ASC");
            if(cursor.moveToFirst()){
                headers.add("Additives");
                List<String> additive = new ArrayList<>();
                for(int i=0;i<cursor.getCount();i+=1){
                    additive.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                listHashMap.put(headers.get(2),additive);
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e){ }

        ExpandableListView listView = (ExpandableListView) findViewById(R.id.expandablelist);
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, headers, listHashMap);
        listView.setAdapter(listAdapter);
    }
}
