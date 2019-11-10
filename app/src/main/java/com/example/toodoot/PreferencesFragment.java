package com.example.toodoot;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;
import java.util.Calendar;

import model.Priority;
import model.Utils;


public class PreferencesFragment extends PreferenceFragmentCompat {

    private Context context;
    SharedPreferences prefs;
    public PreferencesFragment(Context c){
        context = c;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public void moveFile (String filePath, String newDir){
        try{

            File file =new File(filePath);
            file.renameTo(new File(newDir + file.getName()));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void changeDir(String path){

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("dir", path);
        editor.commit();

    }

    private String getPrioritySummary(){
        if ((prefs.getString("notificationPriority", "")).equals(""))
            return ("Notifications for every task");
        else
            return ("Notifications for tasks with priority greater or equal to " + prefs.getString("notificationPriority", ""));

    }

    private void loadTodoTxt(String path, String parentDir){

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("filename", path);
        editor.commit();

        changeDir(parentDir);
    }
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        setPreferencesFromResource(R.xml.fragment_preference, rootKey);

        ChooserDialog chooserDialog =  new ChooserDialog(context)
                .withFilter(false, false)
                .withDateFormat("HH:mm")
                .displayPath(true)
                .withFileIconsRes(false, R.drawable.ic_insert_drive_file_24px, R.drawable.ic_folder_24px)

                .withOnCancelListener(dialog -> {
                    Log.d("CANCEL", "CANCEL");
                    dialog.cancel();
                });

        Preference filePicker = (Preference) findPreference("todotxtPicker");
        filePicker.setSummary(Utils.getFilePath(prefs));

        Preference dirPicker = (Preference) findPreference("dirPicker");
        dirPicker.setSummary(Utils.getDirPath(prefs));

        filePicker.setOnPreferenceClickListener(preference -> {

            chooserDialog
                    .withStartFile(Utils.getDirPath(prefs))
                    .withFilter(false, false)
                    .withChosenListener((path, pathFile) -> {
                        PreferencesFragment.this.loadTodoTxt(pathFile.getName(), pathFile.getParent());

                        filePicker.setSummary(Utils.getFilePath(prefs));
                        dirPicker.setSummary(Utils.getDirPath(prefs));
                    })
                    .build().show();
            return true;
        });

        dirPicker.setOnPreferenceClickListener(preference -> {
            chooserDialog
                    .withStartFile(Utils.getDirPath(prefs))
                    .withFilter(true, false)
                    .withChosenListener((path, pathFile) -> {
                        PreferencesFragment.this.changeDir(path);
                        moveFile(Utils.getFilePath(prefs), path);

                        filePicker.setSummary(Utils.getFilePath(prefs));
                        dirPicker.setSummary(Utils.getDirPath(prefs));
                    })
                    .build()
                    .show();
            return true;
        });

        Preference priorityPicker = findPreference("priorityPicker");
        priorityPicker.setSummary(getPrioritySummary());
        priorityPicker.setOnPreferenceClickListener(preference -> {
            PriorityDialog dialog =
                    new PriorityDialog(context) {
                        @Override
                        public void onPrioritySet(int val) {
                            SharedPreferences.Editor editor = prefs.edit();
                            if (val > 1) {
                                Priority p = new Priority(val - 1);
                                editor.putString("notificationPriority", p.getCharValue() + "");
                                editor.commit();
                            } else {
                                editor.putString("notificationPriority", "");
                                editor.commit();

                            }
                            priorityPicker.setSummary(getPrioritySummary());
                        }
                    };
            dialog.showPriorityDialog();
            return true;
        });

        Preference setTime = findPreference("notificationTime");
        setTimeSummary(setTime);

        setTime.setOnPreferenceClickListener(preference -> {

            showTimePickerDialog(Utils.getNotificationHour(prefs),Utils.getNotificationMin(prefs), setTime);
            return true;
        });

    }


    private void showTimePickerDialog(int h, int m, Preference p) {
        TimePickerDialog builder = new TimePickerDialog(context, R.style.TimePickerTheme,
            (timePicker, hour, min) -> {


                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("notificationHour", hour);
                editor.putInt("notificationMin", min);
                editor.commit();
                NotificationScheduler.setReminder(context, AlarmReceiver.class);
                setTimeSummary(p);

            }, h, m, true);

        builder.show();

    }

    private void setTimeSummary(Preference p){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, Utils.getNotificationHour(prefs));
        cal.set(Calendar.MINUTE, Utils.getNotificationMin(prefs));

        p.setSummary(Utils.timeFormat().format(cal.getTime()));
    }
}
