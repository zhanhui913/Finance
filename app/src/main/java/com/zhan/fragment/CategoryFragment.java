package com.zhan.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.zhan.activity.EditCategory;
import com.zhan.adapter.CategoryAdapter;
import com.zhan.db.Database;
import com.zhan.finance.R;
import com.zhan.models.Category;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;


public class CategoryFragment extends Fragment {
	private Database db;
    private ArrayList<Category> categoryList = new ArrayList<Category>();

	public CategoryFragment(){}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        final View rootView = inflater.inflate(R.layout.fragment_category, container, false);

        dbOpen();

        //Fetch editText value
        final EditText categoryValue = (EditText)rootView.findViewById(R.id.editText_add_category);

        Button addCategoryBtn = (Button)rootView.findViewById(R.id.btn_add_category);
        addCategoryBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            String value = categoryValue.getText().toString();
            Toast.makeText(rootView.getContext(),"Added category = "+value, Toast.LENGTH_SHORT).show();
            Category c = new Category();
            c.setTitle(value);
            db.createCategory(c);
            backDatabase();

            //Clear category value
            categoryValue.setText("");

            //Update category list view
            populateCategoryListView(rootView);

            //Close softkeyboard
            hideSoftKeyboard();
            }
        });


        populateCategoryListView(rootView);



        return rootView;
    }


    private void hideSoftKeyboard(){

    }


    public void populateCategoryListView(View v){
        categoryList = db.getAllCategory();

        CategoryAdapter categoryAdapter = new CategoryAdapter(getActivity(),categoryList);

        //Set adapter for the list view
        ListView myCatList = (ListView)v.findViewById(R.id.listView_category);

        myCatList.setAdapter(categoryAdapter);

        myCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category c = (Category) adapterView.getItemAtPosition(i);

                String title = c.getTitle();

                Toast.makeText(getActivity().getApplicationContext(), "You clicked "+title, Toast.LENGTH_SHORT).show();

                //Open editCategory activity
                Intent intent = new Intent(view.getContext(), EditCategory.class);
                intent.putExtra("SelectedCategory",title);
                startActivity(intent);
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


    /** THIS IS JUST TO CONFIRM DATABASE IS ACCURATE
     * REMOVE BEFORE RELEASE
     * Backup EnTourDatabase into a readable sdCard since my phone isnt rooted.
     *
     */
    public void backDatabase(){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.zhan.finance//databases//FinanceDatabase";
                String backupDBPath = "Finance/FinanceDatabase.sqlite";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.category, menu);
    }
}
