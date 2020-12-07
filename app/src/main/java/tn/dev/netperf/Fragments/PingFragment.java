package tn.dev.netperf.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import tn.dev.netperf.R;


public class PingFragment extends Fragment {

    private EditText tb, tb2;
    private TextView tv;
    private Context mContext;
    MaterialSpinner spinner;
    List<String> listItems = new ArrayList<>();

    // ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ping, container, false);

        mContext = getActivity().getApplicationContext();

        final Button button = view.findViewById(R.id.btn);
        tb = view.findViewById(R.id.textBox1);
        tb2 = view.findViewById(R.id.textBox3);
        tv = view.findViewById(R.id.text3);

        spinner = view.findViewById(R.id.spinner);
        initItems();


        //adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, listItems);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // spinner.setAdapter(adapter);
        MySpinnerAdapter adapter = new MySpinnerAdapter
                (mContext, android.R.layout.simple_spinner_dropdown_item, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != -1) {
                    int selected = Integer.parseInt(spinner.getItemAtPosition(position).toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                executeCommand();
            }
        });

        final Button button4 = view.findViewById(R.id.btn4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'_'HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());

                String textToSaveString = tv.getText().toString();
                generateNoteOnSD(mContext, "PingTest" + formatter.format(date) + ".txt", textToSaveString);

            }
        });
        return view;
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {
        // Initialise custom font, for example:
        final Typeface font = ResourcesCompat.getFont(getContext(),
                R.font.montserrat_bold);

        // (In reality I used a manager which caches the Typeface objects)
        // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        // Affects default (closed) state of the spinner
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }

        // Affects opened state of the spinner
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            view.setTypeface(font);
            return view;
        }
    }

    private void initItems() {
        listItems.add("32");
        listItems.add("64");
        listItems.add("128");
    }

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dmformat = new SimpleDateFormat("yyyy/dd/MM HH:mm:ss");
    String strDate = dmformat.format(calendar.getTime());
    StringBuilder log = new StringBuilder();
    String line;

    private boolean executeCommand() {

        String size = spinner.getSelectedItem().toString();
        String ip = tb.getText().toString();
        String count = tb2.getText().toString();


        if (ip.matches("")) {
            Toast.makeText(mContext, "HOST IP SHOULDN'T BE NULL", Toast.LENGTH_SHORT).show();
        } else if (size.matches("")) {
            Toast.makeText(mContext, "PACKET SIZE SHOULDN'T BE NULL", Toast.LENGTH_SHORT).show();
        } else if (count.matches("")) {
            Toast.makeText(mContext, "COUNT SHOULDN'T BE NULL", Toast.LENGTH_SHORT).show();
        } else {
            try {
                log.append(strDate + "\n");
                String command = "/system/bin/ping -c" + " " + count + " " + "-s" + " " + size + " " + ip;

                Runtime runtime = Runtime.getRuntime();
                Process mIpAddrProcess = runtime.exec(command);
                int mExitValue = mIpAddrProcess.waitFor();
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(mIpAddrProcess.getInputStream()));

                while ((line = bufferedReader.readLine()) != null) {

                    if (line.contains("time=")) {
                        String[] result = line.split("time=");
                        log.append("Time=" + result[1] + "\n");
                    }
                    if (line.contains(count + " packets transmitted")) log.append(line + "\n");
                    if (line.contains("rtt")) log.append(line + "\n");

                    Toast.makeText(mContext, "host pinged " + count + " times with size of " + size, Toast.LENGTH_SHORT).show();
                }
                log.append("-----------------------------\n");
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
        }
        return false;
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
            Toast.makeText(context, "Successfully saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}