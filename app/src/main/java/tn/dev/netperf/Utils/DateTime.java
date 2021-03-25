package tn.dev.netperf.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTime {

    public String getTime() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat HourFormat = new SimpleDateFormat("HH:mm:ss");
        HourFormat.setCalendar(calendar);
        return HourFormat.format(calendar.getTime());
    }

    public String getDate() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat.setCalendar(calendar);
        return DateFormat.format(calendar.getTime());
    }

}
