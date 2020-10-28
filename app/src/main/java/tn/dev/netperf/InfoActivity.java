package tn.dev.netperf;


import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;

public class InfoActivity extends AppCompatActivity {
    // text view to display information
    private TextView tx1, version, tx3, manufacturer, tx5, imei, tx7, system, tx9, bluetooth, tx11, ip, tx13, imsi;

    private static final int PERMISSION_REQUEST_CODE = 100;
    TelephonyManager telephonyManager;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        tx1 = findViewById(R.id.tx1);
        version = findViewById(R.id.tx2);
        tx3 = findViewById(R.id.tx3);
        manufacturer = findViewById(R.id.tx4);
        tx5 = findViewById(R.id.tx5);
        imei = findViewById(R.id.tx6);

        tx7 = findViewById(R.id.tx7);
        system = findViewById(R.id.tx8);
        tx9 = findViewById(R.id.tx9);
        bluetooth = findViewById(R.id.tx10);
        tx11 = findViewById(R.id.tx11);
        ip = findViewById(R.id.tx12);
        tx13 = findViewById(R.id.tx13);
        imsi = findViewById(R.id.tx14);


        tx1.setText(getString(R.string.versioncode));
        version.setText(Build.VERSION.RELEASE);

        tx3.setText(getString(R.string.manufacturer));
        manufacturer.setText(Build.MANUFACTURER);
        tx5.setText(getString(R.string.IMEI));
        tx13.setText(getString(R.string.IMSI));
        tx11.setText(getString(R.string.IPaddress));
        tx9.setText(getString(R.string.Bluetooth_mac));
        tx7.setText(getString(R.string.system));

        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            } else {
                imei.setText("" + telephonyManager.getImei());
                imsi.setText("" + telephonyManager.getSubscriberId());
            }
        } else {

            imei.setText("" + telephonyManager.getImei());
            imsi.setText("" + telephonyManager.getSubscriberId());
        }

        String ipAddress = "No internet connection";
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        boolean is3Genabled = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks) {
                NetworkInfo info = connectivityManager.getNetworkInfo(network);
                if (info != null) {
                    if (info.getType() == connectivityManager.TYPE_MOBILE) {

                        ipAddress = getMobileIPAddress();
                    }
                }
            }

        } else {
            NetworkInfo mMobile = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);
            if (mMobile != null) {
                ipAddress = is3Genabled + "";

            }
        }
        ip.setText(ipAddress);


        String bluMac = android.provider.Settings.Secure.getString(this.getContentResolver(),"bluetooth_address");
        bluetooth.setText(bluMac);

    }

    private static String getMobileIPAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface anInterface : interfaces) {
                List<InetAddress> inetAddresses = Collections.list(anInterface.getInetAddresses());
                for (InetAddress inetAddress : inetAddresses) {
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }

        } catch (Exception e) {
        }
        return "";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                    imei.setText("" + telephonyManager.getImei());
                    imsi.setText("" + telephonyManager.getSubscriberId());
                } else {
                    Toast.makeText(this, "Enable needed permission from settings", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}