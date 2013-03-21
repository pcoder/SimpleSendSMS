package com.endoapi.simplesendsms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class SimpleSMSActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    String msg;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button sendBtn = (Button) findViewById(R.id.sendSmsBtn);


        String msg = "";
        Calendar now = Calendar.getInstance();
        int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
        if (hourOfDay > 11 && hourOfDay < 14) {
            msg = "manger?";
        } else {
            msg = "un café?";
        }
        prefs = this.getSharedPreferences(
                "com.endoapi.simplesendsms", Context.MODE_PRIVATE);
        if (prefs.getString("destinations", new String("")).trim().equals("")) {
            sendBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText addrTxt =
                            (EditText) SimpleSMSActivity.this.findViewById(R.id.addrEditText);

                    String msg = "";
                    Calendar now = Calendar.getInstance();
                    int hourOfDay = now.get(Calendar.HOUR_OF_DAY);
                    if (hourOfDay > 11 && hourOfDay < 14) {
                        msg = "manger?";
                    } else {
                        msg = "un café?";
                    }

                    String destination = addrTxt.getText().toString().trim();
                    try {

                        String dests[] = destination.split(";");
                        for (String dest : dests) {
                            // safe to assume phone number to be 10
                            if (dest.trim().length() == 10) {
                                sendSmsMessage(dest, msg);
                            }
                            Toast.makeText(SimpleSMSActivity.this, "SMS Sent to " + dest,
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(SimpleSMSActivity.this, "Failed to send SMS",
                                Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }

                    // save the destination values so that they can be loaded next time
                    prefs.edit().putString("destinations", destination).commit();
                }
            });
        } else {
            checkAndSend();
            finish();
        }
    }

    private void checkAndSend() {

        String destinations = prefs.getString("destinations", new String(""));
        if (!destinations.trim().equals("")) {
            try {
                String dests[] = destinations.split(";");
                for (String dest : dests) {
                    // safe to assume phone number to be 10
                    if (dest.trim().length() == 10) {
                        sendSmsMessage(dest, msg);
                    }
                    Toast.makeText(SimpleSMSActivity.this, "SMS Sent to " + dest,
                            Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(SimpleSMSActivity.this, "Failed to send SMS",
                        Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    private void sendSmsMessage(String address, String message) throws Exception {
        SmsManager smsMgr = SmsManager.getDefault();
        smsMgr.sendTextMessage(address, null, message, null, null);
    }


}