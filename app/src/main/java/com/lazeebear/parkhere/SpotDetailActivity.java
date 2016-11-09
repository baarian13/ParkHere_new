package com.lazeebear.parkhere;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * An activity representing a single Spot detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SpotListActivity}.
 */
public class SpotDetailActivity extends AppCompatActivity {

    private int spotID;
    private String userUniqueID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        Intent intent = getIntent();
        if (intent != null) {
            spotID = Integer.parseInt(intent.getStringExtra("id"));
            hideInformation();
            setInformation();
            setActionListeners();
        }

    }

    private void hideInformation(){
        if (isSpotOwner()){
            Button reserveSpotButton = (Button) findViewById(R.id.reserveButton_spotDetail);
            reserveSpotButton.setVisibility(View.GONE);
            Button rateUserButton = (Button) findViewById(R.id.rateUserButton_spotDetail);
            rateUserButton.setVisibility(View.GONE);
            Button rateSpotButton = (Button) findViewById(R.id.rateSpotButton_spotDetail);
            rateSpotButton.setVisibility(View.GONE);
        } else {
            Button deleteSpotButton = (Button) findViewById(R.id.deleteSpotButton_spotDetail);
            deleteSpotButton.setVisibility(View.GONE);
            Button editSpotButton = (Button) findViewById(R.id.editSpotButton_spotDetail);
            editSpotButton.setVisibility(View.GONE);
        }
    }

    private void setInformation(){
        userUniqueID = "email";

        TextView addressField = (TextView) findViewById(R.id.address_spotDetail);
        addressField.setText(spotID+"");
        RatingBar spotRating = (RatingBar) findViewById(R.id.rating_spot_spotDetail);
        spotRating.setRating(3);
        TextView price = (TextView) findViewById(R.id.price_spotDetail);
        price.setText("$" + 2.00);
        TextView owner = (TextView) findViewById(R.id.owner_spotDetail);
        owner.setText("First" + " " + "Last");
        int cancellationIndex = 1;
        Resources res = getResources();
        TextView cancellationPolicyName = (TextView) findViewById(R.id.cancellation_policy_spotDetail);
        String[] nameOptions = res.getStringArray(R.array.cancellation_policy);
        cancellationPolicyName.setText("Cancellation Policy   " + nameOptions[cancellationIndex]);
        TextView cancellationPolicyDescription = (TextView) findViewById(R.id.cancellation_policy_description_spotDetail);
        String[] descriptionOptions = res.getStringArray(R.array.cancellation_policy_description);
        String description = descriptionOptions[cancellationIndex];
        cancellationPolicyDescription.setText(description);

    }


    private void openReserveSpot() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("id", spotID);
        startActivity(intent);
    }

    private void openRateUser() {
        Intent intent = new Intent(this, AddUserRatingActivity.class);
        intent.putExtra("id", userUniqueID);
        startActivity(intent);
    }

    private void openRateSpot() {
        Intent intent = new Intent(this, AddUserRatingActivity.class);
        //putExtra needs string??
        intent.putExtra("id", spotID+"");
        startActivity(intent);
    }

    private void deleteSpot() {
        //delete spot first
        //then redirect to account page
        Intent intent = new Intent(this, Account.class);
        intent.putExtra("id",userUniqueID);
        startActivity(intent);
    }

    private void toggleEditForm(){

    }

    private void setActionListeners(){
        Button reserveSpotButton = (Button) findViewById(R.id.reserveButton_spotDetail);
        reserveSpotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openReserveSpot();
            }
        });

        Button rateUserButton = (Button) findViewById(R.id.rateUserButton_spotDetail);
        rateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRateUser();
            }
        });

        Button rateSpotButton = (Button) findViewById(R.id.rateSpotButton_spotDetail);
        rateSpotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRateSpot();
            }
        });

        Button deleteSpotButton = (Button) findViewById(R.id.deleteSpotButton_spotDetail);
        deleteSpotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSpot();
            }
        });

        Button editSpotButton = (Button) findViewById(R.id.editSpotButton_spotDetail);
        editSpotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleEditForm();
            }
        });
    }

    //TODO
    private boolean isSpotOwner(){
        return false;
    }
}
