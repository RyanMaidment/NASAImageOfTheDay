package com.example.nasaimageoftheday;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public DatePickerDialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        //get the date from picker
        DatePickerDialog selecteddate = new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);
        // nothing in the future - not available - this limits choices to today or past
        DatePicker maxdate = selecteddate.getDatePicker();
        Calendar c  = Calendar.getInstance();
        maxdate.setMaxDate(c.getTimeInMillis());
        return selecteddate;
    }
}
