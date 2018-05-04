package org.gammf.proxima.util;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class KeyManagerTest {

    @Test
    public void keyManagerTest() {
        final Context context = InstrumentationRegistry.getTargetContext();
        KeyManager.init(context);
        assertNotNull(KeyManager.getPrivateKey());
    }
}