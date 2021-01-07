package tn.dev.netperf.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import tn.dev.netperf.R;
import tn.dev.netperf.Utils.Time;


public class BrowsingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = BrowsingFragment.class.getSimpleName();

    WebView webView;
    TextInputEditText editText;
    ImageButton img1, img2, img3, img4, img5, img6;

    TextView tv1, tv2, tv3, tv4, tv5;
    long a, b;
    int counter;
    int size;

    private String time;
    int Permission_All = 1;
    private TelephonyManager telephonyManager;
    private String imei, imsi, website;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View myView = inflater.inflate(R.layout.fragment_browsing, container, false);


        webView = myView.findViewById(R.id.web_View_Brow);
        editText = myView.findViewById(R.id.input_text);
        img1 = myView.findViewById(R.id.google);
        img1.setOnClickListener(this);
        img2 = myView.findViewById(R.id.fb);
        img2.setOnClickListener(this);
        img3 = myView.findViewById(R.id.cnn);
        img3.setOnClickListener(this);
        img4 = myView.findViewById(R.id.tunisia_net);
        img4.setOnClickListener(this);
        img5 = myView.findViewById(R.id.mos_aik);
        img5.setOnClickListener(this);


        tv1 = myView.findViewById(R.id.tv_load);
        tv2 = myView.findViewById(R.id.tv_con_tent);
        tv3 = myView.findViewById(R.id.tv_per_ctge);
        tv4 = myView.findViewById(R.id.tv_Red);
        tv5 = myView.findViewById(R.id.tv_website);

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        String[] Permissions = {Manifest.permission.READ_PHONE_STATE};
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), Permissions, Permission_All);
        }
        imsi = telephonyManager.getSubscriberId();
        imei = telephonyManager.getImei();

        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        editText.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_DPAD_CENTER:
                    case KeyEvent.KEYCODE_ENTER:
                        webView.loadUrl("http://" + editText.getText().toString());
                        return true;
                    default:
                        break;
                }
            }
            editText.setText("");
            return false;
        });

        return myView;
    }


    private class MyBrowser extends WebViewClient {

        boolean loadingFinished = true;
        boolean redirect = false;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, WebResourceRequest request) {
            if (!loadingFinished) {
                redirect = true;
            }
            loadingFinished = false;
            webView.loadUrl(request.getUrl().toString());
            return true;

        }


        @Override
        public void onPageStarted(
                WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


            website = webView.getUrl();
            tv5.setText(website);

            loadingFinished = false;
            a = (new Date()).getTime();
            counter = 1;
            size = 0;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {

                loadingFinished = true;
                b = (new Date()).getTime();
                onLoadResource(view, url);
                tv1.setText(String.valueOf(b - a));
                tv3.setText(view.getProgress() + "%");

            } else {
                redirect = false;
            }
            if (view.getProgress() == 100) {
                try {
                    writeToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            view.clearCache(true);
            view.clearFormData();
        }

        @Override
        public void onLoadResource(WebView view, final String url) {
            super.onLoadResource(view, url);

            counter++;
            Log.e("counter", counter + " AND URL: " + url);
            tv4.setText(String.valueOf(counter));

            new Thread(() -> {
                try {
                    URL myUrl = new URL(url);
                    URLConnection urlConnection = myUrl.openConnection();
                    urlConnection.connect();
                    int file_size = urlConnection.getContentLength();
                    size = size + file_size;
                    Log.e("file_Size", size + " AND " + file_size);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            tv2.setText(String.valueOf(size / 10));

        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google:
                webView.loadUrl("https://www.google.com/");
                break;
            case R.id.fb:
                webView.loadUrl("https://www.facebook.com/");
                break;
            case R.id.cnn:
                webView.loadUrl("https://edition.cnn.com/");
                break;
            case R.id.mos_aik:
                webView.loadUrl("https://www.mosaiquefm.net/amp/fr/");
                break;
            case R.id.tunisia_net:
                webView.loadUrl("https://www.tunisianet.com.tn/");
                break;
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


        data = new String[]{getTime(), imei, imsi, "HTTP browsing", webView.getTitle(), website, String.valueOf(size), String.valueOf(b - a), String.valueOf(counter)};


        writer.writeNext(data);
        Toast.makeText(getContext(), "Data saved", Toast.LENGTH_SHORT).show();
        writer.close();
    }


}