package com.lazeebear.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.lazeebear.parkhere.ServerConnector.ServerConnector;

public class AddSpotReviewActivity extends AppCompatActivity {

    private String spotID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot_review);
        Button submit = (Button) findViewById(R.id.submitReviewUser);

        //get intent from spot
        Intent intent = getIntent();
        spotID = intent.getStringExtra(spotID);

        submit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                addReviewSpot();
            }
        });
    }

    private void addReviewSpot() {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        int rate = ratingBar.getNumStars();
        ServerConnector.addReviewSpot(rate, spotID);
    }
}
