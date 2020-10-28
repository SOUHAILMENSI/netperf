package tn.dev.netperf;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private TextView tx1, version, tx3, manufacturer, tx5, imei, tx7, txsystem, tx9,
            bluetooth, tx11, ip, tx13, imsi, tx15, internet, tx18, txcallstt, tx20, txradiotype,
            tx22, simState,tx24,serviceProvider,tx26,mcc_mnc;
    String PhoneType = "";
    String NetworkType = "";
    String Callstte = "";
    String SimState = "";

    SwipeRefreshLayout swipeRefreshLayout;

    private static final int PERMISSION_REQUEST_CODE = 100;
    TelephonyManager telephonyManager;
    private BroadcastReceiver MyReceiver = null;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        MyReceiver = new MyReceiver();

        tx1 = findViewById(R.id.tx1);
        version = findViewById(R.id.tx2);
        tx3 = findViewById(R.id.tx3);
        manufacturer = findViewById(R.id.tx4);
        tx5 = findViewById(R.id.tx5);
        imei = findViewById(R.id.tx6);
        tx7 = findViewById(R.id.tx7);
        txsystem = findViewById(R.id.tx8);
        tx9 = findViewById(R.id.tx9);
        bluetooth = findViewById(R.id.tx10);
        tx11 = findViewById(R.id.tx11);
        ip = findViewById(R.id.tx12);
        tx13 = findViewById(R.id.tx13);
        imsi = findViewById(R.id.tx14);
        tx15 = findViewById(R.id.tx15);
        internet = findViewById(R.id.tx16);
        tx18 = findViewById(R.id.tx18);
        txcallstt = findViewById(R.id.tx19);
        tx20 = findViewById(R.id.tx20);
        txradiotype = findViewById(R.id.tx21);
        tx22 = findViewById(R.id.tx22);
        simState = findViewById(R.id.tx23);
        tx24 = findViewById(R.id.tx24);
        serviceProvider= findViewById(R.id.tx25);
        tx26 = findViewById(R.id.tx26);
        mcc_mnc= findViewById(R.id.tx27);



        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);

        tx1.setText(getString(R.string.versioncode));
        version.setText(Build.VERSION.RELEASE);

        tx3.setText(getString(R.string.manufacturer));
        manufacturer.setText(Build.MANUFACTURER);
        tx5.setText(getString(R.string.IMEI));
        tx13.setText(getString(R.string.IMSI));
        tx11.setText(getString(R.string.IPaddress));
        tx9.setText(getString(R.string.Bluetooth_mac));
        tx7.setText(getString(R.string.system));
        tx15.setText(getString(R.string.internet));
        tx18.setText(getString(R.string.callstate));
        tx20.setText(getString(R.string.radioType));
        tx22.setText(getString(R.string.simState));
        tx24.setText(getString(R.string.serviceProvider));
        tx26.setText(getString(R.string.mcc_mnc));


        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
            } else {
                imei.setText("" + telephonyManager.getImei());
                imsi.setText("" + telephonyManager.getSubscriberId());
                serviceProvider.setText(""+telephonyManager.getSimOperatorName());
                mcc_mnc.setText(""+telephonyManager.getSimOperator());
            }
        } else {

            imei.setText("" + telephonyManager.getImei());
            imsi.setText("" + telephonyManager.getSubscriberId());
            serviceProvider.setText(""+telephonyManager.getSimOperatorName());
            mcc_mnc.setText(""+telephonyManager.getSimOperator());

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


        String bluMac = android.provider.Settings.Secure.getString(this.getContentResolver(), "bluetooth_address");
        bluetooth.setText(bluMac);
        broadcastIntent();


        if (getConnectivityStatusString(this) == "No internet is available") {
            internet.setTextColor(Color.RED);
            internet.setText(getConnectivityStatusString(this));
        } else {
            internet.setText(getConnectivityStatusString(this));
        }

        /*********************************Current network*****************************/

        txsystem.setText(getNeworkType());
        txcallstt.setText(getCallstte());
        txradiotype.setText(getRadioType());
        simState.setText(getSimState());

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCallstte();
                getNeworkType();
                getRadioType();
                getSimState();
                txsystem.setText(getNeworkType());
                simState.setText(getSimState());
                txcallstt.setText(getCallstte());
                txradiotype.setText(getRadioType());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        /**************************************OnCreate Ends here **********************************************/

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    recreate();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);

                        return;
                    }
                    imei.setText("" + telephonyManager.getImei());
                    imsi.setText("" + telephonyManager.getSubscriberId());
                } else {
                    Toast.makeText(this, "Enable needed permission from settings", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void broadcastIntent() {
        registerReceiver(MyReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }


    public static String getConnectivityStatusString(Context context) {
        String status = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                status = "Wifi enabled";

                return status;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                status = "Mobile data enabled";
                return status;
            }
        } else {
            status = "No internet is available";
            return status;
        }
        return status;

    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(MyReceiver);

    }

    public String getNeworkType() {

        int networktype = telephonyManager.getNetworkType();

        switch (networktype) {
            case (TelephonyManager.NETWORK_TYPE_1xRTT):
                NetworkType = "1xRTT";
                break;
            case (TelephonyManager.NETWORK_TYPE_CDMA):
                NetworkType = "CDMA";
                break;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                NetworkType = "EDGE";
                break;
            case (TelephonyManager.NETWORK_TYPE_EHRPD):
                NetworkType = "EHRPD";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_0):
                NetworkType = "EVDO 0";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_A):
                NetworkType = "EVDO A";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_B):
                NetworkType = "EVDO B";
                break;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                NetworkType = "GPRS";
                break;
            case (TelephonyManager.NETWORK_TYPE_GSM):
                NetworkType = "GSM";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSDPA):
                NetworkType = "HSDPA";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPA):
                NetworkType = "HSPA";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPAP):
                NetworkType = "HSPA+";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSUPA):
                NetworkType = "HSUPA";
                break;
            case (TelephonyManager.NETWORK_TYPE_IWLAN):
                NetworkType = "IWLAN";
                break;
            case (TelephonyManager.NETWORK_TYPE_IDEN):
                NetworkType = "IDEN";
                break;
            case (TelephonyManager.NETWORK_TYPE_LTE):
                NetworkType = "LTE";
                break;
            case (TelephonyManager.NETWORK_TYPE_TD_SCDMA):
                NetworkType = "TD-CDMA";
                break;
            case (TelephonyManager.NETWORK_TYPE_UMTS):
                NetworkType = "UMTS";
                break;

            case (TelephonyManager.NETWORK_TYPE_UNKNOWN):
                NetworkType = "UNKNOWN";
                break;

        }
        return NetworkType;

    }

    /***************************************Call Status******************************************************/

    public String getCallstte() {
        int callstat = telephonyManager.getCallState();

        switch (callstat) {
            case (TelephonyManager.CALL_STATE_IDLE):
                Callstte = "Idle";
                break;
            case (TelephonyManager.CALL_STATE_OFFHOOK):
                Callstte = "Connected";
                break;
            case (TelephonyManager.CALL_STATE_RINGING):
                Callstte = "Connected";
                break;
        }
        return Callstte;
    }

    /************Call status**************************************************/

    public String getRadioType() {
        int phoneType = telephonyManager.getPhoneType();
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                PhoneType = "CDMA";
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                PhoneType = "GSM";
                break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                PhoneType = "SIP";
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                PhoneType = "No phone radio";
        }
        return PhoneType;
    }

    /*********************************SIM*******************************************/
    public String getSimState() {
        int simState = telephonyManager.getSimState();

        switch (simState) {
            case (TelephonyManager.SIM_STATE_ABSENT):
                SimState = "Absent";
                break;
            case (TelephonyManager.SIM_STATE_CARD_RESTRICTED):
                SimState = "Restrected";
                break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
                SimState = "Network locked";
                break;
            case (TelephonyManager.SIM_STATE_NOT_READY):
                SimState = "Not Ready";
                break;
            case (TelephonyManager.SIM_STATE_READY):
                SimState = "Ready";
                break;
        }
        return SimState;

    }

}