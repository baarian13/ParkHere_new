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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by palet on 11/9/2016.
 */

@RunWith(AndroidJUnit4.class)
public class SearchActivityTest {

    private static final String SEARCH_ADDRESS = "707 West 28th Street, Los Angeles, CA 90007";

    private static final int LAUNCH_TIMEOUT = 2000;
    private static final String PACKAGE_NAME = "com.lazeebear.parkhere"; //from AndroidManifest.xml

    private Instrumentation instr;
    private UiDevice mDevice;

    /* uses Ui Automator
     * NOTE: turns the DEBUG_MODE on for all tests, and navigates to the "Search" page from the
     * shortcuts.
     */
    @Before
    public void launchAppFromHome() throws InterruptedException {
        MainActivity.setDebugMode(true);
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

        onView(withId(R.id.mainpage_search_button)).perform(click());
    }

    @Test
    public void testSearchByAddress() {
        Log.i("STATE", "Starting testSearchByAddress()");
        onView(withId(R.id.address)).perform(typeText(SEARCH_ADDRESS));
        onView(withId(R.id.search_submit_button)).perform(click());
        onView(withId(R.id.reservedSpotId)).check(matches(isDisplayed()));
        Log.i("STATE", "  Success: the search turned up results.");
        Log.i("STATE", "  Complete");
    }
}
