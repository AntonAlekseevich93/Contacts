package com.example.contacts;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CustomPhoneStateListener extends PhoneStateListener {

    private static final String TAG = "CustomPhoneStateListener";

    public void onCallStateChanged(int state, String incomingNumber) {


        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                Log.d(TAG, "RINGING");
                Log.v(TAG, "WE ARE INSIDE!!!!!!!!!!!");
                Log.v(TAG, incomingNumber);
                break;
        }
    }
}