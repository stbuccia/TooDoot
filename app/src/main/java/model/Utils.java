package model;

import android.content.SharedPreferences;
import android.os.Environment;

import com.example.todot.R;

import java.text.DateFormat;
import java.text.Format;
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

    public static boolean match(String s, String tocompare){
        s = s.toLowerCase();
        tocompare = tocompare.toLowerCase();
        String [] words = s.split(" ");
        String [] list = tocompare.split(" ");

        boolean exist = true;

        for (int i = 0; (i < words.length && exist); i++){
            exist = false;
            for (int j = 0; (j < list.length && !exist); j++){
                if (list[j].contains(words[i])){
                    exist = true;
                    list[j] = "";
                }
            }
        }
        return exist;

    }

    public static DateFormat timeFormat(){
        return new SimpleDateFormat("HH:mm");
    }

    public static int getPriorityResColor(int val){
        if (val > 6)
            return R.color.lowPriorityColor;
        else if (val > 3)
            return R.color.mediumPriorityColor;
        else
            return R.color.highPriorityColor;
    }

    public static Calendar setTime(Calendar calDate, Date time){

        Calendar calTime = Calendar.getInstance();
        calTime.setTime(time);
        int hour = calTime.get(Calendar.HOUR_OF_DAY);
        int min = calTime.get(Calendar.MINUTE);

        calDate = setStartDay(calDate);
        calDate.set(Calendar.HOUR_OF_DAY, hour);
        calDate.set(Calendar.MINUTE, min);
        return calDate;
    }

    public static boolean isSameDay(Date date1, Date date2){
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");


        String date1Str = ((SimpleDateFormat) formatter).format(date1);
        String date2Str = ((SimpleDateFormat) formatter).format(date2);

        return  date1Str.equals(date2Str);

    }

    public static String getDirPath(SharedPreferences preferences){
        return preferences.getString("dir", Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public static String getFilename(SharedPreferences preferences){
        return preferences.getString("filename", "todo.txt");
    }

    public static String getFilePath(SharedPreferences preferences){
        return getDirPath(preferences) + "/" + getFilename(preferences);
    }

    public static int getNotificationHour(SharedPreferences preferences){
        return preferences.getInt("notificationHour", 7);
    }

    public static int getNotificationMin(SharedPreferences preferences){
        return preferences.getInt("notificationMin", 0);
    }
}
