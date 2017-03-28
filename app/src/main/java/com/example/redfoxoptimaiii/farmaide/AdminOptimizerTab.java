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
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 1/10/2017.
 */

public class AdminOptimizerTab extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_optimizer_tab, container, false);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> recipe_name = new ArrayList<>();
        ArrayList<String> animal = new ArrayList<>();
        ArrayList<String> animal_type = new ArrayList<>();
        final ArrayList<Object> animals = new ArrayList<>();

        try {
            SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("RECIPE",
                    new String[]{"recipe_name","animal","animal_type","dm_req","cp_req","me_req","ca_req","p_req"},
                    null,null,null,null,"animal ASC");

            if(cursor.moveToFirst()){
                for (int i=0;i<cursor.getCount();i+=1){
                    if(!list.contains(cursor.getString(1))){
                        list.add(cursor.getString(1));
                        animals.add(null);
                    }
                    if(!cursor.getString(2).equals("-") && !list.contains(cursor.getString(2))) {
                        list.add(cursor.getString(2));
                        animals.add(null);
                    }
                    list.add(cursor.getString(0));
                    recipe_name.add(cursor.getString(0));
                    animal.add(cursor.getString(1));
                    if(!cursor.getString(2).equals("-")) animal_type.add(cursor.getString(2));
                    animals.add(new Animal(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getFloat(3),cursor.getFloat(4),cursor.getFloat(5),cursor.getFloat(6),cursor.getFloat(7)));
                    cursor.moveToNext();
                }
            }
            ListView listView = (ListView) rootView.findViewById(R.id.listView);
            listView.setAdapter(new RecipeAdapter(getContext(),list,recipe_name,animal,animal_type));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(recipe_name.contains(list.get(position))) {
                        Intent intent = new Intent(getContext(), OptimizerMenuActivity.class);
                        Animal animal = (Animal) animals.get(position);
                        float[] nutri_req = new float[] {animal.getDm_req(),animal.getCp_req(),animal.getMe_req(),animal.getCa_req(),animal.getP_req()};
                        intent.putExtra("recipe_name",animal.getName());
                        intent.putExtra("animal",animal.getAnimal());
                        intent.putExtra("animal_type",animal.getType());
                        intent.putExtra("nutri_req", nutri_req);
                        startActivity(intent);
                    }
                }
            });

            cursor.close();
            db.close();
        } catch (SQLiteException e){ }

        return rootView;
    }
}
