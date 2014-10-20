package com.zhan.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zhan.db.Database;
import com.zhan.finance.R;
import com.zhan.fragment.DatePickerFragment;
import com.zhan.models.Category;
import com.zhan.models.Item;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class Input extends Activity {
    private Database db;
    private String title, category, price, type, date;

    private EditText money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("Type");
        if(type.equalsIgnoreCase("Expense")){
            //Change actionbar title
            setTitle("Expense");

            //Set global variable
            this.type = "Expense";
        }else{
            //Change actionbar title
            setTitle("Income");

            //Change name textView color
            TextView nameTextView = (TextView)findViewById(R.id.textView_name);
            nameTextView.setTextColor(getResources().getColorStateList(R.color.green));

            //Change category textView color
            TextView categoryTextView = (TextView)findViewById(R.id.textView_category);
            categoryTextView.setTextColor(getResources().getColorStateList(R.color.green));

            //Change price textView color
            TextView priceTextView = (TextView)findViewById(R.id.textView_price);
            priceTextView.setTextColor(getResources().getColorStateList(R.color.green));

            //Change date textView color
            TextView dateTextView = (TextView)findViewById(R.id.textView_date);
            dateTextView.setTextColor(getResources().getColorStateList(R.color.green));

            //Change sign to the left of price editText to represent income
            TextView v = (TextView)findViewById(R.id.textView_sign);
            v.setText(getString(R.string.positive));
            v.setTextColor(getResources().getColorStateList(R.color.green));

            //Change button color
            Button addButton = (Button)findViewById(R.id.btn_add);
            addButton.setBackgroundResource(R.drawable.button_income_shape);

            //Set global variable
            this.type = "Income";
        }

        dbOpen();

        setUpCategorySpinner();

        //Text listener for expense/income amount
        money = (EditText)findViewById(R.id.editText_price);
        money.addTextChangedListener(tw);
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
        //db.getWritableDatabase();
    }

    public void dbClose(){
        db.close();
    }

    /**
     * When the user clicks the button "Add Expense" or "Add Income"
     * @param v
     */
    public void add(View v){
        finish();

        //Fetch title
        EditText titleEditText = (EditText)findViewById(R.id.editText_name);
        title = titleEditText.getText().toString();

        //Fetch Category
        Spinner categorySpinner = (Spinner)findViewById(R.id.spinner_category);
        category = categorySpinner.getSelectedItem().toString();

        //Fetch Price
        EditText priceEditText = (EditText)findViewById(R.id.editText_price);
        price = priceEditText.getText().toString();
        Toast.makeText(getApplicationContext(), "Added price = " + price, Toast.LENGTH_SHORT).show();
        //Fetch Date
        Button dateButton = (Button)findViewById(R.id.btn_selectDate);
        date = dateButton.getText().toString();

        //Create Item object
        Item i = new Item();
        i.setTitle(title);
        i.setCategory(new Category(
                db.getCategoryByTitle(category).getId(),
                category)
        );
        i.setPrice(Double.parseDouble(removeDollarSign(price)));
        i.setType(type);
        i.setDate(date);

        db.createItem(i);

        backDatabase();
    }

    /**
     * Remove first character from a string, the dollar sign "$" in price
     * @param moneyString
     * @return The string without the first character
     */
    public String removeDollarSign(String moneyString){
        return moneyString.substring(1);
    }

    //Used for editing expense/income amount in a currency format
    TextWatcher tw = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!s.toString().matches("^\\$(\\d{1,3}(\\,\\d{3})*|(\\d+))(\\.\\d{2})?$")) {
                String userInput = "" + s.toString().replaceAll("[^\\d]", "");
                StringBuilder cashAmountBuilder = new StringBuilder(userInput);

                while (cashAmountBuilder.length() > 3 && cashAmountBuilder.charAt(0) == '0') {
                    cashAmountBuilder.deleteCharAt(0);
                }
                while (cashAmountBuilder.length() < 3) {
                    cashAmountBuilder.insert(0, '0');
                }
                cashAmountBuilder.insert(cashAmountBuilder.length() - 2, '.');

                money.removeTextChangedListener(this);
                money.setText(cashAmountBuilder.toString());

                money.setTextKeepState("$" + cashAmountBuilder.toString());
                Selection.setSelection(money.getText(), cashAmountBuilder.toString().length() + 1);

                money.addTextChangedListener(this);
            }
        }
    };


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
}
