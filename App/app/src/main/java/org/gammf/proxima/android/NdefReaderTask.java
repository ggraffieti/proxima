package org.gammf.proxima.android;

import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;

import org.gammf.proxima.interfaces.AsyncTaskListener;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * AsyncTask responsible for reading the content of an NFC tag, following the NDEF technology.
 */
public class NdefReaderTask extends AsyncTask<Tag, Void, String> {

    private final AsyncTaskListener mListener;

    public NdefReaderTask(final AsyncTaskListener listener) {
        mListener = listener;
    }

    /**
     * Reads all the NDEF records containing in the tag.
     * @param tags an array containing the tag.
     * @return the content contained in the tag.
     */
    @Override
    protected String doInBackground(final Tag... tags) {
        if (tags == null || tags[0] == null) {
            return "";
        }
        final Ndef ndef = Ndef.get(tags[0]);

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

    /**
     * Reads a single NDEF record.
     * @param record the record to be read.
     * @return the record content.
     * @throws UnsupportedEncodingException if the record text encoding is not supported by the device.
     */
    private String readText(final NdefRecord record) throws UnsupportedEncodingException {
        /*
            The most significant bit of the first byte of the record payload stores the text encoding.
            The six least significant bits of the first bytes store the language-code length.
            The actual content of the payload starts at language-code length + 1 (first byte).
        */
        byte[] payload = record.getPayload();

        final String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        int languageCodeLength = payload[0] & 0x3F;

        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }
}
