package com.lazeebear.parkhere;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

public class Search extends AppCompatActivity {
    private DatePickerDialog datePicker;
    private TimePickerDialog timePicker;
    private TextView search_date_textview, search_time_textview;
    private Button search_date_button, search_time_button;
    private Calendar c;
    private int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //===Search by date===//
        search_date_textview = (TextView) findViewById(R.id.search_date_textview);
        search_date_button = (Button) findViewById(R.id.search_date_button);
        search_date_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {showDatePicker(view);}
        });
        createDatePicker();

        //===Search by time===//
        search_time_textview = (TextView) findViewById(R.id.search_time_textview);
        search_time_button = (Button) findViewById(R.id.search_time_button);
        search_time_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {showTimePicker(view);}
        });
       createTimePicker();

        Button searchButton = (Button) findViewById(R.id.search_submit_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                search();
            }
        });
    }

    private void createDatePicker(){
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
    }

    private void createTimePicker() {
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

    private void search(){
        TextView address = (TextView) findViewById(R.id.address);
        String addressString = (String)address.getText();
        List<SpotDAO> spots = ServerConnector.SearchSpotTask(addressString);
        Intent intent = new Intent(this, SpotListActivity.class);
        for (int i=0; i< spots.size(); i++) {
            intent.putExtra("address"+i, spots.get(i).getAddress() + ":" + spots.get(i).getId());
        }
        startActivity(intent);
    }

}
