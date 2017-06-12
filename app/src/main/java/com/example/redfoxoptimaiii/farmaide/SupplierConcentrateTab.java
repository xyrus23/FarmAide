package com.example.redfoxoptimaiii.farmaide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 1/10/2017.
 */

public class SupplierConcentrateTab extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.supplier_concentrate_tab, container, false);
        final ArrayList<String> list = new ArrayList<>();

        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("FEED",
                    new String[]{"feed_name"},
                    "farm_id = ? AND feed_type = ?",
                    new String[]{Integer.toString(Supplier.farm_id), "Concentrate"},
                    null, null, "feed_name ASC");

            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i += 1) {
                    list.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
                ListView listView = (ListView) rootView.findViewById(R.id.listView_userConcentrate);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), SupplierInventoryDetailActivity.class);
                        intent.putExtra("id", (int) id);
                        intent.putExtra("feed_names", list);
                        startActivity(intent);
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
