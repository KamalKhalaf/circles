package com.circles.circlesapp.settings.ui;

/*
 * Created By mabrouk on 20/01/19
 * Testing
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.circles.circlesapp.R;

public class DatePickerFragment extends DialogFragment {
    public static final String EXTRA_DATE = "date";
    private static final String ARG_DATE = "date";
    private static String ISConstrant = "ISConstrant";
    private static String TITLE = "TITLE";
    private static DateListener listener;
    private DatePicker mDatePicker;

    public static DatePickerFragment NewInstence(String title, boolean anyDate, Date date, DateListener listeners) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE, date);
        bundle.putString(TITLE, title);
        bundle.putBoolean(ISConstrant, anyDate);
        listener = listeners;
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    //***  for sending result from date  picker ***//
    public void SendResult(int requestCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), requestCode, intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        if (date==null) date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.date_layout, null, false);
        mDatePicker = v.findViewById(R.id.date_picker);

        mDatePicker.init(year, month, day, null);
        return new AlertDialog.Builder(getActivity()).
                setView(v).
                setTitle(getArguments().getString(TITLE)).
                setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, month, day).getTime();
                                Date today = Calendar.getInstance().getTime();
                                Log.d("after", formateDate(today) + " : " + formateDate(date));
                                if (getArguments().getBoolean(ISConstrant, false)) {
                                    if (today.after(date)) {
                                        listener.date(date);
                                    } else {
                                        Toast.makeText(getActivity(), "InValid Date", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    listener.date(date);
                                }

                            }
                        }
                ).create();
    }

    private String formateDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(date);
    }

    public interface DateListener {
        void date(Date date);
    }
}
