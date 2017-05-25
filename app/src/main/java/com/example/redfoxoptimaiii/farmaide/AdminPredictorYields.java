package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class AdminPredictorYields extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private double[][] dataX;
    private String cow_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_predictor_yields);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int id = getIntent().getIntExtra("id", 0);
        ArrayList<String> list = getIntent().getStringArrayListExtra("list");
        final String cow_name = list.get(id);
        toolbar.setTitle(cow_name);
        final ArrayList<String> dates = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminPredictorYields.this, AddMilkYieldActivity.class);
                intent.putExtra("cow_name", cow_name);
                startActivity(intent);
            }
        });
        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("COW",
                    new String[]{"cow_id"},
                    "farm_id=? AND cow_name=?",
                    new String[]{Integer.toString(Admin.farm_id),cow_name},
                    null,null,null);
            if (cursor.moveToFirst()){
                cow_id = cursor.getString(0);
                cursor = db.query("MILK_YIELD",
                        new String[]{"time_stamp, milk_yield, days, fats_yield, protein_yield, total_solids_yield"},
                        "farm_id=? AND cow_id=?",
                        new String[]{Integer.toString(Admin.farm_id),cow_id},
                        null,null,null);
                if (cursor.moveToFirst()) {
                    dataX = new double[cursor.getCount()][cursor.getColumnCount()-1];
                    for (int i = 0; i < cursor.getCount(); i += 1) {
                        dates.add(cursor.getString(0));
                        for (int j=1;j<cursor.getColumnCount();j+=1)
                            dataX[i][j-1] = cursor.getDouble(j);
                        cursor.moveToNext();
                    }
                    ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dates);
                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(listAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(AdminPredictorYields.this, MilkYieldDetailActivity.class);
                            intent.putExtra("cow_id", cow_id);
                            intent.putExtra("time_stamp", dates.get(position));
                            startActivity(intent);
                        }
                    });
                }
            }
            cursor.close();
            db.close();
        } catch(SQLiteException e){}
        Button button = (Button) findViewById(R.id.btn_predict);
        if (dates.size() < 5) button.setEnabled(false);
    }

    public void predict(View view){
        Intent intent = new Intent(this, PredictorInputActivity.class);
        intent.putExtra("cow_id", cow_id);
        intent.putExtra("sizeX", dataX.length);
        for (int i=0;i<dataX.length;i+=1)
            intent.putExtra("dataX"+i, dataX[i]);
        startActivity(intent);
    }
}
