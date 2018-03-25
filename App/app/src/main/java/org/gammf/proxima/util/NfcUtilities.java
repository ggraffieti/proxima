package org.gammf.proxima.util;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.support.v7.app.AppCompatActivity;

import org.gammf.proxima.R;

/**
 * Utility class, meant to contain nfc-related methods.
 */
public final class NfcUtilities {

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private NfcUtilities() {}

    /**
     * Enables the foreground dispatch for the passed activity. The system will give priority to the activity
     * when an NFC tag is discovered.
     * @param activity the activity to enable the foreground dispatch for.
     */
    public static void setupForegroundDispatch(final AppCompatActivity activity) {
        final Intent intent = new Intent(activity, activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity, 0, intent, 0);

        final IntentFilter[] filters = new IntentFilter[1];

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException(activity.getString(R.string.mime_error));
        }

        NfcAdapter.getDefaultAdapter(activity).enableForegroundDispatch(activity, pendingIntent, filters, new String[][]{});
    }

    /**
     * Stops the foreground dispatch for the passed activity.
     * @param activity the activity to stop the foreground dispatch for.
     */
    public static void stopForegroundDispatch(final AppCompatActivity activity) {
        NfcAdapter.getDefaultAdapter(activity).disableForegroundDispatch(activity);
    }
}
