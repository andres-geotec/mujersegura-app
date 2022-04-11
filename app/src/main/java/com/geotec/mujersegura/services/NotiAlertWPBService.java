package com.geotec.mujersegura.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.geotec.mujersegura.broadcastReceiver.NotiAlertReceiver;
import com.geotec.mujersegura.R;

public class NotiAlertWPBService extends Service {

    //private NotificationManager mNotificationManager;
    private final long notifyId = System.currentTimeMillis();

    private static final String CHANNEL_ID = "mujer_segura";
    private static final String CHANNEL_NAME = "Mujer Segura";
    private static final String CHANNEL_DESC = "Mujer Segura Notifications";
    private static final int importance = NotificationManager.IMPORTANCE_DEFAULT;
    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat notificationManager;

    public NotiAlertWPBService() {}

    @Override
    public void onCreate() {
        super.onCreate();
        //mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
        createNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        //showNotification();
        ShowNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mNotificationManager.cancel((int) notifyId);
        notificationManager.cancel((int) notifyId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification() {
        Intent broadcastIntent = new Intent(this, NotiAlertReceiver.class);
        //broadcastIntent.putExtra("toastMessage", "Hola mundo!");
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(actionIntent)
                .setSmallIcon(R.drawable.ic_ribbon_notification)
                .setContentText(getString(R.string.action_press_active_alert))
                .setColor(Color.rgb(255, 121, 63))
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(getText(R.string.v_sex_description)))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.addAction(new Action(R.drawable.ic_face, "yes", actionIntent))
                .setPriority(importance);
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESC);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void ShowNotification() {
        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify((int) notifyId, mBuilder.build());
        startForeground((int) notifyId, mBuilder.getNotification());
    }
}
