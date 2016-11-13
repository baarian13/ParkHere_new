package com.lazeebear.parkhere;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lazeebear.parkhere.DAOs.SentObjects.SentSpotDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class CreateSpotActivity extends AppCompatActivity {
    private TextView address, price_field, description_field;
    private CheckBox repeat_weekly_checkbox, covered_checkbox;
    private Button upload_photo_button, date_button_lower, date_button_upper, time_button_lower, time_button_upper, submit_button;
    private Calendar c;
    private DatePickerDialog datePicker_lower, datePicker_upper;
    private TimePickerDialog timePicker_lower, timePicker_upper;
    private int year, month, day, hour, minute;
    public static final int GET_FROM_GALLERY = 3; //request code for opening the gallery

    //not needed for creating the spot
    //but to redirect the user back to the account page
    private String uniqueID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spot);
        getCurrentTime();
        //===variables===//
        address = (TextView) findViewById(R.id.addressCreateSpot);
        price_field = (TextView) findViewById(R.id.create_spot_price_field);
        description_field = (TextView) findViewById(R.id.create_spot_description_field);

        repeat_weekly_checkbox = (CheckBox) findViewById(R.id.create_spot_repeat_weekly_checkbox);
        covered_checkbox = (CheckBox) findViewById(R.id.create_spot_covered_checkbox);

        upload_photo_button = (Button) findViewById(R.id.upload_photo_button);
        submit_button = (Button) findViewById(R.id.create_spot_submit_button);


        Intent intent = getIntent();

        if (intent != null) {
            uniqueID = intent.getStringExtra("id");
            setActionListeners();
        }
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
        date_button_lower = (Button) findViewById(R.id.date_button_lower_create);
        date_button_upper = (Button) findViewById(R.id.date_button_upper_create);
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
        return year + "-" + month + "-" + day;
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
        time_button_lower = (Button) findViewById(R.id.time_button_lower_create);
        time_button_upper = (Button) findViewById(R.id.time_button_upper_create);
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
        return hour + ":" + minute;
    }

    //Time Picker
    private void showTimePickerLower(View view){
        timePicker_lower.show();
    }
    private void showTimePickerUpper(View view){
        timePicker_upper.show();
    }


    private void setActionListeners(){
        setCancellationPolicyListener();
        createDatePickers();
        createTimePickers();
        setUploadButtonListener();
        setSubmitButtonListener();
    }

    private void setCancellationPolicyListener(){
        Spinner cancellationPolicySelect = (Spinner) findViewById(R.id.cancellation_policy_selection);
        cancellationPolicySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                String[] optionDescriptions = res.getStringArray(R.array.cancellation_policy_description);
                TextView description = (TextView) findViewById(R.id.cancellation_policy_description);
                int selectionPosition = parent.getSelectedItemPosition();
                description.setText(optionDescriptions[selectionPosition]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });
    }

    private void setUploadButtonListener() {
        upload_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }

        });
    }

    //opens the gallery when upload_photo_button is clicked
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void createSpot(){
        String addressString = address.getText().toString();


        String price = price_field.getText().toString();
        String description = description_field.getText().toString();
        int cancellation = ((Spinner) findViewById(R.id.cancellation_policy_selection)).getSelectedItemPosition();
        boolean isCovered = true;
        if (covered_checkbox.isChecked())
            isCovered = true;
        else
            isCovered = false;
        int spot_type = ((Spinner) findViewById(R.id.spot_type)).getSelectedItemPosition();

        String startString = formatDateTime(date_button_lower.getText().toString(), time_button_lower.getText().toString());
        String endString = formatDateTime(date_button_upper.getText().toString(), time_button_upper.getText().toString());

        //send data
        SentSpotDAO newSpot = new SentSpotDAO(addressString, startString, endString, description, price, spot_type, isCovered, cancellation, false);
        ServerConnector.createSpot(newSpot);

        Intent intent = new Intent(this, Account.class);
        intent.putExtra("id",uniqueID);
        startActivity(intent);
    }

    private void setSubmitButtonListener(){
        Button submitButton = (Button) findViewById(R.id.create_spot_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createSpot();
            }
        });
    }

    private String formatDateTime(String date , String time){
        return date + " " + time + ":00";
    }
}
