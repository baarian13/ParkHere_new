package com.lazeebear.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDetailsDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

public class AddSpotRatingActivity extends AppCompatActivity {

    private int spotID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spot_rating);
        Button submit = (Button) findViewById(R.id.submitReviewUser);

        //get intent from spot
        Intent intent = getIntent();
        if (intent != null) {
            spotID = Integer.parseInt(intent.getStringExtra("id"));
            fillInformation();
            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    addRatingSpot();
                }
            });
        }
    }

    private void fillInformation(){
        try {
            SpotDetailsDAO spot = ServerConnector.spotDetails(spotID);
            TextView addressTextView = (TextView) findViewById(R.id.address_addSpotRating);
            addressTextView.setText(spot.getAddress());
        } catch (Exception e){
            Log.i("ERROR", "Exception while getting user details after successful login");
        }
    }

    private void addRatingSpot() {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        int rate = ratingBar.getNumStars();
        //ServerConnector.addReviewSpot(rate, spotID);
    }
}
