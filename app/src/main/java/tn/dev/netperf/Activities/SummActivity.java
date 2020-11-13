package tn.dev.netperf.Activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import tn.dev.netperf.R;
import tn.dev.netperf.Models.RadioInfo;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class SummActivity extends AppCompatActivity {


    public SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    private static final int PERMISSION_REQUEST_CODE = 100;
    public Handler mHandler;

    public static RadioInfo radioInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summ);

        mHandler = new Handler();

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SummActivity.this, new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);

        }
        radioInfo = new RadioInfo(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
       mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
         tabLayout.setupWithViewPager(mViewPager);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

            return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        private String updateText() {
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            switch (sectionNumber) {
                case 1:
                    String lteCellInfo = String.format(Locale.US, " MCC: %d\n MNC: %d\n CI: %d\n PCI: %d\n TAC:" +
                                    " %d\n EARFCN: %d\n eNB: %d\n Sector ID: %d\n",
                            radioInfo.getLte_MCC(), radioInfo.getLte_MNC(), radioInfo.getLteCI(),
                            radioInfo.getLtePCI(), radioInfo.getLteTAC(),radioInfo.getLteEarfcn(),
                            radioInfo.GetEnB(),radioInfo.getSectorId());
                    String lteSignal = String.format(Locale.US, "\n RSRP: %d\n RSRQ: %d\n SINR: %d\n CQI: %d\n",
                            radioInfo.getLteRSRP(), radioInfo.getLteRSRQ(), radioInfo.getLteSINR(), radioInfo.getLteCQI());
                    return getString(R.string.lte_cellular_info) + lteCellInfo + lteSignal;
                case 2:
                    String wcdmaCellInfo = String.format(Locale.US, " MCC: %d\n MNC: %d\n LAC: %d\n CID: %d\n PSC: %d\n UARFCN: %d\n",
                            radioInfo.getWcdma_MCC(), radioInfo.getWcdma_MNC(), radioInfo.getWcdma_LAC(),
                            radioInfo.getWcdma_CID(), radioInfo.getWcdma_PSC(),radioInfo.getWcdma_Uarfcn());
                    String wcdmaSignal = String.format(Locale.US, "\n RSSI: %d\n RSCP: %d\n Ec/No: %d\n ASU: %d\n", radioInfo.getWcdma_RSSI(),
                            radioInfo.getwcdm_RSCP(),radioInfo.getWcdma_EcNo(),radioInfo.getWcdma_ASU());
                    return getString(R.string.umts_cellular_info) + wcdmaCellInfo + wcdmaSignal;
                case 3:
                    String gsmCellInfo = String.format(Locale.US, " MCC: %d\n MNC: %d\n LAC: %d\n CID: %d\n BSIC: %d\n BCCH: %d\n",
                            radioInfo.getGsm_MCC(), radioInfo.getGsm_MNC(), radioInfo.getGsm_LAC(),
                            radioInfo.getGsm_CID(),radioInfo.getgsm_Bsic(),radioInfo.getGsm_Arfcn());
                    String gsmSignal = String.format(Locale.US, "\n RSSI: %d\n BER: %d\n ASU: %d\n Rxlev: %d\n",
                            radioInfo.getGsm_RSSI(),radioInfo.getgsm_Berror(),radioInfo.getgsm_Asu(),radioInfo.getgsm_rxlev());
                    return getString(R.string.gsm_cellular_info) + gsmCellInfo + gsmSignal;

                default:
                    return null;
            }
        }

        public void updateView() {
            View rootView = getView();
            if (rootView == null) {
                return;
            }

            TextView textView = (TextView) getView().findViewById(R.id.section_label);
            textView.setText(updateText());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(updateText());

            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof PlaceholderFragment) {
                ((PlaceholderFragment) object).updateView();
            }
            return super.getItemPosition(object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LTE";
                case 1:
                    return "UMTS";
                case 2:
                    return "GSM";
            }
            return null;
        }
    }
}
