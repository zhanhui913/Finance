package com.zhan.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhan.db.Database;
import com.zhan.finance.R;
import com.zhan.fragment.DatePickerFragment;
import com.zhan.models.Category;

import java.util.ArrayList;

public class Input extends Activity {
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("Type");
        if(type.equalsIgnoreCase("Expense")){
            //Change actionbar title
            setTitle("Expense");

            //Change sign to the left of price editText to represent expense
            TextView v = (TextView)findViewById(R.id.textView_sign);
            v.setText(getString(R.string.negative));
            v.setTextColor(getResources().getColorStateList(R.color.red));
        }else{
            //Change actionbar title
            setTitle("Income");

            //Change sign to the left of price editText to represent income
            TextView v = (TextView)findViewById(R.id.textView_sign);
            v.setText(getString(R.string.positive));
            v.setTextColor(getResources().getColorStateList(R.color.green));
        }

        dbOpen();

        setUpCategorySpinner();

    }

    public void setUpCategorySpinner(){
        Spinner spinner = (Spinner)findViewById(R.id.spinner_category);

        ArrayList<Category> c = db.getAllCategory();
        ArrayList<String> temp = new ArrayList<String>();

        for(int i =0;i<c.size();i++){
            temp.add(c.get(i).getTitle());
        }


        //Application of the array to the spinner
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,temp);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);

    }


    public void dbOpen(){
        db = new Database(this.getApplicationContext());
        db.getWritableDatabase();
    }

    public void dbClose(){
        db.close();
    }

    /**
     * When the user clicks the button "Add"
     * @param v
     */
    public void add(View v){
        finish();
    }

    public void showDatePickerDialog(View v){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.input, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
