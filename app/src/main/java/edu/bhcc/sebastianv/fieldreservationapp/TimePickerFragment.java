package edu.bhcc.sebastianv.fieldreservationapp;

import android.support.v4.app.DialogFragment;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by svasco on 10/10/2017.
 */

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_TIME =
            "edu.bhcc.sebastianv.fieldreservationapp.time";

    private static final String ARG_TIME = "time";

    private TimePicker mTimePicker;
    private Date time;

    //Stashing data into Timepickerfragment, stash date in timefragment bundle, where timepickerFrag can access it
    public static TimePickerFragment newInstance(Date date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME, date);
        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);
        return timePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        time = (Date) getArguments().getSerializable(ARG_TIME);

        //To show current time on widget, time needs to be converted into ints hour and min
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null); //inflate the view

        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker); //set
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(min);

        //When OK is pressed, save the selected time into ints and convert them Date class
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override  //Send result to crimeFragment
                            public void onClick(DialogInterface dialog, int which) {
                                int hour = mTimePicker.getCurrentHour();
                                int min = mTimePicker.getCurrentMinute();

                                Calendar calendar1=Calendar.getInstance();
                                calendar1.setTime(time);
                                calendar1.set(Calendar.HOUR_OF_DAY,hour);
                                calendar1.set(Calendar.MINUTE,min);

                                time = calendar1.getTime();
                                sendResult(Activity.RESULT_OK, time);
                            }
                        })
                .create();
    }

    private void sendResult(int resultCode, Date time) { //Send result back to crimeFragment
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, time);

        getTargetFragment()
                .onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
