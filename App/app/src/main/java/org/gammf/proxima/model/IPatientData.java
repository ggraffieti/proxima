package org.gammf.proxima.model;

import java.util.List;

/**
 * Interface representing the patient data.
 */
public interface IPatientData {
    /**
     * Getter for the patient name.
     * @return the patient name.
     */
    String getName();

    /**
     * Getter for the patient surname.
     * @return the patient surname.
     */
    String getSurname();

    /**
     * Getter for the patient italian fiscal code.
     * @return the patient italian fiscal code.
     */
    String getFiscalCode();

    /**
     * Getter for the patient age.
     * @return the patient age.
     */
    Integer getAge();

    /**
     * Getter for the patient blood group.
     * @return the patient blood group.
     */
    String getBloodGroup();

    /**
     * Getter that can be used to know if the patient is an organ donor or not.
     * @return true if the patient is an organ donor, false otherwise.
     */
    Boolean isOrganDonor();

    /**
     * Getter for the patient medical conditions.
     * @return the patient medical conditions.
     */
    List<String> getMedicalConditions();

    /**
     * Getter for the patient drug allergies.
     * @return the patient drug allergies.
     */
    List<String> getDrugAllergies();

    /**
     * Getter for the patient general allergies.
     * @return the patient general allergies.
     */
    List<String> getOtherAllergies();

    /**
     * Getter for the patient medications.
     * @return the patient medications.
     */
    List<String> getMedications();
}
