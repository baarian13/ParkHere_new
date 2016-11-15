package com.lazeebear.parkhere;

/**
 * Created by Zhicheng on 10/20/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static java.security.AccessController.getContext;

public class ValidationFunctions {

    public static final int minLengthOfPassword = 10;

    //for requesting storage permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 3;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean isEmailAddress(String s) {
        // email address should not be empty
        if (s == null || s.trim().isEmpty() || s.length() < 4) {
            return false;
        }

        // email should end with ".com"
        if(!s.substring(s.length() - 4).equalsIgnoreCase(".com"))
            return false;

        // email should have "@"
        Pattern p = Pattern.compile("@");
        Matcher m = p.matcher(s);
        boolean b = m.find();
        if (b == true)
            return true;
        else
            return false;
    }

    public static boolean hasEnoughLength(String s){
        if (s.length() >= minLengthOfPassword)
            return true;
        return false;
    }

    public static boolean hasSpecialCharacter(String s) {
        // s should not be empty
        if (s == null || s.trim().isEmpty()) {
            return false;
        }

        // s should have at least 1 special character
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(s);
        boolean b = m.find();
        if (b == true)
            //System.out.println("There is a special character in my string ");
            return true;
        else
            //System.out.println("There is no special char.");
            return false;
    }

    public static boolean isPhoneNum(String s){
        // s should not be empty
        if (s == null || s.trim().isEmpty() || s.length()!=10)
            return false;

        // s should have no dot (.)
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(s);
        boolean b = m.find();
        if (b == true)
            return false; // Should not have other character than number, so false if there is
        else
            return true;
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES); //will be private to the app
        File storageDir = context.getFilesDir(); //can be accessed by the Media scanned ("upload photos" button)
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        // String mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    //required to call before every "Read gallery" activity on API 23+
    public static boolean needToGrantGalleryPermissions(Activity activity) {
        Log.i("STATE", "Checking permissions");
        int writeGalleryPermission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writeGalleryPermission != PackageManager.PERMISSION_GRANTED) {
            //prompt the user for permission
            ActivityCompat.requestPermissions(activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
            Log.i("STATE", "  Asked for permissions. Permission denied, so requested.");
            return true;
        } else {
            Log.i("STATE", "  Asked for permissions. Permission granted.");
            return false;
        }
    }

    // Returns the filepath of the selected Gallery photo
    public static String decodeURIDataToImagePath(Activity activity, Intent data) {
        Uri selectedImageUri = data.getData();

        //selectedImagePath = getPath(selectedImageUri);
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = activity.getContentResolver().query(selectedImageUri,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String selectedImagePath = cursor.getString(columnIndex);
        cursor.close();
        return selectedImagePath;
    }

    public static int getRequestExternalStorage() { return REQUEST_EXTERNAL_STORAGE; }
}
