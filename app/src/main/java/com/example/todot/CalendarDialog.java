package com.example.todot;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarDialog extends ButtonsDialog implements DatePickerDialog.OnDateSetListener{

    private DatePickerDialog datePickerDialog;
    private View.OnClickListener dateButtonListener;

    private Chip date_chip = null;

    public CalendarDialog(Context context, View view, int idButton){
        super(context, view, idButton);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(context, CalendarDialog.this, year, month, day);

        dateButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        };
        setListener(dateButtonListener);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        date_chip= addChip(R.id.date_chip, dateButtonListener);
        date_chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipgroup.removeView(date_chip);
            }
        });

        date_chip.setText(dateFormat.format(calendar.getTime()));
    }
}
