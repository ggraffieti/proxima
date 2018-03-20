package org.gammf.proxima.util;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.gammf.proxima.R;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public final class AppUtilities {

    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    private AppUtilities() {}

    public static String digitalSignature(final String data) throws NoSuchAlgorithmException,
                                                                    InvalidKeyException,
                                                                    SignatureException {
        final Signature s = Signature.getInstance(SIGNATURE_ALGORITHM);
        s.initSign(KeyManager.getPrivateKey());
        byte[] dataTmp = data.getBytes(Charset.forName("UTF-8"));
        s.update(dataTmp);
        return Base64.encodeToString(s.sign(), Base64.DEFAULT);
    }
}
