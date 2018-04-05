package org.gammf.proxima.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.gammf.proxima.R;
import org.gammf.proxima.model.IPatientData;
import org.gammf.proxima.util.NfcUtilities;
import org.gammf.proxima.util.ModelUtilities;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity responsible for displaying the medical data of a certain patient. The data is passed inside the intent
 * used to start the activity itself, as an extra JSONObject.
 * The data is shown upon creation.
 */
public class DataPrinterActivity extends AppCompatActivity {

    private IPatientData mPatientData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_printer);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        try {
            final JSONObject jsonPatientData = new JSONObject(getIntent().getStringExtra("patientData"));
            mPatientData = ModelUtilities.jsonToPatientData(jsonPatientData);
        } catch (JSONException e) {
            Toast.makeText(this, R.string.parse_error, Toast.LENGTH_LONG).show();
            finish();
        }

        displayBaseInformation();
        displayMedicalConditions();
        displayAllergies();
        displayMedications();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcUtilities.setupForegroundDispatch(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcUtilities.stopForegroundDispatch(this);
    }

    private void displayBaseInformation() {
        ((TextView)findViewById(R.id.data_text_name)).setText(mPatientData.getName());
        ((TextView)findViewById(R.id.data_text_surname)).setText(mPatientData.getSurname());
        ((TextView)findViewById(R.id.data_text_fiscal_code)).setText(mPatientData.getFiscalCode());
        final String age = mPatientData.getAge() + "";
        ((TextView)findViewById(R.id.data_text_age)).setText(age);
        ((TextView)findViewById(R.id.data_text_blood_group)).setText(mPatientData.getBloodGroup());
        ((TextView)findViewById(R.id.data_text_organ_donor)).setText(mPatientData.isOrganDonor() ? "YES" : "NO");
    }

    private void displayMedicalConditions() {
        final ListView medicalConditions = findViewById(R.id.data_text_medical_conditions);
        medicalConditions.setAdapter(new ArrayAdapter<>(
                this, R.layout.list_custom_item, mPatientData.getMedicalConditions()));
    }

    private void displayAllergies() {
        final ListView drugAllergies = findViewById(R.id.data_text_drug_allergies);
        drugAllergies.setAdapter(new ArrayAdapter<>(this, R.layout.list_custom_item, mPatientData.getDrugAllergies()));

        final ListView otherAllergies = findViewById(R.id.data_text_other_allergies);
        otherAllergies.setAdapter(new ArrayAdapter<>(this, R.layout.list_custom_item, mPatientData.getOtherAllergies()));
    }

    private void displayMedications() {
        final ListView medications = findViewById(R.id.data_text_medications);
        medications.setAdapter(new ArrayAdapter<>(this, R.layout.list_custom_item, mPatientData.getMedications()));
    }
}
