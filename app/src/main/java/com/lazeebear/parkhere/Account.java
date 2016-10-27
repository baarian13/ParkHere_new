package com.lazeebear.parkhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * Created by Zhicheng on 10/22/2016.
 */

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        hideComponentsOnStartUp();
        fillInformationOnStartUp();
    }

    private void hideComponentsOnStartUp() {
        Button createSpotButton = (Button)findViewById(R.id.createSpotButton);
        if (!isViewingOwnAccount())
            createSpotButton.setVisibility(View.GONE);
        else if (!isOwner())
            createSpotButton.setVisibility(View.GONE);

        Button searchSpotButton = (Button)findViewById(R.id.searchSpotButton_account);
        if (isViewingOwnAccount()) {
            if (!isSeeker())
                searchSpotButton.setVisibility(View.GONE);
        }
        else
            searchSpotButton.setVisibility(View.GONE);

        ScrollView spotHistoryList = (ScrollView) findViewById(R.id.spotHistoryList);
        Button spotHistoryButton = (Button) findViewById(R.id.spotHistoryButton);
        if (!isViewingOwnAccount()){
            spotHistoryList.setVisibility(View.GONE);
            spotHistoryButton.setVisibility(View.GONE);
        }
        else if (!isSeeker()) {
            spotHistoryList.setVisibility(View.GONE);
            spotHistoryButton.setVisibility(View.GONE);
        }

        ScrollView ownedSpotsList = (ScrollView) findViewById(R.id.ownedSpotsList);
        Button ownedSpotsButton = (Button) findViewById(R.id.ownedSpotsButton);
        if (!isOwner()) {
            ownedSpotsList.setVisibility(View.GONE);
            ownedSpotsButton.setVisibility(View.GONE);
        }

        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        if (!isViewingOwnAccount())
            phoneNumber.setVisibility(View.GONE);

        TextView email = (TextView) findViewById(R.id.email);
        if (!isViewingOwnAccount())
            email.setVisibility(View.GONE);

    }

    private void fillInformationOnStartUp() {
        TextView accountName = (TextView) findViewById(R.id.accountName_account);
        accountName.setText(getDisplayName());
    }

    private boolean isSeeker() {
        return true;
    }

    private boolean isOwner() {
        return false;
    }

    private boolean isViewingOwnAccount() { return true; }

    private String getDisplayName() {
        if ( isViewingOwnAccount() )
            return "First and Last Name";
        else
            return "First Name Only";
    }
}
