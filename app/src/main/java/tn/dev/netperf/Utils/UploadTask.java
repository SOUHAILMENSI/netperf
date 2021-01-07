package tn.dev.netperf.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;


public class UploadTask {

    private static final String TAG = "Upload Task";
    private String host;
    private String username;
    private String password;
    private int port;
    private String upFile;
    Context context;

    private FTPClient mFTPClient;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public UploadTask(String host, String username, String password, int port, String upFile) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.username = username;
        this.upFile = upFile;

        new UploadingTask().execute();


    }


    private class UploadingTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            mFTPClient = new FTPClient();
            boolean statue = false;

            try {

                mFTPClient.connect(host, port);
                mFTPClient.setPassive(true);
                mFTPClient.login(username, password);
                mFTPClient.setType(FTPClient.TYPE_BINARY);

                Log.i("testConnection", "Info: connected to host " + host);

                mFTPClient.changeDirectory(upFile);
                final File folder = new File(Environment.getExternalStorageDirectory() + "/netPerf/Results/");

                for (final File fileEntry : folder.listFiles()) {
                    try {
                        if (!fileEntry.isDirectory()) {
                            mFTPClient.upload(fileEntry);
                            Log.i(TAG, "sent");
                            statue = true;
                        }
                    } catch (Exception e) {
                        Log.i(TAG, "error uploading");

                    }
                }

            } catch (Exception e) {
                Log.i("testConnection", "Error: could not connect to host " + host);

            }

            Log.i("testConnection", String.valueOf(statue));

            return statue;
        }

    }

}