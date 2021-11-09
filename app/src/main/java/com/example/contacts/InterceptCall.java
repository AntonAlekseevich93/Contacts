package com.example.contacts;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

//public  class InterceptCall extends BroadcastReceiver {
//    public void onReceive(Context context, Intent intent) {
//
//        try{
//            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
//            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
//                Toast.makeText(context, "Running", Toast.LENGTH_SHORT).show();
//            }
//            if (state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
//                Toast.makeText(context, "Running", Toast.LENGTH_SHORT).show();
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}

public  class InterceptCall extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            System.out.println("Receiver start");
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.e("Incoming Number", "Number is ," + incomingNumber);
            Log.e("State", "State is ," + state);
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                //Трубка не поднята, телефон звонит
//                Toast.makeText(context,"Incoming Call State",Toast.LENGTH_SHORT).show();
//                Toast.makeText(context,"Ringing State Number is -"+incomingNumber,Toast.LENGTH_SHORT).show();
                System.out.println("Нам звонят " + incomingNumber);

            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
                //Телефон находится в режиме звонка(набор номера при исходящем звонке/разговор)
//                Toast.makeText(context,"Call Received State",Toast.LENGTH_SHORT).show();
                if(incomingNumber!= null) {
                    System.out.println("Мы разговариваем/или звоним сами " + incomingNumber);
                }
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                Toast.makeText(context,"Call Idle State",Toast.LENGTH_SHORT).show();
                //Телефон в ждущем режиме - событие наступает по окончанию разговора или
                // в ситуации отказался поднимать трубку и сбросил звонок

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


}