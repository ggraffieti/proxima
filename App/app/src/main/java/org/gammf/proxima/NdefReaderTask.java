package org.gammf.proxima;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;

import org.gammf.proxima.interfaces.AsyncTaskListener;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    private final AsyncTaskListener mListener;

    public NdefReaderTask(final AsyncTaskListener listener) {
        mListener = listener;
    }

    @Override
    protected String doInBackground(Tag... tags) {
        final Ndef ndef = Ndef.get(tags[0]);
        if (ndef == null) {
            return "";
        }

        final NdefMessage ndefMessage = ndef.getCachedNdefMessage();

        final NdefRecord[] records = ndefMessage.getRecords();
        final StringBuilder stringBuilder = new StringBuilder();
        for (NdefRecord ndefRecord : records) {
            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                try {
                    stringBuilder.append(readText(ndefRecord));
                } catch (UnsupportedEncodingException e) {
                    return "";
                }
            }
        }

        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(final String result) {
        mListener.onAsyncTaskCompletion(result);
    }

    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();

        final String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        int languageCodeLength = payload[0] & 0x3F;

        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }
}
