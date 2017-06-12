package com.example.redfoxoptimaiii.farmaide;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class AdminSupplierTab extends Fragment {
    HashMap<String, List<String>> listHashMap = new HashMap<>();
    ProgressDialog dialog;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.admin_supplier_tab, container, false);
        dialog = ProgressDialog.show(getContext(), "", "Loading", true);
        getData(rootView);
//        try {
//            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
//            db = FarmAideDBHelper.getReadableDatabase();
//            cursor = db.query("FARM",
//                    new String[]{"farm_id, farm_name, contact_no"},
//                    "farm_id != ?",
//                    new String[]{Integer.toString(Admin.farm_id)},
//                    null, null, null);
//            if (cursor.moveToFirst()) {
//                for (int i = 0; i < cursor.getCount(); i += 1) {
//                    headers.add(cursor.getString(1));
//                    contacts.add(cursor.getString(2));
//
//                    cursor1 = db.query("FEED",
//                            new String[]{"feed_name, supply_amount"},
//                            "farm_id = ?",
//                            new String[]{cursor.getString(0)},
//                            null, null, null);
//                    if (cursor1.moveToFirst()) {
//                        List<String> feeds = new ArrayList<>();
//                        List<String> supply = new ArrayList<>();
//                        for (int j = 0; j < cursor1.getCount(); j += 1) {
//                            feeds.add(cursor1.getString(0));
//                            supply.add(cursor1.getString(1) + "kg");
//                            cursor1.moveToNext();
//                        }
//                        listHashMap.put(headers.get(i), feeds);
//                        hashSupply.put(headers.get(i), supply);
//                    } else {
//                        listHashMap.put(headers.get(i), new ArrayList<String>());
//                        hashSupply.put(headers.get(i), new ArrayList<String>());
//                    }
//                    cursor.moveToNext();
//                }
//                ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.expandable_listView);
//                ExpandableListAdapter listAdapter = new ExpandableListAdapter(getContext(), headers, listHashMap, hashSupply, contacts);
//                listView.setAdapter(listAdapter);
//            }
//
//            db.close();
//            cursor.close();
//            cursor1.close();
//        } catch (SQLiteException e) {
//        }
        return rootView;
    }
    public void getData(final View rootView){
        final List<String> headers = new ArrayList<>();
        final List<String> contacts = new ArrayList<>();
        final HashMap<String, List<String>> hashSupply = new HashMap<>();

        RestClient.get("suppliers/"+Admin.farm_id, null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] header, JSONArray response) {
                try{
                    if(statusCode == 200){
                        for (int i=0;i<response.length();i+=1){
                            final JSONObject object = (JSONObject) response.get(i);
                            headers.add(object.getString("farm_name"));
                            contacts.add(object.getString("contact_no"));

                            final int finalI = i;
                            RestClient.get("supplierFeed/"+object.getInt("farm_id"), null, new JsonHttpResponseHandler(){
                                @Override
                                public void onSuccess(int statusCode, Header[] header, JSONArray response) {
                                    try {
                                        List<String> feeds = new ArrayList<>();
                                        List<String> supply = new ArrayList<>();
                                        feeds.add("No available feeds");
                                        supply.add("-");
                                        if(response.length()>0) {
                                            feeds.clear();
                                            supply.clear();
                                            for (int j = 0; j < response.length(); j += 1) {
                                                JSONObject obj = (JSONObject) response.get(j);
                                                feeds.add(obj.getString("feed_name"));
                                                supply.add(obj.getString("supply_amount") + "kg");
                                            }
                                            listHashMap.put(headers.get(finalI),feeds);
                                            hashSupply.put(headers.get(finalI), supply);
                                        }
                                        else {
                                            listHashMap.put(headers.get(finalI), feeds);
                                            hashSupply.put(headers.get(finalI), supply);
                                        }
                                    } catch (JSONException e){}
                                }
                            });
                        }
                    }
                } catch (JSONException e) {}
            }

            @Override
            public void onFinish() {
                dialog.dismiss();
                Toast.makeText(getContext(), listHashMap.toString(), Toast.LENGTH_LONG).show();
                ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.expandable_listView);
                ExpandableListAdapter listAdapter = new ExpandableListAdapter(getContext(), headers, listHashMap, hashSupply, contacts);
                listView.setAdapter(listAdapter);
            }
        });
    }
}
