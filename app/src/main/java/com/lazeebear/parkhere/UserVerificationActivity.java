package com.lazeebear.parkhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by palet on 11/8/2016.
 */

public class UserVerificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("STATE", "Started User Verification Activity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_verification_needed);
    }
}
