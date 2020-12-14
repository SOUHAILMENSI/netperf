package tn.dev.netperf.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tn.dev.netperf.R;

public class MapActivity extends AppCompatActivity {

    private GoogleMap mMap;
    TextView textView1;
    TextView textView2;
    private MyBroadcastReceiver myBroadcastReceiver;

    LatLng newLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        this.textView1 = findViewById(R.id.text_view1);
        this.textView2 = findViewById(R.id.text_view2);


    }


    private class MyBroadcastReceiver extends BroadcastReceiver implements OnMapReadyCallback {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            double Latitude = b.getDouble("Latitude");
            double Longitude = b.getDouble("Longitude");
            textView1.setText(String.valueOf(Latitude));
            textView2.setText(String.valueOf(Longitude));

            newLocation = new LatLng(Latitude, Longitude);

            Log.e("ADebugTag", String.valueOf(newLocation));

            mapFragment.getMapAsync(this);

        }

        @Override
        public void onMapReady(GoogleMap googleMap) {

            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(newLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myBroadcastReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter("location_update");
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (myBroadcastReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcastReceiver);
        myBroadcastReceiver = null;
    }

}