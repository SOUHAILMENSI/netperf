package tn.dev.netperf.Activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import java.io.File;

import tn.dev.netperf.R;
import tn.dev.netperf.Services.GpsService;
import tn.dev.netperf.Utils.Iconstants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int Permission_All = 1;
    CardView card1, card2, card3, card4, card5, card6;
    MediaPlayer player = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card1 = findViewById(R.id.c1);
        card1.setOnClickListener(this);

        card2 = findViewById(R.id.c2);
        card2.setOnClickListener(this);

        card3 = findViewById(R.id.c3);
        card3.setOnClickListener(this);

        card4 = findViewById(R.id.c4);
        card4.setOnClickListener(this);
        card5 = findViewById(R.id.c5);
        card5.setOnClickListener(this);
        card6 = findViewById(R.id.c6);
        card6.setOnClickListener(this);


            String[] Permissions = {Manifest.permission.READ_SMS, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                    Manifest.permission.READ_EXTERNAL_STORAGE,};
            if (!hasPermissions(this, Permissions)) {
                ActivityCompat.requestPermissions(this, Permissions, Permission_All);
            }
            MakeDirectories();
            startLocationService();

        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }


    public static boolean hasPermissions(Context context, String... permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onClick(View view) {

        Intent i;

        switch (view.getId()) {

            case R.id.c1:
                i = new Intent(this, InfoActivity.class);
                startActivity(i);
                break;
            case R.id.c2:
                i = new Intent(this, SumActivity.class);
                startActivity(i);
                break;
            case R.id.c3:
                i = new Intent(this, FtpActivity.class);
                startActivity(i);
                break;
            case R.id.c4:
                i = new Intent(this, TestActivity.class);
                startActivity(i);
                break;
            case R.id.c5:
                i = new Intent(this, MapActivity.class);
                startActivity(i);
                break;
            case R.id.c6:
                i = new Intent();
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setAction(android.provider.Settings.ACTION_DATA_ROAMING_SETTINGS);
                startActivity(i);
                break;
        }

    }

    public void MakeDirectories() {

        String folder_main = "netPerf";

        File f = new File(Environment.getExternalStorageDirectory(), folder_main);
        if (!f.exists()) {
            f.mkdirs();
            Log.i("DirectoryMaker", folder_main);

        } else {
            Log.i("DirectoryMaker", "making directories failed");
        }

        File f1 = new File(Environment.getExternalStorageDirectory() + "/" + folder_main, "Results");
        if (!f1.exists()) {
            f1.mkdirs();
            Log.i("DirectoryMaker", String.valueOf(f1));

        } else {
            Log.i("DirectoryMaker", "making directories " + f1 + " failed");
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (GpsService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), GpsService.class);
            intent.setAction(Iconstants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
        }
        Toast.makeText(this, "gps service running", Toast.LENGTH_SHORT).show();
    }
}
