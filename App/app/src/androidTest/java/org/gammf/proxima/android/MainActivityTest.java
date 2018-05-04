package org.gammf.proxima.android;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.gammf.proxima.R;
import org.gammf.proxima.util.CommunicationUtilities;
import org.gammf.proxima.util.NfcUtilities;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.Espresso.*;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void homeButtonTest() {
        if(CommunicationUtilities.isProximaServerAvailable()) {
            onView(withId(R.id.homeButton))
                    .check(matches(withText(R.string.home_button_text_default)))
                    .perform(click())
                    .check(matches(withText(R.string.home_button_text_stop_searching)));
        } else {
            onView(withId(R.id.homeButton)).check(matches(not(isEnabled())));
        }
    }

    @Test
    public void homeTextViewTest() throws InterruptedException {
        if(CommunicationUtilities.isProximaServerAvailable()) {
            onView(withId(R.id.homeTextView)).check(matches(withText(R.string.home_message_default)));
            onView(withId(R.id.homeButton)).perform(click());
            onView(withId(R.id.homeTextView)).check(matches(withText(R.string.home_message_first_search)));

            final Intent intent = new Intent("android.nfc.action.NDEF_DISCOVERED");
            intent.setAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            intent.setType(NfcUtilities.MIME_TEXT_PLAIN);
            mActivityRule.getActivity().onNewIntent(intent);
            Thread.sleep(100);
            onView(withId(R.id.homeTextView)).check(matches(withText(R.string.home_message_second_search)));

            mActivityRule.getActivity().onNewIntent(intent);
            Thread.sleep(100);
            onView(withId(R.id.homeTextView)).check(matches(withText(R.string.home_message_sending_request)));

            Thread.sleep(1500);
            onView(withId(R.id.homeTextView)).check(matches(withText(R.string.home_message_first_search)));

        } else {
            onView(withId(R.id.homeTextView)).check(matches(withText(R.string.home_message_no_server_available)));
        }
    }
}