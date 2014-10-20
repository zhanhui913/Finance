package com.zhan.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.zhan.adapter.ItemAdapter;
import com.zhan.db.Database;
import com.zhan.finance.R;
import com.zhan.models.Item;

import java.util.ArrayList;


public class ItemFragment extends Fragment {
    private Database db;
    private ArrayList<Item> itemList = new ArrayList<Item>();

	public ItemFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_item, container, false);

        dbOpen();

        populateItemListView(rootView);

        return rootView;
    }

    public void populateItemListView(View v){
        itemList = db.getAllItem();

        ItemAdapter itemAdapter = new ItemAdapter(getActivity(),itemList);

        //Set adapter for the list view
        ListView myItemList = (ListView)v.findViewById(R.id.listView_item);

        myItemList.setAdapter(itemAdapter);

        myItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Item item = (Item) adapterView.getItemAtPosition(i);

                String title = item.getTitle();

                Toast.makeText(getActivity().getApplicationContext(), "You clicked "+title, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void dbOpen(){
        db = new Database(getActivity().getApplicationContext());
        //db.getWritableDatabase();
    }

    public void dbClose(){
        db.close();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        dbClose();
    }
}
