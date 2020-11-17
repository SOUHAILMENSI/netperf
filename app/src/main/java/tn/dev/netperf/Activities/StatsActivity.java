package tn.dev.netperf.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import tn.dev.netperf.R;


public class StatsActivity extends AppCompatActivity {
    private TextView txsys, tx1, tx2, tx3, tx4, tx5, tx6, tx7, tx8, tx9,
            tx10, tx11, tx12, tx13, tx14, tx15, tx16, tx17, tx18, tx19, tx20, tx21, tx22, txband, txbandval,txmodul,txmodulval;


    private TelephonyManager telephonyManagerToListen = null;

    private int lte_MCC = Integer.MAX_VALUE;
    private int lte_MNC = Integer.MAX_VALUE;
    private int lte_CI = Integer.MAX_VALUE;
    private int lte_PCI = Integer.MAX_VALUE;
    private int lte_TAC = Integer.MAX_VALUE;
    private int lte_RSRP = Integer.MAX_VALUE;
    private int lte_RSRQ = Integer.MAX_VALUE;
    private int lte_SINR = Integer.MAX_VALUE;
    private int lte_CQI = Integer.MAX_VALUE;
    private int lte_Earfcn = Integer.MAX_VALUE;
    private double lte_bandwith = Double.MAX_VALUE;
   // private int[] lte_band ;


    private int Lte_Asu = Integer.MAX_VALUE;
    private int lte_dbm = Integer.MAX_VALUE;


    private int gsm_MCC = Integer.MAX_VALUE;
    private int gsm_MNC = Integer.MAX_VALUE;
    private int gsm_LAC = Integer.MAX_VALUE;
    private int gsm_Arfcn = Integer.MAX_VALUE;
    private int gsm_CID = Integer.MAX_VALUE;
    private int gsm_RSSI = Integer.MAX_VALUE;
    private int gsm_Bsic = Integer.MAX_VALUE;
    private int gsm_Berror = Integer.MAX_VALUE;
    private int gsm_Asu = Integer.MAX_VALUE;
    private int gsm_rxlev = Integer.MAX_VALUE;

    private int wcdma_MCC = Integer.MAX_VALUE;
    private int wcdma_MNC = Integer.MAX_VALUE;
    private int wcdma_LAC = Integer.MAX_VALUE;
    private int wcdma_Uarfcn = Integer.MAX_VALUE;
    private int wcdma_CID = Integer.MAX_VALUE;
    private int wcdma_PSC = Integer.MAX_VALUE;
    private int wcdma_RSSI = Integer.MAX_VALUE;
    private int wcdma_RSCP = Integer.MAX_VALUE;
    private int wcdma_ASU = Integer.MAX_VALUE;
    private int wcdma_Ecno = Integer.MAX_VALUE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        txsys = findViewById(R.id.system);
        tx1 = findViewById(R.id.txradio1);
        tx2 = findViewById(R.id.txradio2);
        tx3 = findViewById(R.id.txradio3);
        tx4 = findViewById(R.id.txradio4);
        tx5 = findViewById(R.id.txradio5);
        tx6 = findViewById(R.id.txradio6);
        tx7 = findViewById(R.id.txradio7);
        tx8 = findViewById(R.id.txradio8);
        tx9 = findViewById(R.id.txradio9);
        tx10 = findViewById(R.id.txradio10);
        tx11 = findViewById(R.id.txradio11);
        tx12 = findViewById(R.id.txradio12);
        tx13 = findViewById(R.id.txradio13);
        tx14 = findViewById(R.id.txradio14);
        tx15 = findViewById(R.id.txradio15);
        tx16 = findViewById(R.id.txradio16);
        tx17 = findViewById(R.id.txradio17);
        tx18 = findViewById(R.id.txradio18);
        tx19 = findViewById(R.id.txradio19);
        tx20 = findViewById(R.id.txradio20);
        tx21 = findViewById(R.id.txradio21);
        tx22 = findViewById(R.id.txradio22);
        txband = findViewById(R.id.txradioband);
        txbandval = findViewById(R.id.txradiobandval);
        txmodul=findViewById(R.id.txmodulation);
        txmodulval = findViewById(R.id.txmodulval);


        /*******************************OnCreate ends here *******************************************/

