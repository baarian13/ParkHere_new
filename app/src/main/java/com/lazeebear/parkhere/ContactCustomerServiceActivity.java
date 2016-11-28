package com.lazeebear.parkhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lazeebear.parkhere.ServerConnector.ServerConnector;


/**
 * Created by palet on 11/27/2016.
 */

public class ContactCustomerServiceActivity extends AppCompatActivity {

    private String uniqueId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_customer_service);

        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("id");

        Button submitButton = (Button) findViewById(R.id.contact_cs_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToCustomerService();
            }
        });
        //send email and message
    }

    private void sendMessageToCustomerService() {
        EditText message = (EditText) findViewById(R.id.contact_cs_textedit);
       // ServerConnector.sendCustomerServiceMessage(uniqueId, message.getText()); TODO
    }
}
