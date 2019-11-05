package com.example.todot;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.obsez.android.lib.filechooser.ChooserDialog;

import java.io.File;

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
            return ("Notifications for tasks with priority greater then " + prefs.getString("notificationPriority", ""));

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
            new PriorityDialog(context){
                @Override
                public void onPrioritySet(int val) {
                    SharedPreferences.Editor editor = prefs.edit();
                    if (val > 1) {
                        Priority p = new Priority(val - 1);
                        editor.putString("notificationPriority", p.getCharValue() + "");
                        editor.commit();
                    }
                    else{
                        editor.putString("notificationPriority", "");
                    }
                    priorityPicker.setSummary(getPrioritySummary());
                }
            }.showPriorityDialog();
            return true;
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);

    }
}
