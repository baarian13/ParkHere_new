package com.lazeebear.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.ReturnedUserDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.lazeebear.parkhere.R.layout.activity_sign_up;

public class SignUpActivity extends AppCompatActivity {

    private EditText sEmailView, sFirstName, sLastName, sPassword, sPasswordRe, sPhoneNum;
    private CheckBox seekerBox, ownerBox;
    private int isSeeker = 1, isOwner = 0;

    //pictures
    private Button sTakeVerificationPhotoButton, sChooseVerificationPhotoButton;
    private ImageView verificationPhotoView, profilePicView;
    //profile picture
    private static final int SELECT_PROFILE_PICTURE = 1;
    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSIONS = 3; //ValidationFunctions.getRequestExternalStorage();
    //verification photo
    private static final int REQUEST_IMAGE_CAPTURE = 4;                  //take a photo
    private static final int SELECT_VERIFICATION_PHOTO = 5;
    private String selectedImagePathp, selectedImagePathv, mCurrentPhotoPath;
    private Bitmap verificationPhotoBitmap, profilePicBitmap;

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
        seekerBox = (CheckBox) findViewById(R.id.isSeeker_checkBox_signup);
        ownerBox = (CheckBox) findViewById(R.id.isOwner_checkBox_signup);

        verificationPhotoView = (ImageView) findViewById(R.id.sign_up_verification_photo_preview);
        profilePicView = (ImageView) findViewById(R.id.sign_up_profile_picture_preview);
        hidePicViews();

