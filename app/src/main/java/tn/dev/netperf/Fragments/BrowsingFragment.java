package tn.dev.netperf.Fragments;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import tn.dev.netperf.R;


public class BrowsingFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = BrowsingFragment.class.getSimpleName();

    WebView webView;
    TextInputEditText editText;
    ImageButton img1, img2, img3, img4, img5, img6;

    TextView tv1, tv2, tv3, tv4, tv5;
    long a, b;
    int counter;
    int size;


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

        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.loadUrl("https://www.google.tn/");

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


        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int newProgress) {

                tv3.setText(newProgress + "%");
                if (newProgress == 100) {
                    tv3.setText("100%");
                }
            }
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

            if (view.getUrl().startsWith("https://m.facebook.com/")) {
                tv5.setText("https://m.facebook.com/");
            } else {
                tv5.setText(view.getUrl());
            }

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
                tv1.setText(String.valueOf(b - a));


            } else {
                redirect = false;
            }

            webView.clearHistory();
            webView.clearCache(true);
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
}