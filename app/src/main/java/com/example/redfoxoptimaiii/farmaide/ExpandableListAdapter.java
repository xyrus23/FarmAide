package com.example.redfoxoptimaiii.farmaide;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by REDFOX™ OptimaIII on 3/26/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listHashMap;
    private HashMap<String, List<String>> hashSupply;
    private List<String> contacts;
    private HashMap<String, List<String>> checkedItems = new HashMap<>();

    public ExpandableListAdapter(Context context, List<String> listDataHeader, HashMap<String, List<String>> listHashMap, HashMap<String, List<String>> hashSupply, List<String> contacts) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listHashMap = listHashMap;
        this.hashSupply = hashSupply;
        this.contacts = contacts;
        for (int i=0;i<listDataHeader.size();i+=1){
            List<String> list = new ArrayList<>();
            for (int j=0;j<listHashMap.get(listDataHeader.get(i)).size();j+=1){
                list.add(listHashMap.get(listDataHeader.get(i)).get(j));
            }
            this.checkedItems.put(listDataHeader.get(i),list);
        }
    }

    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    public String getSupply(int groupPosition, int childPosition){
        return hashSupply.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(contacts!=null) convertView = inflater.inflate(R.layout.supplier_list_header,null);
            else convertView = inflater.inflate(R.layout.list_group_header,null);
        }
        if (contacts!=null){
            TextView textView_header = (TextView) convertView.findViewById(R.id.textView_header);
            textView_header.setText(headerTitle);
            TextView textView_subheader = (TextView) convertView.findViewById(R.id.textView_subheader);
            textView_subheader.setText(contacts.get(groupPosition));
        }
        else{
            TextView list_header = (TextView) convertView.findViewById(R.id.listgroup_header);
            list_header.setText(headerTitle);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition,childPosition);
        final String supplyText = getSupply(groupPosition,childPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (contacts!=null) convertView = inflater.inflate(R.layout.supplier_list_group_item, null);
            else convertView = inflater.inflate(R.layout.list_group_item,null);
        }

        TextView listItem = (TextView) convertView.findViewById(R.id.list_item);
        listItem.setText(childText);
        TextView listItem_supply = (TextView) convertView.findViewById(R.id.list_item_supply);
        listItem_supply.setText(supplyText);
        if(contacts==null){
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.list_item);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        getCheckedGroup(getGroup(groupPosition).toString()).add(buttonView.getText().toString());
                    else
                        getCheckedGroup(getGroup(groupPosition).toString()).remove(buttonView.getText().toString());
                }
            });
        }
        return convertView;
    }

    public HashMap getCheckedItems(){
        return checkedItems;
    }

    public List getCheckedGroup(String groupName){ return checkedItems.get(groupName);}

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
