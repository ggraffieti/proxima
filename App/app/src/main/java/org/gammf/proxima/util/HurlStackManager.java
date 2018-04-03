package org.gammf.proxima.util;

import android.content.Context;
import android.util.Log;

import com.android.volley.toolbox.HurlStack;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Singleton class used to set up an SSL socket factory, necessary for HTTPs communication.
 */
public class HurlStackManager {

    private static final String CERTIFICATE_FILE_NAME = "proxima_certificate.crt";
    private static final HurlStackManager INSTANCE = new HurlStackManager();

    private HurlStack mCustomHurlStack;

    private HurlStackManager() {}

    public static HurlStackManager getInstance() {
        return INSTANCE;
    }

    /**
     * Method used to retrieve a custom Hurl stack, creating it if necessary.
     *
     * The creation of it works as follows.
     * It firstly sets up a SSL socket:
     * <ul>
     *     <li>it loads the server certificate from the app assets.</li>
     *     <li>it creates a keystore inserting the certificate in it.</li>
     *     <li>it creates a SSLContext, setting the keystore created previously as default.</li>
     *     <li>it finally creates an SSL socket factory from the SSLContext created previously.</li>
     * </ul>
     * Then, it creates a host name verifier, used to check the server identity, based on its ip address.
     * Finally, the custom hurl stack is created, setting the SSL socket and the host name verifier as parameters.
     * @param context the application context.
     * @return the custom hurl stack used for HTTPs communication with the server.
     */
    public HurlStack getCustomHurlStack(final Context context) {
        if(mCustomHurlStack == null) {
            mCustomHurlStack = new HurlStack() {
                @Override
                protected HttpURLConnection createConnection(final URL url) throws IOException {
                    HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                    try {
                        httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory(context));
                        httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                    } catch (final Exception e) {
                        e.printStackTrace();
                    }
                    return httpsURLConnection;
                }
            };
        }
        return mCustomHurlStack;
    }

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return hostname.compareTo(ServerParameters.SERVER_IP) == 0;
            }
        };
    }

    private TrustManager[] getWrappedTrustManagers(final TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.d("HTTPS", "Error in trust manager");
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.d("HTTPS", "Error in trust manager");
                        }
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory(final Context context)
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        final CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        final InputStream caInput = context.getAssets().open(CERTIFICATE_FILE_NAME);
        final Certificate ca = certificateFactory.generateCertificate(caInput);
        caInput.close();

        final KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        final TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    }
}
