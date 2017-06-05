package com.example.redfoxoptimaiii.farmaide;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdminPredictorTab extends Fragment {
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_predictor_tab, container, false);
        final ArrayList<String> list = new ArrayList<>();
        ArrayList<Integer> cow_ids = new ArrayList<>();
        final ArrayList<Double> rs = new ArrayList<>();
        final ArrayList<Double> ars = new ArrayList<>();
        double[][] dataX = null;

        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();
            cursor = db.query("COW",
                    new String[]{"cow_name", "cow_id"},
                    "farm_id=?",
                    new String[]{Integer.toString(Admin.farm_id)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i += 1) {
                    list.add(cursor.getString(0));
                    cow_ids.add(cursor.getInt(1));
                    cursor.moveToNext();
                }
                for (int i = 0; i < list.size(); i += 1) {
                    cursor = db.query("MILK_YIELD",
                            new String[]{"milk_yield, days, fats_yield, protein_yield, total_solids_yield"},
                            "farm_id=? AND cow_id=?",
                            new String[]{Integer.toString(Admin.farm_id), cow_ids.get(i).toString()},
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        dataX = new double[cursor.getCount()][cursor.getColumnCount()];
                        for (int r = 0; r < cursor.getCount(); r += 1) {
                            for (int j = 0; j < cursor.getColumnCount(); j += 1)
                                dataX[r][j] = cursor.getDouble(j);
                            cursor.moveToNext();
                        }
                    }
                    MLR mlr = new MLR(dataX);
                    double adj_rs = mlr.getAdj_r_sqrd() * 100;
                    double r_squared = mlr.getR_squared() * 100;
                    rs.add(r_squared);
                    ars.add(adj_rs);
                }
                ArrayAdapter<String> listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, list);
                ListView listView = (ListView) rootView.findViewById(R.id.listView);
                listView.setAdapter(listAdapter);
//                listView.setAdapter(new ListAdapter() {
//                    @Override
//                    public boolean areAllItemsEnabled() {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean isEnabled(int position) {
//                        return true;
//                    }
//
//                    @Override
//                    public void registerDataSetObserver(DataSetObserver observer) {
//
//                    }
//
//                    @Override
//                    public void unregisterDataSetObserver(DataSetObserver observer) {
//
//                    }
//
//                    @Override
//                    public int getCount() {
//                        return list.size();
//                    }
//
//                    @Override
//                    public Object getItem(int position) {
//                        return list.get(position);
//                    }
//
//                    @Override
//                    public long getItemId(int position) {
//                        return position;
//                    }
//
//                    @Override
//                    public boolean hasStableIds() {
//                        return false;
//                    }
//
//                    @Override
//                    public View getView(int position, View convertView, ViewGroup parent) {
//                        if (convertView==null){
//                            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            convertView = inflater.inflate(R.layout.list_animals, null);
//                            ((TextView)convertView.findViewById(R.id.textView1)).setText(list.get(position));
//                            String value = String.format("%.2f", ars.get(position));
//                            ((TextView)convertView.findViewById(R.id.adj_r_squared)).setText("Adequacy Fit: "+value+"%");
//                            if (ars.get(position) > 50) ((TextView)convertView.findViewById(R.id.adj_r_squared)).setTextColor(getResources().getColor(R.color.colorPrimary));
//                            else ((TextView)convertView.findViewById(R.id.adj_r_squared)).setTextColor(Color.RED);
//                        }
//                        return convertView;
//                    }
//
//                    @Override
//                    public int getItemViewType(int position) {
//                        return position;
//                    }
//
//                    @Override
//                    public int getViewTypeCount() {
//                        return list.size();
//                    }
//
//                    @Override
//                    public boolean isEmpty() {
//                        return false;
//                    }
//                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getContext(), AdminPredictorYields.class);
                        intent.putExtra("id", position);
                        intent.putExtra("list", list);
                        startActivity(intent);
                    }
                });
            }

            cursor.close();
            db.close();
        } catch (SQLiteException e) {
        }

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Snackbar snackbar = Snackbar.make(v, "", Snackbar.LENGTH_INDEFINITE);
                Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
                layout.setBackgroundColor(Color.WHITE);
                layout.setMinimumHeight(80);
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View snackView = inflater.inflate(R.layout.snackbar_add_animal, null);
                snackView.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputLayout input_animal = (TextInputLayout) snackView.findViewById(R.id.input_animal_name);
                        input_animal.setErrorEnabled(false);
                        String animal_name = ((TextView) snackView.findViewById(R.id.animal_name)).getText().toString();
                        if (animal_name.isEmpty()) input_animal.setError("This field is required");
                        else {
                            try {
                                FarmAideDatabaseHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
                                db = FarmAideDBHelper.getReadableDatabase();
                                cursor = db.query("COW",
                                        new String[]{"cow_name"},
                                        "farm_id=? AND cow_name=?",
                                        new String[]{Integer.toString(Admin.farm_id), animal_name},
                                        null, null, null);
                                if (cursor.moveToFirst())
                                    input_animal.setError("Name already exists");
                                else {
                                    FarmAideDBHelper.insertCow(db, Admin.farm_id, animal_name);
                                    Toast.makeText(getContext(), "Successfully Added Animal", Toast.LENGTH_SHORT).show();
                                    getActivity().finish();
                                    getActivity().getIntent().putExtra("position", 4);
                                    getActivity().startActivity(getActivity().getIntent());
                                }
                            } catch (SQLiteException e) {
                            }
                        }
                    }
                });
                layout.addView(snackView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                snackbar.show();
            }
        });
        return rootView;
    }
}
