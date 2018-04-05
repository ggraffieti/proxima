package org.gammf.proxima.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Utility class, meant to contain class-independent/communication-related methods.
 */
public final class CommunicationUtilities {

    private final static Integer TIMEOUT = 1000;

    private static final String SERVICE_NAME = "proxima.medical.firstAid";
    private static final String DATA_RESOURCE_NAME = "data";
    private static final String PATIENT_QUERY_PARAMETER = "targetID";
    private static final String RESCUER_QUERY_PARAMETER = "operatorID";
    private static final String SERVICE_QUERY_PARAMETER = "service";
    private static final String SIGNATURE_QUERY_PARAMETER = "signature";

    private CommunicationUtilities() {}

    /**
     * Creates the correct URL to use in order to retrieve medical data from the server.
     * @param patientIdentifier the patient identifier, which will be set as a query parameter.
     * @param rescuerIdentifier the rescuer identifier, which will be set as a query parameter.
     * @param signature the RSA digital signature, which will be set as a query parameter.
     * @return the URL.
     */
    public static String buildMedicalDataUrl(final String patientIdentifier, final String rescuerIdentifier, final String signature) {
        return ServerParameters.PROTOCOL +"://" +
                ServerParameters.SERVER_IP + ":" +
                ServerParameters.SERVER_PORT + "/" +
                DATA_RESOURCE_NAME + "?" + PATIENT_QUERY_PARAMETER + "=" + patientIdentifier + "&"
                                         + RESCUER_QUERY_PARAMETER + "=" + rescuerIdentifier + "&"
                                         + SERVICE_QUERY_PARAMETER + "=" + SERVICE_NAME + "&"
                                         + SIGNATURE_QUERY_PARAMETER + "=" + signature;
    }

    /**
     * Checks is the proxima front server is reachable and available.
     * @return true if the proxima front server is reachable and available, false otherwise.
     */
    public static boolean isProximaServerAvailable() {
        try {
            final Socket socket = new Socket();
            final InetSocketAddress inetSocketAddress = new InetSocketAddress(ServerParameters.SERVER_IP, ServerParameters.SERVER_PORT);
            socket.connect(inetSocketAddress, TIMEOUT);
            socket.close();
            return true;
        } catch (final IOException e) {
            return false;
        }
    }
}
