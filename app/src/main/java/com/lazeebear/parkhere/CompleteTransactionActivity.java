package com.lazeebear.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CompleteTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_transaction);

        Button confirmButton = (Button) findViewById(R.id.confirmButton_complete_trans);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                backToAccountPage();
            }
        });
    }

    private void backToAccountPage() {
        Intent intent = new Intent(this, Account.class);
        startActivity(intent);
    }
}
