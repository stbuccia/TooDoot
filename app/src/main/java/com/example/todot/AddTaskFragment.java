package com.example.todot;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

import model.Task;

import static com.example.todot.R.id;
import static com.example.todot.R.layout;
import static com.example.todot.R.style;

public class AddTaskFragment extends BottomSheetDialogFragment{


    private ChipGroup chipgroup;
    private  View.OnClickListener dateButtonListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(AddTaskFragment.STYLE_NORMAL, style.DialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(layout.fragment_add_task, null);

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

        //set up save button
        MaterialButton saveButton = view.findViewById(id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Task task = new Task(editText.getText().toString(), "", cal.getTime(), 0, new ArrayList<String>());
                task.addTaskInFile(getActivity());
                AddTaskFragment.super.dismiss();

                //refresh todo fragment
                Fragment todoFragment = getActivity().getSupportFragmentManager().findFragmentById(id.fragment_container);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.detach(todoFragment);
                transaction.attach(todoFragment);
                transaction.commit();
            }
        });
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        //set up date button
        CalendarDialog calendarDialog = new CalendarDialog(getContext(), view, id.select_date_button);

        //set up priority button

        PriorityDialog priorityDialog = new PriorityDialog(getContext(), view, id.choose_priority_button);
        //set up tag button

        TagDialog tagDialog = new TagDialog(getContext(), view, id.choose_tag_button);


        ListDialog listDialog = new ListDialog(getContext(), view, id.choose_list_button);
        chipgroup = view.findViewById(id.chip_group);

        return view;

    }

}
