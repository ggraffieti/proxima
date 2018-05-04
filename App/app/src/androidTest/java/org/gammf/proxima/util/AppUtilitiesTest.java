package org.gammf.proxima.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.util.Base64;

import org.junit.Test;

import java.nio.charset.Charset;
import java.security.Signature;

import static org.junit.Assert.*;

public class AppUtilitiesTest {
    @Test
    public void digitalSignatureTest() {
        final String toSign = "test";
        final Context context = InstrumentationRegistry.getTargetContext();
        KeyManager.init(context);

        final Signature signature;
        try {
            signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(KeyManager.getPrivateKey());
            final byte[] dataBytes = toSign.getBytes(Charset.forName("UTF-8"));
            signature.update(dataBytes);
            assertEquals(Base64.encodeToString(signature.sign(), Base64.DEFAULT), AppUtilities.digitalSignature(toSign));
        } catch (final Exception e) {
            fail();
        }
    }
}