package org.gammf.proxima.util;

public final class CommunicationUtilities {

    private static final String SERVICE_NAME = "proxima.medical.firstAid";
    private static final String DATA_RESOURCE_NAME = "data";
    private static final String PATIENT_QUERY_PARAMETER = "targetID";
    private static final String RESCUER_QUERY_PARAMETER = "operatorID";
    private static final String SERVICE_QUERY_PARAMETER = "service";
    private static final String SIGNATURE_QUERY_PARAMETER = "signature";

    private CommunicationUtilities() {}

    public static String buildMedicalDataUrl(final String patientIdentifier, final String rescuerIdentifier, final String signature) {
        return ServerParameters.PROTOCOL +"://" +
                ServerParameters.SERVER_IP + ":" +
                ServerParameters.SERVER_PORT + "/" +
                DATA_RESOURCE_NAME + "?" + PATIENT_QUERY_PARAMETER + "=" + patientIdentifier + "&"
                                         + RESCUER_QUERY_PARAMETER + "=" + rescuerIdentifier + "&"
                                         + SERVICE_QUERY_PARAMETER + "=" + SERVICE_NAME + "&"
                                         + SIGNATURE_QUERY_PARAMETER + "=" + signature;
    }
}
