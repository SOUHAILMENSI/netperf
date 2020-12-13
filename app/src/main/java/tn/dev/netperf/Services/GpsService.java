package tn.dev.netperf.Services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;

import tn.dev.netperf.R;
import tn.dev.netperf.Utils.Iconstants;

public class GpsService extends Service {

    double latitude,longitude;
    float speed;

    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult != null && locationResult.getLastLocation() != null) {
                 latitude = locationResult.getLastLocation().getLatitude();
                 longitude = locationResult.getLastLocation().getLongitude();
                 speed = locationResult.getLastLocation().getSpeed();

                Log.d("LOCATION_UPDATE", latitude + "," + longitude + "\n" + speed );

                Intent intent = new Intent("location_update");
                Bundle bundle = new Bundle();

                bundle.putDouble("Longitude", longitude);
                bundle.putDouble("Latitude", latitude);
                bundle.putFloat("Speed", speed);
                intent.putExtras(bundle);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            }
        }
    };

   @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @SuppressLint("MissingPermission")
    public void startLocationService() {

        String channel_Id = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (getApplicationContext(),
                        channel_Id
                );

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("gps service");
        builder.setContentText("Running");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channel_Id) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channel_Id,
                        "gps Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("this channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper());

        startForeground(Iconstants.LOCATION_SERVICE_ID, builder.build());
    }

    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(Iconstants.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

}

