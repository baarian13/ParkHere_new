package com.lazeebear.parkhere;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.res.Resources;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.ReturnedUserDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotDetailsDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentUserDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Zhicheng on 10/22/2016.
 */

public class Account extends AppCompatActivity {
    private static final int CHOOSE_PROFILE_PIC_FROM_GALLERY = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 3; //ValidationFunctions.getRequestExternalStorage();
    private boolean isViewingOwnAccount = true;
    private String uniqueID = "";
    private boolean isOwner = true;
    private boolean isSeeker = true;
    private boolean spotHistoryOpen = false;
    private boolean ownedSpotsOpen = false;
    private boolean currentReservationsOpen = false;
    private boolean userTypeEditorsShown = true;
    private boolean phoneNumberEditorsShown = true;
    private List<Integer>  ownedSpotList = new ArrayList<>();
    private List<Integer> spotHistoryList = new ArrayList<>();
    private List<Integer> currentReservationsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();

        //grab info, setUserType(), fillInfo
        if (intent != null) {
            String idArray = intent.getStringExtra("id");
            uniqueID = idArray;
            resetViewVisibility();
            fillInformation();
            hideToggleButton();
            hideComponents();
            addActionListeners();
        }
    }

    private void resetViewVisibility(){
        Button createSpotButton = (Button) findViewById(R.id.createSpotButton);
        createSpotButton.setVisibility(View.VISIBLE);
        Button searchSpotButton = (Button) findViewById(R.id.searchSpotButton_account);
        searchSpotButton.setVisibility(View.VISIBLE);
        Button spotHistoryButton = (Button) findViewById(R.id.spotHistoryButton_account);
        spotHistoryButton.setVisibility(View.VISIBLE);
        LinearLayout spotList = (LinearLayout) findViewById(R.id.spotList_account);
        spotList.setVisibility(View.VISIBLE);
        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        phoneNumber.setVisibility(View.VISIBLE);
        TextView email = (TextView) findViewById(R.id.email);
        email.setVisibility(View.VISIBLE);

        hideUserTypeEditors();
        hidePhoneNumberEditors();
        spotList.removeAllViews();
        spotHistoryOpen = false;
        ownedSpotsOpen = false;
        currentReservationsOpen = false;
    }

    //the only component that sticks on the screen
    //even if the user wants to toggle between
    //your own profile and public profile
    private void hideToggleButton(){
        Button toggleButton = (Button) findViewById(R.id.toggleButton_account);
        if (!isViewingOwnAccount()){
            toggleButton.setVisibility(View.GONE);
        }
    }

    //only show when need to edit
    private void hideUserTypeEditors(){
        Spinner userTypeSpinner = (Spinner) findViewById(R.id.editUserTypeSpinner_account);
        userTypeSpinner.setVisibility(View.GONE);
        Button confirmUserTypeButton = (Button) findViewById(R.id.confirmUserTypeButton_account);
        confirmUserTypeButton.setVisibility(View.GONE);
        userTypeEditorsShown = false;
    }

    private void showUserTypeEditors(){
        Spinner userTypeSpinner = (Spinner) findViewById(R.id.editUserTypeSpinner_account);
        userTypeSpinner.setVisibility(View.VISIBLE);
        Button confirmUserTypeButton = (Button) findViewById(R.id.confirmUserTypeButton_account);
        confirmUserTypeButton.setVisibility(View.VISIBLE);
        userTypeEditorsShown = true;
    }

    private void hidePhoneNumberEditors(){
        EditText phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText_account);
        phoneNumberEditText.setVisibility(View.GONE);
        Button confirmPhoneNumberButton = (Button) findViewById(R.id.confirmPhoneNumberButton_account);
        confirmPhoneNumberButton.setVisibility(View.GONE);
        //also show phone number
        //because we hide it when we show the editText
        TextView phoneNumberTextView = (TextView) findViewById(R.id.phoneNumber);
        phoneNumberTextView.setVisibility(View.VISIBLE);
        phoneNumberEditorsShown = false;
    }

    private void showPhoneNumberEditors(){
        EditText phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText_account);
        phoneNumberEditText.setVisibility(View.VISIBLE);
        Button confirmPhoneNumberButton = (Button) findViewById(R.id.confirmPhoneNumberButton_account);
        confirmPhoneNumberButton.setVisibility(View.VISIBLE);
        //hide the phone number to give room for editText
        TextView phoneNumberTextView = (TextView) findViewById(R.id.phoneNumber);
        phoneNumberTextView.setVisibility(View.GONE);
        phoneNumberEditorsShown = true;
    }


    private void changeToggleButtonText(){
        Button toggleButton = (Button) findViewById(R.id.toggleButton_account);
        if (isViewingOwnAccount()){
            toggleButton.setText(getString(R.string.toggle_public_profile_button));
        } else{
            toggleButton.setText(getString(R.string.toggle_private_profile_button));
        }
    }

    private void hideComponents() {
        Button createSpotButton = (Button) findViewById(R.id.createSpotButton);
        if (!isViewingOwnAccount())
            createSpotButton.setVisibility(View.GONE);
        else if (!isOwner())
            createSpotButton.setVisibility(View.GONE);

        Button searchSpotButton = (Button) findViewById(R.id.searchSpotButton_account);
        if (isViewingOwnAccount()) {
            if (!isSeeker())
                searchSpotButton.setVisibility(View.GONE);
        } else
            searchSpotButton.setVisibility(View.GONE);

        LinearLayout spotList = (LinearLayout) findViewById(R.id.spotList_account);
        Button spotHistoryButton = (Button) findViewById(R.id.spotHistoryButton_account);
        if (!isViewingOwnAccount()) {
            spotHistoryButton.setVisibility(View.GONE);
        } else if (!isSeeker()) {
            spotHistoryButton.setVisibility(View.GONE);
        }

        Button currentReservationsButton = (Button) findViewById(R.id.currentReservationsButton);
        if (!isViewingOwnAccount()) {
            currentReservationsButton.setVisibility(View.GONE);
        } else if (!isSeeker()) {
            currentReservationsButton.setVisibility(View.GONE);
        }

        Button ownedSpotsButton = (Button) findViewById(R.id.ownedSpotsButton);
        if (!isOwner()) {
            ownedSpotsButton.setVisibility(View.GONE);
        }

        TextView phoneNumber = (TextView) findViewById(R.id.phoneNumber);
        if (!isViewingOwnAccount())
            phoneNumber.setVisibility(View.GONE);

        TextView email = (TextView) findViewById(R.id.email);
        if (!isViewingOwnAccount())
            email.setVisibility(View.GONE);

        Button editPhoneNumberButton = (Button) findViewById(R.id.editPhoneNumberButton_account);
        Button editUserTypeButton = (Button) findViewById(R.id.editUserTypeButton_account);
        if (!isViewingOwnAccount()){
            editPhoneNumberButton.setVisibility(View.GONE);
            editUserTypeButton.setVisibility(View.GONE);
        }

    }

    private void fillInformation() {
        try {

            System.out.println("In Account page0: " + uniqueID);
            ReturnedUserDAO userInfo = ServerConnector.userDetails(uniqueID);
            System.out.println("successfully grabbed user info from server");
            currentReservationsList = ServerConnector.viewRentals(uniqueID);
            if (currentReservationsList == null)
                currentReservationsList = new ArrayList<>();

            ownedSpotList = userInfo.getSpots();
            if (ownedSpotList == null)
                ownedSpotList = new ArrayList<>();

           spotHistoryList = ServerConnector.viewSpotHistory(uniqueID);
            if (spotHistoryList == null)
                spotHistoryList = new ArrayList<>();

            if(userInfo.isOwner()==1)
                isOwner = true;
            else
                isOwner = false;
            if (userInfo.isSeeker()==1)
                isSeeker = true;
            else
                isSeeker = false;

            System.out.println("Checking if user is same as account page...");
            isViewingOwnAccount = ServerConnector.checkUser(uniqueID);
            System.out.println("User / account check complete!");


            TextView accountName = (TextView) findViewById(R.id.accountName_account);
            accountName.setText(getDisplayName(userInfo.getFirst(), userInfo.getLast()));

            ImageView profilePic = (ImageView) findViewById(R.id.account_profile_picture);
            //profilePic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            Log.i("STATE","Getting Image...");
            profilePic.setImageBitmap(ValidationFunctions.convertBase64StringToBitmap(userInfo.getPicture()));
            Log.i("STATE","Successfully got image");
            profilePic.setVisibility(View.VISIBLE);

            Log.i("STATE","getting other user info");
            RatingBar ratingOfUser = (RatingBar) findViewById(R.id.ratingBar);
            ratingOfUser.setRating(userInfo.getRating());
            TextView phoneNumberTextView = (TextView) findViewById(R.id.phoneNumber);
            phoneNumberTextView.setText(userInfo.getPhoneNumber()+"");
            EditText phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText_account);
            phoneNumberEditText.setText(userInfo.getPhoneNumber()+"");
            TextView email = (TextView) findViewById(R.id.email);
            email.setText(userInfo.getEmail());

            TextView userTypeTextView = (TextView) findViewById(R.id.userType_account);

            userTypeTextView.setText(getUserTypeString());
            //also change the preset value for edit
            Spinner editSpinner = (Spinner) findViewById(R.id.editUserTypeSpinner_account);
            editSpinner.setSelection(getUserType());

            Log.i("STATE","Getting current reservations");

        } catch (Exception e){
            Log.i("ERROR", "Exception while getting user details opening account");
        }
    }

    private void addActionListeners() {
        addOwnedSpotsButtonActionListener();
        addSpotHistoryButtonActionListener();
        addCurrentReservationsButtonActionListener();
        addSearchSpotButtonActionListener();
        addCreateSpotButtonActionListener();
        addLogoutButtonActionListener();
        addToggleButtonActionListener();
        addEditUserTypeListener();
        addEditPhoneNumberListener();
        addConfirmUserTypeListener();
        addConfirmPhoneNumberListener();
        addChangeProfilePicListener();
    }

    private void addOwnedSpotsButtonActionListener() {
        Button ownedSpotsButton = (Button) findViewById(R.id.ownedSpotsButton);
        ownedSpotsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                populateOwnedSpots();
            }
        });
    }

    private void addCurrentReservationsButtonActionListener(){
        Button currentReservationsButton = (Button) findViewById(R.id.currentReservationsButton);
        currentReservationsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                populateCurrentReservations();
            }
        });
    }

    private void clearSpotList(){
        LinearLayout list = (LinearLayout) findViewById(R.id.spotList_account);
        list.removeAllViews();
    }

    //we only have one list to show
    //so remove everything already there and replace with the new content
    private void populateOwnedSpots() {
        if (!ownedSpotsOpen) {
            clearSpotList();
            LinearLayout list = (LinearLayout) findViewById(R.id.spotList_account);
            int spotCt = ownedSpotList.size();
            System.out.println("Populating owned spots...");
            for (int i = 0; i < spotCt; i++) {
                Button spotButton = createSpotButton(ownedSpotList.get(i));
                System.out.println("Creating button with ID: "+ ownedSpotList.get(i));
                spotButton.setId(ownedSpotList.get(i)); //for referencing from tests. doesn't need to be unique.
                list.addView(spotButton);
            }

            System.out.println("Successfully populated owned spots!!");

            ownedSpotsOpen = true;
            spotHistoryOpen = false;
            currentReservationsOpen = false;
        }
    }

    private void populateSpotsHistory() {
        if (!spotHistoryOpen) {
            clearSpotList();
            LinearLayout list = (LinearLayout) findViewById(R.id.spotList_account);
            int spotCt = spotHistoryList.size();
            System.out.println("Populating spot history with " + spotCt + " buttons...");
            for (int i = 0; i < spotCt; i++) {
                Button spotButton = createSpotButton(spotHistoryList.get(i));
                System.out.println("Creating button with ID: " + spotHistoryList.get(i));
                list.addView(spotButton);
            }
            System.out.println("Successfully populated spot history!");
            spotHistoryOpen = true;
            ownedSpotsOpen = false;
            currentReservationsOpen = false;
        }
    }

    private void populateCurrentReservations() {
        if (!currentReservationsOpen) {
            clearSpotList();
            LinearLayout list = (LinearLayout) findViewById(R.id.spotList_account);
            int spotCt = currentReservationsList.size();
            for (int i = 0; i < spotCt; i++) {
                Button spotButton = createSpotButton(currentReservationsList.get(i));
                list.addView(spotButton);
            }
            currentReservationsOpen = true;
            spotHistoryOpen = false;
            ownedSpotsOpen = false;
        }
    }

    private Button createSpotButton(int id) {
        String address = "";
        final int finalID = id;
        try {
            System.out.println("Getting spot DAO from spot ID...");
            SpotDetailsDAO spot = ServerConnector.spotDetails(id);
            System.out.println("Successfully got spot DAO!");
            address = spot.getAddress();
        } catch (Exception e){
            Log.i("ERROR", "Exception while getting spot info on account page");
        }
        //THIS DOES NOT GET CALLED
        System.out.println("Creating button in createSpotButton()");
        Button button = new Button(this);
        button.setText(address);
        button.setOnClickListener( new View.OnClickListener(){
            public void onClick(View view){
                addIntentDetailedSpotButton(finalID);
            }
        });
        System.out.println("Returning button in createSpotButton()");
        return button;
    }

    private void addIntentDetailedSpotButton(int id){
        Intent intent = new Intent(this, SpotDetailActivity.class);
        intent.putExtra("id", id+"");
        startActivity(intent);
    }

    private void addSpotHistoryButtonActionListener() {
        Button spotHistoryButton = (Button) findViewById(R.id.spotHistoryButton_account);
        spotHistoryButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                populateSpotsHistory();
            }
        });
    }

    private void addSearchSpotButtonActionListener() {
        Button searchSpotsButton = (Button) findViewById((R.id.searchSpotButton_account));
        searchSpotsButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                goToSearchSpots();
            }
        }));
    }

    private void goToSearchSpots() {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    private void addCreateSpotButtonActionListener() {
        Button createSpotButton = (Button) findViewById((R.id.createSpotButton));
        createSpotButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                goToCreateSpot();
            }
        }));
    }

    private void goToCreateSpot() {
        Intent intent = new Intent(this, CreateSpotActivity.class);
        intent.putExtra("id",uniqueID);
        startActivity(intent);
    }

    private void addLogoutButtonActionListener(){
        Button logoutButton = (Button) findViewById(R.id.logoutButton_acount);
        logoutButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                logout();
            }
        }));
    }

    private void addToggleButtonActionListener(){
        Button toggleButton = (Button) findViewById(R.id.toggleButton_account);
        toggleButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                toggleViewingOwnAccount();
            }
        }));
    }

    private void logout(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean isSeeker() {
        return isSeeker;
    }

    private boolean isOwner() {
        return isOwner;
    }

    private boolean isViewingOwnAccount() {
        return isViewingOwnAccount;
    }

    private String getDisplayName(String first, String last) {
        if (isViewingOwnAccount())
            //return "First and Last Name";
            return first + " " + last;
        else
            //return "First Name Only";
            return first;
    }

    //we should only show this initially when we verify that the user is viewing own account
    private void toggleViewingOwnAccount() {
        if (isViewingOwnAccount()){
            //we need to show public profile
            isViewingOwnAccount = false;

        } else {
            isViewingOwnAccount = true;
        }

        resetViewVisibility();
        hideComponents();
        //redundant code except for user name
        fillInformation();
        changeToggleButtonText();
    }

    private void addConfirmUserTypeListener(){
        Button confirmUserTypeButton = (Button) findViewById(R.id.confirmUserTypeButton_account);
        confirmUserTypeButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                confirmUserType();
            }
        }));
    }

    private void confirmUserType(){
        //get chosen user type
        Spinner spinner = (Spinner) findViewById(R.id.editUserTypeSpinner_account);
        //I assume the user types are by index?
        int choiceIndex = spinner.getSelectedItemPosition();
        setUserType(choiceIndex);
        //send
        try {
            SentUserDAO updatedUser = new SentUserDAO(uniqueID, null, null, null, null, null, isSeeker(), isOwner());
            ServerConnector.modifyUser(updatedUser);
        } catch (Exception e){
            Log.i("ERROR", "Exception while modifying user type");
        }
        //update view
        resetViewVisibility();
        hideComponents();
        fillInformation();

    }

    private void addConfirmPhoneNumberListener(){
        Button confirmUserTypeButton = (Button) findViewById(R.id.confirmPhoneNumberButton_account);
        confirmUserTypeButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                confirmPhoneNumber();
            }
        }));
    }

    private void confirmPhoneNumber(){
        //get chosen phoneNumber
        EditText phoneNumberEditText = (EditText) findViewById(R.id.phoneNumberEditText_account);
        String phoneNumberString = phoneNumberEditText.getText().toString();

        if (ValidationFunctions.isPhoneNum(phoneNumberString)){
            try {
                SentUserDAO updatedUser = new SentUserDAO(uniqueID, null, null, null, phoneNumberString, null, isSeeker(), isOwner());
                ServerConnector.modifyUser(updatedUser);
            } catch (Exception e){
                Log.i("ERROR", "Exception while modifying phone number");
            }
        }

        //this is not a temporary variable so do not erase!
        User.phoneNumber = phoneNumberString;

        //update view
        resetViewVisibility();
        hideComponents();
        fillInformation();

    }

    private void addEditUserTypeListener(){
        Button editUserTypeButton = (Button) findViewById(R.id.editUserTypeButton_account);
        editUserTypeButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                toggleUserTypeEditors();
            }
        }));
    }

    private void addEditPhoneNumberListener(){
        Button editPhoneNumberButton = (Button) findViewById(R.id.editPhoneNumberButton_account);
        editPhoneNumberButton.setOnClickListener((new View.OnClickListener() {
            public void onClick(View view) {
                togglePhoneNumberEditors();
            }
        }));
    }

    private void toggleUserTypeEditors(){
        if (userTypeEditorsShown)
            hideUserTypeEditors();
        else
            showUserTypeEditors();
    }

    private void togglePhoneNumberEditors(){
        if (phoneNumberEditorsShown)
            hidePhoneNumberEditors();
        else
            showPhoneNumberEditors();
    }

    private int getUserType(){
        if (isOwner() && isSeeker()){
            return 0;
        } else if (isOwner()){
            return 1;
        } else {
            return 2;
        }
    }

    private void setUserType(int index){
        if (index == 0){
            isOwner = true;
            isSeeker = true;
        } else if (index == 1){
            isOwner = true;
            isSeeker = false;
        } else{
            isOwner = false;
            isSeeker = true;
        }
    }

    private String getUserTypeString(){
        Resources res = getResources();
        String[] userTypeArray = res.getStringArray(R.array.user_types);
        int index = -1;
        if (isOwner() && isSeeker()){
            index = 0;
        } else if (isOwner()){
            index = 1;
        } else {
            index = 2;
        }

        return userTypeArray[index].toString();
    }

    /* Profile picture stuff */

    // upload different profile picture
    private void addChangeProfilePicListener() {
        Button changeProfilePicButton = (Button) findViewById(R.id.account_change_profile_pic_button);
        changeProfilePicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadNewProfilePicFromGallery();
            }
        });
    }

    private void uploadNewProfilePicFromGallery() {
        //if validation returns false, it means it's already been granted
        if (!ValidationFunctions.needToGrantGalleryPermissions(this)){
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch(requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                Log.i("STATE", "Account: onRequestPermissionsResult: checking Permissions");
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();
                } else { Log.i("STATE", "  permission denied."); }
                return;
            }
            //put other cases here
        }
    }

    private void openGallery() {
        Log.i("STATE","Open gallery.");
        //permission granted. Open gallery.
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, CHOOSE_PROFILE_PIC_FROM_GALLERY);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PROFILE_PIC_FROM_GALLERY) {
                String profilePicBase64 = ValidationFunctions.decodeURIDataToImagePath(this, data);
                Bitmap profilePicBitmap = BitmapFactory.decodeFile(profilePicBase64);
                ImageView profilePicView = (ImageView) findViewById(R.id.account_profile_picture);

                // send the new profile picture back to the server.
                try {
                    SentUserDAO updatedUser = new SentUserDAO(uniqueID, null, null, null, null, profilePicBase64, isSeeker(), isOwner());
                    ServerConnector.modifyUser(updatedUser);
                } catch (Exception e){
                    Log.i("ERROR", "Exception while modifying user image");
                }

                // refresh view
                profilePicView.setImageBitmap(profilePicBitmap);
                profilePicView.setVisibility(View.VISIBLE);
            }
        }
    }
}
