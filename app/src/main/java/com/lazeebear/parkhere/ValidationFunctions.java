package com.lazeebear.parkhere;

/**
 * Created by Zhicheng on 10/20/2016.
 */

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ValidationFunctions {

    public static final int minLengthOfPassword = 10;

    public static boolean isEmailAddress(String s) {
        // email address should not be empty
        if (s == null || s.trim().isEmpty()) {
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
}
