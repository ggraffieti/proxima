package org.gammf.proxima.util;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

/**
 * Utility class, meant to contain useful methods for the other app components.
 */
public final class AppUtilities {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private AppUtilities() {}

    /**
     * Creates a digital signature, using RSA encryption on the SHA-256 digest of the given data.
     * @param data the data to sign.
     * @return the RSA digital signature.
     * @throws NoSuchAlgorithmException thrown if the device doesn't support the "SHA256withRSA" algorithm.
     * @throws InvalidKeyException thrown if the private key used to sign is not valid.
     * @throws SignatureException thrown if the signature procedure goes wrong.
     */
    public static String digitalSignature(final String data) throws NoSuchAlgorithmException,
                                                                    InvalidKeyException,
                                                                    SignatureException {
        final Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(KeyManager.getPrivateKey());
        final byte[] dataBytes = data.getBytes(Charset.forName("UTF-8"));
        signature.update(dataBytes);
        return Base64.encodeToString(signature.sign(), Base64.DEFAULT);
    }
}
