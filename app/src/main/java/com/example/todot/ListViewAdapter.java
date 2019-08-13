package com.example.todot;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {
    private Activity activity;
    private ArrayList<String> Tasks;
    public ListViewAdapter(Activity activity, int resource, ArrayList<String> tasks){
        super(activity, resource, tasks);
        this.activity = activity;
        this.Tasks = tasks;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        // inflate layout from xml
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.lv_item, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }


        String task_name = Tasks.get(position);
        //set Task data to views
        holder.name.setText(task_name);

        //set event for checkbox
        holder.check.setOnCheckedChangeListener(onCheckedChangeListener(task_name));


        return convertView;
    }
    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener(final String s){
            return new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    remove(s);
                }
            };
    }
    private class ViewHolder {
        private TextView name;
        private CheckBox check;
        public ViewHolder(View v) {
            name = (TextView) v.findViewById(R.id.name);
            check = (CheckBox) v.findViewById(R.id.check);
        }
    }

}

