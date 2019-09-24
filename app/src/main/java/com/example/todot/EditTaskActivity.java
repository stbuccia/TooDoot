package com.example.todot;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.text.ParseException;

import model.Task;

public class EditTaskActivity extends AppCompatActivity {

    Task task;
    EditTaskActivity activity = this;
    ListDialog listDialog;
    TagDialog tagDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);


        try {
            task = new Task("(A) Prova");
        } catch (ParseException e) {
            e.printStackTrace();
        }



        changeView(findViewById(R.id.priority), (getLayoutInflater().inflate(R.layout.add_priority_button, (ViewGroup)findViewById(R.id.priority).getParent(), false)));

        setPriorityBtn();
        setDateBtn();

        //changeView(findViewById(R.id.list), getLayoutInflater().inflate(R.layout.add_list_button, (ViewGroup)findViewById(R.id.list).getParent(), false) );
        setListBtn();
        setTagBtn();

    }

    @Override
    public void onResume (){
        super.onResume();


    }

    private void changeView(View from, View to){

        ViewGroup parent = (ViewGroup) from.getParent();
        int index = parent.indexOfChild(from);

        parent.removeView(from);
        parent.addView(to, index);

    }


    public void setDateBtn(){
        changeView(findViewById(R.id.date), getLayoutInflater().inflate(R.layout.add_date_button, (ViewGroup)findViewById(R.id.date).getParent(), false) );
        CalendarDialog calendarDialog = new CalendarDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_date) {
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
        return chip;
    }

    public void setPriorityBtn(){
        changeView(findViewById(R.id.priority), getLayoutInflater().inflate(R.layout.add_priority_button, (ViewGroup)findViewById(R.id.priority).getParent(), false) );
        PriorityDialog priorityDialog = new PriorityDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_priority) {
            @Override
            public void onPrioritySet(int val) {
                super.onPrioritySet(val);
                if (priority != '0') {
                    chip = setPriorityChipGroup(chip);
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

        listDialog = new ListDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_lists){
            @Override
            public void onSetLists(){
                super.onSetLists();
                setListChipGroup(getChips(), getLists().size());
            }
        };
    }

    public Chip[] setListChipGroup(final Chip[] chips, int size){
        if (activity.findViewById(R.id.task_lists_chipgroup) == null) {
            changeView(activity.findViewById(R.id.list), getLayoutInflater().inflate(R.layout.list_chipgroup, (ViewGroup) activity.findViewById(R.id.list).getParent(), false));
        }
        else ((ChipGroup) activity.findViewById(R.id.task_lists_chipgroup)).removeAllViews();

        for (int i = 0 ; i < size; i++) {
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
        }

        return chips;

    }

    public void setTagBtn(){
        changeView(findViewById(R.id.tag), getLayoutInflater().inflate(R.layout.add_tag_button, (ViewGroup)findViewById(R.id.tag).getParent(), false) );

        tagDialog = new TagDialog(activity, findViewById(R.id.edit_task_layout), R.id.task_tags) {
            @Override
            public void onSetTag(){
                super.onSetTag();
                setTagChipGroup(getChips(), getTags().size());
            }
        };
    }

    public Chip[] setTagChipGroup(final Chip[] chips, int size){
        if (activity.findViewById(R.id.task_tags_chipgroup) == null) {
            changeView(activity.findViewById(R.id.tag), getLayoutInflater().inflate(R.layout.tag_chipgroup, (ViewGroup) activity.findViewById(R.id.tag).getParent(), false));
        }
        else ((ChipGroup) activity.findViewById(R.id.task_tags_chipgroup)).removeAllViews();

        for (int i = 0 ; i < size; i++) {
            ((ChipGroup) activity.findViewById(R.id.task_tags_chipgroup)).addView(chips[i]);
            final int finalI = i;
            chips[i].setOnCloseIconClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ChipGroup) activity.findViewById(R.id.task_tags_chipgroup)).removeView(chips[finalI]);
                    tagDialog.removeTag(finalI);
                    if (tagDialog.getTags().size() == 0)
                        setTagBtn();
                }
            });
        }

        return chips;

    }


}
