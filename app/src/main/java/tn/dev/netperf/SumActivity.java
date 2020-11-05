package tn.dev.netperf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class SumActivity extends AppCompatActivity {

    FusedLocationProviderClient fusedLocationProviderClient;
    TextView textView1;
    TextView textView2;
    TextView textView4;
    TextView Statusvalue;
    private static final int PERMISSION_REQUEST_CODE = 100;
    MediaPlayer player;


    Gsm_fragment fragment1Action;
    Umts_fragment fragment2Action;
    Lte_fragment fragment3Action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sum);


        this.textView1 = findViewById(R.id.text_view1);
        this.textView2 = findViewById(R.id.text_view2);
        this.textView4 = findViewById(R.id.text_view4);
        this.Statusvalue = findViewById(R.id.Statusvalue);
        this.fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(SumActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(SumActivity.this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
            Statusvalue.setText("GPS not Ready");


            if (player == null) {
                player = MediaPlayer.create(this, R.raw.ns_gps_disconnected);
                player.setVolume(5, 5);
                player.start();
            }

            return;

        } else {
            if (player != null) {
                player.release();
                player = MediaPlayer.create(this, R.raw.ns_gps_connected);
                player.setVolume(5, 5);
                player.start();
            }
            Statusvalue.setText("GPS Ready");
            getLocation();

        }

        fragment1Action = new Gsm_fragment();
        fragment2Action = new Umts_fragment();
        fragment3Action = new Lte_fragment();
    }

    private void selectFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
            //permission granted
            getLocation();
        } else {
            //permisssion denied
            Toast.makeText(getApplicationContext(), "Permissions denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    public void getLocation() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            this.fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {

                @SuppressLint("MissingPermission")
                public void onComplete(Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {


                        if (player != null) {
                            player.release();
                        } else {
                            player = MediaPlayer.create(SumActivity.this, R.raw.ns_gps_connected);
                            player.setVolume(5, 5);
                            player.start();
                        }

                        try {
                            List<Address> addresses = new Geocoder(SumActivity.this, Locale.getDefault()).getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            TextView Statusvalue = SumActivity.this.Statusvalue;
                            Statusvalue.setText("GPS Ready");
                            TextView textView = SumActivity.this.textView1;
                            textView.setText(String.valueOf(addresses.get(0).getLatitude()));
                            TextView textView2 = SumActivity.this.textView2;
                            textView2.setText(String.valueOf(addresses.get(0).getLongitude()));
                            TextView textView4 = SumActivity.this.textView4;
                            textView4.setText(String.valueOf(addresses.get(0).getLocality()));
                        } catch (
                                IOException e) {
                            e.printStackTrace();
                        }
                    } else {  //WHEN LOCATION RESULT IS NULL
                        //INITIALIZE LOCATION REQUEST
                        if (player != null) {
                            player.release();
                        } else {
                            player = MediaPlayer.create(SumActivity.this, R.raw.ns_gps_fix_lost);
                            player.setVolume(5, 5);
                            player.start();
                        }
                        LocationRequest locationRequest = new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        //INITIALIZE LOCATION CALLBACK
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                try {
                                    List<Address> addresses1 = new Geocoder(SumActivity.this, Locale.getDefault()).getFromLocation(location1.getLatitude(), location1.getLongitude(), 1);
                                    TextView Statusvalue = SumActivity.this.Statusvalue;
                                    Statusvalue.setText("GPS Ready");
                                    TextView textView = SumActivity.this.textView1;
                                    textView.setText(String.valueOf(addresses1.get(0).getLatitude()).substring(0,10));
                                    TextView textView2 = SumActivity.this.textView2;
                                    textView2.setText(String.valueOf(addresses1.get(0).getLongitude()));
                                    TextView textView4 = SumActivity.this.textView4;
                                    textView4.setText(String.valueOf(addresses1.get(0).getLocality()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        //Request location updates
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }
                }
            });
        } else {
            //location services is disabled
            //open location setting
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

        }
    }
}

