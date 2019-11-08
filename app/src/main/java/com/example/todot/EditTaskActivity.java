package com.example.todot;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import model.Priority;
import model.Task;
import model.Utils;

import static com.example.todot.R.drawable.ic_arrow_back_24px;
import static model.Task.getAllLists;
import static model.Task.getAllTags;

public class EditTaskActivity extends AppCompatActivity {

    Task task;
    int position;
    EditTaskActivity activity = this;
    CalendarDialog calendarDialog;
    PriorityDialog priorityDialog;
    ListDialog listDialog;
    ListDialog tagDialog;
    TimeDialog timeDialog;
    EditText name;
    EditText description;
    CheckBox checkBox;
    MaterialButton priorityIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);



        task = (Task) getIntent().getSerializableExtra("TASK_CLICKED");
        position = (int) getIntent().getSerializableExtra("TASK_POS");

        name = findViewById(R.id.task_name);
        description = findViewById(R.id.task_description);
        checkBox = findViewById(R.id.checkBox);
        priorityIcon = findViewById(R.id.choose_priority_button);

        name.setText(task.getName());
        description.setText(task.getDescription());
        /*if (task.getTime() != null)
            description.setText(Utils.timeFormat().format(task.getTime()));*/
        checkBox.setChecked(task.isComplete());

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Drawable upArrow = getResources().getDrawable(ic_arrow_back_24px);
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        setPriorityBtn();

        setDateBtn();
        setTimeBtn();

        setListBtn();
        setTagBtn();

        priorityDialog.onPrioritySet(task.getPriority().getValue() + 1);
        if (task.getdue_date() != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(task.getdue_date());
            calendarDialog.onDateSet(null, calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        }

        if (task.getTime() != null) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(task.getTime());
            timeDialog.onTimeSet(null, calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE));

        }
        listDialog.setLists(task.getLists());
        listDialog.onListSet();

        tagDialog.setLists(task.getTags());
        tagDialog.onListSet();

    }

    @Override
    public void onBackPressed() {

        task.setName(name.getText().toString());
        task.setDescription(description.getText().toString());

        if (checkBox.isChecked())
            task.completeTask();
        else
            task.uncompleteTask();
        if (activity.findViewById(R.id.task_date_chipgroup) != null) {
            task.setDueDate(calendarDialog.getDate());
        } else
            task.setDueDate(null);

        if (activity.findViewById(R.id.task_time_chipgroup) != null)
            task.setTime(timeDialog.getTime());
        else
            task.setTime(null);


        if (activity.findViewById(R.id.task_priority_chipgroup) != null) {
            task.setPriority(priorityDialog.getPriority());
        } else
            task.setPriority('0');

        if (activity.findViewById(R.id.task_lists_chipgroup) != null){
            task.setLists(listDialog.getLists());
        }
        else
            task.setLists(Collections.<String>emptyList());

        if (activity.findViewById(R.id.task_tags_chipgroup) != null){
            task.setTags(tagDialog.getLists());
        }
        else
            task.setTags(Collections.<String>emptyList());

        task.updateTaskInFile(this);
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_icon_button, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.design_default_color_on_primary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }

    private void changeView(View from, View to){

        ViewGroup parent = (ViewGroup) from.getParent();
        int index = parent.indexOfChild(from);

        parent.removeView(from);
        parent.addView(to, index);

    }


    public void setDateBtn(){
        changeView(findViewById(R.id.date), getLayoutInflater().inflate(R.layout.add_date_button, (ViewGroup)findViewById(R.id.date).getParent(), false) );
        calendarDialog = new CalendarDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_date) {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                super.onDateSet(datePicker, year, month, day);
                chip = setDateChipGroup(chip);

            }
        };
    }

    public Chip setDateChipGroup(final Chip chip){

        if (activity.findViewById(R.id.task_date_chipgroup) == null) {
            changeView(activity.findViewById(R.id.date), getLayoutInflater().inflate(R.layout.date_chipgroup, (ViewGroup) activity.findViewById(R.id.date).getParent(), false));
            ((ChipGroup) activity.findViewById(R.id.task_date_chipgroup)).addView(chip);
        }
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChipGroup) activity.findViewById(R.id.task_date_chipgroup)).removeView(chip);
                setDateBtn();
            }
        });
        chip.setChipBackgroundColorResource(R.color.calColor);
        return chip;
    }

    public void setTimeBtn(){
        changeView(findViewById(R.id.time), getLayoutInflater().inflate(R.layout.add_time_button, (ViewGroup)findViewById(R.id.time).getParent(), false) );
        timeDialog = new TimeDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_time){
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min){
                super.onTimeSet(timePicker, hour, min);
                chip = setTimeChipGroup(chip);
            }
        };

    }

    public Chip setTimeChipGroup(final Chip chip) {
        if (activity.findViewById(R.id.task_time_chipgroup) == null) {
            changeView(activity.findViewById(R.id.time), getLayoutInflater().inflate(R.layout.time_chipgroup, (ViewGroup) activity.findViewById(R.id.time).getParent(), false));
            ((ChipGroup) activity.findViewById(R.id.task_time_chipgroup)).addView(chip);
        }
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChipGroup) activity.findViewById(R.id.task_time_chipgroup)).removeView(chip);
                setTimeBtn();
            }
        });
        chip.setChipBackgroundColorResource(R.color.timeColor);
        return chip;

    }

    public void setPriorityBtn(){
        priorityIcon.setIconTintResource(Utils.getPriorityResColor(0));
        changeView(findViewById(R.id.priority), getLayoutInflater().inflate(R.layout.add_priority_button, (ViewGroup)findViewById(R.id.priority).getParent(), false) );
        priorityDialog = new PriorityDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_priority) {
            @Override
            public void onPrioritySet(int val) {
                super.onPrioritySet(val);
                if (priority != '0') {
                    chip.setChipBackgroundColorResource(Utils.getPriorityResColor(Priority.fromCharToInt(priority)));
                    chip = setPriorityChipGroup(chip);
                    priorityIcon.setIconTintResource(Utils.getPriorityResColor(Priority.fromCharToInt(priority)));
                }
                else setPriorityBtn();
            }
        };
    }

    public Chip setPriorityChipGroup(final Chip chip){
        if (activity.findViewById(R.id.task_priority_chipgroup) == null) {
            changeView(activity.findViewById(R.id.priority), getLayoutInflater().inflate(R.layout.priority_chipgroup, (ViewGroup) activity.findViewById(R.id.priority).getParent(), false));
            ((ChipGroup) activity.findViewById(R.id.task_priority_chipgroup)).addView(chip);
        }
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ChipGroup) activity.findViewById(R.id.task_priority_chipgroup)).removeView(chip);
                setPriorityBtn();
            }
        });

        return chip;

    }

    public void setListBtn(){
        changeView(findViewById(R.id.list), getLayoutInflater().inflate(R.layout.add_list_button, (ViewGroup)findViewById(R.id.list).getParent(), false) );

        listDialog = new ListDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_lists, getAllLists(), "Set Lists", 500, R.color.listColor){
            @Override
            public void onListSet(){
                super.onListSet();
                setListChipGroup(getChips(), getLists().size());
            }
        };
    }

    public Chip[] setListChipGroup(final Chip[] chips, int size){
        if (size == 0) setListBtn();
        else {
            if (activity.findViewById(R.id.task_lists_chipgroup) == null) {
                changeView(activity.findViewById(R.id.list), getLayoutInflater().inflate(R.layout.list_chipgroup, (ViewGroup) activity.findViewById(R.id.list).getParent(), false));
            } else ((ChipGroup) activity.findViewById(R.id.task_lists_chipgroup)).removeAllViews();

            for (int i = 0; i < size; i++) {
                ((ChipGroup) activity.findViewById(R.id.task_lists_chipgroup)).addView(chips[i]);
                final int finalI = i;
                chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ChipGroup) activity.findViewById(R.id.task_lists_chipgroup)).removeView(chips[finalI]);
                        listDialog.removeList(finalI);
                        if (listDialog.getLists().size() == 0)
                            setListBtn();
                    }
                });
                chips[i].setChipBackgroundColorResource(R.color.listColor);

            }
        }
        return chips;

    }

    public void setTagBtn(){
        changeView(findViewById(R.id.tag), getLayoutInflater().inflate(R.layout.add_tag_button, (ViewGroup)findViewById(R.id.tag).getParent(), false) );

        tagDialog = new ListDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_tags, getAllTags(), "Set Tags", 0, R.color.tagColor) {
            @Override
            public void onListSet(){
                super.onListSet();
                setTagChipGroup(getChips(), getLists().size());
            }
        };
    }

    public Chip[] setTagChipGroup(final Chip[] chips, int size){
        if (size == 0) setTagBtn();
        else {
            if (activity.findViewById(R.id.task_tags_chipgroup) == null) {
                changeView(activity.findViewById(R.id.tag), getLayoutInflater().inflate(R.layout.tag_chipgroup, (ViewGroup) activity.findViewById(R.id.tag).getParent(), false));
            } else ((ChipGroup) activity.findViewById(R.id.task_tags_chipgroup)).removeAllViews();

            for (int i = 0; i < size; i++) {
                ((ChipGroup) activity.findViewById(R.id.task_tags_chipgroup)).addView(chips[i]);
                final int finalI = i;
                chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ChipGroup) activity.findViewById(R.id.task_tags_chipgroup)).removeView(chips[finalI]);
                        tagDialog.removeList(finalI);
                        if (tagDialog.getLists().size() == 0)
                            setTagBtn();
                    }
                });
                chips[i].setChipBackgroundColorResource(R.color.tagColor);
            }

        }
        return chips;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.delete_button) {
            task.removeTaskInFile(activity);
            super.onBackPressed();

        }
        return super.onOptionsItemSelected(menuItem);
    }


}
