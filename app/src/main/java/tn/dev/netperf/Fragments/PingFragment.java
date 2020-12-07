package tn.dev.netperf.Fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import tn.dev.netperf.R;


public class PingFragment extends Fragment {

    private EditText tb, tb1, tb2;
    private TextView tv;
    private static final String TAG = PingFragment.class.getSimpleName();
    private Context mContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ping, container, false);

        final TextView textView = view.findViewById(R.id.text2);
        final Button button = view.findViewById(R.id.btn);
        tb = view.findViewById(R.id.textBox1);
        tb1 = view.findViewById(R.id.textBox2);
        tb2 = view.findViewById(R.id.textBox3);
        tv = view.findViewById(R.id.text3);

        mContext = getActivity().getApplicationContext();
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                executeCommand();
            }
        });

        final Button button4 = view.findViewById(R.id.btn4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

            }
        });
        TextView tv = view.findViewById(R.id.text3);
        String textToSaveString = tv.getText().toString();
        writeToFile(textToSaveString);

        return view;
    }

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dmformat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
    String strDate = dmformat.format(calendar.getTime());
    StringBuilder log = new StringBuilder();
    String line;

    private boolean executeCommand() {


        String ip = tb.getText().toString();
        if (ip.matches("")) {
            Toast.makeText(mContext, "Pinging localhost", Toast.LENGTH_LONG).show();
            ip = "localhost";
        }

        String size = tb1.getText().toString();
        if (size.matches("")) {
            size = "64";
        }

        String count = tb2.getText().toString();
        if (count.matches("")) {
            count = "3";
        }
        try {
            String command = "/system/bin/ping -c" + " " + count + " " + "-s" + " " + size + " " + ip;
            //System.out.println(command);
            Runtime runtime = Runtime.getRuntime();
            Process mIpAddrProcess = runtime.exec(command);
            int mExitValue = mIpAddrProcess.waitFor();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(mIpAddrProcess.getInputStream()));

            // Grab the results
            log.append(strDate + ">\n");
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("time=")) {
                    String[] result = line.split("time=");
                    log.append("Time=" + result[1] + "\n");
                }
                if (line.contains(count + " packets transmitted")) log.append(line + "\n");
                if (line.contains("rtt")) log.append(line + "\n");

            }
            tv.setText(log.toString());

            System.out.println(" mExitValue " + mExitValue);
            if (mExitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.out.println(" Exception:" + e);
        }
        return false;
    }

    private void writeToFile(String data) {

        String filename = "myfile.txt";

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput(filename, Context.MODE_APPEND));
            outputStreamWriter.write(data);
            outputStreamWriter.close();

            Log.e(TAG, "File write successfully: " + filename);
        } catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }

    }
}