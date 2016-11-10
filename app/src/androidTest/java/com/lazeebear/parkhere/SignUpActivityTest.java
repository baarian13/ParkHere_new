package com.lazeebear.parkhere;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.FlakyTest;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by palet on 11/6/2016.
 */

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    private static final int LAUNCH_TIMEOUT = 2000;
    private static final String PACKAGE_NAME = "com.lazeebear.parkhere"; //from AndroidManifest.xml

    private static String USER1_EMAIL = "abc";
    private static String USER1_EMAIL_DOMAIN = "@yahoo.com";
    private static String USER_PASSWORD = "Password!1";
    private static String USER_FIRST = "First";
    private static String USER_LAST = "Last";
    private static String USER_PHONE = "1231231234";

    private Instrumentation instr;
    private UiDevice mDevice;

    // UI Automator
    // Automatically launches the app in debug mode and navigates to the Sign Up page.
    @Before
    public void launchAppFromHomeAndGoToSignUpActivity() throws InterruptedException {
        MainActivity.setDebugMode(true);
        instr = InstrumentationRegistry.getInstrumentation();
        mDevice = UiDevice.getInstance(instr);

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

    // Tests that after inputting fresh input, a verification page should pop up.
    @Test
    public void testsSignUp() {
        Log.i("STATE","Starting testsSignUp()");
        //fill in new user info
        onView(withId(R.id.EmailEditText)).perform(typeText(USER1_EMAIL+USER1_EMAIL_DOMAIN));
        onView(withId(R.id.password_sign_up)).perform(typeText(USER_PASSWORD));
        onView(withId(R.id.passwordConfirm_sign_up)).perform(typeText(USER_PASSWORD));
        onView(withId(R.id.LastNameEditText)).perform(typeText(USER_FIRST));
        onView(withId(R.id.FirstNameEditText)).perform(typeText(USER_LAST));
        onView(withId(R.id.phoneNum_sign_up)).perform(typeText(USER_PHONE));
        //onView(withId(R.id.upload_verification_button)).perform(click());
        //TODO take a photo http://stackoverflow.com/questions/28019657/camera-operation-ui-testing-with-espresso
        //TODO choose user type
        onView(withId(R.id.sign_up_button)).perform(click());

        onView(withId(R.id.verification_needed_textView)).check(matches(isDisplayed()));
        Log.i("STATE","  Completed successfully");
    }
}
