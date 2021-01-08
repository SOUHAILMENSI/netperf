package tn.dev.netperf.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;

import it.sauronsoftware.ftp4j.FTPClient;


public class UploadTask {

    private static final String TAG = "Upload Task";
    private Context ctx;
    private String host;
    private String username;
    private String password;
    private int port;
    private String upFile;
    private ProgressDialog p;

    int i=0,j=0;


    private FTPClient mFTPClient;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public UploadTask(String host, String username, String password, int port, String upFile, Context ctx) {
        this.host = host;
        this.password = password;
        this.port = port;
        this.username = username;
        this.upFile = upFile;
        this.p = new ProgressDialog(ctx);
        this.ctx = ctx;


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
                i = folder.listFiles().length;
                for (final File fileEntry : folder.listFiles()) {

                    try {
                        if (!fileEntry.isDirectory()) {
                            mFTPClient.upload(fileEntry);
                            j++;
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


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final File folder = new File(Environment.getExternalStorageDirectory() + "/netPerf/Results/");
            i = folder.listFiles().length;

            p.setMessage("Uploading measurement files ("+i+")..");
            p.setIndeterminate(false);
            p.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            p.setCancelable(false);
            p.show();
        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            p.dismiss();
            if (result) {
                Toast.makeText(ctx, j+" measurement files uploaded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ctx, "Upload failed", Toast.LENGTH_SHORT).show();
            }
        }

    }

}