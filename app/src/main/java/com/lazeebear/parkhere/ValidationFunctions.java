package com.lazeebear.parkhere;

/**
 * Created by Zhicheng on 10/20/2016.
 */

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static java.security.AccessController.getContext;

public class ValidationFunctions {

    public static final int minLengthOfPassword = 10;

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
}
