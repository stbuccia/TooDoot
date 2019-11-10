package com.example.toodoot;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import model.Task;
import model.Utils;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements Filterable {
    private ArrayList<Task> tasklist;
    private ArrayList<Task> tasklistFiltered;
    private String charString = "";

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView titleView;
        public TextView priorityView;
        public CheckBox checkButton;
        public TextView taskDate;
        public TextView timeView;
        public LinearLayout linearLayout;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View v) {
            super(v);
            view = v;
            titleView = v.findViewById(R.id.taskname);
            titleView.setGravity(Gravity.CENTER_VERTICAL);
            priorityView = v.findViewById(R.id.priority_textview);
            taskDate = v.findViewById(R.id.date_textview);
            timeView = v.findViewById(R.id.time_textview);
            linearLayout = v.findViewById(R.id.task_info);
            checkButton = v.findViewById(R.id.checkBox);

            RippleDrawable rippleDrawable = (RippleDrawable)checkButton.getBackground();
            int[][] states = new int[][] { new int[] { android.R.attr.state_enabled} };
            int[] colors = new int[] { v.getResources().getColor(R.color.design_default_color_secondary) };

            ColorStateList colorStateList = new ColorStateList(states, colors);
            rippleDrawable.setColor(colorStateList);

            constraintLayout = v.findViewById(R.id.constraint);
        }
    }

    public TaskAdapter(ArrayList<Task> data) {
        tasklist = sortTask(data);
        tasklistFiltered = sortTask(data);
    }


    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.task, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    private void clearViews(ViewHolder holder){
        costumTextView(holder.priorityView, null, holder.view.getResources().getColor(R.color.design_default_color_on_primary), Color.TRANSPARENT);
        holder.priorityView.setPadding(0, 0,0, 0);
        holder.titleView.setPadding(0, 0, 0, 0);
        holder.taskDate.setText("");
        holder.titleView.setText("");
        holder.timeView.setText("");
        holder.linearLayout.removeAllViews();

    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Task task = tasklistFiltered.get(position);

        clearViews(holder);
        holder.titleView.setText(tasklistFiltered.get(position).getName());

        DateFormat dateFormat = new SimpleDateFormat("dd MMM");
        holder.taskDate.setText(dateFormat.format(task.getDate()));
        holder.taskDate.setTextColor(holder.taskDate.getResources().getColor(R.color.calColor));



        if (!task.getPriority().isNull()) {
            costumTextView(holder.priorityView, task.getPriority().getCharValue() + "", holder.view.getResources().getColor(R.color.design_default_color_on_primary), holder.view.getResources().getColor(Utils.getPriorityResColor(task.getPriority().getValue())));
            holder.priorityView.setPadding(8, -1 ,8, -1);
            holder.titleView.setPadding(8, 0, 0, 0);
        }

        if(task.getTime() != null){
            holder.timeView.setText(Utils.timeFormat().format(task.getTime()));
            holder.timeView.setTextColor(holder.view.getResources().getColor(R.color.timeColor));
        }

        int i;
        for (i = 0; i < task.getLists().size(); i++){
            holder.linearLayout.addView(createTextView(holder.linearLayout, task.getLists().get(i), holder.view.getResources().getColor(R.color.listColor)));
        }
        for (i = 0; i < task.getTags().size(); i++){
            holder.linearLayout.addView(createTextView(holder.linearLayout, task.getTags().get(i), holder.view.getResources().getColor(R.color.tagColor)));
        }

        holder.checkButton.setChecked(task.isComplete());
        holder.checkButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                checkTask(buttonView.getContext(), tasklistFiltered.indexOf(task), isChecked);

            }

        });

        holder.constraintLayout.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, EditTaskActivity.class);
            intent.putExtra("TASK_CLICKED", task);
            intent.putExtra("TASK_POS", position);
            context.startActivity(intent);
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


    public void checkTask(Context context, int position, boolean isChecked){
        Task task = tasklistFiltered.get(position);
        if (isChecked) {
            task.completeTask();
        } else {
            task.uncompleteTask();
        }
        task.updateTaskInFile(context);
        sortTask(tasklistFiltered);
        notifyItemMoved(position, tasklistFiltered.indexOf(task));

    }


    public void insertItem(Task task, String txt){
        tasklist.add(task);
        getFilter().filter(txt);
        sortTask(tasklist);
        sortTask(tasklistFiltered);
        if (tasklistFiltered.indexOf(task) >= 0)
            notifyItemInserted(tasklistFiltered.indexOf(task));
    }

    public void changeDate(Context context, int position, Date date, boolean toRemove){
        Task task = tasklistFiltered.get(position);
        task.setDueDate(date);
        task.updateTaskInFile(context);
        if (!toRemove) {
            sortTask(tasklistFiltered);
            notifyItemMoved(position, tasklistFiltered.indexOf(task));
            notifyItemChanged(tasklistFiltered.indexOf(task));
        }
        else{
            tasklist.remove(task);
            notifyItemRemoved(tasklistFiltered.indexOf(task));
            tasklistFiltered.remove(task);
        }
    }
    public void changeTime(Context context, int position, Date time) {
        Task task = tasklistFiltered.get(position);
        task.setTime(time);
        task.updateTaskInFile(context);
        sortTask(tasklistFiltered);
        notifyItemMoved(position, tasklistFiltered.indexOf(task));
        notifyItemChanged(tasklistFiltered.indexOf(task));
    }


        private void costumTextView(TextView textView, String txt, int textColor, int colorBackground){
        textView.setText(txt);
        textView.setTextSize(12);
        textView.setTextColor(textColor);
        textView.setBackgroundResource(R.drawable.tags_rounded_corners);
        GradientDrawable drawable = (GradientDrawable) textView.getBackground();
        drawable.setColor(colorBackground);
        textView.setSingleLine(true);


    }

    private TextView createTextView(View view, String txt, int color){
        TextView textView = new TextView(view.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,4,0);
        textView.setLayoutParams(params);
        costumTextView(textView, txt, view.getResources().getColor(R.color.design_default_color_on_primary), color );

        return textView;
    }


    class SortCheck implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            return (t1.isComplete()  ? 1 : 0) - (t2.isComplete()  ? 1 : 0);
        }
    }
    class SortPriority implements Comparator<Task>{
        public int compare(Task t1, Task t2) {
            char char1 = 'Z' + 1;
            char char2  = 'Z' + 1;
            if (!t1.getPriority().isNull())
                char1 = t1.getPriority().getCharValue();
            if (!t2.getPriority().isNull())
                char2 = t2.getPriority().getCharValue();

            return (char1 + " ").compareTo(char2 + " ");
        }
    }
    class SortTime implements Comparator<Task> {
        public int compare(Task t1, Task t2) {
            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();

            date1.setTime(t1.getDate());
            date2.setTime(t2.getDate());

            date1 = Utils.setEndDay(date1);
            date2 = Utils.setEndDay(date2);

            if (t1.getTime() != null)
                date1 = Utils.setTime(date1, t1.getTime());

            if (t2.getTime() != null)
                date2 = Utils.setTime(date2, t2.getTime());

            return date1.compareTo(date2);

        }
    }
    private ArrayList<Task> sortTask(ArrayList<Task> tasks){
        Collections.sort(tasks, new SortPriority());
        Collections.sort(tasks, new SortTime());
        Collections.sort(tasks, new SortCheck());
        return tasks;
    }
}