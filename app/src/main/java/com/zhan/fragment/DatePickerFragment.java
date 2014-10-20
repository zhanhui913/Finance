package com.zhan.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;

import com.zhan.finance.R;

import java.util.Calendar;

/**
 * Created by Zhan on 2014-10-13.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        Log.d("zhan","zhan have chosen a date "+day+"-"+month+"-"+year);


        Button dateButton = (Button) getActivity().findViewById(R.id.btn_selectDate);
        String date = getMonthString(month)+"-"+day+"-"+year;
        dateButton.setText(date);
    }


    /**
     * Convert an integer into an actual string
     * @param month The months integer
     * @return The actual month's string
     */
    public String getMonthString(int month){
        String stringMonth = "";
        switch (month) {
            case 0:
                stringMonth = "Jan";
                break;
            case 1:
                stringMonth = "Feb";
                break;
            case 2:
                stringMonth = "Mar";
                break;
            case 3:
                stringMonth = "Apr";
                break;
            case 4:
                stringMonth = "May";
                break;
            case 5:
                stringMonth = "Jun";
                break;
            case 6:
                stringMonth = "Jul";
                break;
            case 7:
                stringMonth = "Aug";
                break;
            case 8:
                stringMonth = "Sep";
                break;
            case 9:
                stringMonth = "Oct";
                break;
            case 10:
                stringMonth = "Nov";
                break;
            case 11:
                stringMonth = "Dec";
                break;
            default:
                break;
        }
        return stringMonth;
    }

}