package tn.dev.netperf.Fragments;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.TrafficStats;
import android.net.http.SslError;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import tn.dev.netperf.R;


public class BrowsingFragment extends Fragment implements View.OnClickListener {


    WebView webView;
    TextInputEditText editText;
    ImageButton img1, img2, img3, img4, img5,img6;

    TextView tv1, tv2, tv3;
    long a, b;

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
        img4 = myView.findViewById(R.id.jum_ia);
        img4.setOnClickListener(this);
        img5 = myView.findViewById(R.id.mos_aik);
        img5.setOnClickListener(this);
        img6 = myView.findViewById(R.id.kep_ler);
        img6.setOnClickListener(this);

        tv1 = myView.findViewById(R.id.tv_load);

        tv2 = myView.findViewById(R.id.tv_con_tent);
        tv3 = myView.findViewById(R.id.tv_per_ctge);

        webView.setWebViewClient(new MyBrowser());
        webView.getSettings().setJavaScriptEnabled(true);

        if (Build.VERSION.SDK_INT >= 21) {
            webView.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }
        webView.loadUrl("https://www.google.com/");

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            webView.loadUrl("https://" + editText.getText().toString());
                            return true;
                        default:
                            break;
                    }
                }

                return false;
            }
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
            loadingFinished = false;

            a = (new Date()).getTime();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (!redirect) {
                loadingFinished = true;
                b = (new Date()).getTime();

                tv1.setText(String.valueOf(b - a));
                tv2.setText(""+(TrafficStats.getUidRxBytes(myUid())/1024));

            } else {
                redirect = false;
            }
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google:
                webView.loadUrl("https://www.google.tn/");
                break;
            case R.id.fb:
                webView.loadUrl("https://www.facebook.com/");
                break;
            case R.id.kep_ler:
                webView.loadUrl("http://mfr63.customer.swissqual.com/data/keplerhtml/");
                break;
            case R.id.cnn:
                webView.loadUrl("https://edition.cnn.com/");
                break;
            case R.id.mos_aik:
                webView.loadUrl("https://www.mosaiquefm.net/amp/fr/");
                break;
            case R.id.jum_ia:
                webView.loadUrl("https://www.jumia.com.tn/");
                break;
        }
    }

    public int myUid(){
        int uid = 0;
        try {
            uid = getContext().getPackageManager().getApplicationInfo("tn.dev.netperf", 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return uid;

    }
}