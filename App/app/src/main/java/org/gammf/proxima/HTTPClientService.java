package org.gammf.proxima;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.gammf.proxima.interfaces.HTTPClientServiceListener;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HTTPClientService extends Service {

    private RequestQueue mRequestQueue;
    private String mRescuerIdentifier;
    private String mPatientIdentifier;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
        return new LocalBinder();
    }

    public class LocalBinder extends Binder {
        public HTTPClientService getService() {
            return HTTPClientService.this;
        }
    }

    public void setIdentifier(final Role role, final String identifier) {
        switch (role) {
            case RESCUER: mRescuerIdentifier = identifier; break;
            case PATIENT: mPatientIdentifier = identifier; break;
        }
    }

    public void sendDataRequest(final HTTPClientServiceListener listener) {
        listener.onDataReceived(new JSONObject());
    }
}
