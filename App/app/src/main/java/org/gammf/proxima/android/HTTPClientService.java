package org.gammf.proxima.android;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.gammf.proxima.R;
import org.gammf.proxima.interfaces.HTTPClientServiceListener;
import org.gammf.proxima.util.AppUtilities;
import org.gammf.proxima.util.CommunicationUtilities;
import org.gammf.proxima.util.IdentifiersManager;
import org.gammf.proxima.util.KeyManager;

import org.json.JSONObject;

/**
 * Bounded service responsible for communicating with the proxima front server in order to retrieve the data
 * related to a certain patient.
 */
public class HTTPClientService extends Service {

    private RequestQueue mRequestQueue;

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

    /**
     * Sends a medical data request to the proxima front server.
     * @param listener the listener to be notified upon the reception of a server response/error.
     */
    public void sendDataRequest(final HTTPClientServiceListener listener) {
        try {
            final String patientIdentifier = IdentifiersManager.getPatientIdentifier();
            final String rescuerIdentifier = IdentifiersManager.getRescuerIdentifier();

            final String url = CommunicationUtilities.buildMedicalDataUrl(patientIdentifier,
                                                                          rescuerIdentifier,
                                                                          AppUtilities.digitalSignature(patientIdentifier+rescuerIdentifier)).replaceAll("\n", "");
            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    listener.onDataReceived(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    if(error.networkResponse == null) {
                        error.printStackTrace();
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
