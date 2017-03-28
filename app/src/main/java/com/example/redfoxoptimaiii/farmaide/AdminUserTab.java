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
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 1/10/2017.
 */

public class AdminUserTab extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;
//    private AdView mAdView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_user_tab, container, false);
        ArrayList<String> list = new ArrayList<>();

        try{
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("USER",
                    new String[] {"username"},
                    null,null,
                    null,null,"username ASC");

            if(cursor.moveToFirst()){
                for (int i=0;i<cursor.getCount();i+=1){
                    list.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,list);
                ListView listView = (ListView) rootView.findViewById(R.id.listView_adminUsers);
                listView.setAdapter(listAdapter);
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {}

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

//        // Create a banner ad. The ad size and ad unit ID must be set before calling loadAd.
//        mAdView = (AdView) rootView.findViewById(R.id.adView);
//        mAdView.setAdSize(AdSize.SMART_BANNER);
//        mAdView.setAdUnitId("myAdUnitId");
//
//        // Create an ad request.
//        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
//
//        // Optionally populate the ad request builder.
//        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
//
//        // Add the AdView to the view hierarchy.
//        layout.addView(mAdView);
//
//        // Start loading the ad.
//        mAdView.loadAd(adRequestBuilder.build());

        return rootView;
    }
}
