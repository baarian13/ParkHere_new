package com.lazeebear.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

public class PaymentActivity extends AppCompatActivity {
    final int REQUEST_CODE = 999;
    private String clientToken;
    private String costOfSpot, email;
    private String spotID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);

        costOfSpot=""; email = ""; spotID = "";
        // get this stuff from the server

        Intent intent = getIntent();
        if (intent != null) {
            spotID = intent.getStringExtra("id");
            //get data
            //costOfSpot = intent.getStringExtra("costOfSpot");
            //email = intent.getStringExtra("email");
            //spotID = intent.getStringExtra("spotID:");
            addActionListeners();
        }
    }

    private void addActionListeners(){
        Button launchBrainTreeButton = (Button) findViewById(R.id.launch_braintree_button);
        launchBrainTreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBraintreeSubmit();
            }
        });
    }

    private void onBraintreeSubmit() {
        clientToken = ServerConnector.getToken();
        PaymentRequest paymentRequest = new PaymentRequest()
                .primaryDescription("Booking")
                //.secondaryDescription()
                .amount(costOfSpot)
                .submitButtonText("")
                .clientToken(clientToken);
        startActivityForResult(paymentRequest.getIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                            BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                    );
                    String nonce = paymentMethodNonce.getNonce();
                    int result = ServerConnector.bookSpot(costOfSpot, nonce, email, spotID);
                    break;
                case BraintreePaymentActivity.BRAINTREE_RESULT_DEVELOPER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_ERROR:
                case BraintreePaymentActivity.BRAINTREE_RESULT_SERVER_UNAVAILABLE:
                    // handle errors here, a throwable may be available in
                    // data.getSerializableExtra(BraintreePaymentActivity.EXTRA_ERROR_MESSAGE)
                    break;
                default:
                    break;
            }
        }
    }
}
