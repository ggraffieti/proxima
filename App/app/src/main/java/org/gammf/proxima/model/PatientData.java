package org.gammf.proxima.model;

import java.util.List;

/**
 * Simple implementation of {@link IPatientData}, including a builder as inner class.
 */
public class PatientData implements IPatientData {

    private final String mName;
    private final String mSurname;
    private final String mFiscalCode;
    private final Integer mAge;
    private final String mBloodGroup;
    private final Boolean mIsOrganDonor;
    private final List<String> mMedicalConditions;
    private final List<String> mDrugAllergies;
    private final List<String> mOtherAllergies;
    private final List<String> mMedications;

    private PatientData(final String name, final String surname, final String fiscalCode,
                        final Integer age, final String bloodGroup, final Boolean isOrganDonor,
                        final List<String> medicalConditions, final List<String> drugAllergies,
                        final List<String> otherAllergies, final List<String> medications) {
        mName = name;
        mSurname = surname;
        mFiscalCode = fiscalCode;
        mAge = age;
        mBloodGroup = bloodGroup;
        mIsOrganDonor = isOrganDonor;
        mMedicalConditions = medicalConditions;
        mDrugAllergies = drugAllergies;
        mOtherAllergies = otherAllergies;
        mMedications = medications;
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getSurname() {
        return mSurname;
    }

    @Override
    public String getFiscalCode() {
        return mFiscalCode;
    }

    @Override
    public Integer getAge() {
        return mAge;
    }

    @Override
    public String getBloodGroup() {
        return mBloodGroup;
    }

    @Override
    public Boolean isOrganDonor() {
        return mIsOrganDonor;
    }

    @Override
    public List<String> getMedicalConditions() {
        return mMedicalConditions;
    }

    @Override
    public List<String> getDrugAllergies() {
        return mDrugAllergies;
    }

    @Override
    public List<String> getOtherAllergies() {
        return mOtherAllergies;
    }

    @Override
    public List<String> getMedications() {
        return mMedications;
    }

    /**
     * Builder that can be used to build a {@link PatientData} object.
     */
    public static class Builder {
        private String mName;
        private String mSurname;
        private String mFiscalCode;
        private Integer mAge;
        private String mBloodGroup;
        private Boolean mIsOrganDonor;
        private List<String> mMedicalConditions;
        private List<String> mDrugAllergies;
        private List<String> mOtherAllergies;
        private List<String> mMedications;

        public Builder(final String name, final String surname) {
            mName = name;
            mSurname = surname;
        }

        public Builder setFiscalCode(final String fiscalCode) {
            mFiscalCode = fiscalCode;
            return this;
        }

        public Builder setAge(final Integer age) {
            mAge = age;
            return this;
        }

        public Builder setBloodGroup(final String bloodGroup) {
            mBloodGroup = bloodGroup;
            return this;
        }

        public Builder setIsOrganDonor(final Boolean isOrganDonor) {
            mIsOrganDonor = isOrganDonor;
            return this;
        }

        public Builder setMedicalConditions(final List<String> medicalConditions) {
            mMedicalConditions = medicalConditions;
            return this;
        }

        public Builder setDrugAllergies(final List<String> drugAllergies) {
            mDrugAllergies = drugAllergies;
            return this;
        }

        public Builder setOtherAllergies(final List<String> otherAllergies) {
            mOtherAllergies = otherAllergies;
            return this;
        }

        public Builder setMedications(final List<String> medications) {
            mMedications = medications;
            return this;
        }

        public IPatientData build() {
            return new PatientData(mName, mSurname, mFiscalCode, mAge,
                                   mBloodGroup, mIsOrganDonor, mMedicalConditions,
                                   mDrugAllergies, mOtherAllergies, mMedications);
        }
    }
}
