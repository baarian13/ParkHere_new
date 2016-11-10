package com.lazeebear.parkhere;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.Until;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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

    private static final String both = getContext().getResources().getStringArray(R.array.user_types)[1];
    private static final String owner = getContext().getResources().getStringArray(R.array.user_types)[2];
    private static final String seeker = getContext().getResources().getStringArray(R.array.user_types)[3];

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
        Log.i("STATE","Starting testAccountPageGUI()");
        signInAsAnOwner();
        onView(withId(R.id.ratingBar)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: ratingBar is displayed");
        onView(withId(R.id.userType_account)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: account type is displayed");
        //expect settings button
        //TODO expect default profile pic when implemented
        Log.i("STATE","  Completed");
    }

    /*
     * Tests if, when the logged-in user is an owner
     *   - the user can create a new post through a button
     *   - the user has their own user and spot ratings
     *   - the account page shows the user as an owner
     *   - the user can edit their account type (to be "both")
     */
    @Test
    public void testOwnerAccountPageGUI() {
        Log.i("STATE","Starting testOwnerAccountPageGUI()");
        signInAsAnOwner();
        onView(withId(R.id.createSpotButton)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: create spot button is displayed");
        onView(withId(R.id.userType_account)).check(matches(withText(owner)));
        Log.i("STATE","  Success: account type is Owner");
        onView(withId(R.id.editUserTypeButton_account)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: edit user type button is displayed");
        onView(withId(R.id.ownedSpotsButton)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: owned spots button is displayed");
        Log.i("STATE","  Completed");
    }

    /*
     * Signs in as an Owner with spots.
     * Upon clicking the view owned spots button, expects to see a populated list of spots owned.
     */
    @Test
    public void testOwnerHistoryOfSpotsList() {
        Log.i("STATE", "Starting testOwnerHistoryOfSpotsList()");
        signInAsAnOwner();
        onView(withId(R.id.ownedSpotsButton)).perform(click());
        onView(withId(R.id.spotList_account)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: spot list is displayed");
        onView(withId(R.id.reservedSpotId)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: spot list is not empty");
        Log.i("STATE","  Completed");
    }

    /*
     * Signs in as a Seeker with spots.
     * Upon clicking the view spot history button, expects to see a populated list of spots rented.
     */
    @Test
    public void testSeekerHistoryOfSpotsList() {
        Log.i("STATE", "Starting testSeekerHistoryOfSpotsList()");
        signInAsASeeker();
        onView(withId(R.id.spotHistoryButton_account)).perform(click());
        onView(withId(R.id.spotList_account)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: spot list is displayed");
        onView(withId(R.id.reservedSpotId)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: spot list is not empty");
        Log.i("STATE","  Completed");
    }

    /*
     * Tests if, when the logged-in user is a seeker
     *   - the user can
     *   - the user's account type is "Seeker"
     *    - the user can edit their account type (to be "both")
     */
    @Test
    public void testSeekerAccountPageGUI() {
        Log.i("STATE", "Starting testSeekerAccountPageGUI()");
        signInAsASeeker();
        onView(withId(R.id.userType_account)).check(matches(withText(seeker)));
        onView(withId(R.id.editUserTypeButton_account)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: edit user type button is displayed");
        onView(withId(R.id.spotHistoryButton_account)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: spot history button is displayed");
        Log.i("STATE","  Completed");
    }

    /*
     * Tests that an Owner can edit the information in a spot that they own.
     */
    @Test
    public void testOwnerHistoryOfSpotsListPopup() {
        Log.i("STATE", "Starting testOwnerHistoryOfSpotsListPopup()");
        signInAsAnOwner();
        onView(withId(R.id.spotHistoryButton_account)).perform(click());
        onView(withId(R.id.editPriceButton_spotDetail)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: edit price button is displayed");
        onView(withId(R.id.deleteSpotButton_spotDetail)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: delete spot button is displayed");
        Log.i("STATE","  Completed");
    }

    /*
     * Tests that a Seeker can view the policy of a spot that they reserved.
     */
    @Test
    public void testSeekerViewCancellationPolicy() {
        Log.i("STATE", "Starting testSeekerHistoryOfSpotsListPopup()");
        signInAsASeeker();
        onView(withId(R.id.spotHistoryButton_account)).perform(click());
        onView(withId(R.id.cancellation_policy_spotDetail)).check(matches(isDisplayed()));
        Log.i("STATE","  Success: cancellation policy is displayed");
        Log.i("STATE","  Completed");
    }

    // helper functions //
    private void signInAsAnOwner() {
        Log.i("STATE", "Signing in as an Owner with username: " + OWNER_USERNAME
               + " and password: " + OWNER_PASSWORD);
        onView(withId(R.id.email)).perform(typeText(OWNER_USERNAME));
        onView(withId(R.id.password)).perform(typeText(OWNER_PASSWORD));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        Log.i("STATE", "Completed signing in as an Owner.");
    }

    private void signInAsASeeker() {
        Log.i("STATE", "Signing in as an Seeker with username: " + SEEKER_USERNAME
                + " and password: " + SEEKER_PASSWORD);
        onView(withId(R.id.email)).perform(typeText(SEEKER_USERNAME));
        onView(withId(R.id.password)).perform(typeText(SEEKER_PASSWORD));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        Log.i("STATE", "Completed signing in as a Seeker.");
    }

    private void signInAsBoth() {
        Log.i("STATE", "Signing in as Both with username: " + BOTH_USERNAME
                + " and password: " + BOTH_PASSWORD);
        onView(withId(R.id.email)).perform(typeText(BOTH_USERNAME));
        onView(withId(R.id.password)).perform(typeText(BOTH_PASSWORD));
        onView(withId(R.id.email_sign_in_button)).perform(click());
        Log.i("STATE", "Completed signing in as Both.");
    }
}
