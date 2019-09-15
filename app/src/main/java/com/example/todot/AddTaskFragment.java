package com.example.todot;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import model.Task;

public class AddTaskFragment extends BottomSheetDialogFragment implements DatePickerDialog.OnDateSetListener  {


    private boolean dateChosen = false;
    private boolean priorityChosen = false;
    private ChipGroup chipgroup;
    private  View.OnClickListener dateButtonListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(AddTaskFragment.STYLE_NORMAL, R.style.DialogStyle);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_task, null);

        //focus on editext
        final TextInputEditText editText = view.findViewById(R.id.textInputEditText);
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);

        //set up save button
        MaterialButton saveButton = view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                Task task = new Task(editText.getText().toString(), "", cal.getTime(), 0, new ArrayList<String>());
                task.addTaskInFile(getActivity());
                AddTaskFragment.super.dismiss();

                //refresh todo fragment
                Fragment todoFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.detach(todoFragment);
                transaction.attach(todoFragment);
                transaction.commit();
            }
        });
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        //set up date button
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AddTaskFragment.this, year, month, day);
        MaterialButton dateButton = view.findViewById(R.id.select_date_button);
        dateButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        };
        dateButton.setOnClickListener(dateButtonListener);

        //set up priority button
        MaterialButton priorityButton = view.findViewById(R.id.choose_priority_button);
        View.OnClickListener priorityListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriorityDialog();
            }
        };
        priorityButton.setOnClickListener(priorityListener);
        chipgroup = view.findViewById(R.id.chip_group);


        return view;

    }

    public Chip addChip(Chip chip, int id, View.OnClickListener listener, boolean cond){

        if (cond){
            chip = getView().findViewById(R.id.date_chip);
        }
        else {
            chip = new Chip(getContext());
            chip.setId(id);
            chip.setLayoutParams(new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.WRAP_CONTENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT));
            chipgroup.addView(chip);
            chip.setCloseIconVisible(true);
            chip.setOnClickListener(listener);
        }
        return chip;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Chip date_chip = null;
        date_chip= addChip(date_chip, R.id.date_chip, dateButtonListener, dateChosen);
        dateChosen = true;

        date_chip.setText(dateFormat.format(calendar.getTime()));
    }


    private void showPriorityDialog() {
        final Dialog d = new Dialog(getContext());
        d.setTitle("Select Priority");
        d.setContentView(R.layout.priority_dialog);

        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        String[] alphabet = new String[27];

        alphabet[0] = "Nessuna";

        for(char i=1; i <= 26 ; i++){
            alphabet[i] = "" + (char)((i-1) + 'A');
        }
        Button set_btn = (Button) d.findViewById(R.id.set_button);
        Button cancel_btn = (Button) d.findViewById(R.id.cancel_button);

        np.setDisplayedValues(null);
        np.setMinValue(1);
        np.setDisplayedValues(alphabet);

        np.setMaxValue(alphabet.length);
        np.setWrapSelectorWheel(false);
        set_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                prioritySet(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                d.dismiss();
            }
        });
        d.show();

    }
    private void prioritySet(String txt){
        Chip priority_chip = null;
        priority_chip = addChip(priority_chip, R.id.priority_chip, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPriorityDialog();
            }
        }, priorityChosen);
        priorityChosen = true;
        priority_chip.setText(txt);

    }
}
