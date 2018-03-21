package org.gammf.proxima.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public final class CommunicationUtilities {

    private static final String SERVICE_NAME = "proxima.medical.firstAid";
    private static final String DATA_RESOURCE_NAME = "data";
    private static final String PATIENT_QUERY_PARAMETER = "targetID";
    private static final String RESCUER_QUERY_PARAMETER = "operatorID";
    private static final String SERVICE_QUERY_PARAMETER = "service";
    private static final String SIGNATURE_QUERY_PARAMETER = "signature";

    private CommunicationUtilities() {}

    public static boolean isDeviceConnectedToInternet (final Context context) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && connectivityManager.getActiveNetworkInfo()!= null) {
            try {
                final HttpURLConnection google = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                google.setRequestProperty("User-Agent", "Test");
                google.setRequestProperty("Connection", "close");
                google.setConnectTimeout(1500);
                google.connect();
                return (google.getResponseCode() == 200 || google.getResponseCode() > 400);
            } catch (final IOException e) {
                return false;
            }
        }
        return false;
    }

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
