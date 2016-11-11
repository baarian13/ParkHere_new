package com.lazeebear.parkhere;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.ReturnedUserDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDetailsDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

/**
 * An activity representing a single Spot detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SpotListActivity}.
 */
public class SpotDetailActivity extends AppCompatActivity {

    private int spotID;
    private String userUniqueID;
    private boolean isSpotOwner = false;

    private boolean editPriceOpen = true;
    private boolean editCancellationPolicyOpen = true;
    //for resetting the description when the user closes (not confirms) editor
    private int defaultCancellationPolicyIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_detail);
        Intent intent = getIntent();
        if (intent != null) {
            spotID = Integer.parseInt(intent.getStringExtra("id"));
            System.out.println("SpotDetail spot id: " + spotID);
            setInformation();
            hideInformation();
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
            Button editPriceButton = (Button) findViewById(R.id.editPriceButton_spotDetail);
            editPriceButton.setVisibility(View.GONE);
        }

        hideEditPrice();
        hideEditCancellationPolicy();
    }

    private void setInformation(){
        try {
            SpotDetailsDAO spot = ServerConnector.spotDetails(spotID);
            userUniqueID = spot.getOwnerEmail();
            //TO-DO
            int rating = 3;
            double price = 10.00;
            ReturnedUserDAO userInfo = ServerConnector.userDetails(userUniqueID);
            String firstName = userInfo.getFirst();
            int cancellationIndex = spot.getCancelationPolicy();

            isSpotOwner = ServerConnector.checkUser(userUniqueID);

            TextView addressField = (TextView) findViewById(R.id.address_spotDetail);
            addressField.setText(spot.getAddress());
            RatingBar spotRating = (RatingBar) findViewById(R.id.rating_spot_spotDetail);
            spotRating.setRating(rating);
            TextView priceTextView = (TextView) findViewById(R.id.price_spotDetail);
            priceTextView.setText("$" + price);
            EditText priceEditText = (EditText) findViewById(R.id.priceEditText_spotDetail);
            priceEditText.setText(price + "");
            TextView owner = (TextView) findViewById(R.id.owner_spotDetail);
            owner.setText(firstName);

            Resources res = getResources();
            TextView cancellationPolicyName = (TextView) findViewById(R.id.cancellation_policy_spotDetail);
            String[] nameOptions = res.getStringArray(R.array.cancellation_policy);
            cancellationPolicyName.setText("Cancellation Policy   " + nameOptions[cancellationIndex]);
            TextView cancellationPolicyDescription = (TextView) findViewById(R.id.cancellation_policy_description_spotDetail);
            String[] descriptionOptions = res.getStringArray(R.array.cancellation_policy_description);
            String description = descriptionOptions[cancellationIndex];
            cancellationPolicyDescription.setText(description);
            Spinner cancellationPolicySpinner = (Spinner) findViewById(R.id.cancellationPolicySpinner_spotDetail);
            cancellationPolicySpinner.setSelection(cancellationIndex);
            defaultCancellationPolicyIndex = cancellationIndex;
        } catch (Exception e){
            Log.i("ERROR", "Exception while getting spot details creating spotDetail view");
        }
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
        Intent intent = new Intent(this, AddSpotRatingActivity.class);
        //putExtra needs string??
        intent.putExtra("id", spotID+"");
        startActivity(intent);
    }

    private void deleteSpot() {
        //delete spot first
        ServerConnector.deleteSpot(spotID);
        //then redirect to account page
        Intent intent = new Intent(this, Account.class);
        intent.putExtra("id",userUniqueID);
        startActivity(intent);
    }

    //edit price methods
    private void toggleEditPrice(){
        if (editPriceOpen){
            hideEditPrice();
            editPriceOpen = false;
        } else {
            showEditPrice();
            editPriceOpen = true;
        }
    }

    private void hideEditPrice(){
        EditText priceEditText = (EditText) findViewById(R.id.priceEditText_spotDetail);
        priceEditText.setVisibility(View.GONE);
        Button priceConfirmButton = (Button) findViewById(R.id.confirmPriceButton_spotDetail);
        priceConfirmButton.setVisibility(View.GONE);
        TextView priceTextView = (TextView) findViewById(R.id.price_spotDetail);
        priceTextView.setVisibility(View.VISIBLE);

        editPriceOpen = false;
    }

    private void showEditPrice(){
        EditText priceEditText = (EditText) findViewById(R.id.priceEditText_spotDetail);
        priceEditText.setVisibility(View.VISIBLE);
        Button priceConfirmButton = (Button) findViewById(R.id.confirmPriceButton_spotDetail);
        priceConfirmButton.setVisibility(View.VISIBLE);
        TextView priceTextView = (TextView) findViewById(R.id.price_spotDetail);
        priceTextView.setVisibility(View.GONE);
        editPriceOpen = true;
    }

    private void confirmPrice(){
        EditText priceEditText = (EditText) findViewById(R.id.priceEditText_spotDetail);
        String price = priceEditText.getText().toString();
        //update price
        SpotDetailsDAO updatedSpot = new SpotDetailsDAO();
        //ServerConnector.modifySpot(updateSpot);
        refreshView();

    }

    //cancellation policy editors
    private void toggleEditCancellationPolicy() {
        if (editCancellationPolicyOpen) {
            hideEditCancellationPolicy();
        } else {
            showEditCancellationPolicy();
        }
    }

    private void hideEditCancellationPolicy(){
        Spinner cancellationPolicySpinner = (Spinner) findViewById(R.id.cancellationPolicySpinner_spotDetail);
        cancellationPolicySpinner.setVisibility(View.GONE);
        Button cancellationConfirmButton = (Button) findViewById(R.id.confirmCancellationPolicy_spotDetail);
        cancellationConfirmButton.setVisibility(View.GONE);
        TextView cancellationPolicyTextView = (TextView) findViewById(R.id.cancellation_policy_spotDetail);
        cancellationPolicyTextView.setVisibility(View.VISIBLE);
        //reset description
        Resources res = getResources();
        TextView cancellationPolicyDescription = (TextView) findViewById(R.id.cancellation_policy_description_spotDetail);
        String[] descriptionOptions = res.getStringArray(R.array.cancellation_policy_description);
        String description = descriptionOptions[defaultCancellationPolicyIndex];
        cancellationPolicyDescription.setText(description);
        //also change the spinner back to default for when they decide to click edit again
        cancellationPolicySpinner.setSelection(defaultCancellationPolicyIndex);
        editCancellationPolicyOpen = false;
    }

    private void showEditCancellationPolicy(){
        Spinner cancellationPolicySpinner = (Spinner) findViewById(R.id.cancellationPolicySpinner_spotDetail);
        cancellationPolicySpinner.setVisibility(View.VISIBLE);
        Button cancellationConfirmButton = (Button) findViewById(R.id.confirmCancellationPolicy_spotDetail);
        cancellationConfirmButton.setVisibility(View.VISIBLE);
        TextView cancellationPolicyTextView = (TextView) findViewById(R.id.cancellation_policy_spotDetail);
        cancellationPolicyTextView.setVisibility(View.GONE);
        editCancellationPolicyOpen = true;
    }

    private void confirmCancellationPolicy(){
        Spinner cancellationPolicySpinner = (Spinner) findViewById(R.id.cancellationPolicySpinner_spotDetail);
        int index = cancellationPolicySpinner.getSelectedItemPosition();

        SpotDetailsDAO updatedSpot = new SpotDetailsDAO();
        //ServerConnector.modifySpot(updateSpot);

        refreshView();
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

        Button editPriceButton = (Button) findViewById(R.id.editPriceButton_spotDetail);
        editPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleEditPrice();
            }
        });

        Button editCancellationPolicyButton = (Button) findViewById(R.id.editCancellationPolicy_spotDetail);
        editCancellationPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleEditCancellationPolicy();
            }
        });

        Button confirmPriceButton = (Button) findViewById(R.id.confirmPriceButton_spotDetail);
        confirmPriceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmPrice();
            }
        });

        Button confirmCancellationPolicyButton = (Button) findViewById(R.id.confirmCancellationPolicy_spotDetail);
        confirmCancellationPolicyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmCancellationPolicy();
            }
        });

        Spinner cancellationPolicySpinner = (Spinner) findViewById(R.id.cancellationPolicySpinner_spotDetail);
        cancellationPolicySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                TextView cancellationPolicyDescription = (TextView) findViewById(R.id.cancellation_policy_description_spotDetail);
                String[] descriptionOptions = res.getStringArray(R.array.cancellation_policy_description);
                int selectionPosition = parent.getSelectedItemPosition();
                String description = descriptionOptions[selectionPosition];
                cancellationPolicyDescription.setText(description);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });



    }

    private void refreshView(){
        Intent intent = new Intent(this, SpotDetailActivity.class);
        intent.putExtra("id", spotID+"");
        startActivity(intent);
    }

    private boolean isSpotOwner(){
        return isSpotOwner;
    }
}
