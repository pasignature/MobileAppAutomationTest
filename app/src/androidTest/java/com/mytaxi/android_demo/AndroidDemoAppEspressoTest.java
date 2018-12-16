package com.mytaxi.android_demo;

import android.Manifest;
import android.os.SystemClock;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.mytaxi.android_demo.activities.AuthenticationActivity;
import com.mytaxi.android_demo.activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AndroidDemoAppEspressoTest {

    @Rule
    public ActivityTestRule<AuthenticationActivity> nActivityRule
            = new ActivityTestRule<>(AuthenticationActivity.class);
    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule
            .grant(Manifest.permission.ACCESS_FINE_LOCATION);

   @Before
    public void setup(){
       //IdlingRegistry.getInstance().register();
       nActivityRule.getActivity();
    }

    @After
    public void unregisterIdlingResource() {
        // Unregister your Idling Resource so it can be garbage collected and does not leak any memory
        //IdlingRegistry.getInstance().unregister(); //disabled
        nActivityRule.finishActivity();
    }

    @Test
    public void shouldLoginSearchAutocompleteAndCallDriver() {
        //type in username //hardcoding is fine for demo ONLY
        onView(withId(R.id.edt_username)).perform(typeText("crazydog335"));

        //type in password and close the Soft Keyboard
        onView(withId(R.id.edt_password)).perform(typeText("venture"),
                closeSoftKeyboard());

        //click login button
        onView(withId(R.id.btn_login)).perform(click());

        //resume the activity after successful login
        ActivityTestRule<MainActivity> mActivityRule
                = new ActivityTestRule<>(MainActivity.class, true, false);
        mActivityRule.launchActivity(null);

        //better to use idling resources but overkill here.
        //wait for activity to resume after authentication and search box to be loaded
        SystemClock.sleep(2000);

        //type "sa" to trigger driver name suggestions
        onView(withId(R.id.textSearch)).perform(typeText("sa"),
                closeSoftKeyboard());
        SystemClock.sleep(2000);

        // select the 2nd result via the driver's name
        onView(withText("Sarah Scott"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow()
                        .getDecorView())))).perform(click());
        SystemClock.sleep(2000);

        // Check that the selected driver's name is displayed
        onView(withText("Sarah Scott")).check(matches(isDisplayed()));

        //Call the driver
        onView(withId(R.id.fab)).perform(click());
    }

}