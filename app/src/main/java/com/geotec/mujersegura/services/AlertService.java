package com.geotec.mujersegura.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.geotec.mujersegura.R;
import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.others.Internet;
import com.geotec.mujersegura.preferences.SessionPreferences;

public class AlertService extends Service implements
        Complements.OnResponseSendDataBackground {

    private final long notifyId = System.currentTimeMillis();
    private static final String CHANNEL_ID = "mujer_segura";
    private static final String CHANNEL_NAME = "Mujer Segura";
    private static final String CHANNEL_DESC = "Mujer Segura Notifications";
    private static final int importance = NotificationManager.IMPORTANCE_DEFAULT;
    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat notificationManager;

    private SessionPreferences sessionPreferences;
    private Complements C;
    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Handler handler = new Handler();

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                addPoint();
                /**
                 * Aqui el codigo a ejecutar cada sierto tiempo
                 */
                handler.postDelayed(runnable, 20000);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public AlertService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        createNotification();

        sessionPreferences = new SessionPreferences(this);
        C = new Complements(this);
        C.iniOnResponseSendDataBackground();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        ShowNotification();
        getLocalizacion();
        startAlert();
        handler.postDelayed(runnable, 5000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlert();
        handler.removeCallbacks(runnable);
        notificationManager.cancel((int) notifyId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotification() {
        mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_ribbon_notification)
                .setColor(Color.rgb(250, 77, 75))
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
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

    private void getLocalizacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localization localization = new Localization(this, this);

        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            /*ActivityCompat.requestPermissions(
                    getApplicationContext(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                    },
                    1000);*/
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, (LocationListener) localization);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, (LocationListener) localization);
    }

    private double lat, lng;
    public void setLocation(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();
    }

    private String getCurrentTimestamp() {
        Calendar calendar = Calendar.getInstance();
        return simpleDateFormat.format(calendar.getTime());
    }

    private String cveAlert = "none";
    private void startAlert() {
        if (Internet.isConnected(this)) {
            JSONObject params = new JSONObject();
            try {
                params.put(getString(R.string.rest_var_user_cve),           sessionPreferences.getCveUser());
                params.put(getString(R.string.rest_var_user_name),          sessionPreferences.getName());
                params.put(getString(R.string.rest_var_alert_device_start), getCurrentTimestamp());
            } catch (JSONException e){
                e.printStackTrace();
            }
            C.sendDataBackground(params, C.Api(R.string.api_start_alert));
        } else {
            C.PrintToastShort(getString(R.string.error_connected_internet));
        }
    }
    private void addPoint() {
        if (Internet.isConnected(this)) {
            JSONObject params = new JSONObject();
            try {
                params.put(getString(R.string.rest_var_alert_cve),              cveAlert);
                params.put(getString(R.string.rest_var_alert_point_lat),        lat);
                params.put(getString(R.string.rest_var_alert_point_lng),        lng);
                params.put(getString(R.string.rest_var_alert_point_timestamp),  getCurrentTimestamp());
            } catch (JSONException e){
                e.printStackTrace();
            }
            C.sendDataBackground(params, C.Api(R.string.api_add_point));
        } else {
            C.PrintToastShort(getString(R.string.error_connected_internet));
        }
    }
    private void stopAlert() {
        if (Internet.isConnected(this)) {
            JSONObject params = new JSONObject();
            try {
                params.put(getString(R.string.rest_var_user_cve),           sessionPreferences.getCveUser());
                params.put(getString(R.string.rest_var_user_name),          sessionPreferences.getName());
                params.put(getString(R.string.rest_var_alert_cve),          cveAlert);
                params.put(getString(R.string.rest_var_alert_device_stop),  getCurrentTimestamp());
            } catch (JSONException e){
                e.printStackTrace();
            }
            C.sendDataBackground(params, C.Api(R.string.api_stop_alert));
        } else {
            C.PrintToastShort(getString(R.string.error_connected_internet));
        }
    }

    @Override
    public void onResponseSendDataBackground(JSONObject r) {
        try {
            if (r.getBoolean(getString(R.string.rest_json_response_result))) {
                JSONObject alert = r.getJSONObject(getString(R.string.rest_json_response_data));
                cveAlert = alert.getString(getString(R.string.rest_var_alert_cve));
                Toast.makeText(this, cveAlert, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onErrorResponseSendDataBackground() {}

    private class Localization implements LocationListener {
        private Context context;
        private AlertService p;

        public Localization(Context context, AlertService service) {
            this.context = context;
            this.p = service;
        }

        @Override
        public void onLocationChanged(Location location) {
            this.p.setLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(context, "GPS Activado", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(context, "GPS Desactivado", Toast.LENGTH_SHORT).show();
        }
    }
}
