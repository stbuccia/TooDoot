package com.example.todot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Priority;
import model.Task;

public class AlarmReceiver extends BroadcastReceiver {


    String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction() != null && context != null) {
            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                NotificationScheduler.setReminder(context, AlarmReceiver.class);
                return;
            }
        }

        ArrayList<Task> tasks = Task.getSavedTasks(context);
        String minPriority = PreferenceManager.getDefaultSharedPreferences(context).getString("notificationPriority", "");

        for (int i = 0; i < tasks.size() ; i++) {
            Task task = tasks.get(i);


            if (!task.isComplete() && task.getDate().before(new Date())) {

                if (minPriority.equals("") || (!task.getPriority().isNull() && task.getPriority().getValue() <= Priority.fromCharToInt(minPriority.charAt(0)))) {
                    List<String> lists = task.getLists();
                    List<String> tags = task.getTags();

                    StringBuilder listString = new StringBuilder();

                    for (int j = 0; j < lists.size(); j ++)
                        listString.append(lists.get(j) + " ");

                    for (int j = 0; j < tags.size(); j ++)
                        listString.append(tags.get(j) + " ");

                    NotificationScheduler.showNotifications(context, MainActivity.class, task.getName(), task.getDescription() + " " + listString.toString(), i, task);
                }
            }
        }


    }


}