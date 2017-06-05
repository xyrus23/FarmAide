package com.example.redfoxoptimaiii.farmaide;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 1/10/2017.
 */

public class AdminOptimizerTab extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.admin_optimizer_tab, container, false);
        final ArrayList<String> list = new ArrayList<>();
        final ArrayList<String> recipe_name = new ArrayList<>();
        final ArrayList<String> animal = new ArrayList<>();
        final ArrayList<String> animal_type = new ArrayList<>();
        final ArrayList<Animal> animals = new ArrayList<>();

        FloatingActionButton fab_recipe = (FloatingActionButton) rootView.findViewById(R.id.fab_recipe);
        fab_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });

        try {
            final SQLiteOpenHelper FarmAideDBHelper = new FarmAideDatabaseHelper(getContext());
            db = FarmAideDBHelper.getReadableDatabase();

            cursor = db.query("RECIPE",
                    new String[]{"recipe_name", "animal", "animal_type", "dm_req", "cp_req", "me_req", "ca_req", "p_req", "animal_weight"},
                    "farm_id=?", new String[]{Integer.toString(Admin.farm_id)}, null, null, "animal ASC");

            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i += 1) {
                    if (!list.contains(cursor.getString(1))) {
                        list.add(cursor.getString(1));
                        animals.add(null);
                    }
                    if (!cursor.getString(2).equals("-") && !list.contains(cursor.getString(2))) {
                        list.add(cursor.getString(2));
                        animals.add(null);
                    }
                    list.add(cursor.getString(0));
                    recipe_name.add(cursor.getString(0));
                    animal.add(cursor.getString(1));
                    animal_type.add(cursor.getString(2));
                    animals.add(new Animal(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getFloat(3), cursor.getFloat(4), cursor.getFloat(5), cursor.getFloat(6), cursor.getFloat(7), cursor.getFloat(8)));
                    cursor.moveToNext();
                }
                ListView listView = (ListView) rootView.findViewById(R.id.listView);
                listView.setAdapter(new RecipeAdapter(getContext(), list, recipe_name, animal, animal_type));
                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
                        if (recipe_name.contains(list.get(position))) {
                            final String[] items = new String[]{"View Recipe", "Edit Recipe", "Delete Recipe"};
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, items);
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("What do you want to do?");
                            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        Animal a = animals.get(position);
                                        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                                        intent.putExtra("recipe_name", a.getName());
                                        intent.putExtra("animal", a.getAnimal());
                                        intent.putExtra("animal_type", a.getType());
                                        intent.putExtra("dm", Float.toString(a.getDm_req()));
                                        intent.putExtra("cp", Float.toString(a.getCp_req()));
                                        intent.putExtra("me", Float.toString(a.getMe_req()));
                                        intent.putExtra("ca", Float.toString(a.getCa_req()));
                                        intent.putExtra("p", Float.toString(a.getP_req()));
                                        intent.putExtra("animal_weight", Float.toString(a.getAnimal_weight()));
                                        startActivity(intent);
                                    } else if (which == 1) {
                                        Animal a = animals.get(position);
                                        Intent intent = new Intent(getContext(), EditRecipeActivity.class);
                                        intent.putExtra("recipe_name", a.getName());
                                        intent.putExtra("animal", a.getAnimal());
                                        intent.putExtra("animal_type", a.getType());
                                        startActivity(intent);
                                    } else {
                                        final String[] decisions = new String[]{"Yes", "No"};
                                        ArrayAdapter<String> decisionAdpt = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_item, decisions);
                                        AlertDialog.Builder decisionBldr = new AlertDialog.Builder(getContext());
                                        decisionBldr.setTitle("Are you sure you want to delete this recipe?");
                                        decisionBldr.setAdapter(decisionAdpt, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (which == 0) {
                                                    // get the recipe info here for deletion
                                                    Animal a = animals.get(position);
                                                    String recipeName = a.getName();
                                                    String animalName = a.getAnimal();
                                                    String animalType = a.getType();
                                                    try {
                                                        db = FarmAideDBHelper.getWritableDatabase();
                                                        Cursor cursor2 = db.query("RECIPE", new String[]{"recipe_id", "recipe_name"}, "farm_id=? AND recipe_name=? AND animal=? AND animal_type=?", new String[]{Integer.toString(Admin.farm_id), recipeName, animalName, animalType}, null, null, null);
                                                        if (cursor2.moveToFirst()) {
                                                            ((FarmAideDatabaseHelper) FarmAideDBHelper).deleteRecipe(db, cursor2.getInt(0));
                                                            getActivity().finish();
                                                            getActivity().getIntent().putExtra("position", 2);
                                                            startActivity(getActivity().getIntent());
                                                        }
                                                        Toast.makeText(getContext(), "Recipe deleted", Toast.LENGTH_LONG).show();
                                                        cursor2.close();
                                                    } catch (SQLiteException e) {
                                                    }
                                                }
                                            }
                                        });
                                        final AlertDialog dialog2 = decisionBldr.show();
                                        dialog2.show();
                                    }
                                }
                            });
                            final AlertDialog dialog = builder.show();
                            dialog.show();
                        }
                        return true;
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (recipe_name.contains(list.get(position))) {
                            Intent intent = new Intent(getContext(), OptimizerMenuActivity.class);
                            Animal animal = animals.get(position);
                            float[] nutri_req = new float[]{animal.getCp_req() / 100, animal.getMe_req(), animal.getCa_req() / 100, animal.getP_req() / 100};
                            intent.putExtra("recipe_name", animal.getName());
                            intent.putExtra("animal", animal.getAnimal());
                            intent.putExtra("animal_type", animal.getType());
                            intent.putExtra("nutri_req", nutri_req);
                            intent.putExtra("animal_weight", animal.getAnimal_weight());
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
