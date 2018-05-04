package org.gammf.proxima.android;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;

import org.gammf.proxima.interfaces.HTTPClientServiceListener;
import org.gammf.proxima.util.CommunicationUtilities;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

import static org.junit.Assert.*;

public class HTTPClientServiceTest {

    private HTTPClientService service;

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Before
    public void setUp() throws TimeoutException {
        final Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), HTTPClientService.class);
        final IBinder binder = mServiceRule.bindService(serviceIntent);
        service = ((HTTPClientService.LocalBinder) binder).getService();
    }

    @Test
    public void serverResponseTest() throws InterruptedException {
        if(CommunicationUtilities.isProximaServerAvailable()) {
            service.sendDataRequest(new HTTPClientServiceListener() {
                @Override
                public void onDataReceived(JSONObject jsonObject) {
                    fail();
                }

                @Override
                public void onError(Integer errorCode) {
                    assertEquals(HttpsURLConnection.HTTP_UNAUTHORIZED, errorCode.intValue());
                }

                @Override
                public void onConnectionError() {
                    fail();
                }
            });
        } else {
            service.sendDataRequest(new HTTPClientServiceListener() {
                @Override
                public void onDataReceived(JSONObject jsonObject) {
                    fail();
                }

                @Override
                public void onError(Integer errorCode) {
                    fail();
                }

                @Override
                public void onConnectionError() {}
            });
        }
        Thread.sleep(5000);
    }
}