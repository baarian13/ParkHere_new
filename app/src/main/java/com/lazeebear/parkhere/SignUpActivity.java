package com.lazeebear.parkhere;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

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

        Button sSelectPictureButton = (Button) findViewById(R.id.sign_up_profile_picture_button);
        sSelectPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAFile();
            }
        });

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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
            }
        }
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

        //if user not verified
        // Intent intent = new Intent(this, UserVerificationActivity.class);
        // startActivity(intent);
    }

    private void backToRegister() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void selectAFile() {
        // in onCreate or any event where your want the user to
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
}