        Context mContext = this;
        telephonyManagerToListen = (TelephonyManager) this.getSystemService
                (this.TELEPHONY_SERVICE);
        telephonyManagerToListen.listen(mPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_CELL_INFO | PhoneStateListener.LISTEN_CELL_LOCATION);


    }

    private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {


        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {

            super.onSignalStrengthsChanged(signalStrength);
            //Log.e(Tag, signalStrength.toString());

            get_Reflection_Method(signalStrength);

            try {
               /* Method getLteRsrp = signalStrength.getClass().getDeclaredMethod("getLteRsrp");
                getLteRsrp.setAccessible(true);
                lte_RSRP = (int) getLteRsrp.invoke(signalStrength);*/

                @SuppressLint("SoonBlockedPrivateApi") Method getLteRsrq = signalStrength.getClass().getDeclaredMethod("getLteRsrq");
                getLteRsrq.setAccessible(true);
                lte_RSRQ = (int) getLteRsrq.invoke(signalStrength);

                @SuppressLint("SoonBlockedPrivateApi") Method getLteRssnr = signalStrength.getClass().getDeclaredMethod("getLteRssnr");
                getLteRssnr.setAccessible(true);
                lte_SINR = (int) getLteRssnr.invoke(signalStrength);

                @SuppressLint("SoonBlockedPrivateApi") Method getLteCqi = signalStrength.getClass().getDeclaredMethod("getLteCqi");
                getLteRssnr.setAccessible(true);
                lte_CQI = (int) getLteCqi.invoke(signalStrength);

                gsm_RSSI = signalStrength.getGsmSignalStrength();
                gsm_Berror = signalStrength.getGsmBitErrorRate();

                try {
                    gsm_rxlev = -113 + 2 * gsm_RSSI;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                getCellSignalStrength();
                getCellIdentity();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onCellInfoChanged(List<CellInfo> cellInfoList) {
            super.onCellInfoChanged(cellInfoList);


            if (cellInfoList == null) {
                //Log.e(Tag,"onCellInfoChanged is null");
                return;
            }

            //Log.e(Tag,"onCellInfoChanged size "+cellInfoList.size());

            for (CellInfo cellInfo : cellInfoList) {

                if (!cellInfo.isRegistered())
                    continue;

                if (cellInfo instanceof CellInfoLte) {

                    CellInfoLte lteinfo = (CellInfoLte) cellInfo;

                    lte_MCC = lteinfo.getCellIdentity().getMcc();
                    lte_MNC = lteinfo.getCellIdentity().getMnc();
                    lte_CI = lteinfo.getCellIdentity().getCi();
                    lte_PCI = lteinfo.getCellIdentity().getPci();
                    lte_TAC = lteinfo.getCellIdentity().getTac();
                    lte_Earfcn = lteinfo.getCellIdentity().getEarfcn();
                    GetEnB();
                    getSectorId();
                    //Log.e(Tag,lteinfo.toString());
                } else if (cellInfo instanceof CellInfoGsm) {
                    CellInfoGsm gsmInfo = (CellInfoGsm) cellInfo;

                    gsm_MCC = gsmInfo.getCellIdentity().getMcc();
                    gsm_MNC = gsmInfo.getCellIdentity().getMnc();
                    gsm_CID = gsmInfo.getCellIdentity().getCid();
                    gsm_LAC = gsmInfo.getCellIdentity().getLac();
                    gsm_Arfcn = gsmInfo.getCellIdentity().getArfcn();
                    gsm_Bsic = gsmInfo.getCellIdentity().getBsic();

                } else if (cellInfo instanceof CellInfoWcdma) {
                    CellInfoWcdma wcdmaInfo = (CellInfoWcdma) cellInfo;
                    wcdma_MCC = wcdmaInfo.getCellIdentity().getMcc();
                    wcdma_MNC = wcdmaInfo.getCellIdentity().getMnc();
                    wcdma_CID = wcdmaInfo.getCellIdentity().getCid() % 65536;
                    wcdma_LAC = wcdmaInfo.getCellIdentity().getLac();
                    wcdma_PSC = wcdmaInfo.getCellIdentity().getPsc();
                    wcdma_Uarfcn = wcdmaInfo.getCellIdentity().getUarfcn();
                }
            }


        }
    };

    @SuppressLint("LongLogTag")
    private void getCellSignalStrength() {
        @SuppressLint("MissingPermission") List<CellInfo> cellInfoList = telephonyManagerToListen.getAllCellInfo();
        if (cellInfoList == null) {
            Log.e("TaggetCellSignalStrength", "getAllCellInfo is null");
            return;
        }
        Log.e("TaggetCellSignalStrength", "getAllCellInfo: " + cellInfoList.size() + "\n" + cellInfoList);
        for (CellInfo cellInfo : cellInfoList) {
            if (!cellInfo.isRegistered())    // Primary cell
                continue;
            if (cellInfo instanceof CellInfoWcdma) {
                CellInfoWcdma wcdmaInfo = (CellInfoWcdma) cellInfo;
                wcdma_RSSI = wcdmaInfo.getCellSignalStrength().getDbm();
                wcdma_ASU = wcdmaInfo.getCellSignalStrength().getAsuLevel();
                // NeighboringCellInfo neighboringCellInfo = new NeighboringCellInfo();
                // wcdm_RSCP = neighboringCellInfo.getNetworkType();

                tx13.setText("RSSI");
                tx14.setText(wcdma_RSSI + " dBm");
                tx15.setText("ASU");
                tx16.setText(wcdma_ASU + " dBm");
                tx17.setText("EcIo");
                tx18.setText(getWcdma_EcNo() + " dB");
                tx19.setText("RSCP");
                tx20.setText(getwcdm_RSCP() + " dBm");


            } else if (cellInfo instanceof CellInfoGsm) {
                CellInfoGsm gsmInfo = (CellInfoGsm) cellInfo;
                gsm_Asu = gsmInfo.getCellSignalStrength().getAsuLevel();
                tx13.setText("RSSI");
                tx14.setText(gsm_RSSI + " dBm");
                tx15.setText("BER");
                tx16.setText(String.valueOf(gsm_Berror));
                tx17.setText("ASU");
                tx18.setText(String.valueOf(gsm_Asu +" dBm"));
                tx19.setText("Rxlev");
                tx20.setText(gsm_rxlev +" dBm");

            } else if (cellInfo instanceof CellInfoLte) {
                CellInfoLte lteInfo = (CellInfoLte) cellInfo;
                Lte_Asu = lteInfo.getCellSignalStrength().getAsuLevel();
                lte_dbm = lteInfo.getCellSignalStrength().getDbm();
                Log.e("LTEASU/DBM", "" + Lte_Asu + "\n" + lte_dbm);

                tx13.setText("RSRP");
                tx14.setText(lte_dbm + " dBm");
                tx15.setText("RSRQ");
                tx16.setText(lte_RSRQ + " dB");
                tx17.setText("SINR");
                tx18.setText(getLteSINR() + " dB");
                tx19.setText("CQI");
                tx20.setText(String.valueOf(lte_CQI));

            }
        }
    }


    @androidx.annotation.RequiresApi(api = Build.VERSION_CODES.P)
    private void getCellIdentity() {
        @SuppressLint("MissingPermission") List<CellInfo> cellInfoList = telephonyManagerToListen.getAllCellInfo();

        if (cellInfoList == null) {
            // Log.e(Tag, "getAllCellInfo is null");
            return;
        }
        //  Log.e(Tag, "getAllCellInfo size " + cellInfoList.size());
        for (CellInfo cellInfo : cellInfoList) {
            if (!cellInfo.isRegistered())
                continue;
            if (cellInfo instanceof CellInfoLte) {
                CellInfoLte lteinfo = (CellInfoLte) cellInfo;
                lte_MCC = lteinfo.getCellIdentity().getMcc();
                lte_MNC = lteinfo.getCellIdentity().getMnc();
                lte_CI = lteinfo.getCellIdentity().getCi();
                lte_PCI = lteinfo.getCellIdentity().getPci();
                lte_TAC = lteinfo.getCellIdentity().getTac();
                lte_Earfcn = lteinfo.getCellIdentity().getEarfcn();
                lte_bandwith = lteinfo.getCellIdentity().getBandwidth();
              // lte_band = lteinfo.getCellIdentity().getBands();


                txsys.setText("LTE");
                tx1.setText("MCC/MNC");
                tx2.setText(lte_MCC + "/" + lte_MNC);
                tx3.setText("CID");
                tx4.setText(String.valueOf(lte_CI));
                tx5.setText("TAC");
                tx6.setText(String.valueOf(lte_TAC));
                tx7.setText("EARFCN");
                tx8.setText(String.valueOf(lte_Earfcn));
                tx9.setText("PCI");
                tx10.setText(String.valueOf(lte_PCI));
                tx11.setText("DL/UL Freq");
                tx12.setText(getGsmDLfrequency() + "/" + getGsmULfrequency());
                txband.setText("Band (bandwith)");
                txbandval.setText(String.valueOf(lte_bandwith ));
                tx21.setVisibility(View.VISIBLE);
                tx22.setVisibility(View.VISIBLE);
                tx21.setText("eNB/Sector ID");
                tx22.setText(GetEnB()+"/"+getSectorId());
                txmodul.setText("Modulation");
                txmodulval.setText(getLTEmodulation());

            }
            else if (cellInfo instanceof CellInfoGsm) {
                CellInfoGsm gsmInfo = (CellInfoGsm) cellInfo;

                gsm_MCC = gsmInfo.getCellIdentity().getMcc();
                gsm_MNC = gsmInfo.getCellIdentity().getMnc();
                gsm_CID = gsmInfo.getCellIdentity().getCid();
                gsm_LAC = gsmInfo.getCellIdentity().getLac();
                gsm_Arfcn = gsmInfo.getCellIdentity().getArfcn();
                gsm_Bsic = gsmInfo.getCellIdentity().getBsic();

                txsys.setText("GSM");
                tx1.setText("MCC/MNC");
                tx2.setText(gsm_MCC + "/" + gsm_MNC);
                tx3.setText("CID");
                tx4.setText(String.valueOf(gsm_CID));
                tx5.setText("LAC");
                tx6.setText(String.valueOf(gsm_LAC));
                tx7.setText("ARFCN");
                tx8.setText(String.valueOf(gsm_Arfcn));
                tx9.setText("BSIC");
                tx10.setText(String.valueOf(gsm_Bsic));
                tx11.setText("DL/UL Freq");
                tx12.setText(getGsmDLfrequency() + "/" + getGsmULfrequency());
                txband.setText("Band (bandwith)");
                txbandval.setText(getGsmDLband().get(0) + " (" + getGsmDLband().get(1) + ")");
                tx21.setVisibility(View.GONE);
                tx22.setVisibility(View.GONE);
                txmodul.setVisibility(View.GONE);
                txmodulval.setVisibility(View.GONE);

            }
            else if (cellInfo instanceof CellInfoWcdma) {
                CellInfoWcdma wcdmaInfo = (CellInfoWcdma) cellInfo;

                wcdma_MCC = wcdmaInfo.getCellIdentity().getMcc();
                wcdma_MNC = wcdmaInfo.getCellIdentity().getMnc();
                wcdma_CID = wcdmaInfo.getCellIdentity().getCid();
                wcdma_LAC = wcdmaInfo.getCellIdentity().getLac();
                wcdma_PSC = wcdmaInfo.getCellIdentity().getPsc();
                wcdma_Uarfcn = wcdmaInfo.getCellIdentity().getUarfcn();


                txsys.setText("WCDMA");
                tx1.setText("MCC/MNC");
                tx2.setText(wcdma_MCC + "/" + wcdma_MNC);
                tx3.setText("CID");
                tx4.setText(String.valueOf(getWcdma_CID()));
                tx5.setText("LAC");
                tx6.setText(String.valueOf(wcdma_LAC));
                tx7.setText("UARFCN");
                tx8.setText(String.valueOf(wcdma_Uarfcn));
                tx9.setText("PSC");
                tx10.setText(String.valueOf(wcdma_PSC));
                tx11.setText("DL/UL Freq");
                tx12.setText(getGsmDLfrequency() + "/" + getGsmULfrequency());
                txband.setText("Band (bandwith)");
                txbandval.setText(getGsmDLband().get(0) + " (" + getGsmDLband().get(1) + ")");
                tx21.setVisibility(View.GONE);
                tx22.setVisibility(View.GONE);
                txmodul.setVisibility(View.GONE);
                txmodulval.setVisibility(View.GONE);
            }
        }
    }


    public int GetEnB() {

        String cellidHex = DecToHex(getLteCI());
        String eNBHex = cellidHex.substring(0, cellidHex.length() - 2);


        return HexToDec(eNBHex);

    }

    public int getSectorId() {
        String cellidHex = DecToHex(getLteCI());
        String eNBHex = cellidHex.substring(0, cellidHex.length() - 2);
        Log.e("SectorID", String.valueOf(getLteCI() - (256 * HexToDec(eNBHex))));

        return getLteCI() - (256 * HexToDec(eNBHex));
    }

    // Decimal -> hexadecimal
    public String DecToHex(int dec) {
        return String.format("%x", dec);
    }

    // hex -> decimal
    public int HexToDec(String hex) {
        return Integer.parseInt(hex, 16);
    }


    public static void get_Reflection_Method(Object r) {
        String TAG = "RadioInfo ";
        Log.d(TAG, "get_Reflection_Method begin!");

        Class temp = r.getClass();
        String className = temp.getName();
        Log.d(TAG, className);
        /*
         getDeclaredMethods() can only get all the methods defined by the current class, not the methods inherited from the parent class
         The getMethods() method can not only obtain the public methods defined by the current class, but also the public methods
         inherited from the parent class and have implemented interfaces
         */
        Method[] methods = temp.getDeclaredMethods();
//        Method[] methods = temp.getMethods();

        for (int i = 0; i < methods.length; i++) {

            int mod = methods[i].getModifiers();
            System.out.print(Modifier.toString(mod) + " ");
            System.out.print(methods[i].getReturnType().getName());
            System.out.print(" " + methods[i].getName() + "(");
            Class[] parameterTypes = methods[i].getParameterTypes();
            for (int j = 0; j < parameterTypes.length; j++) {
                System.out.print(parameterTypes[j].getName());

                if (parameterTypes.length > j + 1) {
                    System.out.print(", ");
                }
            }
            System.out.println(")");
        }


        Log.d(TAG, "get_Reflection_Method end!");
    }


    public int getLteSINR() {
        if (Build.VERSION.SDK_INT > 23) {
            return lte_SINR / 10;
        }
        return lte_SINR;
    }


    public int getwcdm_RSCP() {

        try {
            wcdma_RSCP =  wcdma_ASU - 95 ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wcdma_RSCP;
    }


    public int getWcdma_EcNo() {
        return wcdma_Ecno = wcdma_RSCP - wcdma_RSSI;
    }

    public int getWcdma_CID() {
        return wcdma_CID % 65536;
    }

    public int getLteCI() {
        return lte_CI;
    }

    public Double getGsmULfrequency() {
        double ULfreq = 0;

        if (gsm_Arfcn >= 0 && 124 >= gsm_Arfcn) {

            ULfreq = Float.valueOf(gsm_Arfcn) / 5 + 890;

        } else if (gsm_Arfcn >= 975 && gsm_Arfcn <= 1023) {

            ULfreq = 890 + Float.valueOf((float) (0.2 * (Float.valueOf(gsm_Arfcn) - 1024)));
        }

        BigDecimal bd = new BigDecimal(ULfreq).setScale(2, RoundingMode.HALF_UP);
        ULfreq = bd.doubleValue();
        return ULfreq;
    }

    private Double getGsmDLfrequency() {
        double DLfreq = 0;
        return DLfreq = getGsmULfrequency() + 45;

    }

    public List<String> getGsmDLband() {

        List<String> gsmband_bandwith = new ArrayList<>();
        String gsm_dLband = null;
        String gsm_bandwith = null;

        if (gsm_Arfcn >= 0 && gsm_Arfcn <= 124 || gsm_Arfcn >= 975 && gsm_Arfcn <= 1023) {
            gsm_dLband = "GSM-900";
            gsm_bandwith = "34.6";

        } else if (gsm_Arfcn >= 515 && gsm_Arfcn <= 885) {
            gsm_dLband = "GSM-1800";
            gsm_bandwith = "74.6";
        }
        gsmband_bandwith.add(gsm_dLband);
        gsmband_bandwith.add(gsm_bandwith);


        return gsmband_bandwith;
    }

    public String getLTEmodulation(){
        String modulation =null;

        if (lte_CQI==0){
            modulation ="Out of range";
        }else if (lte_CQI>=1 && lte_CQI <= 3){
            modulation = "QPSK";
        }else if (lte_CQI>=4 && lte_CQI <= 6){
            modulation ="16QAM";
        }else if (lte_CQI>=7 && lte_CQI <= 11){
            modulation ="64QAM";
        }else if (lte_CQI>=12){
            modulation ="256QAM";
        }
        return modulation;
    }

}