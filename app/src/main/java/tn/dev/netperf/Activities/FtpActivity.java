package tn.dev.netperf.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import tn.dev.netperf.R;
import tn.dev.netperf.Utils.UploadTask;

public class FtpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button, saveConf;
    private EditText etUrl, etport, etusername, etpwd, etRemFolder;

    SharedPreferences sharedpreferences;

    public static final String mypreference = "ftppref";
    public static final String ftpUrl = "ftpUrl";
    public static final String ftpPwd = "ftpPwd";
    public static final String ftpUser = "ftpUser";
    public static final int ftpPort = 0;
    public static final String ftpRemoteFolder = "ftpRemoteFolder";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        saveConf = findViewById(R.id.saveConf);
        saveConf.setOnClickListener(this);

        etUrl = findViewById(R.id.et_url);
        etport = findViewById(R.id.et_port);
        etusername = findViewById(R.id.et_user);
        etpwd = findViewById(R.id.et_pwd);
        etRemFolder = findViewById(R.id.et_remote);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(ftpUrl)) {
            etUrl.setText(sharedpreferences.getString(ftpUrl, ""));
        }
        if (sharedpreferences.contains(ftpPwd)) {
            etpwd.setText(sharedpreferences.getString(ftpPwd, ""));
        }
        if (sharedpreferences.contains(ftpUser)) {
            etusername.setText(sharedpreferences.getString(ftpUser, ""));
        }
        if (sharedpreferences.contains(ftpRemoteFolder)) {
            etRemFolder.setText(sharedpreferences.getString(ftpRemoteFolder, ""));
        }
        if (sharedpreferences.contains(String.valueOf(ftpPort))) {
            etport.setText(""+sharedpreferences.getInt("ftpPort", 0));
        }

    }

    @Override
    public void onClick(View v) {
        hideKeybaord(FtpActivity.this);
        switch (v.getId()) {
            case R.id.button:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    new UploadTask(etUrl.getText().toString(), etusername.getText().toString(), etpwd.getText().toString(),
                            Integer.parseInt(etport.getText().toString()), etRemFolder.getText().toString(),FtpActivity.this);
                }
                break;

            case R.id.saveConf:
                Save(v);
                break;
        }

    }

    private static void hideKeybaord(Activity v) {
        InputMethodManager inputMethodManager = (InputMethodManager) v.getSystemService(v.INPUT_METHOD_SERVICE);
        View view = v.getCurrentFocus();
        if (view == null) {
            view = new View(v);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void Save(View view) {
        String url = etUrl.getText().toString();
        String usern = etusername.getText().toString();
        String pwd = etpwd.getText().toString();
        String remoteF = etRemFolder.getText().toString();
        int port = Integer.parseInt(etport.getText().toString());
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(ftpUrl, url);
        editor.putString(ftpUser, usern);
        editor.putString(ftpPwd, pwd);
        editor.putString(ftpRemoteFolder, remoteF);
        editor.putInt("ftpPort", port);
        editor.commit();
        Toast.makeText(this, "FTP configuration saved", Toast.LENGTH_SHORT).show();
    }


    public void Get(View view) {
        ;
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(ftpUrl)) {
            etUrl.setText(sharedpreferences.getString(ftpUrl, ""));
        }
        if (sharedpreferences.contains(ftpPwd)) {
            etpwd.setText(sharedpreferences.getString(ftpPwd, ""));
        }
        if (sharedpreferences.contains(ftpUser)) {
            etusername.setText(sharedpreferences.getString(ftpUser, ""));
        }
        if (sharedpreferences.contains(ftpRemoteFolder)) {
            etRemFolder.setText(sharedpreferences.getString(ftpRemoteFolder, ""));
        }
        if (sharedpreferences.contains(String.valueOf(ftpPort))) {
            etport.setText(""+sharedpreferences.getInt("ftpPort",0));
        }
    }
}
