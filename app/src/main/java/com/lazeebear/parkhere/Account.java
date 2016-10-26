package com.lazeebear.parkhere;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

/**
 * Created by Zhicheng on 10/22/2016.
 */

public class Account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //hideComponents();
    }
    private void hideComponents() {
        Button createSpotButton = (Button)findViewById(R.id.createSpotButton);
        if (!isOwner())
            createSpotButton.setVisibility(View.GONE);

        ScrollView spotHistoryList = (ScrollView) findViewById(R.id.spotHistoryList);
        if (!isSeeker())
            spotHistoryList.setVisibility(View.GONE);

        ScrollView ownedSpotsList = (ScrollView) findViewById(R.id.ownedSpotsList);
        if (!isOwner())
            ownedSpotsList.setVisibility(View.GONE);
    }

    private boolean isSeeker() {
        return true;
    }

    private boolean isOwner() {
        return true;
    }
}
