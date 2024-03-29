package tn.dev.netperf.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import tn.dev.netperf.Activities.InfoActivity;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = InfoActivity.getConnectivityStatusString(context);
        if (status.isEmpty()) {
            status = "No Internet Connection";
        }
        Toast.makeText(context, status, Toast.LENGTH_LONG).show();
    }
}