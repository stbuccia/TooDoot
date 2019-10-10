package com.example.todot;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import model.Task;

import static com.example.todot.R.id;
import static com.example.todot.R.layout;
import static com.example.todot.R.style;
import static model.Task.getAllLists;
import static model.Task.getAllTags;

public class AddTaskFragment extends BottomSheetDialogFragment{


    private  View.OnClickListener dateButtonListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;

                FrameLayout bottomSheet = (FrameLayout) d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        // Do something with your dialog like setContentView() or whatever
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
        try {
            todoFragment = (TodoFragment) getActivity().getSupportFragmentManager().findFragmentById(id.fragment_container);
        }
        catch (Exception e ){
            todoFragment = (TodoFragment)(getActivity().getSupportFragmentManager().findFragmentById(id.fragment_container)).getFragmentManager().findFragmentById(id.container);
        }

        //focus on editext
        final TextInputEditText editText = view.findViewById(id.textInputEditText);
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);


        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        //set up date button
        final CalendarDialog calendarDialog = new CalendarDialog(getContext(), view, id.select_date_button){
            @Override
            public void onDateSet(final DatePicker datePicker, int year, int month, int day){
                super.onDateSet(datePicker, year, month, day);
                chip.setChipIconResource(R.drawable.ic_event);
                chip.setChipBackgroundColorResource(R.color.calColor);
                if (chipgroup.findViewById(chip_id) == null) addChip();
            }
        };

        if (todoFragment.getCalDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(todoFragment.getCalDate());

            calendarDialog.onDateSet(null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        }
        //set up priority button

        final PriorityDialog priorityDialog = new PriorityDialog(getContext(), view, id.choose_priority_button){
            @Override
            public void onPrioritySet(int val){
                super.onPrioritySet(val);
                if (val > 1 ) {
                    chip.setChipIconResource(R.drawable.ic_priority_24px);
                    chip.setChipBackgroundColorResource(R.color.priorityColor);
                    if (chipgroup.findViewById(chip_id) == null) addChip();
                }

            }
        };
        //set up tag button

        final ListDialog tagDialog = new ListDialog(getContext(), view, id.choose_tag_button, getAllTags(), "Set Tags", 0, R.color.tagColor){
            @Override
            public void onListSet(){
                        
                if (list_chips != null)
                    for (int j = 0; j < list_chips.length; j++){
                        try {
                            chipgroup.removeView(list_chips[j]);
                        }
                        catch (Exception e){}
                    }

                super.onListSet();

                for(int i = 0; i < getLists().size(); i++ ) {
                    chip = super.list_chips[i];
                    chip.setChipIconResource(R.drawable.ic_tag_24px);
                    chip.setChipBackgroundColorResource(R.color.tagColor);
                    addChip();
                }
            }
        };


        final ListDialog listDialog = new ListDialog(getContext(), view, id.choose_list_button, getAllLists(), "Set Lists", 500, R.color.listColor){
            @Override
            public void onListSet() {
                if (list_chips != null)
                    for (int j = 0; j < list_chips.length; j++){
                        try {
                            chipgroup.removeView(list_chips[j]);
                        }
                        catch (Exception e){}
                    }

                super.onListSet();


                for(int i = 0; i < getLists().size(); i++ ) {
                    chip = super.list_chips[i];
                    chip.setChipIconResource(R.drawable.ic_list_18px);
                    chip.setChipBackgroundColorResource(R.color.listColor);
                    addChip();
                }
            }
        };
        ChipGroup chipgroup = view.findViewById(id.chip_group);

        //set up save button
        MaterialButton saveButton = view.findViewById(id.save_button);
        final TodoFragment finalTodoFragment = todoFragment;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString() != "") {

                    finalTodoFragment.insertTask(new Task(editText.getText().toString(), "", calendarDialog.getDate(), priorityDialog.getPriority(), tagDialog.getLists(), listDialog.getLists()));
                    AddTaskFragment.super.dismiss();

                }
            }
        });
        return view;

    }



}
