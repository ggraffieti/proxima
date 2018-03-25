package org.gammf.proxima.android;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.gammf.proxima.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.Espresso.*;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DataPrinterActivityTest {

    @Rule
    public ActivityTestRule<DataPrinterActivity> mActivityRule = new ActivityTestRule<DataPrinterActivity>(DataPrinterActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            final Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
            final Intent intent = new Intent(targetContext, DataPrinterActivity.class);
            intent.putExtra("patientData", "{\"name\": \"John\",\"surname\": \"Doe\",\"CF\": \"DOEJHN70P16F205Y\",\"birthDate\": \"1970-09-16T00:00:00.000Z\",\"bloodGroup\": \"A+\",\"organDonor\": \"YES\",\"medicalConditions\": [\"Heart Disease\",\"Diabetes type 2\",\"Pacemaker\"],\"drugAllergies\": [\"Penicillin\",\"Morphine\"],\"otherAllergies\": [\"Peanuts\",\"Bee sting\",\"Latex\",\"Wheat\"],\"medications\": [\"Prozac\",\"Refludan\",\"Hydrochiorothiazide\",\"Cialis\",\"Plavix\"]}");
            return intent;
        }
    };

    @Test
    public void atomicFieldsTest() {
        onView(withId(R.id.data_text_name)).check(matches(withText("John")));
        onView(withId(R.id.data_text_surname)).check(matches(withText("Doe")));
        onView(withId(R.id.data_text_age)).check(matches(withText("47")));
        onView(withId(R.id.data_text_fiscal_code)).check(matches(withText("DOEJHN70P16F205Y")));
        onView(withId(R.id.data_text_organ_donor)).check(matches(withText("YES")));
        onView(withId(R.id.data_text_blood_group)).check(matches(withText("A+")));
    }

    @Test
    public void medicalConditionsTest() {
        onData(anything()).inAdapterView(withId(R.id.data_text_medical_conditions))
                .atPosition(0)
                .check(matches(withText("Heart Disease")));
        onData(anything()).inAdapterView(withId(R.id.data_text_medical_conditions))
                .atPosition(1)
                .check(matches(withText("Diabetes type 2")));
        onData(anything()).inAdapterView(withId(R.id.data_text_medical_conditions))
                .atPosition(2)
                .check(matches(withText("Pacemaker")));
    }

    @Test
    public void drugAllergiesTest() {
        onData(anything()).inAdapterView(withId(R.id.data_text_drug_allergies))
                .atPosition(0)
                .check(matches(withText("Penicillin")));
        onData(anything()).inAdapterView(withId(R.id.data_text_drug_allergies))
                .atPosition(1)
                .check(matches(withText("Morphine")));
    }

    @Test
    public void otherAllergiesTest() {
        onData(anything()).inAdapterView(withId(R.id.data_text_other_allergies))
                .atPosition(0)
                .check(matches(withText("Peanuts")));
        onData(anything()).inAdapterView(withId(R.id.data_text_other_allergies))
                .atPosition(1)
                .check(matches(withText("Bee sting")));
        onData(anything()).inAdapterView(withId(R.id.data_text_other_allergies))
                .atPosition(2)
                .check(matches(withText("Latex")));
        onData(anything()).inAdapterView(withId(R.id.data_text_other_allergies))
                .atPosition(3)
                .check(matches(withText("Wheat")));
    }

    @Test
    public void medicationsTest() {
        onData(anything()).inAdapterView(withId(R.id.data_text_medications))
                .atPosition(0)
                .check(matches(withText("Prozac")));
        onData(anything()).inAdapterView(withId(R.id.data_text_medications))
                .atPosition(1)
                .check(matches(withText("Refludan")));
        onData(anything()).inAdapterView(withId(R.id.data_text_medications))
                .atPosition(2)
                .check(matches(withText("Hydrochiorothiazide")));
        onData(anything()).inAdapterView(withId(R.id.data_text_medications))
                .atPosition(3)
                .check(matches(withText("Cialis")));
        onData(anything()).inAdapterView(withId(R.id.data_text_medications))
                .atPosition(4)
                .check(matches(withText("Plavix")));
    }
}