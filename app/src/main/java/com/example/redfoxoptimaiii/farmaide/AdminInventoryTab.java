package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 1/10/2017.
 */

public class AdminInventoryTab extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor, cursor2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.admin_inventory_tab, container, false);

        final ArrayList<String> feed_names = new ArrayList<>();
        final ArrayList<String> list = new ArrayList<>();
        ArrayList<String> feed_types = new ArrayList<>();
        final ArrayList<byte[]> pics = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddInventoryActivity.class);
                startActivity(intent);
            }
        });

        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("FEED",
                    new String[]{"feed_type"},
                    "farm_id = ?",
                    new String[]{Integer.toString(Admin.farm_id)},
                    null, null, "feed_type ASC");

            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i += 1) {
                    if (!feed_types.contains(cursor.getString(0)))
                        feed_types.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                for (int i = 0; i < feed_types.size(); i += 1) {
                    cursor2 = db.query("FEED",
                            new String[]{"feed_name"},
                            "farm_id=? AND feed_type=?",
                            new String[]{Integer.toString(Admin.farm_id), feed_types.get(i).trim()}, null, null, "feed_name ASC");
                    if (cursor2.moveToFirst()) {
                        for (int j = 0; j < cursor2.getCount(); j++) {
                            if (!list.contains(feed_types.get(i))) {
                                list.add(feed_types.get(i));
                                pics.add(null);
                            }
                            list.add(cursor2.getString(0));
                            feed_names.add(cursor2.getString(0));
                            cursor2.moveToNext();
                        }
                    }
                    cursor2.close();
                }
                ListView listView = (ListView) rootView.findViewById(R.id.listView_adminInventory);
                listView.setAdapter(new InventoryAdapter(getContext(), list, feed_names, feed_types, pics));
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (feed_names.contains(list.get(position))) {
                            Intent intent = new Intent(getContext(), AdminInventoryDetailActivity.class);
                            intent.putExtra("feed_name", list.get(position));
                            startActivity(intent);
                        }
                    }
                });
            }
            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }
        return rootView;
    }
}
