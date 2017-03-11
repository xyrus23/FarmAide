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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 1/10/2017.
 */

public class AdminUserTab extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_user_tab, container, false);
        ArrayList<String> list = new ArrayList<>();

        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("USER",
                    new String[] {"username"},
                    "user_type = ?",
                    new String[] {"user"},
                    null,null,"username ASC");

            if(cursor.moveToFirst()){
                for (int i=0;i<cursor.getCount();i+=1){
                    list.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,list);
                ListView listView = (ListView) rootView.findViewById(R.id.listView_adminUsers);
                listView.setAdapter(listAdapter);
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {}

        return rootView;
    }
}
