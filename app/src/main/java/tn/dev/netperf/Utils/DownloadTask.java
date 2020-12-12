package tn.dev.netperf.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import tn.dev.netperf.R;

/**
 * Created by SONU on 29/10/15.
 */
public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private String downloadUrl = "", downloadFileName = "";
    private TextView text_;

    StringBuilder log = new StringBuilder();

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DownloadTask(Context context, Button buttonText, String downloadUrl, TextView text_) {
        this.context = context;
        this.buttonText = buttonText;
        this.downloadUrl = downloadUrl;
        this.text_ = text_;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        LocalDateTime now = LocalDateTime.now();

        downloadFileName = dtf.format(now);
        Log.e(TAG, downloadFileName);
        new DownloadingTask().execute();
    }

    public DownloadTask() {

    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonText.setEnabled(false);
            buttonText.setText(R.string.downloadStarted);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... arg0) {
            try {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                log.append("\n Date Time" + dtf.format(now) +"\n");

                URL url = new URL(downloadUrl);
                long startTime = System.currentTimeMillis();

                Log.d("DownloadManager", "download begining: " + startTime);
                Log.d("DownloadManager", "download url:" + url);
                Log.d("DownloadManager", "downloaded file name:" + downloadFileName);

                long time_connect_start = System.currentTimeMillis();
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                long time_connect_finish = System.currentTimeMillis();



                if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    log.append("\n HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());


                    InputStream is = c.getInputStream();

                    int lenghtOfFile = c.getContentLength();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    long size1 = 0;
                    int red = 0;
                    int currentValue = 0;
                    byte[] buf = new byte[2048];

                    while ((red = bis.read(buf)) != -1) {
                        size1 += red;
                        currentValue = (int) (size1 * 100 / lenghtOfFile);
                        buttonText.setText((int) ((size1 * 100) / lenghtOfFile) + "%");
                    }


                    is.close();
                    long endTime = System.currentTimeMillis();

                    File done = new File(context.getFilesDir() + "/temp/" + downloadFileName);
                    Log.d("DownloadManager", "Location being searched: " + context.getFilesDir() + "/temp/" + downloadFileName);
                    // double size = done.length();
                    if (done.exists()) {
                        done.delete();
                    }

                    Log.d("DownloadManager", "download ended: " + ((endTime - startTime) / 1000) + " secs");
                    double rate = (((size1 / 1024) / ((endTime - startTime) / 1000)) * 8);
                    rate = Math.round(rate * 100.0) / 100.0;
                    String ratevalue;

                    if (rate > 1000) {
                        ratevalue = String.valueOf(rate / 1024).concat(" Mbps");
                    } else
                        ratevalue = String.valueOf(rate).concat(" Kbps");

                            log.append("\n Server connection time: "+(time_connect_finish-time_connect_start)+"ms"+
                            "\n File size : " + (size1 / 1024) / 1024 +"M" +
                            " | Download time: " + (endTime - startTime) / 1000 +"s"+
                            " \n Download speed: " + ratevalue);
                    log.append("\n-----------------------");

                    Log.d("DownloadManager",
                            "Size: " + size1 +
                                    "\n size / 1024 : " + size1 / 1024 +
                                    "\n (endTime - startTime) / 1000 : " + (endTime - startTime) / 1000 +
                                    "\ndownload speed: " + ratevalue);
                }
            } catch (Exception e) {
                e.printStackTrace();
                outputFile = null;
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            try {
                if (outputFile == null) {
                    buttonText.setEnabled(true);
                    text_.append(log.toString());
                } else {
                    buttonText.setText(R.string.downloadFailed);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buttonText.setEnabled(true);
                        }
                    }, 2000);

                    Log.e(TAG, "Download Failed");

                }
                buttonText.setText(R.string.downloadAgain);

            } catch (Exception e) {
                e.printStackTrace();

                buttonText.setText(R.string.downloadFailed);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        buttonText.setEnabled(true);
                        buttonText.setText(R.string.downloadAgain);
                    }
                }, 3000);
                Log.e(TAG, "Download Failed with Exception - " + e.getLocalizedMessage());

            }


            super.onPostExecute(result);
        }
    }


    public void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "netPerf/perfmeans");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

            //Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();


        }
    }
}