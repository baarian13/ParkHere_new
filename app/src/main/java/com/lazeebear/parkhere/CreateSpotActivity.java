package com.lazeebear.parkhere;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class CreateSpotActivity extends AppCompatActivity {
    private TextView start_date, end_date, start_hour,
        end_hour, price_field, description_field;
    private CheckBox repeat_weekly_checkbox, covered_checkbox;
    private Button upload_photo_button, submit_button;
    private DatePickerDialog startDatePicker, endDatePicker;
    private TimePickerDialog startTimePicker, endTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //===variables===//
        start_date = findViewById(R.id.create_spot_start_date);
        end_date = findViewById(R.id.create_spot_end_date);
        start_hour = findViewById(R.id.create_spot_start_hour);
        end_hour = findViewById(R.id.create_spot_end_hour);
        price_field = findViewById(R.id.create_spot_price_field);
        description_field = findViewById(R.id.create_spot_description_field);

        repeat_weekly_checkbox = findViewById(R.id.create_spot_repeat_weekly_checkbox);
        covered_checkbox = findViewById(R.id.create_spot_covered_checkbox);

        upload_photo_button = findViewById(R.id.upload_photo_button);
        submit_button = findViewById(R.id.create_spot_submit_button);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //===StartDatePicker===//
        start_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {startDatePicker.show();}
        });
        DatePickerDialog.OnDateSetListener onStartDateSetHandler = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Update the TextView with the date chosen by the user
                start_date.setText(month + "/" + day + "/" + year );
            }
        };
        startDatePicker = new DatePickerDialog(this, onStartDateSetHandler, year, month, day);

        //===EndDatePicker===//
        end_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {startDatePicker.show();}
        });
        DatePickerDialog.OnDateSetListener onEndDateSetHandler = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Update the TextView with the date chosen by the user
                end_date.setText(month + "/" + day + "/" + year );
            }
        };
        endDatePicker = new DatePickerDialog(this, onEndDateSetHandler, year, month, day);

        //===Search by time===//
        start_hour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {startTimePicker.show();};
        });
        TimePickerDialog.OnTimeSetListener onStartTimeSetHandler =new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                // Update the TextView with the date chosen by the user
                start_hour.setText(hour + ":" + minute);
            }
        };
        startTimePicker = new TimePickerDialog(this, onStartTimeSetHandler, hour, minute,
                DateFormat.is24HourFormat(this));

        //===End Hour Picker===//
        end_hour.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {endTimePicker.show();};
        });
        TimePickerDialog.OnTimeSetListener onEndTimeSetHandler =new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                // Update the TextView with the date chosen by the user
                end_hour.setText(hour + ":" + minute);
            }
        };
        endTimePicker = new TimePickerDialog(this, onEndTimeSetHandler, hour, minute,
                DateFormat.is24HourFormat(this));
    }
}