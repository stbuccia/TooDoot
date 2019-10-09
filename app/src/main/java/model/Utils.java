package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Utils {
    public static String getStringDate(Date date) {
        SimpleDateFormat dateFormat;
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (year == calendar.get(Calendar.YEAR))
            dateFormat = new SimpleDateFormat("E, dd MMM");
        else
            dateFormat = new SimpleDateFormat("E, dd MMM yyyy");

        calendar.set(year, month, day);
        date = calendar.getTime();

        return dateFormat.format(date);

    }

    public static Calendar setStartDay(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        return date;
    }

    public static Calendar setEndDay(Calendar endDate) {
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        endDate.set(Calendar.MILLISECOND, 999);

        return endDate;
    }

    public static Calendar setStartWeek(Calendar startDate) {
        startDate = setStartDay(startDate);
        startDate.set(Calendar.DAY_OF_WEEK, startDate.getFirstDayOfWeek());
        return startDate;
    }

    public static Calendar setEndWeek(Calendar endDate) {
        endDate = setEndDay(endDate);
        endDate.set(Calendar.DAY_OF_WEEK, setStartWeek(endDate).getFirstDayOfWeek() + 6);
        return endDate;
    }

    public static Calendar setStartMonth(Calendar startDate){
        startDate = setStartDay(startDate);
        startDate.set(Calendar.DAY_OF_MONTH, 1);
        return startDate;
    }

    public static Calendar setEndMonth(Calendar endDate){
        endDate = setEndDay(endDate);
        endDate.set(Calendar.DAY_OF_MONTH, endDate.getActualMaximum(Calendar.DAY_OF_MONTH));
        return endDate;
    }

    public static Calendar initCalendar(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return cal;
    }
}
