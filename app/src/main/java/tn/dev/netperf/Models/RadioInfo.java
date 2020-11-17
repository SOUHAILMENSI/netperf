package tn.dev.netperf.Models;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import tn.dev.netperf.Activities.StatsActivity;
import tn.dev.netperf.Activities.SummActivity;
import tn.dev.netperf.R;

public class RadioInfo {

    private static final int PERMISSION_REQUEST_CODE = 100;
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


    private Context mcontext;
    private TelephonyManager mTM;
    private PhoneStateMonitor mPSM;

    private final String Tag = RadioInfo.class.getName();


    public RadioInfo(Context context) {
        mcontext = context;

        mTM = (TelephonyManager) mcontext.getSystemService(Context.TELEPHONY_SERVICE);

        mPSM = new PhoneStateMonitor();

        mTM.listen(mPSM, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS | PhoneStateListener.LISTEN_CELL_INFO | PhoneStateListener.LISTEN_CELL_LOCATION);
    }


    public void RdioInfo_Exit() {
        mTM.listen(mPSM, PhoneStateListener.LISTEN_NONE);
    }

    public int getLte_MCC() {
        return lte_MCC;
    }

    public int getLte_MNC() {
        return lte_MNC;
    }

    public int getLteCI() {
        return lte_CI;
    }

    public int getLtePCI() {
        return lte_PCI;
    }

    public int getLteTAC() {
        return lte_TAC;
    }

    public int getLteRSRP() {
        return lte_dbm;
    }

    public int getLteRSRQ() {
        return lte_RSRQ;
    }

    public int getLteSINR() {
        if (Build.VERSION.SDK_INT > 23) {
            return lte_SINR / 10;
        }
        return lte_SINR;
    }

    public int getLteCQI() {
        return lte_CQI;
    }

    public int getLteEarfcn() {
        return lte_Earfcn;
    }

    public int getGsm_MCC() {
        return gsm_MCC;
    }

    public int getGsm_MNC() {
        return gsm_MNC;
    }

    public int getGsm_LAC() {
        return gsm_LAC;
    }

    public int getGsm_Arfcn() {
        return gsm_Arfcn;
    }

    public int getgsm_Bsic() {
        return gsm_Bsic;
    }

    public int getgsm_Berror() {
        return gsm_Berror;
    }

    public int getgsm_Asu() {
        return gsm_Asu;
    }

    public int getgsm_rxlev() {
        return gsm_rxlev;
    }


    public int getGsm_CID() {
        return gsm_CID;
    }

    public int getGsm_RSSI() {
        return gsm_RSSI;
    }

    public int getWcdma_MCC() {
        return wcdma_MCC;
    }

    public int getWcdma_MNC() {
        return wcdma_MNC;
    }

    public int getWcdma_LAC() {
        return wcdma_LAC;
    }

    public int getWcdma_Uarfcn() {
        return wcdma_Uarfcn;
    }

    public int getWcdma_CID() {
        return wcdma_CID % 65536;
    }

    public int getWcdma_PSC() {
        return wcdma_PSC;
    }

    public int getWcdma_RSSI() {
        return wcdma_RSSI;
    }

    public int getwcdm_RSCP() {

        try {
            wcdma_RSCP = wcdma_ASU - 115;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wcdma_RSCP;
    }

    public int getWcdma_ASU() {
        return wcdma_ASU;
    }

    public int getWcdma_EcNo() {
        return wcdma_Ecno = wcdma_RSCP - wcdma_RSSI;
    }


    private class PhoneStateMonitor extends PhoneStateListener {

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

              ((SummActivity) mcontext).mSectionsPagerAdapter.notifyDataSetChanged();

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
                    wcdma_CID = wcdmaInfo.getCellIdentity().getCid();
                    wcdma_LAC = wcdmaInfo.getCellIdentity().getLac();
                    wcdma_PSC = wcdmaInfo.getCellIdentity().getPsc();
                    wcdma_Uarfcn = wcdmaInfo.getCellIdentity().getUarfcn();
                }
            }

           ((SummActivity) mcontext).mSectionsPagerAdapter.notifyDataSetChanged();

        }

    }


    private void getCellSignalStrength() {
        @SuppressLint("MissingPermission") List<CellInfo> cellInfoList = mTM.getAllCellInfo();
        if (cellInfoList == null) {
            Log.e(Tag, "getAllCellInfo is null");
            return;
        }
        Log.e(Tag, "getAllCellInfo: " + cellInfoList.size() + "\n" + cellInfoList);
        for (CellInfo cellInfo : cellInfoList) {
            if (!cellInfo.isRegistered())    // Primary cell
                continue;
            if (cellInfo instanceof CellInfoWcdma) {
                CellInfoWcdma wcdmaInfo = (CellInfoWcdma) cellInfo;
                wcdma_RSSI = wcdmaInfo.getCellSignalStrength().getDbm();
                wcdma_ASU = wcdmaInfo.getCellSignalStrength().getAsuLevel();
                // NeighboringCellInfo neighboringCellInfo = new NeighboringCellInfo();
                // wcdm_RSCP = neighboringCellInfo.getNetworkType();
            } else if (cellInfo instanceof CellInfoGsm) {
                CellInfoGsm gsmInfo = (CellInfoGsm) cellInfo;
                gsm_Asu = gsmInfo.getCellSignalStrength().getAsuLevel();
            } else if (cellInfo instanceof CellInfoLte) {
                CellInfoLte lteInfo = (CellInfoLte) cellInfo;
                Lte_Asu = lteInfo.getCellSignalStrength().getAsuLevel();
                lte_dbm = lteInfo.getCellSignalStrength().getDbm();
                Log.e("LTEASU/DBM", "" + Lte_Asu + "\n" + lte_dbm);

            }
        }
    }


    @TargetApi(Build.VERSION_CODES.N)
    private void getCellIdentity() {
        @SuppressLint("MissingPermission") List<CellInfo> cellInfoList = mTM.getAllCellInfo();

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
                GetEnB();
                getSectorId();

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
                wcdma_CID = wcdmaInfo.getCellIdentity().getCid();
                wcdma_LAC = wcdmaInfo.getCellIdentity().getLac();
                wcdma_PSC = wcdmaInfo.getCellIdentity().getPsc();
                wcdma_Uarfcn = wcdmaInfo.getCellIdentity().getUarfcn();
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
}
