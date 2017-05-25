package com.example.redfoxoptimaiii.farmaide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by REDFOX™ OptimaIII on 3/23/2017.
 */

public class RecipeAdapter extends BaseAdapter {

    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> recipes = new ArrayList<>();
    ArrayList<String> animals = new ArrayList<>();
    ArrayList<String> animal_types = new ArrayList<>();
    private LayoutInflater inflater;
    private static final int RECIPE = 0;
    private static final int ANIMAL = 1;
    private static final int ANIMAL_TYPE = 2;

    public RecipeAdapter(Context context, ArrayList<String> list, ArrayList<String> recipes, ArrayList<String> animals, ArrayList<String> animal_types){
        this.list = list;
        this.recipes = recipes;
        this.animals = animals;
        this.animal_types = animal_types;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if(recipes.contains(list.get(position))) return RECIPE;
        if(animals.contains(list.get(position))) return ANIMAL;
        else return ANIMAL_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            switch (getItemViewType(position)){
                case RECIPE:
                    convertView = inflater.inflate(android.R.layout.simple_list_item_1,null);
                    break;
                case ANIMAL:
                    convertView = inflater.inflate(R.layout.animal_list_header,null);
                    break;
                case ANIMAL_TYPE:
                    convertView = inflater.inflate(R.layout.list_subheader,null);
                    break;
            }
        }
        switch (getItemViewType(position)){
            case RECIPE:
                TextView name = (TextView) convertView.findViewById(android.R.id.text1);
                name.setText(list.get(position));
                break;
            case ANIMAL:
                TextView animal = (TextView) convertView.findViewById(R.id.list_header);
                animal.setText(list.get(position));
                ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
                if (list.get(position).equals("Poultry")) image.setImageResource(R.drawable.poultry);
                else if (list.get(position).equals("Ruminants")) image.setImageResource(R.drawable.ruminants);
                else if (list.get(position).equals("Swine")) image.setImageResource(R.drawable.swine);
                break;
            case ANIMAL_TYPE:
                TextView animalType = (TextView) convertView.findViewById(R.id.list_subheader);
                animalType.setText(list.get(position));
                break;
        }
        return convertView;
    }
}
