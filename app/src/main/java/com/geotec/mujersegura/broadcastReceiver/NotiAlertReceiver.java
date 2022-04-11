package com.geotec.mujersegura.broadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.geotec.mujersegura.services.NotiAlertWPBService;
import com.geotec.mujersegura.services.AlertService;


public class NotiAlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //String message = intent.getStringExtra("toastMessage");
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context, AlertService.class));
        context.stopService(new Intent(context, NotiAlertWPBService.class));
    }
}
