package com.lazeebear.parkhere;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.lazeebear.parkhere.DAOs.SentObjects.AddressDetailsDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import java.util.ArrayList;
import java.util.List;

public class CreateAddressActivity extends AppCompatActivity {
    private String uniqueID;
    private Spinner addressSelect;
    private Button addressEnter;
    private int selectedPosition;
    private List<Integer> addresses;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addressSelect = (Spinner) findViewById(R.id.address_selection);
        addressEnter = (Button) findViewById(R.id.address_submit_button);

        Intent intent = getIntent();
        if (intent != null){
            uniqueID = intent.getStringExtra("id");
            setActionListeners();
        }
    }

    private void setActionListeners() {
        setAddressSelectionListener();
    }

    private void populateSpinnerWithAddresses() {
        try{
            addresses = ServerConnector.getAddressesOf(uniqueID);

            ArrayList<String> addressList = new ArrayList<String>();
            for (int i = 0; i < addresses.size(); i++) {
                AddressDetailsDAO details = ServerConnector.getAddressDetails(addresses.get(i);
                addressList.add(details.getAddress());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, addressList);

            addressSelect.setAdapter(adapter);
        } catch (Exception e) {
            Log.i("STATE","Error while getting the addresses of " + uniqueID);
        }
    }

    private void setAddressSelectionListener() {
        addressSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                selectedPosition = parent.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }
}
