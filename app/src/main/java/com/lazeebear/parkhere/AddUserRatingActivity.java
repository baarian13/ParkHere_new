package com.lazeebear.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

public class AddUserRatingActivity extends AppCompatActivity {

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        Button submit = (Button) findViewById(R.id.submitReviewUser);
        //receives intent from Account class and from Spot class?
        Intent intent = getIntent();
        if (intent != null) {
            userID = intent.getStringExtra(userID);

            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    addReviewUser();
                }
            });
        }
    }

    private void addReviewUser() {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        int rate = ratingBar.getNumStars();
        //ServerConnector.addReviewUser(rate, userID);
    }
}
