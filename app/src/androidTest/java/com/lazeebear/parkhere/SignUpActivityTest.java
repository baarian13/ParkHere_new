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

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by palet on 11/6/2016.
 */

@RunWith(AndroidJUnit4.class)
public class SignUpActivityTest {

    private static final int LAUNCH_TIMEOUT = 2000;
    private static final String PACKAGE_NAME = "com.lazeebear.parkhere"; //from AndroidManifest.xml

    private static String UNVERIFIED_NEW_EMAIL = "";    //TODO
    private static String UNVERIFIED_NEW_PASSWORD = ""; //TODO

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

        Log.i("STATE","  Completed successfully");
    }
}
