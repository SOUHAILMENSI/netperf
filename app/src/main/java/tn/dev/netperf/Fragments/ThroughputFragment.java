package tn.dev.netperf.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import tn.dev.netperf.R;
import tn.dev.netperf.Utils.DownloadTask;


public class ThroughputFragment extends Fragment {

    private EditText edit_Tv;
    private Button download, save;
    private TextView tv;
    private int Permission_All = 1;
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_throughput, container, false);
        tv = view.findViewById(R.id.tv);
        edit_Tv = view.findViewById(R.id.edit_Tv);
        download = view.findViewById(R.id.download);
        save = view.findViewById(R.id.save);

        mContext = getActivity().getApplicationContext();
        String[] Permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE,};
        if (!hasPermissions(mContext, Permissions)) {
            ActivityCompat.requestPermissions(getActivity(), Permissions, Permission_All);
        }

        download.setOnClickListener(v -> {
            if (connected()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    new DownloadTask(mContext, download, edit_Tv.getText().toString(), tv);
                }
            }
        });

        save.setOnClickListener(v -> {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'_'HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());

            String textToSaveString = tv.getText().toString();
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.generateNoteOnSD(mContext, "HTTP_DL_Throughput" + formatter.format(date) + ".txt", textToSaveString);
            Toast.makeText(mContext, "Logfile successfully saved", Toast.LENGTH_SHORT).show();


        });

        return view;
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

    public boolean connected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        } else {
            Toast.makeText(mContext, "Check your internet connection", Toast.LENGTH_SHORT).show();
            connected = false;
        }
        return connected;
    }

}