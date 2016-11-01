package com.lazeebear.parkhere;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Zhicheng on 10/22/2016.
 */

public class Account extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        hideComponentsOnStartUp();
        fillInformationOnStartUp();
        addActionListeners();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void hideComponentsOnStartUp() {
        Button createSpotButton = (Button) findViewById(R.id.createSpotButton);
        if (!isViewingOwnAccount())
            createSpotButton.setVisibility(View.GONE);
        else if (!isOwner())
            createSpotButton.setVisibility(View.GONE);

        Button searchSpotButton = (Button) findViewById(R.id.searchSpotButton_account);
        if (isViewingOwnAccount()) {
            if (!isSeeker())
                searchSpotButton.setVisibility(View.GONE);
        } else
            searchSpotButton.setVisibility(View.GONE);

        LinearLayout spotHistoryList = (LinearLayout) findViewById(R.id.spotHistoryList);
        Button spotHistoryButton = (Button) findViewById(R.id.spotHistoryButton);
        if (!isViewingOwnAccount()) {
            spotHistoryList.setVisibility(View.GONE);
            spotHistoryButton.setVisibility(View.GONE);
        } else if (!isSeeker()) {
            spotHistoryList.setVisibility(View.GONE);
            spotHistoryButton.setVisibility(View.GONE);
        }

        LinearLayout ownedSpotsList = (LinearLayout) findViewById(R.id.ownedSpotsList);
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
        RatingBar ratingOfUser = (RatingBar) findViewById(R.id.ratingBar);
        ratingOfUser.setNumStars(3);

        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        phoneNumber.setText("9494994949");

        TextView email = (TextView) findViewById(R.id.email);
        email.setText("jimmy@aol.com");
    }


    private void populateSpotsHistory() {
        LinearLayout list = (LinearLayout) findViewById(R.id.ownedSpotsList);
        for (int i = 0; i < 5; i++) {
            Button spotButton = createSpotsHistoryButton("Address " + i);
            list.addView(spotButton);
        }
    }

    private Button createSpotsHistoryButton(String address) {
        Button button = new Button(this);
        button.setText(address);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });

        return button;
    }

    private void addActionListeners() {
        addOwnedSpotsButtonActionListener();
        addSpotHistoryButtonActionListener();
        addSearchSpotButtonActionListener();
        addCreateSpotButtonActionListener();
    }

    private void addOwnedSpotsButtonActionListener() {
        Button ownedSpotsButton = (Button) findViewById(R.id.ownedSpotsButton);
        ownedSpotsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                populateOwnedSpots();
            }
        });
    }

    private void populateOwnedSpots() {
        LinearLayout list = (LinearLayout) findViewById(R.id.ownedSpotsList);
        for (int i=0; i<5; i++) {
            Button spotButton = createOwnedSpotButton("Address " + i);
            list.addView(spotButton);
        }
    }

    private Button createOwnedSpotButton(String address) {
        Button button = new Button(this);
        button.setText(address);
        button.setOnClickListener( new View.OnClickListener(){
            public void onClick(View view){

            }
        });

        return button;
    }

    private void addSpotHistoryButtonActionListener() {
        Button ownedSpotsButton = (Button) findViewById(R.id.ownedSpotsButton);
        ownedSpotsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                populateSpotsHistory();
            }
        });
    }

    private void addSearchSpotButtonActionListener() {
        Button searchSpotsButton = (Button) findViewById((R.id.searchSpotButton_account));
        searchSpotsButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                goToSearchSpots();
            }
        }));
    }

    private void goToSearchSpots() {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    private void addCreateSpotButtonActionListener() {
        Button createSpotButton = (Button) findViewById((R.id.createSpotButton));
        createSpotButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                goToCreateSpot();
            }
        }));
    }

    private void goToCreateSpot() {
        Intent intent = new Intent(this, CreateSpotActivity.class);
        startActivity(intent);
    }

    private boolean isSeeker() {
        return true;
    }

    private boolean isOwner() {
        return false;
    }

    private boolean isViewingOwnAccount() {
        return true;
    }

    private String getDisplayName() {
        if (isViewingOwnAccount())
            return "First and Last Name";
        else
            return "First Name Only";
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Account Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
