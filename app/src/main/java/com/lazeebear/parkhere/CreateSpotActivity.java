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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class CreateSpotActivity extends AppCompatActivity {
    private TextView address, start_date, end_date, start_hour,
        end_hour, price_field, description_field;
    private CheckBox repeat_weekly_checkbox, covered_checkbox;
    private Button upload_photo_button, submit_button;
    private DatePickerDialog startDatePicker, endDatePicker;
    private TimePickerDialog startTimePicker, endTimePicker;
    private int year, month, day, hour, minute;
    public static final int GET_FROM_GALLERY = 3; //request code for opening the gallery

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_spot);


        //===variables===//
        address = (TextView) findViewById(R.id.addressCreateSpot);
        start_date = (TextView) findViewById(R.id.create_spot_start_date);
        end_date = (TextView) findViewById(R.id.create_spot_end_date);
        start_hour = (TextView) findViewById(R.id.create_spot_start_hour);
        end_hour = (TextView) findViewById(R.id.create_spot_end_hour);
        price_field = (TextView) findViewById(R.id.create_spot_price_field);
        description_field = (TextView) findViewById(R.id.create_spot_description_field);

        repeat_weekly_checkbox = (CheckBox) findViewById(R.id.create_spot_repeat_weekly_checkbox);
        covered_checkbox = (CheckBox) findViewById(R.id.create_spot_covered_checkbox);

        upload_photo_button = (Button) findViewById(R.id.upload_photo_button);
        submit_button = (Button) findViewById(R.id.create_spot_submit_button);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        setActionListeners();
    }

    private void createPickers(){
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
                // Update the TextView with the date chosen byint  the user
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


    private void setActionListeners(){
        setCancellationPolicyListener();
        createPickers();
        setUploadButtonListener();
        setSubmitButtonListener();
    }

    private void setCancellationPolicyListener(){
        Spinner cancellationPolicySelect = (Spinner) findViewById(R.id.cancellation_policy_selection);
        cancellationPolicySelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                String[] options = res.getStringArray(R.array.cancellation_policy);
                TextView description = (TextView) findViewById(R.id.cancellation_policy_description);
                if ( parent.getSelectedItem().equals(options[0]) ){ //flexible
                    description.setText(getString(R.string.flexible_option));
                } else if  ( parent.getSelectedItem().equals(options[1]) ) { //moderate
                    description.setText(getString(R.string.moderate_option));
                } else if ( parent.getSelectedItem().equals(options[2]) ) { //strict
                    description.setText(getString(R.string.strict_option));
                }
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
        String addressString = (String)address.getText();

        String startTime = (String)start_hour.getText();
        String endTime = (String)end_hour.getText();
        String startDate = (String)start_date.getText();
        String endDate = (String)end_date.getText();
        //pretty sure THIS ISN'T THE RIGHTWAY to concatenate these strings///
        //but just pushing so we can test
        String startString = startDate + startTime;
        String endString = endDate + endTime;
        double price = Double.parseDouble((String)price_field.getText());
        String description = (String)description_field.getText();
        String cancellation = ((Spinner) findViewById(R.id.cancellation_policy_selection)).getSelectedItemPosition() + "";
        String isCovered = "";
        if (covered_checkbox.isChecked())
            isCovered = "1";
        else
            isCovered = "0";
        String spot_type = ((Spinner) findViewById(R.id.spot_type)).getSelectedItemPosition() + "";

        //send data
    }

    private void setSubmitButtonListener(){
        Button submitButton = (Button) findViewById(R.id.create_spot_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createSpot();
            }
        });
    }
}
