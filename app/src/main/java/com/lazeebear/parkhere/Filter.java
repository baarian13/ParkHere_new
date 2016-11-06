package com.lazeebear.parkhere;

import android.content.Intent;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class Filter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Intent intent = getIntent();
        //still not sure what I will need to filter

        if (intent != null){
            addActionListeners();
        }
    }

    private void addActionListeners(){
        Button submitButton = (Button) findViewById(R.id.filter_button_filter);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                filter();
            }
        });
    }

    private void filter(){
        Spinner spot_lower_spinner = (Spinner) findViewById(R.id.spot_rating_lower);
        Spinner spot_upper_spinner = (Spinner) findViewById(R.id.spot_rating_upper);
        Spinner owner_lower_spinner = (Spinner) findViewById(R.id.owner_rating_lower);
        Spinner owner_upper_spinner = (Spinner) findViewById(R.id.owner_rating_upper);

        int spotRatingLower = Integer.parseInt(spot_lower_spinner.getSelectedItem().toString());
        int spotRatingUpper = Integer.parseInt(spot_upper_spinner.getSelectedItem().toString());
        int ownerRatingLower = Integer.parseInt(owner_lower_spinner.getSelectedItem().toString());
        int ownerRatingUpper = Integer.parseInt(owner_upper_spinner.getSelectedItem().toString());

        Spinner car_type_spinner = (Spinner) findViewById(R.id.car_types_filter);
        int carTypeIndex = car_type_spinner.getSelectedItemPosition();

        //filter

        //get returned results and send user to search result page
        Intent intent = new Intent(this, SpotListActivity.class);
        startActivity(intent);
    }
}
