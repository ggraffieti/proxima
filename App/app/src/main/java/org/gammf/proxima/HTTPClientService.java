package org.gammf.proxima;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.gammf.proxima.interfaces.HTTPClientServiceListener;
import org.gammf.proxima.util.AppUtilities;
import org.gammf.proxima.util.CommunicationUtilities;
import org.gammf.proxima.util.KeyManager;
import org.json.JSONObject;

public class HTTPClientService extends Service {

    private RequestQueue mRequestQueue;
    private String mRescuerIdentifier;
    private String mPatientIdentifier;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
            KeyManager.init(getApplicationContext());
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
        try {
            final String url = CommunicationUtilities.buildMedicalDataUrl(mPatientIdentifier,
                                                                          mRescuerIdentifier,
                                                                          AppUtilities.digitalSignature(mPatientIdentifier+mRescuerIdentifier));

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    listener.onDataReceived(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    if(error.networkResponse == null) {
                        listener.onConnectionError();
                    } else {
                        listener.onError(error.networkResponse.statusCode);
                    }
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mRequestQueue.add(request);
        }  catch (final IllegalStateException e) {
            Toast.makeText(this, R.string.key_null_error, Toast.LENGTH_LONG).show();
        } catch (final Exception e) {
            Toast.makeText(this, R.string.signature_error, Toast.LENGTH_LONG).show();
        }
    }
}
