package tn.dev.netperf.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.opencsv.CSVWriter;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import tn.dev.netperf.R;


public class DownloadTask {

    private static final String TAG = "Download Task";
    private Context context;
    private Button buttonText;
    private String downloadUrl = "";
    private String downloadFileName = "";
    private String time;
    private String imei;
    private String imsi;
    private String host;
    private String protocol;
    private String requestmessage;
    private double downloadSpeed;
    private int port, requestCode;
    private long serverConnectionTime, fileSize, downloadtime;
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


        @SuppressLint("MissingPermission")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... arg0) {

            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = telephonyManager.getSubscriberId();
            imei = telephonyManager.getImei();


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

                host = url.getHost();
                protocol = url.getProtocol();
                port = url.getDefaultPort();

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

                long startTime = System.currentTimeMillis();
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
                    while ((red = bis.read(buf)) != -1) {
                        size1 += red;
                        currentValue = (int) (size1 * 100 / lenghtOfFile);
                        buttonText.setText((int) ((size1 * 100) / lenghtOfFile) + "%");
                    }

                    is.close();
                    long endTime = System.currentTimeMillis();

                    Log.d("DownloadManager", "download ended: " + ((endTime - startTime) / 1000) + " secs");
                    double rate = (((size1 / 1000) / ((endTime - startTime) / 1000)) * 8);
                    rate = Math.round(rate * 100.0) / 100.0;
                    double ratevalue = rate / 1000;

                    log.append("\n Server connection time: " + (time_connect_finish - time_connect_start) + "ms" +
                            "\n File size : " + (size1 / 1000) / 1000 + "M" +
                            " | Download time: " + (endTime - startTime) / 1000 + "s" +
                            " \n Download speed: " + ratevalue);

                    serverConnectionTime = time_connect_finish - time_connect_start;
                    fileSize = (size1 / 1000) / 1000;
                    downloadtime = (endTime - startTime) / 1000;
                    downloadSpeed = ratevalue;

                    log.append("\n-----------------------");
                    c.disconnect();

                } else {
                    log.append("\n HTTP Req: " + c.getResponseCode()
                            + " " + c.getResponseMessage());
                }

                requestCode = c.getResponseCode();
                requestmessage = c.getResponseMessage();

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
                log.append("\n" + e.getMessage());
            }
            return null;
        }

        /***********************************onPostExecute*******************************/

        @Override
        protected void onPostExecute(Void result) {


            try {
                writeToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
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


    public String getTime() {
        Time myTime = new Time();
        time = myTime.getTime();
        return time;
    }

    public void writeToFile() throws IOException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH");
        String[] data;
        Date date = new Date(System.currentTimeMillis());
        String filePath = Environment.getExternalStorageDirectory() + "/netPerf/Results/PerfMeans_" + imei + "_" + formatter.format(date) + ".csv";
        File file = new File(filePath);

        CSVWriter writer;
        if (file.exists() && !file.isDirectory()) {
            FileWriter myFileWriter = new FileWriter(filePath, true);
            writer = new CSVWriter(myFileWriter, ';', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        } else {
            writer = new CSVWriter(new FileWriter(filePath), ';', CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.NO_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
            String[] header = {"Time", "IMEI", "IMSI", "Service", "Host", "URL", "Content size", "Page load time", "Redirection", "Video ID",
                    "Time to 1st picture", "Video load delay", "Video start delay", "Buffering count", "Protocol", "Port", "Request code", "Status", "Server time to connect",
                    "File Size", "Download time", "Avg throughput"};
            writer.writeNext(header);
        }


        data = new String[]{getTime(), imei, imsi, "HTTP transfert", host, null, null, null, null, null, null, null, null, null, protocol, String.valueOf(port),
                String.valueOf(requestCode), requestmessage, String.valueOf(serverConnectionTime), String.valueOf(fileSize), String.valueOf(downloadtime),
                String.valueOf(downloadSpeed)};


        writer.writeNext(data);
        Toast.makeText(context, "Data saved", Toast.LENGTH_SHORT).show();
        writer.close();
    }
}