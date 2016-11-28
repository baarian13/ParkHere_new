package com.lazeebear.parkhere;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
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

import com.lazeebear.parkhere.DAOs.SentObjects.AddressDetailsDAO;
import com.lazeebear.parkhere.ServerConnector.ServerConnector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateAddressActivity extends AppCompatActivity {
    private String uniqueID;
    private Spinner addressSelect;
    private Button addressEnter, upload_photo_button, submit_address_button, address_create_new_button;
    private TextInputEditText address_input_description, address_input_address;
    private CheckBox address_iscovered_checkbox;
    private ImageView upload_photo_image_view;
    private LinearLayout createNewAddressLayout;
    private int selectedPosition;
    private List<Integer> addresses;
    private String base64photo;
    private static final int GET_FROM_GALLERY = 3;

    private ArrayList<String> addressList;

    @Override
    public void onCreate(Bundle savedInstanceState){
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

        selectedPosition = 404;

        Intent intent = getIntent();
        if (intent != null){
            uniqueID = intent.getStringExtra("id");
            setActionListeners();
            populateSpinnerWithAddresses();
        }
    }

    private void setActionListeners() {
        hideCreateNewAddressItems();
        setCreateNewAddressButtonListener();
        setAddressSelectionListener();
        setButtonListener();
        setUploadPhotoButtonListener();
        setSubmitNewAddressButtonListener();
    }

    private void hideCreateNewAddressItems() {
        createNewAddressLayout.setVisibility(View.GONE);
        /*address_input_address.setVisibility(View.GONE);
        address_input_description.setVisibility(View.GONE);
        address_iscovered_checkbox.setVisibility(View.GONE);
        upload_photo_button.setVisibility(View.GONE);
        upload_photo_image_view.setVisibility(View.GONE);
        submit_address_button.setVisibility(View.GONE);*/
    }

    private void setCreateNewAddressButtonListener() {
        //unhide the items
        address_create_new_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAddressLayout.setVisibility(View.VISIBLE);
                address_create_new_button.setVisibility(View.GONE);
                addressEnter.setVisibility(View.GONE);
            }
        });
    }

    private void setSubmitNewAddressButtonListener() {
        submit_address_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addressEnter.setVisibility(View.VISIBLE);
                    String address = address_input_address.getText().toString();
                    String description = address_input_description.getText().toString();
                    int spotType = 0;
                    int isCovered = 1; //0 false 1 true
                    if (address_iscovered_checkbox.isChecked())
                        isCovered = 1;
                    else
                        isCovered = 0;
                    ServerConnector.createAddress(address, uniqueID, description, spotType, isCovered, base64photo);
                    //reload the spinner
                    populateSpinnerWithAddresses(); //TODO: hopefully this works as a refresh
                } catch (Exception e){
                    Log.i("STATE","Error while creating new address");
                }
            }
        });
    }

    private void populateSpinnerWithAddresses() {
        try{
            addresses = ServerConnector.getAddressesOf(uniqueID);

            addressList = new ArrayList<>();
            for (int i = 0; i < addresses.size(); i++) {
                AddressDetailsDAO details = ServerConnector.getAddressDetails(addresses.get(i));
                addressList.add(details.getAddress());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, addressList);

            addressSelect.setAdapter(adapter);
        } catch (Exception e) {
            Log.i("STATE","Error while getting the addresses of " + uniqueID);
        }
    }

    private void setAddressSelectionListener() {
        addressSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Resources res = getResources();
                selectedPosition = parent.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
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
        if (!ValidationFunctions.needToGrantGalleryPermissions(this)){
            openGallery();
        }
        //else go to onRequestPermissionsResult for the intent to select a file.
    }

    private void openGallery() {
        Log.i("STATE","Open gallery.");
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
        intent.putExtra("id",uniqueID);
        intent.putExtra("addressID",addresses.get(selectedPosition));
        startActivity(intent);
    }
}
