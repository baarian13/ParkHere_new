package com.lazeebear.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.ReturnedUserDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

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
            userID = intent.getStringExtra("id");
            fillInformation();
            submit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    addRatingUser();
                }
            });
        }
    }

    private void fillInformation(){
        try {
            ReturnedUserDAO user = ServerConnector.userDetails(userID);
            TextView nameTextView = (TextView) findViewById(R.id.name_addUserRating);
            nameTextView.setText(user.getFirst());
        } catch (Exception e){
            Log.i("ERROR", "Exception while getting user details after successful login");
        }
    }

    private void addRatingUser() {
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating);
        int rating = ratingBar.getNumStars();
        int returnCode = ServerConnector.rateUser(userID, rating);
        if (returnCode == 200){
            //return user to the account page of the user he rated
            Intent intent = new Intent(this, Account.class);
            intent.putExtra("id",userID);
            startActivity(intent);
        }
    }
}
