package com.example.todot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import model.Task;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable {
    private ArrayList<Task> tasklist;
    private ArrayList<Task> tasklistFiltered;
    private Task mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public CheckBox checkButton;

        public ViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.taskname);
            titleView.setGravity(Gravity.CENTER_VERTICAL);

            checkButton = v.findViewById(R.id.checkBox);
        }
    }

    public TaskAdapter(ArrayList<Task> data) {
        tasklist = data;
        tasklistFiltered = tasklist;
    }

    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Task task = tasklistFiltered.get(position);
        holder.titleView.setText(tasklistFiltered.get(position).getName());
        holder.checkButton.setChecked(task.isComplete());

        holder.checkButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    if (isChecked) {
                        task.completeTask();
                    } else {
                        task.uncompleteTask();
                    }
                    task.updateTaskInFile(buttonView.getContext());
                }
            }
        });

        holder.titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, EditTaskActivity.class);
                intent.putExtra("TASK_CLICKED", task);
                intent.putExtra("TASK_POS", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasklistFiltered.size();
    }


    @Override
    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {


                String charString = charSequence.toString();
                if (charString.isEmpty())
                    tasklistFiltered = tasklist;
                else {
                    ArrayList<Task> filteredList = new ArrayList<>();
                    for (Task row : tasklist){
                        if (row.getTextdef().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(row);
                        }
                    }
                    tasklistFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = tasklistFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                tasklistFiltered = (ArrayList<Task>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void deleteItem(Activity activity, int position) {
        mRecentlyDeletedItem = tasklistFiltered.get(position);
        mRecentlyDeletedItemPosition = position;
        tasklistFiltered.remove(position);
        mRecentlyDeletedItem.removeTaskInFile(activity);
        notifyItemRemoved(position);
        showUndoSnackbar(activity);
    }

    public void insertItem(Context context, Task task){
        tasklist.add(0, task);
        task.addTaskInFile(context);
        notifyItemInserted(0);
    }

    private void showUndoSnackbar(Activity activity) {
        View view = activity.findViewById(R.id.container);
        Snackbar snackbar = (Snackbar) Snackbar.make(view, "Deleted Item", Snackbar.LENGTH_LONG)
                .setAction(R.string.snack_bar_undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        undoDelete();
                    }
                });
        snackbar.show();
    }

    private void undoDelete() {
        tasklistFiltered.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        tasklist.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }
}
