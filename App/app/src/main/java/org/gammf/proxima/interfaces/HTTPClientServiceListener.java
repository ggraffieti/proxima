package org.gammf.proxima.interfaces;

import org.json.JSONObject;

public interface HTTPClientServiceListener {
    void onDataReceived(final JSONObject jsonObject);
    void onError(final Integer errorCode);
    void onConnectionError();
}
