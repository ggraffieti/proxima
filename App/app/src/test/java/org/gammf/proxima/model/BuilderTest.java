package org.gammf.proxima.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BuilderTest {

    private IPatientData patientData;
    private List<String> expectedMedicalConditions;
    private List<String> expectedDrugAllergies;
    private List<String> expectedOtherAllergies;
    private List<String> expectedMedications;

    @Before
    public void setUp() throws Exception {
        expectedMedicalConditions = new ArrayList<>();
        expectedMedicalConditions.add("Heart Disease");
        expectedMedicalConditions.add("Diabetes type 2");
        expectedMedicalConditions.add("Pacemaker");

        expectedDrugAllergies = new ArrayList<>();
        expectedDrugAllergies.add("Penicillin");
        expectedDrugAllergies.add("Morphine");

        expectedOtherAllergies = new ArrayList<>();
        expectedOtherAllergies.add("Peanuts");
        expectedOtherAllergies.add("Bee sting");
        expectedOtherAllergies.add("Latex");
        expectedOtherAllergies.add("Wheat");

        expectedMedications = new ArrayList<>();
        expectedMedications.add("Prozac");
        expectedMedications.add("Refludan");
        expectedMedications.add("Hydrochiorothiazide");
        expectedMedications.add("Cialis");
        expectedMedications.add("Plavix");

        patientData = new PatientData.Builder("John", "Doe")
                .setAge(47)
                .setFiscalCode("DOEJHN70P16F205Y")
                .setBloodGroup("A+")
                .setIsOrganDonor(true)
                .setMedicalConditions(expectedMedicalConditions)
                .setDrugAllergies(expectedDrugAllergies)
                .setOtherAllergies(expectedOtherAllergies)
                .setMedications(expectedMedications)
                .build();

    }

    @Test
    public void builderTest() {
        assertEquals("John", patientData.getName());
        assertEquals("Doe", patientData.getSurname());
        assertEquals("DOEJHN70P16F205Y", patientData.getFiscalCode());
        assertEquals(47, patientData.getAge().intValue());
        assertEquals("A+", patientData.getBloodGroup());
        assertEquals(true, patientData.isOrganDonor());
        assertTrue(patientData.getMedicalConditions().equals(expectedMedicalConditions));
        assertTrue(patientData.getDrugAllergies().equals(expectedDrugAllergies));
        assertTrue(patientData.getOtherAllergies().equals(expectedOtherAllergies));
        assertTrue(patientData.getMedications().equals(expectedMedications));
    }

}