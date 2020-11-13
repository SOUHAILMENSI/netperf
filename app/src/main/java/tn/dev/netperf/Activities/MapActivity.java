package tn.dev.netperf.Activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import tn.dev.netperf.R;

import tn.dev.netperf.Services.GpsService;


public class MapActivity extends AppCompatActivity {

    TextView textView1;
    TextView textView2;
    TextView textView4;
    TextView Statusvalue;
    MediaPlayer player = null;

    private LocationManager locationManager;
    private BroadcastReceiver broadcastReceiver = null;


    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    textView1.setText("" + intent.getExtras().get("Latitude"));
                    textView2.setText("" + intent.getExtras().get("Longitude"));
                    textView4.setText("" + intent.getExtras().get("time"));

                    Log.e("ADebugTag", "Value: " + textView1.getText() + "," + textView2.getText());

                    if (textView1.getText() == null || textView2.getText() == null || textView1.getText() == "" || textView2.getText() == "") {
                        if (player != null) {
                            release();
                            play(R.raw.ns_gps_fix_lost);
                        }
                        play(R.raw.ns_gps_fix_lost);
                    }
                    Statusvalue.setText(R.string.connected);

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);


        this.textView1 = findViewById(R.id.text_view1);
        this.textView2 = findViewById(R.id.text_view2);
        this.textView4 = findViewById(R.id.text_view4);
        this.Statusvalue = findViewById(R.id.Statusvalue);


        if (!runtime_permissions()) {
            configure_location();
        }


        /**********************************onCreate(Bundle savedInstanceState) Ends here****************************/

    }

    private void configure_location() {
        Intent i = new Intent(getApplicationContext(), GpsService.class);
        startService(i);


    }


    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            Statusvalue.setText(R.string.disconnected);

            return true;
        }

        if (player != null) {
            release();
            play(R.raw.ns_gps_connected);
        }
        play(R.raw.ns_gps_connected);

        Statusvalue.setText(R.string.connected);

        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                configure_location();

                if (player != null) {
                    release();
                    play(R.raw.ns_gps_connected);
                }
                play(R.raw.ns_gps_connected);

                Statusvalue.setText(R.string.connected);


            } else {
                runtime_permissions();
            }
        }
    }

    public void play(int raw) {

        player = MediaPlayer.create(MapActivity.this, raw);
        player.setVolume(5, 5);
        player.start();
    }

    public void release() {
        player.release();

    }


}
