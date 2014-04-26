package com.zomboto.offtheblock.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.android.internal.telephony.ITelephony;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.zomboto.offtheblock.Helpers.DatabaseConnector;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IncommingCallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            try {
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber number = phoneUtil.parse(incomingNumber, "US");
                String nationalSignificantNumber = phoneUtil.getNationalSignificantNumber(number);
                String areaCode;

                int areaCodeLength = phoneUtil.getLengthOfGeographicalAreaCode(number);
                if (areaCodeLength > 0) {
                    areaCode = nationalSignificantNumber.substring(0, areaCodeLength);
                    Toast.makeText(context, "Calling from Area Code " + areaCode, Toast.LENGTH_LONG).show();

                    DatabaseConnector connector = new DatabaseConnector(context);
                    Cursor cursor = connector.getAreaCode(areaCode);
                    if(cursor == null || cursor.getCount()>0)
                    {
                        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                        connector.insertLog(String.format("Blocked number %s at %s", incomingNumber, now));
                        Class clazz = Class.forName(telephonyManager.getClass().getName());
                        Method method = clazz.getDeclaredMethod("getITelephony");
                        method.setAccessible(true);
                        ITelephony telephonyService = (ITelephony)method.invoke(telephonyManager);
                        telephonyService.endCall();
                    }
                    if (cursor.isClosed())
                    {
                        cursor.close();
                    }
                    //subscriberNumber = nationalSignificantNumber.substring(areaCodeLength);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
