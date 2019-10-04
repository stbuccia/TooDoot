package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
    public static String getStringDate(Date date){
        SimpleDateFormat dateFormat;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (year ==  calendar.get(Calendar.YEAR))
            dateFormat = new SimpleDateFormat("E, dd MMM");
        else
            dateFormat = new SimpleDateFormat("E, dd MMM yyyy");

        calendar.set(year, month, day);
        date = calendar.getTime();

        return dateFormat.format(date);

    }
}
