package org.gammf.proxima.model;

import java.util.List;

public interface IPatientData {
    String getName();
    String getSurname();
    String getFiscalCode();
    Integer getAge();
    String getBloodGroup();
    Boolean isOrganDonor();
    List<String> getMedicalConditions();
    List<String> getDrugAllergies();
    List<String> getOtherAllergies();
    List<String> getMedications();
}