        Button sSelectPictureButton = (Button) findViewById(R.id.sign_up_profile_picture_button);
        sSelectPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionsAndOpenGallery(SELECT_PROFILE_PICTURE);
            }
        });

        sTakeVerificationPhotoButton = (Button) findViewById(R.id.upload_verification_button);
        sTakeVerificationPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                //check if the device has a camera.
                if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
                    sendTakePictureIntent();
                }
            }
        });

        sChooseVerificationPhotoButton = (Button) findViewById(R.id.choose_verification_button);
        sChooseVerificationPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                checkPermissionsAndOpenGallery(SELECT_VERIFICATION_PHOTO);
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
            if (requestCode == SELECT_PROFILE_PICTURE) {
                selectedImagePathp =  ValidationFunctions.decodeURIDataToImagePath(this, data);
                profilePicBitmap = BitmapFactory.decodeFile(selectedImagePathp);
                profilePicView.setImageBitmap(profilePicBitmap);
                profilePicView.setVisibility(View.VISIBLE);
            } else if (requestCode == SELECT_VERIFICATION_PHOTO) {
                selectedImagePathv =  ValidationFunctions.decodeURIDataToImagePath(this, data);
                verificationPhotoBitmap = BitmapFactory.decodeFile(selectedImagePathv);
                verificationPhotoView.setImageBitmap(verificationPhotoBitmap);
                verificationPhotoView.setVisibility(View.VISIBLE);
            }
            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                verificationPhotoBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                verificationPhotoView.setImageBitmap(verificationPhotoBitmap);
                verificationPhotoView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch(requestCode) {
            case REQUEST_EXTERNAL_STORAGE_PERMISSIONS: {
                Log.i("STATE", "Sign up: onRequestPermissionsResult: checking Permissions");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("STATE", "  permission granted.");
                    //do nothing, let the user re-pick which photo to choose.
                } else {
                    Log.i("STATE", "  permission denied.");
                }
                return;
            }
            //put other cases here
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
        seekerBox.setError(null);

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

        // User type
        if (!seekerBox.isChecked()){
            seekerBox.setError("This type is required by default");
            focusView = seekerBox;
            cancel = true;
        }
        if (ownerBox.isChecked()){
            isOwner = 1;
        }
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

        // User Verification Photo Upload
        if (verificationPhotoBitmap == null) {
            sChooseVerificationPhotoButton.setError("This field is required");
            focusView = sChooseVerificationPhotoButton;
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
        Log.i("STATE", "Sending sign up info to server.");
        if (profilePicBitmap != null) {
            writeImageDebugMessagesToLog();
            ServerConnector.signup(sEmailView.getText().toString(), sPassword.getText().toString(),
                    sFirstName.getText().toString(), sLastName.getText().toString(),
                    sPhoneNum.getText().toString(), isSeeker, isOwner,
                    convertBitmapToString(profilePicBitmap),
                    convertBitmapToString(verificationPhotoBitmap));
            Log.i("STATE", "Received response from server. 1");
        } else {
            ServerConnector.signup(sEmailView.getText().toString(), sPassword.getText().toString(),
                    sFirstName.getText().toString(), sLastName.getText().toString(),
                    sPhoneNum.getText().toString(), isSeeker, isOwner, null,
                    convertBitmapToString(verificationPhotoBitmap));
            Log.i("STATE", "Received response from server. 1");
        }
        Log.i("STATE", "Received response from server. 2");
        startVerificationActivityIntent();
    }

    private void startVerificationActivityIntent() {
        Log.i("STATE", "Sending intent to start User Verification Activity");
        Intent intent = new Intent(this, UserVerificationActivity.class);
        startActivity(intent);
    }

    //this was for a temporary class to store data locally
    private void setUserInformation() {
        try{
            ReturnedUserDAO user = ServerConnector.userDetails(sEmailView.getText().toString());
            //send as part of info to the Account class
            User.firstName = user.getFirst();
            User.lastName = user.getLast();
            User.phoneNumber = user.getPhoneNumber()+"";
            User.email = user.getEmail();
            User.rating = user.getRating();
        } catch (Exception e) {
            Log.i("ERROR", "Exception while getting user details after successful login");
        }
    }

    private void backToRegister() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    //for hiding the picture views until there's a picture
    private void hidePicViews() {
        profilePicView.setVisibility(View.GONE);
        verificationPhotoView.setVisibility(View.GONE);
    }

    // for uploading or selecting a profile picture
    private void checkPermissionsAndOpenGallery(int which_photo) {
        //if validation returns false, it means it's already been granted
        if (!ValidationFunctions.needToGrantGalleryPermissions(this)){
            openGallery(which_photo);
        }
        //else go to onRequestPermissionsResult for the intent to select a file.
    }

    private void openGallery(int request) {
        Log.i("STATE","Open gallery.");
        //permission granted. Open gallery.
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, request);
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

    // for taking a photo of user verification
    private void sendTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
            //create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = ValidationFunctions.createImageFile(getApplicationContext());
                //mCurrentPhotoPath = "file:" + photoFile.getAbsolutePath();
                mCurrentPhotoPath = photoFile.getAbsolutePath();
            } catch (IOException e) {
                //...error while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.lazeebear.parkhere.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //attempt 2
    private String encodeImageFromFile(String file) {
        if (file == null) return "";
        try{
            InputStream inputStream = new FileInputStream(file);//You can get an inputStream using any IO API
            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            //String encodedString = Base64.encodeToString(bytes, Base64.DEFAULT | Base64.URL_SAFE);
            String encodedString = Base64.encodeToString(bytes, Base64.NO_WRAP | Base64.URL_SAFE);
            return encodedString;
        } catch (FileNotFoundException fe) {
            Log.i("STATE", "Could not find file " + file);
        }
        return "";
    }

    // encode bitmap into string. Worked fine for camera photos.
    private String convertBitmapToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
        //String encoded = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT | Base64.URL_SAFE);
        String encoded = Base64.encodeToString(byteArrayOS.toByteArray(), Base64.NO_WRAP | Base64.URL_SAFE);
        return encoded;
    }

    private void writeImageDebugMessagesToLog() {
        Log.i("STATE", "profile photo ver 2: " + encodeImageFromFile(selectedImagePathp));
        Log.i("STATE", "verif photo ver 2: " + encodeImageFromFile(selectedImagePathv));

        Log.i("STATE", "verif photo ver 1: " + convertBitmapToString(verificationPhotoBitmap));

        Log.i("STATE", "profile photo path from gallery:" + selectedImagePathp);
        Log.i("STATE", "verif photo path from gallery: " + selectedImagePathv);
        Log.i("STATE", "verif photo path from camera: " + mCurrentPhotoPath);
    }
}
