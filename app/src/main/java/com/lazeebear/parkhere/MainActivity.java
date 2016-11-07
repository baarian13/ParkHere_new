package com.lazeebear.parkhere;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static boolean DEBUG_MODE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!DEBUG_MODE){
            //make the thread wait a bit so the loading page can show up
            int sec = 1000; //1 second
            openLoginPageAfterWaiting(sec);
        } else {
            setButtonsToVisible();
        }
    }

    private void openLoginPageAfterWaiting(int sec){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //do this after "sec" seconds
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        }, sec);
    }

    private void setButtonsToVisible() {
        (findViewById(R.id.mainpage_sign_in_button)).setVisibility(View.VISIBLE);
        (findViewById(R.id.mainpage_search_button)).setVisibility(View.VISIBLE);
        (findViewById(R.id.mainpage_account_button)).setVisibility(View.VISIBLE);
        (findViewById(R.id.mainpage_payment_button)).setVisibility(View.VISIBLE);
        (findViewById(R.id.mainpage_forgot_pasword_button)).setVisibility(View.VISIBLE);
    }

    public void openSignInActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void openSearchActivity(View view) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    public void openAccountActivity(View view) {
        Intent intent = new Intent(this, Account.class);
        startActivity(intent);
    }

    public void openPaymentActivity(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    public void openForgotPasswordActivity(View view){
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public static void setDebugMode(boolean inDebugMode){ DEBUG_MODE = inDebugMode; }
    public static boolean getDebugMode () { return DEBUG_MODE; }
}
