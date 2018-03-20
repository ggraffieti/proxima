package org.gammf.proxima.util;


import android.content.Context;
import android.widget.Toast;

import org.gammf.proxima.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class KeyManager {

    private static PrivateKey privateKey;

    public static void init(final Context context) {
        if(privateKey == null) {
            try {
                final InputStream inputStream = context.getAssets().open("private_key.der");
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int currentByte;
                while ((currentByte = inputStream.read()) != -1) {
                    buffer.write(currentByte);
                }
                inputStream.close();
                buffer.close();

                byte[] privateKeyBytes = buffer.toByteArray();

                final KeyFactory kf = KeyFactory.getInstance("RSA");
                privateKey = kf.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            } catch (final IOException e) {
                Toast.makeText(context, R.string.io_error, Toast.LENGTH_LONG).show();
            } catch (final NoSuchAlgorithmException e) {
                Toast.makeText(context, R.string.error_rsa, Toast.LENGTH_LONG).show();
            } catch (final InvalidKeySpecException e) {
                Toast.makeText(context, R.string.invalid_key_specification_error, Toast.LENGTH_LONG).show();
            }
        }
    }

    public static PrivateKey getPrivateKey() throws IllegalStateException {
        if(privateKey != null) {
            return privateKey;
        }
        throw new IllegalStateException();
    }

}
