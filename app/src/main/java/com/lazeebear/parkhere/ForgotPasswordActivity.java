package com.lazeebear.parkhere;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText fEmailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        fEmailView = (EditText) findViewById(R.id.email_forgotPassword_page);

        Button fConfirmButton = (Button) findViewById(R.id.forgotpassword_button);
        fConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateEmail()){
                    attempLogin();
                }
            }
        });

        Button fCancelButton = (Button) findViewById(R.id.cancel_forgotpassword_button);
        fCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToRegister();
            }
        });
    }

    private void backToRegister() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean validateEmail() {
        fEmailView.setError(null);

        String email = fEmailView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(email)){
            fEmailView.setError("This field is required");
            focusView = fEmailView;
            cancel = true;
        } else if (!ValidationFunctions.isEmailAddress(email)) {
            fEmailView.setError("This email address is invalid");
            focusView = fEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        }

        return true;
    }

    private void attempLogin() {
    }


}
