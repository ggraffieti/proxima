package org.gammf.proxima.util;

import android.util.Base64;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public final class AppUtilities {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private AppUtilities() {}

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
