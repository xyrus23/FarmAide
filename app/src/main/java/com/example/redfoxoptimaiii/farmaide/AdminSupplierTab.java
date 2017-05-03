package com.example.redfoxoptimaiii.farmaide;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AdminSupplierTab extends Fragment {
    private SQLiteDatabase db;
    private Cursor cursor, cursor1;
    HashMap<String,List<String>> listHashMap = new HashMap<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_supplier_tab, container, false);
        List<String> headers = new ArrayList<>();
        List<String> contacts = new ArrayList<>();
        HashMap<String,List<String>> hashSupply = new HashMap<>();
        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();
            cursor = db.query("FARM",
                    new String[]{"farm_id, farm_name, contact_no"},
                    "farm_id != ?",
                    new String[]{Integer.toString(Admin.farm_id)},
                    null, null, null);
            if(cursor.moveToFirst()){
                for (int i=0;i<cursor.getCount();i+=1){
                    headers.add(cursor.getString(1));
                    contacts.add(cursor.getString(2));

                    cursor1 = db.query("FEED",
                            new String[]{"feed_name, supply_amount"},
                            "farm_id = ?",
                            new String[]{cursor.getString(0)},
                            null, null, null);
                    if (cursor1.moveToFirst()){
                        List<String> feeds = new ArrayList<>();
                        List<String> supply = new ArrayList<>();
                        for (int j=0;j<cursor1.getCount();j+=1){
                            feeds.add(cursor1.getString(0));
                            supply.add(cursor1.getString(1)+"kg");
                            cursor1.moveToNext();
                        }
                        listHashMap.put(headers.get(i),feeds);
                        hashSupply.put(headers.get(i),supply);
                    }
                    else {
                        listHashMap.put(headers.get(i), new ArrayList<String>());
                        hashSupply.put(headers.get(i), new ArrayList<String>());
                    }
                    cursor.moveToNext();
                }
            }

            db.close();
            cursor.close();
            cursor1.close();
        } catch (SQLiteException e){}

        ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.expandable_listView);
        ExpandableListAdapter listAdapter = new ExpandableListAdapter(getContext(), headers, listHashMap, hashSupply, contacts);
        listView.setAdapter(listAdapter);
        return rootView;
    }
}
