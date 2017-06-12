package com.example.redfoxoptimaiii.farmaide;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String username = getIntent().getStringExtra("username");
        TextView textView = (TextView) findViewById(R.id.textView_username);
        textView.setText(username);
        final ArrayList<String> listTimeStamp = new ArrayList<>();
        final ArrayList<String> listNote = new ArrayList<>();

        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(this);
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("USER",
                    new String[]{"user_id"},
                    "farm_id=? AND username=?",
                    new String[]{Integer.toString(Admin.farm_id), username},
                    null, null, null);
            if (cursor.moveToFirst()) {
                String user_id = cursor.getString(0);
                cursor = db.query("TRANSACTIONS",
                        new String[]{"time_stamp, note"},
                        "user_id=? AND farm_id=?",
                        new String[]{user_id, Integer.toString(Admin.farm_id)},
                        null, null, null);
                if (cursor.moveToFirst()) {
                    for (int i = 0; i < cursor.getCount(); i += 1) {
                        listTimeStamp.add(cursor.getString(0));
                        listNote.add(cursor.getString(1));
                        cursor.moveToNext();
                    }
                    BaseAdapter adapter = new BaseAdapter() {
                        @Override
                        public int getCount() {
                            return listNote.size();
                        }

                        @Override
                        public Object getItem(int position) {
                            return listNote.get(position);
                        }

                        @Override
                        public long getItemId(int position) {
                            return position;
                        }

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {
                            if (convertView == null) {
                                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                convertView = inflater.inflate(R.layout.list_item, null);
                            }
                            TextView textView1 = (TextView) convertView.findViewById(R.id.textView1);
                            textView1.setText(listNote.get(position));
                            TextView textView2 = (TextView) convertView.findViewById(R.id.adj_r_squared);
                            textView2.setText(listTimeStamp.get(position));
                            return convertView;
                        }
                    };
                    ListView listView = (ListView) findViewById(R.id.listView);
                    listView.setAdapter(adapter);
                }
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }
    }
}