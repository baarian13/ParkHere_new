package com.lazeebear.parkhere;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.BySelector;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    private static final String CLASS_TEXT_VIEW = "android.widget.TextView";
    private static final String CLASS_BUTTON = "android.widget.Button";
    private static final String LOADING_MESSAGE = "Loading";//R.string.loading_screen_message;

    private Instrumentation instr;
    private UiDevice mDevice;

    @Before
    public void launchAppFromHome() {
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

    // Byselector is used for UI elements that are Activity-dependent and may not exist on-screen if the View changes.
    @Test
    public void testLoadingPageAndLoginPageUponLaunch() {
        // check for existence of loading page.
        // message string on the loading page (to identify the loading page)
        BySelector loadingPageSelector = By.clazz(CLASS_TEXT_VIEW).textContains(LOADING_MESSAGE);

        //wait for the loading page to appear.
        assertTrue(mDevice.wait(Until.hasObject(loadingPageSelector), UI_TIMEOUT));

        // button on the login page (to identify the login page)
        BySelector loginButtonSelector = By.clazz(CLASS_BUTTON);

        //wait for login page to appear.
        assertTrue(mDevice.wait(Until.hasObject(loginButtonSelector), UI_TIMEOUT));
    }

