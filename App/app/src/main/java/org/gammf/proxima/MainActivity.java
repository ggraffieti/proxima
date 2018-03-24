package org.gammf.proxima;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.gammf.proxima.interfaces.AsyncTaskListener;
import org.gammf.proxima.interfaces.HTTPClientServiceListener;
import org.gammf.proxima.HTTPClientService.LocalBinder;

import org.gammf.proxima.util.CommunicationUtilities;
import org.gammf.proxima.util.NfcUtilities;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

/**
 * Main activity of the application, mainly responsible for managing user inputs and interactions.
 */
public class MainActivity extends AppCompatActivity implements AsyncTaskListener, HTTPClientServiceListener {

    private Button mSearchButton;
    private TextView mHomeTextView;

    private Role mCurrentRole;
    private boolean mIsSearching;

    private HTTPClientService mHttpClientService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(final ComponentName className,
                                       final IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            mHttpClientService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(final ComponentName arg0) {}
    };

    /**
     * App initialization. It checks for NFC support, sets up the home button listener and starts the {@link HTTPClientService}.
     * @param savedInstanceState the saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSearchButton = findViewById(R.id.homeButton);
        mHomeTextView = findViewById(R.id.homeTextView);

        final NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mIsSearching = false;

        if (nfcAdapter == null) {
            Toast.makeText(this, R.string.no_nfc_error, Toast.LENGTH_LONG).show();
            finish();
        } else if(!nfcAdapter.isEnabled()) {
            Toast.makeText(this, R.string.enable_nfc, Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
        }

        findViewById(R.id.homeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mIsSearching) {
                    mIsSearching = true;
                    mCurrentRole = Role.RESCUER;
                } else {
                    mIsSearching = false;
                }
                refreshView();
            }
        });

        final Intent intent = new Intent(this, HTTPClientService.class);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Starts a new {@link NdefReaderTask} in order to read identifier contained in the intent.
     * @param intent the intent containing the NFC tag data.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    protected void onResume() {
        super.onResume();
        NfcUtilities.setupForegroundDispatch(this);
        refreshView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcUtilities.stopForegroundDispatch(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    /**
     * Stores the identifiers read from a {@link NdefReaderTask}.
     * If both the identifiers are sets, it starts a request to the server.
     * @param result the result of the async task.
     */
    @Override
    public void onAsyncTaskCompletion(final String result) {
        switch (mCurrentRole) {
            case RESCUER:
                mHttpClientService.setIdentifier(mCurrentRole, result);
                mHomeTextView.setText(R.string.home_message_second_search);
                mCurrentRole = Role.PATIENT;
                break;
            case PATIENT:
                mHttpClientService.setIdentifier(mCurrentRole, result);
                mCurrentRole = Role.RESCUER;
                mHomeTextView.setText(R.string.home_message_sending_request);
                mHttpClientService.sendDataRequest(this);
                break;
        }
    }

    /**
     * Starts a {@link DataPrinterActivity} in order to show the received data.
     * @param jsonObject a {@link JSONObject} containing the server response.
     */
    @Override
    public void onDataReceived(final JSONObject jsonObject) {
        final Intent intent = new Intent(this, DataPrinterActivity.class);
        intent.putExtra("patientData", jsonObject.toString());
        startActivity(intent);
        mHomeTextView.setText(R.string.home_message_printing_data);
    }

    /**
     * Shows a toast containing the communication error.
     * @param errorCode the response error code.
     */
    @Override
    public void onError(final Integer errorCode) {
        if(errorCode == HttpsURLConnection.HTTP_FORBIDDEN) {
            Toast.makeText(this, R.string.unauthorized_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.unknown_error, Toast.LENGTH_SHORT).show();
        }
        refreshView();
    }

    /**
     * Shows a toast telling the user that no connection is present.
     */
    @Override
    public void onConnectionError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show();
        refreshView();
    }

    private void handleIntent(final Intent intent) {
        final String action = intent.getAction();
        if (mIsSearching && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) && NfcUtilities.MIME_TEXT_PLAIN.equals(intent.getType())) {
            final Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            new NdefReaderTask(this).execute(tag);
        }
    }

    private void refreshView() {
        if(!CommunicationUtilities.isProximaServerAvailable()) {
            mHomeTextView.setText(R.string.home_message_no_connection);
            mSearchButton.setEnabled(false);
        } else {
            if (mIsSearching) {
                mHomeTextView.setText(R.string.home_message_first_search);
                mSearchButton.setText(R.string.home_button_text_stop_searching);
            } else {
                mHomeTextView.setText(R.string.home_message_default);
                mSearchButton.setText(R.string.home_button_text_default);
            }
        }
    }
}