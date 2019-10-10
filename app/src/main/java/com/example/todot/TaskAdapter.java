package com.example.todot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.Priority;
import model.Task;
import model.Utils;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable {
    private ArrayList<Task> tasklist;
    private ArrayList<Task> tasklistFiltered;
    private Task mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;
    private String charString = "";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public CheckBox checkButton;
        public TextView taskDate;
        public LinearLayout linearLayout;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View v) {
            super(v);
            titleView = (TextView) v.findViewById(R.id.taskname);
            titleView.setGravity(Gravity.CENTER_VERTICAL);
            taskDate = (TextView) v.findViewById(R.id.date_textview);
            linearLayout = (LinearLayout) v.findViewById(R.id.task_info);
            checkButton = v.findViewById(R.id.checkBox);
            constraintLayout = v.findViewById(R.id.constraint);
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

        DateFormat dateFormat = new SimpleDateFormat("dd MMM");
        holder.taskDate.setText(dateFormat.format(task.getDate()));
        holder.taskDate.setTextColor(holder.taskDate.getResources().getColor(R.color.calColor));

        holder.linearLayout.removeAllViews();
        int i;
        if (!task.getPriority().isNull())
            holder.linearLayout.addView(createTextView(holder.linearLayout, task.getPriority().getCharValue() + "", holder.linearLayout.getResources().getColor(R.color.priorityColor)));
        for (i = 0; i < task.getLists().size(); i++){
            holder.linearLayout.addView(createTextView(holder.linearLayout, task.getLists().get(i), holder.linearLayout.getResources().getColor(R.color.listColor)));
        }
        for (i = 0; i < task.getTags().size(); i++){
            holder.linearLayout.addView(createTextView(holder.linearLayout, task.getTags().get(i), holder.linearLayout.getResources().getColor(R.color.tagColor)));
        }
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

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
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


                charString = charSequence.toString();
                if (charString.isEmpty())
                    tasklistFiltered = tasklist;
                else {
                    ArrayList<Task> filteredList = new ArrayList<>();
                    for (Task row : tasklist){
                        if (Utils.match(charString, row.getTextdef())){
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
        tasklist.add(task);
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

    public String getCharString(){
        return charString;
    }

    public void setTasklistFiltered(ArrayList<Task> list){
        tasklistFiltered = list;
        notifyDataSetChanged();
    }

    private TextView createTextView(View view, String txt, int color){
        TextView textView = new TextView(view.getContext());
        textView.setText(txt);
        textView.setTextSize(12);
        textView.setTextColor(view.getResources().getColor(R.color.design_default_color_on_primary));
        //textView.setPadding(8, 4, 8, 4);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,4,0);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.tags_rounded_corners);
        GradientDrawable drawable = (GradientDrawable) textView.getBackground();
        drawable.setColor(color);
        textView.setSingleLine(true);


        return textView;
    }

}
