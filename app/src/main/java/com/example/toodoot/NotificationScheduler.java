package com.example.toodoot;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import model.Priority;
import model.Task;
import model.Utils;

import static android.content.Context.ALARM_SERVICE;

public class NotificationScheduler {
    public static final int DAILY_REMINDER_REQUEST_CODE = 100;
    public static NotificationManager notificationManager;


    public static void setReminder(Context context, Class<?> cls) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();


        calendar.add( Calendar.DAY_OF_MONTH, 0 );
        setcalendar.add( Calendar.DAY_OF_MONTH, 0 );


        setcalendar.set(Calendar.HOUR_OF_DAY, Utils.getNotificationHour(preferences));
        setcalendar.set(Calendar.MINUTE, Utils.getNotificationMin(preferences));
        setcalendar.set(Calendar.SECOND, 0);


        calendar.setTimeZone(new GregorianCalendar().getTimeZone());
        setcalendar.setTimeZone(new GregorianCalendar().getTimeZone());


        // cancel already scheduled reminders
        cancelReminder(context,cls);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        intent1.setAction("TASK_ALARM");
        intent1.putExtra("id", DAILY_REMINDER_REQUEST_CODE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if(setcalendar.before(calendar)) {
            setcalendar = calendar;
        }

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    public static void cancelReminder(Context context,Class<?> cls)
    {
        // Disable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        intent1.setAction("TASK_ALARM");
        intent1.putExtra("id", DAILY_REMINDER_REQUEST_CODE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }

    public static void showNotifications(Context context, Class<?> cls){
        ArrayList<Task> tasks = Task.getSavedTasks(context);
        String minPriority = PreferenceManager.getDefaultSharedPreferences(context).getString("notificationPriority", "");

        for (int i = 0; i < tasks.size() ; i++) {
            Task task = tasks.get(i);



            Calendar cal = Calendar.getInstance();
            cal.setTime(task.getDate());
            if (!task.isComplete() && Utils.setStartDay(cal).getTime().before(new Date())) {

                if (minPriority.equals("") || (!task.getPriority().isNull() && task.getPriority().getValue() <= Priority.fromCharToInt(minPriority.charAt(0)))) {
                    List<String> lists = task.getLists();
                    List<String> tags = task.getTags();

                    StringBuilder listString = new StringBuilder();

                    for (int j = 0; j < lists.size(); j ++)
                        listString.append(lists.get(j) + " ");

                    for (int j = 0; j < tags.size(); j ++)
                        listString.append(tags.get(j) + " ");

                    Intent notificationIntent = new Intent(context, cls);
                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    notificationIntent.setAction("OPEN" + i);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);


                    Notification notification = builder.setContentTitle(task.getName())
                            .setPriority(Notification.PRIORITY_MAX)
                            .setShowWhen(true)
                            .setContentText(task.getDescription() + " - " + listString.toString())
                            .setSmallIcon(R.drawable.ic_check_24px)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent)
                            .build();
                    notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(DAILY_REMINDER_REQUEST_CODE + i , notification);

                }
            }
        }
    }
}
