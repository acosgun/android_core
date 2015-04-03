package org.ros.android.android_tutorial_pubsub;

import android.support.v4.app.Fragment;


//import android.app.Fragment;

import android.app.Activity;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Arrays;

import static org.ros.android.android_tutorial_pubsub.HexHelpers.BuildSelectApdu;

//import android.support.v4.app.Fragment;

/**
 * Created by acosgun on 3/16/15.
 */
public class CardReaderFragment extends Fragment implements NfcAdapter.ReaderCallback {

    private static String TAG = "CardReaderFragment";
    private static final String SAMPLE_LOYALTY_CARD_AID = "D2760000850101";
    private static final byte[] SELECT_OK_SW = {(byte) 0x90, (byte) 0x00};

    NfcAdapter mNfcAdapter;
    NfcReaderInputListener mCallback;

    public interface NfcReaderInputListener {
        public void nfcCallback(int floor, String name);
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    /*    Activity activity = getActivity();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (mNfcAdapter == null) {
            Toast.makeText(activity, "NFC is not available", Toast.LENGTH_LONG).show();
        }
*/

    }


    @Override
    public void onPause() {
        super.onPause();
        //textView.setText("onPause");
        this.disableReaderMode();

    }

    @Override
    public void onResume() {
        super.onResume();
        //textView.setText("onResume");
        //mNfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A, null);
        this.enableReaderMode();
    }

    private void enableReaderMode() {
        Log.i(TAG, "Enabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.enableReaderMode(activity, this, NfcAdapter.FLAG_READER_NFC_A, null);
        }
    }

    private void disableReaderMode() {
        Log.i(TAG, "Disabling reader mode");
        Activity activity = getActivity();
        NfcAdapter nfc = NfcAdapter.getDefaultAdapter(activity);
        if (nfc != null) {
            nfc.disableReaderMode(activity);
        }
    }
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (NfcReaderInputListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement NfcReaderInputListener");
        }



    }


    public void onTagDiscovered(Tag tag) {

        Log.d(TAG, "onTagDiscovered");
        //Toast.makeText(this, "NFC Detected", Toast.LENGTH_SHORT).show();


        IsoDep isoDep = IsoDep.get(tag);
        if(isoDep != null)
        {

            try{

                // Connect to the remote NFC device
                isoDep.connect();
                // Build SELECT AID command for our loyalty card service.
                // This command tells the remote device which service we wish to communicate with.
                //Log.i(TAG, "Requesting remote AID: " + SAMPLE_LOYALTY_CARD_AID);
                byte[] command = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
                // Send command to remote device
                //Log.i(TAG, "Sending: " + ByteArrayToHexString(command));
                byte[] result = isoDep.transceive(command);
                // If AID is successfully selected, 0x9000 is returned as the status word (last 2
                // bytes of the result) by convention. Everything before the status word is
                // optional payload, which is used here to hold the account number.

                //Log.i(TAG, "Result: " + ByteArrayToHexString(result));
                int resultLength = result.length;
                byte[] statusWord = {result[resultLength-2], result[resultLength-1]};
                byte[] payload = Arrays.copyOf(result, resultLength - 2);
                if (Arrays.equals(SELECT_OK_SW, statusWord)) {
                    // The remote NFC device will immediately respond with its stored account number

                    int floor = (int) payload[0];

                    String name = new String(Arrays.copyOfRange(payload,1,payload.length));
                    //String accountNumber = new String(payload, "UTF-8");

                    //Log.i(TAG, "floor: " + floor);
                    //Log.i(TAG, "name: " + name);
                    //mAccountCallback.get().onAccountReceived(accountNumber);
                    mCallback.nfcCallback(floor,name);
                }

            }
            catch (IOException e)
            {
                Log.e(TAG, "Error communicating with card: " + e.toString());
            }

        }

    }

}
