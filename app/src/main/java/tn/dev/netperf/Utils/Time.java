package tn.dev.netperf.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Time {

    public String getTime() {
        Calendar calendar = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        dateFormat.setCalendar(calendar);
        return dateFormat.format(calendar.getTime());
    }

}
