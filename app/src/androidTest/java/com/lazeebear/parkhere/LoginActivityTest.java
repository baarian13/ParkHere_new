package com.lazeebear.parkhere;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.provider.Contacts;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.util.Assert;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by palet on 11/5/2016.
 */

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    private static final int LAUNCH_TIMEOUT = 2000; //2 seconds
    private static final int UI_TIMEOUT = 2000; //2 seconds
    private static final String PACKAGE_NAME = "com.lazeebear.parkhere"; //from AndroidManifest.xml

    private static final String USERNAME_VERIFIED = "rjason14@gmail.com"; //TODO.
    private static final String PASSWORD_VERIFIED = "Password!1";
    private static final String USERNAME_UNVERIFIED = ""; //TODO verification not yet implemented
    private static final String PASSWORD_UNVERIFIED = PASSWORD_VERIFIED;

    private Instrumentation instr;
    private UiDevice mDevice;
    private boolean testMode;

    // uses Ui Automator
    // NOTE: turns the DEBUG_MODE off for all tests, as all these tests assume the app is at the
    // login page, which will happen automatically if the debug mode is off.
    @Before
    public void launchAppFromHome() throws InterruptedException {
        MainActivity.setDebugMode(false);
        instr = InstrumentationRegistry.getInstrumentation();
        mDevice = UiDevice.getInstance(instr); //this is returning null

        // Wait for launcher
        final String launcherPackage = mDevice.getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)),
                LAUNCH_TIMEOUT);

        // Launch the app
        Context context = instr.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(PACKAGE_NAME);
        // Clear out any previous instances
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(PACKAGE_NAME).depth(0)), LAUNCH_TIMEOUT);
    }

    // uses Espresso.
    // checks if, at the end of loading, the login page appears.
    @Test
    public void testLoadingPageAndLoginPageUponLaunch() {
        Log.i("STATE","Starting testLoadingPageAndLoginPageUponLaunch()");
        onView(withId(R.id.register_button)).check(matches(isDisplayed()));
        Log.i("STATE","  Completed successfully");
    }

    // checks if login page's "Register" button leads to the Sign Up page
    @Test
    public void testLoginPageRegisterButton() {
        Log.i("STATE","Starting testLoginPageRegisterButton()");
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.sign_up_button)).check(matches(isDisplayed()));
        Log.i("STATE","  Complete");
    }

    // tests if signing in with a verified account will bring you to the account page
    @Test
    public void testLoginPageInputVerification() {
        Log.i("STATE","Starting testLoginPageInputVerification()");
        onView(withId(R.id.email)).perform(typeText(USERNAME_VERIFIED));
        onView(withId(R.id.password)).perform(typeText(PASSWORD_VERIFIED));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        //check if the corresponding account page (identified by the rating bar) shows up.
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()));
        Log.i("STATE","  Complete");
    }

    // tests if signing in with an unverified account will bring you to the verification needed page
    @Test
    public void testLoginPageUserVerificationPage() {
        Log.i("STATE","Starting testLoginPageUserVerification()");
        onView(withId(R.id.email)).perform(typeText(USERNAME_UNVERIFIED));
        onView(withId(R.id.password)).perform(typeText(PASSWORD_UNVERIFIED));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        //since server can't check if the user if verified yet, manually set it to check the funcitonality.
        LoginActivity.setVerified(false);
        // check if user verification page shows up
        onView(withId(R.id.verification_needed_textView)).check(matches(isDisplayed()));
        LoginActivity.setVerified(true); //reset the verified boolean.
        Log.i("STATE","  Complete");
    }
}
