package tn.dev.netperf.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import tn.dev.netperf.R;

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap;
    private MyBroadcastReceiver myBroadcastReceiver;

    LatLng newLocation;

    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapFragment= (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

    }

    private class MyBroadcastReceiver extends BroadcastReceiver implements OnMapReadyCallback {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            double Latitude = b.getDouble("Latitude");
            double Longitude = b.getDouble("Longitude");

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
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
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
        //mMap.stopAnimation();
        if (myBroadcastReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcastReceiver);
        myBroadcastReceiver = null;
    }

}