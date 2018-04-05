package org.gammf.proxima.util;

import org.gammf.proxima.model.IPatientData;
import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ModelUtilitiesTest {
    @Test
    public void jsonToPatientDataTest() throws Exception {
        final IPatientData patientData = ModelUtilities.jsonToPatientData(new JSONObject("{\"name\": \"John\",\"surname\": \"Doe\",\"CF\": \"DOEJHN70P16F205Y\",\"birthDate\": \"1970-09-16T00:00:00.000Z\",\"bloodGroup\": \"A+\",\"organDonor\": \"YES\",\"medicalConditions\": [\"Heart Disease\",\"Diabetes type 2\",\"Pacemaker\"],\"drugAllergies\": [\"Penicillin\",\"Morphine\"],\"otherAllergies\": [\"Peanuts\",\"Bee sting\",\"Latex\",\"Wheat\"],\"medications\": [\"Prozac\",\"Refludan\",\"Hydrochiorothiazide\",\"Cialis\",\"Plavix\"]}"));
        assertEquals("John", patientData.getName());
        assertEquals("Doe", patientData.getSurname());
        assertEquals("DOEJHN70P16F205Y", patientData.getFiscalCode());
        assertEquals(47, patientData.getAge().intValue());
        assertEquals("A+", patientData.getBloodGroup());
        assertEquals(true, patientData.isOrganDonor());

        final List<String> expectedMedicalConditions = new ArrayList<>();
        expectedMedicalConditions.add("Heart Disease");
        expectedMedicalConditions.add("Diabetes type 2");
        expectedMedicalConditions.add("Pacemaker");
        final List<String> actualMedicalConditions = patientData.getMedicalConditions();
        assertTrue(actualMedicalConditions.equals(expectedMedicalConditions));

        final List<String> expectedDrugAllergies = new ArrayList<>();
        expectedDrugAllergies.add("Penicillin");
        expectedDrugAllergies.add("Morphine");
        final List<String> actualDrugAllergies = patientData.getDrugAllergies();
        assertTrue(actualDrugAllergies.equals(expectedDrugAllergies));

        final List<String> expectedOtherAllergies = new ArrayList<>();
        expectedOtherAllergies.add("Peanuts");
        expectedOtherAllergies.add("Bee sting");
        expectedOtherAllergies.add("Latex");
        expectedOtherAllergies.add("Wheat");
        final List<String> actualOtherAllergies = patientData.getOtherAllergies();
        assertTrue(actualOtherAllergies.equals(expectedOtherAllergies));

        final List<String> expectedMedications = new ArrayList<>();
        expectedMedications.add("Prozac");
        expectedMedications.add("Refludan");
        expectedMedications.add("Hydrochiorothiazide");
        expectedMedications.add("Cialis");
        expectedMedications.add("Plavix");
        final List<String> actualMedications = patientData.getMedications();
        assertTrue(actualMedications.equals(expectedMedications));
    }
}