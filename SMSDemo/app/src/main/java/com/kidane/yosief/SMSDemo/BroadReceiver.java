package com.kidane.yosief.SMSDemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.Objects;

/***
 * @author Kidane Yosief
 * @version 11/12/2015
 * @since 1.0
 */
public class BroadReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // getting message received
        Bundle bd = intent.getExtras();
        SmsMessage[] message = null;
        String str = "";
        //pdus = protocol description unit
        if(bd!=null){
            Object[] pdus = (Object[])bd.get("pdus");
            message = new SmsMessage[pdus.length];
            for (int i = 0; i<=message.length; i++){
                message[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str +="Mesage From " + message[i].getOriginatingAddress();
                str += " : ";
                str += message[i].getMessageBody().toString();
                str += "\n";
            }
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
        //Communicating with the activity

        Intent intentbroadcast = new Intent();
        intentbroadcast.setAction("kiduney");
        intentbroadcast.putExtra("message", str);
        context.sendBroadcast(intentbroadcast);
    }
}
