package com.example.redfoxoptimaiii.farmaide;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;


/**
 * Created by REDFOX™ OptimaIII on 1/10/2017.
 */

public class UserSupplierTab extends Fragment {
    private SQLiteDatabase db;
    private Cursor cursor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_supplier_tab, container, false);
        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();


            db.close();
            cursor.close();
        } catch (SQLiteException e){}

        ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.expandable_listView);

        return rootView;
    }
}
