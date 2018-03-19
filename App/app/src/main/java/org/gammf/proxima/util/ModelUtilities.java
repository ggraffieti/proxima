package org.gammf.proxima.util;


import org.gammf.proxima.model.IPatientData;
import org.gammf.proxima.model.PatientData;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class ModelUtilities {

    private ModelUtilities() {}

    public static IPatientData jsonToPatientData(final JSONObject jsonObject) throws JSONException {
        final PatientData.Builder builder = new PatientData.Builder(jsonObject.getString("name"),
                                                                    jsonObject.getString("surname"));

        final List<String> medicalConditions = new ArrayList<>();
        final JSONArray jsonMedicalConditions = jsonObject.getJSONArray("medicalConditions");
        for(int i = 0; i < jsonMedicalConditions.length(); i++) {
            medicalConditions.add(jsonMedicalConditions.getString(i));
        }


        final List<String> drugAllergies = new ArrayList<>();
        final JSONArray jsonDrugAllergies = jsonObject.getJSONArray("drugAllergies");
        for(int i = 0; i < jsonDrugAllergies.length(); i++) {
            drugAllergies.add(jsonDrugAllergies.getString(i));
        }

        final List<String> otherAllergies = new ArrayList<>();
        final JSONArray jsonOtherAllergies = jsonObject.getJSONArray("otherAllergies");
        for(int i = 0; i < jsonOtherAllergies.length(); i++) {
            otherAllergies.add(jsonOtherAllergies.getString(i));
        }

        final List<String> medications = new ArrayList<>();
        final JSONArray jsonMedications = jsonObject.getJSONArray("medications");
        for(int i = 0; i < jsonMedications.length(); i++) {
            medications.add(jsonMedications.getString(i));
        }

        return builder.setFiscalCode(jsonObject.getString("CF"))
               .setAge(new Period(new DateTime(jsonObject.getString("birthDate")), DateTime.now()).getYears())
               .setBloodGroup(jsonObject.getString("bloodGroup"))
               .setIsOrganDonor(jsonObject.getString("organDonor").equals("YES"))
               .setMedicalConditions(medicalConditions)
               .setDrugAllergies(drugAllergies)
               .setOtherAllergies(otherAllergies)
               .setMedications(medications)
               .build();
    }
}
