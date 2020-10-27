package tn.dev.netperf;


import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class InfoActivity extends AppCompatActivity {
    // text view to display information
    private TextView textViewSetInformation, tx1, tx2, tx3, tx4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
       // textViewSetInformation = findViewById(R.id.textViewSetInformation);

        tx1 = findViewById(R.id.tx1);
        tx2 = findViewById(R.id.tx2);
        tx3 = findViewById(R.id.tx3);
        tx4 = findViewById(R.id.tx4);

       // String information = getHardwareAndSoftwareInfo();
       // textViewSetInformation.setText(information);


        tx1.setText(getString(R.string.versioncode));
        tx2.setText(Build.VERSION.RELEASE);

        tx3.setText(getString(R.string.manufacturer));
        tx4.setText(Build.MANUFACTURER);


    }

/*
    private String getHardwareAndSoftwareInfo() {

        return getString(R.string.serial) + " " + Build.SERIAL + "\n" +
                getString(R.string.model) + " " + Build.MODEL + "\n" +
                getString(R.string.id) + " " + Build.ID + "\n" +
                getString(R.string.manufacturer) + " " + Build.MANUFACTURER + "\n" +
                getString(R.string.brand) + " " + Build.BRAND + "\n" +
                getString(R.string.type) + " " + Build.TYPE + "\n" +
                getString(R.string.user) + " " + Build.USER + "\n" +
                getString(R.string.base) + " " + Build.VERSION_CODES.BASE + "\n" +
                getString(R.string.incremental) + " " + Build.VERSION.INCREMENTAL + "\n" +
                getString(R.string.sdk) + " " + Build.VERSION.SDK + "\n" +
                getString(R.string.board) + " " + Build.BOARD + "\n" +
                getString(R.string.host) + " " + Build.HOST + "\n" +
                getString(R.string.fingerprint) + " " + Build.FINGERPRINT;

    }
*/
}