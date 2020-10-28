package tn.dev.netperf;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    int Permission_All = 1;
    public CardView card1, card2, card3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        card1 = (CardView) findViewById(R.id.c1);
        card1.setOnClickListener(this);

        card2 = (CardView) findViewById(R.id.c2);
        card2.setOnClickListener(this);

        card3 = (CardView) findViewById(R.id.c3);
        card3.setOnClickListener(this);


        String[] Permissions = {Manifest.permission.READ_SMS,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS,
                Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE,};
        if (!hasPermissions(this, Permissions)) {
            ActivityCompat.requestPermissions(this, Permissions, Permission_All);
        }
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
                i = new Intent(this, StatsActivity.class);
                startActivity(i);
                break;
        }

    }
}
