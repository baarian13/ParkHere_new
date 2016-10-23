package com.lazeebear.parkhere;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class Search extends AppCompatActivity {
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private TextView search_date_textview, search_time_textview;
    private Button search_date_button, search_time_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //---local variables---//
        Calendar c;
        int year, month, day, hour, minute;

        //===Search by date===//
        search_date_textview = (TextView) findViewById(R.id.search_date_textview);
        search_date_button = (Button) findViewById(R.id.search_date_button);
        search_date_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {showDatePicker(view);}
        });
        //===Search by date: DatePicker===//
        // Use the current date as the default date in the picker
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        //create the listener for the DatePickerDialog
        DatePickerDialog.OnDateSetListener onDateSetHandler = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Update the TextView with the date chosen by the user
                search_date_textview.setText(month + "/" + day + "/" + year );
            }
        };
        datePicker = new DatePickerDialog(this, onDateSetHandler, year, month, day);

        //===Search by time===//
        search_time_textview = (TextView) findViewById(R.id.search_time_textview);
        search_time_button = (Button) findViewById(R.id.search_time_button);
        search_time_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {showTimePicker(view);}
        });
        //===Search by date: DatePicker===//
        // Use the current date as the default date in the picker
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
        //create the listener for the DatePickerDialog
        TimePickerDialog.OnTimeSetListener onTimeSetHandler = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                // Update the TextView with the date chosen by the user
                search_time_textview.setText(hour + ":" + minute);
            }
        };
        timePicker = new TimePickerDialog(this, onTimeSetHandler, hour, minute,
                DateFormat.is24HourFormat(this));
    }

    //Date Picker
    public void showDatePicker(View view){
        datePicker.show();
    }

    //Time Picker
    public void showTimePicker(View view){
        timePicker.show();
    }

}
