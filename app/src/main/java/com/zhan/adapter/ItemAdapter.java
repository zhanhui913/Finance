package com.zhan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhan.finance.R;
import com.zhan.models.Item;

import java.util.List;

/**
 * Created by Zhan on 2014-10-19.
 */
public class ItemAdapter extends BaseAdapter{

    private Activity activity;
    private LayoutInflater inflater;
    private List<Item> itemLists;

    public ItemAdapter(Activity aActivity, List<Item> aItemLists){
        this.activity = aActivity;
        this.itemLists = aItemLists;
    }

    @Override
    public int getCount(){
        return this.itemLists.size();
    }

    @Override
    public Object getItem(int index){
        return itemLists.get(index);
    }

    @Override
    public long getItemId(int index){
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null){
            convertView = inflater.inflate(R.layout.item_item,null);
        }

        TextView itemTitle = (TextView) convertView.findViewById(R.id.item_item_title);

        //Getting item data for the row
        Item i = itemLists.get(position);

        //Set item title
        itemTitle.setText(i.getTitle());

        return convertView;
    }
}
