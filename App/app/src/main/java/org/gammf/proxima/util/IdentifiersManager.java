package org.gammf.proxima.util;

/**
 * Manger for rescuer and patient identifiers.
 */
public final class IdentifiersManager {

    private static String mRescuerIdentifier = "";
    private static String mPatientIdentifier = "";

    private IdentifiersManager() {}

    /**
     * Stores the rescuer identifier.
     * @param identifier the identifier.
     */
    public static void setRescuerIdentifier(final String identifier) {
        mRescuerIdentifier = identifier;
    }

    /**
     * Stores the patient identifier.
     * @param identifier the identifier.
     */
    public static void setPatientIdentifier(final String identifier) {
        mPatientIdentifier = identifier;
    }

    /**
     * Gets the rescuer identifier.
     * @return the rescuer identifier.
     */
    public static String getRescuerIdentifier() {
        return mRescuerIdentifier;
    }

    /**
     * Gets the patient identifier.
     * @return the patient identifier.
     */
    public static String getPatientIdentifier() {
        return mPatientIdentifier;
    }

}
