package com.example.toodoot;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import model.Priority;
import model.Task;
import model.Utils;

import static com.example.toodoot.R.id;
import static com.example.toodoot.R.layout;
import static com.example.toodoot.R.style;
import static model.Task.getAllLists;
import static model.Task.getAllTags;

public class AddTaskFragment extends BottomSheetDialogFragment{


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialog1 -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog1;

            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
        });

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(AddTaskFragment.STYLE_NORMAL, style.DialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_add_task, null);

        TodoFragment todoFragment;
        todoFragment = ((MainActivity)getActivity()).getTodoFragment();

        final TextInputEditText editText = view.findViewById(id.textInputEditText);
        editText.requestFocus();
        editText.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }, 200);


        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        //set up date button
        final CalendarDialog calendarDialog = new CalendarDialog(getContext(), view, id.select_date_button){
            @Override
            public void onDateSet(final DatePicker datePicker, int year, int month, int day){
                super.onDateSet(datePicker, year, month, day);
                chip.setChipBackgroundColorResource(R.color.calColor);
                if (chipgroup.findViewById(chip_id) == null) addChip();
            }
        };

        if (todoFragment.getCalDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(todoFragment.getCalDate());
            calendarDialog.onDateSet(null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        }



        //set up time button
        final TimeDialog timeDialog = new TimeDialog(getContext(), view, id.select_time_button){
            @Override
            public void onTimeSet(final TimePicker timePicker, int hour, int min){
                super.onTimeSet(timePicker, hour, min);
                chip.setChipBackgroundColorResource(R.color.timeColor);
                if (chipgroup.findViewById(chip_id) == null) addChip();
            }
        };

        //set up priority button
        final PriorityDialog priorityDialog = new PriorityDialog(getContext(), view, id.choose_priority_button){
            @Override
            public void onPrioritySet(int val){
                super.onPrioritySet(val);
                if (val > 1 ) {
                    char priority = (char)('A' + (val - 2));
                    chip.setChipBackgroundColorResource(Utils.getPriorityResColor(Priority.fromCharToInt(priority)));
                    if (chipgroup.findViewById(chip_id) == null) addChip();
                }

            }
        };
        //set up tag button

        final ListDialog tagDialog = new ListDialog(getContext(), view, id.choose_tag_button, getAllTags(), getString(R.string.set_tags), 0, R.color.tagColor){
            @Override
            public void onListSet(){
                        
                if (list_chips != null)
                    for (Chip list_chip : list_chips) {
                        try {
                            chipgroup.removeView(list_chip);
                        } catch (Exception ignored) {
                        }
                    }

                super.onListSet();

                for(int i = 0; i < getLists().size(); i++ ) {
                    chip = super.list_chips[i];
                    chip.setChipBackgroundColorResource(R.color.tagColor);
                    addChip();
                }
            }
        };


        final ListDialog listDialog = new ListDialog(getContext(), view, id.choose_list_button, getAllLists(), getString(R.string.set_lists), 500, R.color.listColor){
            @Override
            public void onListSet() {
                if (list_chips != null)
                    for (Chip list_chip : list_chips) {
                        try {
                            chipgroup.removeView(list_chip);
                        } catch (Exception ignored) {
                        }
                    }

                super.onListSet();


                for(int i = 0; i < getLists().size(); i++ ) {
                    chip = super.list_chips[i];
                    chip.setChipBackgroundColorResource(R.color.listColor);
                    addChip();
                }
            }
        };

        //set up save button
        MaterialButton saveButton = view.findViewById(id.save_button);
        saveButton.setOnClickListener(v -> {
            if (!editText.getText().toString().equals("")) {
                ((MainActivity)getActivity()).getTodoFragment().insertTask(new Task(editText.getText().toString(), "", calendarDialog.getDate(), timeDialog.getTime(), priorityDialog.getPriority(), tagDialog.getLists(), listDialog.getLists()));
                AddTaskFragment.super.dismiss();

            }
        });
        return view;

    }



}
