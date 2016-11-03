package com.lazeebear.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class AddSpotRatingActivity extends AppCompatActivity {

    private String spotID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot_rating);
        Button submit = (Button) findViewById(R.id.submitReviewUser);

        //get intent from spot
        Intent intent = getIntent();
        if (intent != null) {
            spotID = intent.getStringExtra(spotID);

            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    addReviewSpot();
                }
            });
        }
    }

    private void addReviewSpot() {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        int rate = ratingBar.getNumStars();
        //ServerConnector.addReviewSpot(rate, spotID);
    }
}
