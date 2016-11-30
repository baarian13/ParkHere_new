package com.lazeebear.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.lazeebear.parkhere.DAOs.ReturnedObjects.AddressDetailsDAO;
import com.lazeebear.parkhere.DAOs.ReturnedObjects.SpotButtonDAO;
import com.lazeebear.parkhere.DAOs.SentObjects.SentAddressDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateAddressActivity extends AppCompatActivity {
    private String uniqueID;
    private Spinner addressSelect;
    private Button addressEnter, upload_photo_button, submit_address_button, address_create_new_button, delete_address_button;
    private TextInputEditText address_input_description, address_input_address;
    private CheckBox address_iscovered_checkbox;
    private ImageView upload_photo_image_view;
    private LinearLayout createNewAddressLayout;
    private int selectedPosition;
    private List<SpotButtonDAO> addresses;
    private String base64photo;
    private static final int GET_FROM_GALLERY = 3;
    private int mode; // "create" (0) or "edit" (1)
    private List<AddressDetailsDAO> addressDetailsDAOList;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> addressList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_address);

        addressSelect = (Spinner) findViewById(R.id.address_selection);
        addressEnter = (Button) findViewById(R.id.address_submit_button);

        createNewAddressLayout = (LinearLayout) findViewById(R.id.create_new_address_layout);
        address_create_new_button = (Button) findViewById(R.id.address_create_new_button);
        address_input_address = (TextInputEditText) findViewById(R.id.address_input_address);
        address_input_description = (TextInputEditText) findViewById(R.id.address_input_description);
        address_iscovered_checkbox = (CheckBox) findViewById(R.id.address_isCovered_checkbox);
        upload_photo_button = (Button) findViewById(R.id.address_select_photo);
        upload_photo_image_view = (ImageView) findViewById(R.id.address_image_preview);
        submit_address_button = (Button) findViewById(R.id.address_create_new_submit_button);
        delete_address_button = (Button) findViewById(R.id.address_delete_address_button);

        selectedPosition = 404;

        Intent intent = getIntent();
        if (intent != null) {
            uniqueID = intent.getStringExtra("id");
            mode = intent.getIntExtra("mode", ValidationFunctions.mode_create_address);
            setVisibilityOfCreateNewAddressItems(View.GONE);
            setActionListeners();
            populateSpinnerWithAddresses();
        }
    }

    private void setActionListeners() {
        setCreateNewAddressButtonListener();
        setDeleteAddressButtonListener();
        setAddressSelectionListener();
        setButtonListener();
        setUploadPhotoButtonListener();
        setSubmitNewAddressButtonListener();
    }

    private void setVisibilityOfCreateNewAddressItems(int visibility) {
        createNewAddressLayout.setVisibility(visibility);
        /*address_input_address.setVisibility(View.GONE);
        address_input_description.setVisibility(View.GONE);
        address_iscovered_checkbox.setVisibility(View.GONE);
        upload_photo_button.setVisibility(View.GONE);
        upload_photo_image_view.setVisibility(View.GONE);
        submit_address_button.setVisibility(View.GONE);*/
    }

    private void setCreateNewAddressButtonListener() {
        //if in edit mode, hide this button instead of setting a listener.
        if (mode == ValidationFunctions.mode_edit_address) {
            address_create_new_button.setVisibility(View.GONE);
        } else { //unhide the information boxes when the button is clicked
            address_create_new_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewAddressLayout.setVisibility(View.VISIBLE);
                    address_create_new_button.setVisibility(View.GONE);
                    addressEnter.setVisibility(View.GONE);
                }
            });
        }
    }

    private void setDeleteAddressButtonListener() {
        //if in create mode, hide this button instead of setting a listener.
        switch (mode) {
            case (ValidationFunctions.mode_create_address):
                delete_address_button.setVisibility(View.GONE);
                break;
            case (ValidationFunctions.mode_edit_address):
                delete_address_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (addressSelect.getChildCount() > 0) {
                            //AddressDetailsDAO detailsDAO = addressDetailsDAOList.get(selectedPosition);
                            SpotButtonDAO detailsDAO = addresses.get(selectedPosition);
                            Log.i("STATE", "deleting the address " + detailsDAO.getAddress());
                            try {
                                ServerConnector.deleteAddress(addresses.get(selectedPosition).getID());
                            } catch (Exception e) {
                                Log.i("STATE", "exception while deleting the address " + detailsDAO.getAddress());
                            }
                        } else {
                            Log.i("STATE","There is no address selected to be deleted.");
                        }
                    }
                });
        }
    }

    private void setSubmitNewAddressButtonListener() {
        if (mode == ValidationFunctions.mode_edit_address) submit_address_button.setText("Edit Selected Address");
        submit_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ValidationBoxes()) return; //if something is not valid, quit.
                //get information
                String address = address_input_address.getText().toString();
                String description = address_input_description.getText().toString();
                int spotType = 0;
                int isCovered = 1; //0 false 1 true
                if (address_iscovered_checkbox.isChecked())
                    isCovered = 1;
                else
                    isCovered = 0;

                switch (mode) {
                    case (ValidationFunctions.mode_create_address):
                        try {
                            addressEnter.setVisibility(View.VISIBLE);
                            setVisibilityOfCreateNewAddressItems(View.GONE);

                            int newAddressID = ServerConnector.createAddress(address, uniqueID, description, spotType, isCovered, base64photo);

                            //reload the spinner
                            Log.i("STATE"," Reloading spinner....");
                            populateSpinnerWithAddresses();
                            /*addressList.add(address);
                            addresses = ServerConnector.getAddressesOf(uniqueID);
                            adapter.notifyDataSetChanged();
                            addressSelect.setAdapter(adapter);
                            */
                            Log.i("STATE", "Finished reloading spinner.");
                            break;
                        } catch (Exception e) {
                            Log.i("STATE", "Error while creating new address");
                            setVisibilityOfCreateNewAddressItems(View.VISIBLE);
                        }
                        break;
                    case (ValidationFunctions.mode_edit_address):
                        try {
                            SentAddressDAO editedAddress = new SentAddressDAO(addresses.get(selectedPosition).getID(),
                                    address, base64photo, description, uniqueID, spotType, isCovered);
                            ServerConnector.modifyAddress(editedAddress);
                            // hide the stuff after the spot has been updated.
                            setVisibilityOfCreateNewAddressItems(View.GONE);
                            break;
                        } catch (Exception e) {
                            Log.i("STATE", "Error while editing address");
                            setVisibilityOfCreateNewAddressItems(View.VISIBLE);
                        }
                        break;
                }
            }
        });
    }

    private boolean ValidationBoxes() {
        address_input_address.setError(null);
        address_input_description.setError(null);
        upload_photo_button.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check every box
        // Input address
        if (TextUtils.isEmpty(address_input_address.getText())) {
            address_input_address.setError("Address must not be empty!");
            focusView = address_input_address;
            cancel = true;
        }
        // description
        if (TextUtils.isEmpty(address_input_description.getText())) {
            address_input_description.setError("Description must not be empty!");
            focusView = address_input_description;
            cancel = true;
        }

        if (base64photo.isEmpty()){
            upload_photo_button.setError("You must have a photo!");
            focusView = upload_photo_button;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        }
        return true;
    }

    private void populateSpinnerWithAddresses() {
        Log.i("STATE","populateSpinnerWithAddresses");
        try {
            addresses = ServerConnector.getAddressesOf(uniqueID);

            addressDetailsDAOList = new ArrayList<>();
            addressList = new ArrayList<>();
            for (int i = 0; i < addresses.size(); i++) {
                Log.i("STATE","getting address details "+ i);
                AddressDetailsDAO details = ServerConnector.AddressDetails(addresses.get(i).getID());
                addressDetailsDAOList.add(details);
                addressList.add(addresses.get(i).getAddress());
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, addressList);

            addressSelect.setAdapter(adapter);
        } catch (Exception e) {
            Log.i("STATE", "Error while getting the addresses of " + uniqueID);
        }
    }

    private void setAddressSelectionListener() {
        addressSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                selectedPosition = parent.getSelectedItemPosition();

                if (mode == ValidationFunctions.mode_edit_address) {
                    createNewAddressLayout.setVisibility(View.VISIBLE);
                    fillInInformation(selectedPosition);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setUploadPhotoButtonListener() {
        upload_photo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionsAndOpenGallery();
            }
        });
    }

    //opens the gallery when upload_photo_button is clicked
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                upload_photo_image_view.setImageBitmap(bitmap);
                upload_photo_image_view.setVisibility(View.VISIBLE);

                String photoPath = ValidationFunctions.decodeURIDataToImagePath(this, data);
                base64photo = ValidationFunctions.encodeImageFromFile(photoPath);
                //send this to server in createSpot()
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkPermissionsAndOpenGallery() {
        //if validation returns false, it means it's already been granted
        if (!ValidationFunctions.needToGrantGalleryPermissions(this)) {
            openGallery();
        }
        //else go to onRequestPermissionsResult for the intent to select a file.
    }

    private void openGallery() {
        Log.i("STATE", "Open gallery.");
        //permission granted. Open gallery.
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GET_FROM_GALLERY);
    }

    private void setButtonListener() {
        addressEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntent();
            }
        });
    }

    private void startIntent() {
        Intent intent = new Intent(this, CreateSpotActivity.class);
        intent.putExtra("id", uniqueID);
        intent.putExtra("photo", base64photo);
        intent.putExtra("addressID", addresses.get(selectedPosition).getID());
        startActivity(intent);
    }

    // fills in the information when editing a selected address
    private void fillInInformation(int index) {
        if (mode != ValidationFunctions.mode_edit_address) return;

        AddressDetailsDAO address = addressDetailsDAOList.get(index);
        address_input_address.setText(address.getAddress());
        address_input_address.setEnabled(false); //you can't edit the address.
        address_input_description.setText(address.getDescription());
        if (address.getIsCovered() == 0) { //0 false 1 true
            address_iscovered_checkbox.setChecked(false);
        } else {
            address_iscovered_checkbox.setChecked(true);
        }
        upload_photo_image_view.setImageBitmap(ValidationFunctions
                .convertBase64StringToBitmap(address.getPicture()));
    }
}
