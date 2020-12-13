package tn.dev.netperf.Utils;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
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
import java.util.Random;

import tn.dev.netperf.R;


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

        new DownloadingTask().execute();
    }

    public DownloadTask() {

    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        /***********************************onPreExecute*******************************/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            buttonText.setEnabled(false);
            buttonText.setTextColor(Color.WHITE);
            buttonText.setText(R.string.downloadStarted);

        }

        /***********************************doInBackground*******************************/


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(downloadUrl);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                log.append("\n Date Time" + dtf.format(now) + "\n");

                Log.d("DownloadManager", "download url:" + url);
                Log.d("DownloadManager", "downloaded file name:" + downloadFileName);
                log.append("\n Host: " + url.getHost() +
                        "\n Protocol: " + url.getProtocol() +
                        "\n Port: " + url.getDefaultPort() + "\n");

                long time_connect_start = System.currentTimeMillis();
                HttpURLConnection c = (HttpURLConnection) url.openConnection();

                Log.d("HttpURLConnectionCache", "downloaded file name:" + c.getDefaultUseCaches() + "  " + c.getUseCaches());

                c.setUseCaches(false);
                c.setDefaultUseCaches(false);
                c.addRequestProperty("Cache-Control", "no-cache");
                c.addRequestProperty("Cache-Control", "max-age=0");


                Log.d("HttpURLConnectionCache", "downloaded file name:" + c.getDefaultUseCaches() + "  " +
                        "" + c.getUseCaches());

                c.setRequestMethod("GET");
                c.connect();
                long time_connect_finish = System.currentTimeMillis();


                if (c.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    log.append("\n HTTP Req: " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                    InputStream is = c.getInputStream();

                    int lenghtOfFile = c.getContentLength();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();


                    long size1 = 0;
                    int red = 0;
                    int currentValue = 0;
                    byte[] buf = new byte[128000];
                    new Random().nextBytes(buf);

                    long startTime = System.currentTimeMillis();
                    while ((red = bis.read(buf)) != -1) {
                        size1 += red;
                        currentValue = (int) (size1 * 100 / lenghtOfFile);
                        buttonText.setText((int) ((size1 * 100) / lenghtOfFile) + "%");
                    }

                    is.close();
                    long endTime = System.currentTimeMillis();

                    Log.d("DownloadManager", "download ended: " + ((endTime - startTime) / 1000) + " secs");
                    double rate = (((size1 / 1024) / ((endTime - startTime) / 1000)) * 8);
                    rate = Math.round(rate * 100.0) / 100.0;
                    String ratevalue;

                    if (rate > 1000) {
                        ratevalue = String.valueOf(rate / 1024).concat(" Mbps");
                    } else
                        ratevalue = String.valueOf(rate).concat(" Kbps");

                    log.append("\n Server connection time: " + (time_connect_finish - time_connect_start) + "ms" +
                            "\n File size : " + (size1 / 1024) / 1024 + "M" +
                            " | Download time: " + (endTime - startTime) / 1000 + "s" +
                            " \n Download speed: " + ratevalue);
                    log.append("\n-----------------------");

                    Log.d("DownloadManager",
                            "Size: " + size1 +
                                    "\n size / 1024 : " + size1 / 1024 +
                                    "\n (endTime - startTime) / 1000 : " + (endTime - startTime) / 1000 +
                                    "\ndownload speed: " + ratevalue);

                    c.disconnect();

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Download Error Exception " + e.getMessage());
            }
            return null;
        }

        /***********************************onPostExecute*******************************/

        @Override
        protected void onPostExecute(Void result) {


            buttonText.setEnabled(true);
            text_.append(log.toString());
            buttonText.setText(R.string.downloadAgain);
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

        } catch (IOException e) {
            e.printStackTrace();


        }
    }
}