package com.lazeebear.parkhere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import org.w3c.dom.Text;

import static com.lazeebear.parkhere.R.layout.activity_sign_up;

public class SignUpActivity extends AppCompatActivity {

    private EditText sEmailView, sFirstName, sLastName, sPassword, sPasswordRe, sPhoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sEmailView = (EditText) findViewById(R.id.EmailEditText);
        sFirstName = (EditText) findViewById(R.id.FirstNameEditText);
        sLastName = (EditText) findViewById(R.id.LastNameEditText);
        sPassword = (EditText) findViewById(R.id.password_sign_up);
        sPasswordRe = (EditText) findViewById(R.id.passwordConfirm_sign_up);
        sPhoneNum = (EditText) findViewById(R.id.phoneNum_sign_up);

        Button sSignInButton = (Button) findViewById(R.id.sign_up_button);
        sSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validationBoxes()){
                    attemptSignUp();
                }
            }
        });

        Button sCancelButton = (Button) findViewById(R.id.cancel_button_sign_up);
        sCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToRegister();
            }
        });
    }

    private boolean validationBoxes() {
        // reset errors
        sEmailView.setError(null);
        sFirstName.setError(null);
        sLastName.setError(null);
        sPassword.setError(null);
        sPasswordRe.setError(null);
        sPhoneNum.setError(null);

        // Store values at the time of the login attempt.
        String email = sEmailView.getText().toString();
        String firstName = sFirstName.getText().toString();
        String lastName = sLastName.getText().toString();
        String password = sPassword.getText().toString();
        String passwordRe = sPasswordRe.getText().toString();
        String phoneNum = sPhoneNum.getText().toString();

        boolean cancel = false;
        View focusView = null;

           // check every boxes

        // Phone number
        if (TextUtils.isEmpty(phoneNum)){
            sPhoneNum.setError("This field is required");
            focusView = sPhoneNum;
            cancel = true;
        } else if (!ValidationFunctions.isPhoneNum(phoneNum)){
            sPhoneNum.setError("Phone number is not valid");
            focusView = sPhoneNum;
            cancel = true;
        }

        // Passwords repeat
        if (TextUtils.isEmpty(passwordRe)){
            sPasswordRe.setError("This field is required");
            focusView = sPasswordRe;
            cancel = true;
        } else if (!passwordRe.equals(password)){
            sPasswordRe.setError("Does not match the password");
            focusView = sPasswordRe;
            cancel = true;
        }
        // Passwords
        if (TextUtils.isEmpty(password)){
            sPassword.setError("This field is required");
            focusView = sPassword;
            cancel = true;
        } else if (!ValidationFunctions.hasEnoughLength(password)){
            sPassword.setError("Invalid password form");
            focusView = sPassword;
            cancel = true;
        } else if (!ValidationFunctions.hasSpecialCharacter(password)){
            sPassword.setError("Invalid password form");
            focusView = sPassword;
            cancel = true;
        }

        // Last Name
        if (TextUtils.isEmpty(lastName)){
            sLastName.setError("This field is required");
            focusView = sLastName;
            cancel = true;
        }
        // First Name
        if (TextUtils.isEmpty(firstName)){
            sFirstName.setError("This field is required");
            focusView = sFirstName;
            cancel = true;
        }

        // Email Address
        if (TextUtils.isEmpty(email)){
            sEmailView.setError("This field is required");
            focusView = sEmailView;
            cancel = true;
        } else if (!ValidationFunctions.isEmailAddress(email)) {
            sEmailView.setError("This email address is invalid");
            focusView = sEmailView;
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

    private void attemptSignUp() {
        /*ServerConnector.signup(sEmailView.getText().toString(), sPassword.getText().toString(),
                sFirstName.getText().toString(), sLastName.getText().toString(),
                sPhoneNum.getText().toString(), 0, 1, null);*/
        //save locally


        User.firstName = sFirstName.getText().toString();
        User.lastName = sLastName.getText().toString();
        User.phoneNumber = sPhoneNum.getText().toString();
        User.email = sEmailView.getText().toString();
        Intent intent = new Intent(this, Account.class);
        startActivity(intent);
    }

    private void backToRegister() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

}
