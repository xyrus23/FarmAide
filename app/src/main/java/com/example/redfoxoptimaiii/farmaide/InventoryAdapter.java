package com.example.redfoxoptimaiii.farmaide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class InventoryAdapter extends BaseAdapter {

    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> types = new ArrayList<>();
    ArrayList<byte[]> pics = new ArrayList<>();
    private LayoutInflater inflater;
    private static final int ITEM = 0;
    private static final int TYPE = 1;

    public InventoryAdapter(Context context, ArrayList<String> list, ArrayList<String> items, ArrayList<String> types, ArrayList<byte[]> pics){
        this.list = list;
        this.items = items;
        this.types = types;
        this.pics = pics;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if(items.contains(list.get(position))) return ITEM;
        else return TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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
                case ITEM:
                    convertView = inflater.inflate(R.layout.inventory_list_item,null);
                    break;
                case TYPE:
                    convertView = inflater.inflate(R.layout.list_header,null);
                    break;
            }
        }
        switch (getItemViewType(position)){
            case ITEM:
                TextView name = (TextView) convertView.findViewById(R.id.text1);
                name.setText(list.get(position));
//                Bitmap bitmap = BitmapFactory.decodeByteArray(pics.get(position), 0, pics.get(position).length);
//                ((ImageView)convertView.findViewById(R.id.imageView)).setImageBitmap(bitmap);
                break;
            case TYPE:
                TextView animal = (TextView) convertView.findViewById(R.id.list_header);
                animal.setText(list.get(position));
                break;
        }
        return convertView;
    }
}
