package org.gammf.proxima.interfaces;

import org.json.JSONObject;

/**
 * Interface used for retrieving the results of the {@link org.gammf.proxima.HTTPClientService} server requests.
 */
public interface HTTPClientServiceListener {
    /**
     * Callback invoked when a server request succeeds.
     * @param jsonObject a {@link JSONObject} containing the server response.
     */
    void onDataReceived(final JSONObject jsonObject);

    /**
     * Callback invoked when a server request fails.
     * @param errorCode the response error code.
     */
    void onError(final Integer errorCode);

    /**
     * Callback invoked when there's no internet connectivity.
     */
    void onConnectionError();
}
