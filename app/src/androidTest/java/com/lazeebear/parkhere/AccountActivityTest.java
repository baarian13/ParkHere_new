package com.lazeebear.parkhere;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;

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
 * Created by palet on 11/8/2016.
 */

@RunWith(AndroidJUnit4.class)
public class AccountActivityTest {

    private static final String OWNER_USERNAME = "";
    private static final String OWNER_PASSWORD = "";
    private static final String SEEKER_USERNAME = "";
    private static final String SEEKER_PASSWORD = "";
    private static final String BOTH_USERNAME = "";
    private static final String BOTH_PASSWORD = "";

    private static int LAUNCH_TIMEOUT = 2000;
    private static String PACKAGE_NAME = "com.lazeebear.parkhere";

    private Instrumentation instr;
    private UiDevice mDevice;

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

    /*
     * Tests if the rating bar, user type, settings button, and default profile pic
     * are visible on the Account page.
     * For now, the profile pic is not tested.
     */
    @Test
    public void testAccountPageGUI() {
        signInAsAnOwner();
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()));
        onView(withId(R.id.userType_account)).check(matches(isDisplayed()));
        //expect settings button
        //expect default profile pic
    }

    /*
     * Tests if, when the logged-in user is an owner
     *   - the user can create a new post through a button
     *   - the user has their own user and spot ratings
     *   - the account page shows the user as an owner
     */
    @Test
    public void testOwnerAccountPageGUI() {
        signInAsAnOwner();
        onView(withId(R.id.createSpotButton)).check(matches(isDisplayed()));
    }

    private void signInAsAnOwner() {
        onView(withId(R.id.email)).perform(typeText(OWNER_USERNAME));
        onView(withId(R.id.password)).perform(typeText(OWNER_PASSWORD));
        onView(withId(R.id.email_sign_in_button)).perform(click());
    }

    private void signInAsASeeker() {
        onView(withId(R.id.email)).perform(typeText(SEEKER_USERNAME));
        onView(withId(R.id.password)).perform(typeText(SEEKER_PASSWORD));
        onView(withId(R.id.email_sign_in_button)).perform(click());
    }

    private void signInAsBoth() {
        onView(withId(R.id.email)).perform(typeText(BOTH_USERNAME));
        onView(withId(R.id.password)).perform(typeText(BOTH_PASSWORD));
        onView(withId(R.id.email_sign_in_button)).perform(click());
    }
}
