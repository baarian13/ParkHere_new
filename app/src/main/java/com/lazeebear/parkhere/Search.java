package com.lazeebear.parkhere;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDateDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Search extends AppCompatActivity {
    private DatePickerDialog datePicker_lower, datePicker_upper;
    private TimePickerDialog timePicker_lower, timePicker_upper;
    private Button date_button_lower, date_button_upper, time_button_lower, time_button_upper;
    private Calendar c;
    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getCurrentTime();
        createDatePickers();
        createTimePickers();

        addActionListeners();
    }

    private void getCurrentTime(){
        // Use the current date as the default date in the picker
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        // Use the current date as the default date in the picker
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);
    }

    private void createDatePickers(){
        date_button_lower = (Button) findViewById(R.id.date_button_lower_search);
        date_button_upper = (Button) findViewById(R.id.date_button_upper_search);
        //===Search by date: DatePicker===//

        //create the listener for the DatePickerDialog
        //first the lower
        DatePickerDialog.OnDateSetListener onDateSetHandlerLower = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Update the TextView with the date chosen by the user
                if (canChangeDateButtonLower(year, month, day))
                    date_button_lower.setText(formatDate(year, month, day));
            }
        };
        datePicker_lower = new DatePickerDialog(this, onDateSetHandlerLower, year, month, day);
        date_button_lower.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {showDatePickerLower();}
        });

        //now upper
        DatePickerDialog.OnDateSetListener onDateSetHandlerUpper = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // Update the TextView with the date chosen by the user
                if (canChangeDateButtonUpper(year, month, day))
                    date_button_upper.setText(formatDate(year,month,day));
            }
        };
        datePicker_upper = new DatePickerDialog(this, onDateSetHandlerUpper, year, month, day);
        date_button_upper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {showDatePickerUpper();}
        });
    }

    private boolean canChangeDateButtonLower(int year, int month, int day){
        //assuming createDatePicker populates date_button_upper
        String upperString = date_button_upper.getText().toString();
        if (upperString.equals(R.string.select))
            return true;
        String lowerString = formatDate(year, month, day);
        //s1.compareTo(s2)
        //compareTo returns + if s1 > s2
        // - if s2 > s1
        if (lowerString.compareTo(upperString) > 0) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean canChangeDateButtonUpper(int year, int month, int day){
        //assuming createDatePicker populates date_button_upper
        String lowerString = date_button_lower.getText().toString();
        if (lowerString.equals(R.string.select))
            return true;
        String upperString = formatDate(year, month, day);
        //s1.compareTo(s2)
        //compareTo returns + if s1 > s2
        // - if s2 > s1
        if (lowerString.compareTo(upperString) > 0)
            return false;
        else
            return true;

    }

    private String formatDate(int year, int month, int day){
        String monthStr = month + "";
        String dayStr = day + "";
        if (month < 10){
            monthStr = "0" + monthStr;
        }

        if (day < 10){
            dayStr = "0" + dayStr;
        }
        return year + "-" + monthStr + "-" + dayStr;
    }

    //Date Picker
    private void showDatePickerLower(){
        datePicker_lower.show();
    }
    private void showDatePickerUpper(){ datePicker_upper.show();}


    //
    //time picker
    private void createTimePickers() {
        //===Search by date: DatePicker===//
        time_button_lower = (Button) findViewById(R.id.time_button_lower_search);
        time_button_upper = (Button) findViewById(R.id.time_button_upper_search);
        //create the listener for the DatePickerDialog
        TimePickerDialog.OnTimeSetListener onTimeSetHandlerLower = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                // Update the TextView with the date chosen by the user
                time_button_lower.setText(formatTime(hour, minute));
            }
        };
        timePicker_lower = new TimePickerDialog(this, onTimeSetHandlerLower, hour, minute,
                DateFormat.is24HourFormat(this));

        time_button_lower.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {showTimePickerLower(view);}
        });

        TimePickerDialog.OnTimeSetListener onTimeSetHandlerUpper = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                // Update the TextView with the date chosen by the user
                time_button_upper.setText(formatTime(hour, minute));
            }
        };
        timePicker_upper = new TimePickerDialog(this, onTimeSetHandlerUpper, hour, minute,
                DateFormat.is24HourFormat(this));

        time_button_upper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {showTimePickerUpper(view);}
        });
    }

    private String formatTime(int hour, int minute){
        String hourStr = hour + "";
        String minuteStr = minute + "";
        if (hour < 10){
            hourStr = "0" + hourStr;
        }
        if (minute < 10){
            minuteStr = "0" + minuteStr;
        }
        return hourStr + ":" + minuteStr;
    }

    //Time Picker
    private void showTimePickerLower(View view){
        timePicker_lower.show();
    }
    private void showTimePickerUpper(View view){
        timePicker_upper.show();
    }

    private void search(){
        TextView address = (TextView) findViewById(R.id.address);
        String addressString = address.getText().toString();
        String lowerDate = date_button_lower.getText().toString();
        String lowerTime = time_button_lower.getText().toString();
        String lowerDateTime = formatDateTime(lowerDate, lowerTime);
        String upperDate = date_button_upper.getText().toString();
        String upperTime = time_button_upper.getText().toString();
        String upperDateTime = formatDateTime(upperDate, upperTime);

        System.out.println("search_page lowerDateTime: " + lowerDateTime);
        System.out.println("search_page upperDateTime: " + upperDateTime);

        List<SpotDateDAO> spots = null; //List<SpotDateDAO> spots = null;
        try {
//            spots = ServerConnector.searchSpot(addressString);
            spots = ServerConnector.searchSpotDate(lowerDateTime, upperDateTime);
        } catch (Exception e) {
            Log.i("ERROR", "Exception while getting spot list during search");
        }
        Intent intent = new Intent(this, SpotListActivity.class);
        ArrayList<Integer> spotIDs = new ArrayList<>();
        //too lazy to create a pair class..
        ArrayList<String> spotAddresses = new ArrayList<>();
        int size = spots.size();
        for (int i=0; i< size; i++) {
            spotIDs.add(spots.get(i).getId());
            spotAddresses.add(spots.get(i).getAddress());
            System.out.println("Adding spot ID " + spots.get(i).getId() + " to the intent from Search to SpotList");
        }
        intent.putExtra("ids",spotIDs);
        intent.putExtra("addresses",spotAddresses);
        intent.putExtra("lowerDateTime", lowerDateTime);
        intent.putExtra("upperDateTime", upperDateTime);
        startActivity(intent);
    }

    //lower = 1
    //upper = 2
    private String formatDateTime(String date, String time){
        return date + " " + time + ":00";
    }

    private void addActionListeners(){

        Button searchButton = (Button) findViewById(R.id.search_submit_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                search();
            }
        });
    }
}
