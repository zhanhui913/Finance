package com.zhan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhan.finance.R;
import com.zhan.models.Category;

import java.util.List;

/**
 * Created by Zhan on 2014-10-18.
 */
public class CategoryAdapter extends BaseAdapter{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Category> categoryLists;

    public CategoryAdapter(Activity aActivity, List<Category> aCategoryLists){
        this.activity = aActivity;
        this.categoryLists = aCategoryLists;
    }

    @Override
    public int getCount(){
        return this.categoryLists.size();
    }

    @Override
    public Object getItem(int index){
        return categoryLists.get(index);
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
            convertView = inflater.inflate(R.layout.category_item,null);
        }

        TextView categoryTitle = (TextView) convertView.findViewById(R.id.category_item_title);

        //Getting category data for the row
        Category c = categoryLists.get(position);

        //Set category title
        categoryTitle.setText(c.getTitle());

        return convertView;
    }


}
