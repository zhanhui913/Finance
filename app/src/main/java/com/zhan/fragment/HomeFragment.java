package com.zhan.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhan.activity.Input;
import com.zhan.db.Database;
import com.zhan.finance.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Zhan on 2014-10-12.
 */
public class HomeFragment extends Fragment {
    private Database db;


    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_home, container, false);



        Button expenseBtn = (Button)rootView.findViewById(R.id.btn_expense);
        Button incomeBtn = (Button)rootView.findViewById(R.id.btn_income);

        expenseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("zhan","zhan clicked expense btn");
                //Toast.makeText(v.getContext(),"expense btn pressed",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(rootView.getContext(), Input.class);
                intent.putExtra("Type","Expense");
                startActivity(intent);
            }
        });

        incomeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("zhan","zhan clicked income btn");
                //Toast.makeText(v.getContext(),"income btn pressed",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(rootView.getContext(), Input.class);
                intent.putExtra("Type","Income");
                startActivity(intent);
            }
        });

        openDB();


        //TODO Need to check if directory Finance exists, if not create one in sdcard

        backDatabase();

        return rootView;
    }


    public void openDB(){
        db = new Database(this.getActivity().getApplicationContext());
        db.getWritableDatabase();
    }

    public void closeDB(){
        db.close();
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

}

