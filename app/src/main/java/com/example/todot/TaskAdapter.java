package com.example.todot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private ArrayList<Task> tasklist;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public CheckBox checkButton;
        private boolean firstTime = true;

        public ViewHolder(View v) {
            super(v);
            /*
            titleView = (TextView) v.findViewById(R.id.taskname);
            titleView.setGravity(Gravity.CENTER_VERTICAL);*/

            checkButton = v.findViewById(R.id.checkBox);
        }
    }

    public TaskAdapter(ArrayList<Task> data) {
        tasklist = data;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Task task = tasklist.get(position);
        holder.checkButton.setText(task.getName());
        holder.checkButton.setChecked(task.isComplete());

        holder.checkButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        task.completeTask();
                    } else {
                        task.uncompleteTask();
                        Toast.makeText(buttonView.getContext(), "unchecked", Toast.LENGTH_LONG).show();
                    }
                    task.updateTaskInFile(buttonView.getContext());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasklist.size();
    }
}
