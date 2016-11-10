package com.lazeebear.parkhere;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDetailsDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;
import com.lazeebear.parkhere.dummy.DummyContent;

import java.util.List;

/**
 * An activity representing a list of Spots. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SpotDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SpotListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot_list);
        Intent intent = getIntent();
        int spotID = Integer.parseInt(intent.getStringExtra("id"));
        populateList(spotID);
        addActionListeners();
    }

    private void populateList(int id) {
        try {
            SpotDetailsDAO spot = ServerConnector.spotDetails(id);
            LinearLayout list = (LinearLayout) findViewById(R.id.spotList);
            Button spotButton = createSpotButton(spot.getAddress());
            spotButton.setId(id);
            list.addView(spotButton);
        } catch (Exception e){
            Log.i("ERROR", "Exception while getting spot details creating spot list");
        }
    }

    private Button createSpotButton(final String address) {
        final Button button = new Button(this);
        button.setText(address);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(params);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //send to spot detail page
                goToSpotDetail(button.getId());
            }
        });

        return button;
    }

    private void goToSpotDetail(int id){
        Intent intent = new Intent(this, SpotDetailActivity.class);
        intent.putExtra("id",id+"");
        startActivity(intent);
    }

    private void addActionListeners(){
        Button filterButton = (Button) findViewById(R.id.search_filter_button_spotList);
        filterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                goToFilterPage();
            }
        });
    }
    private void goToFilterPage(){
        Intent intent = new Intent(this, Filter.class);
        startActivity(intent);
    }



}