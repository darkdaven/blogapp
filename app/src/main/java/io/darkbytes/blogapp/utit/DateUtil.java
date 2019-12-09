package io.darkbytes.blogapp.utit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy, hh:mm:ss a");

    public static String format(Long longDate) {

        if(longDate == null)
            return "-- --- --, --:--:-- --";

        Date date = new Date(longDate);
        return simpleDateFormat.format(date);
    }
}
