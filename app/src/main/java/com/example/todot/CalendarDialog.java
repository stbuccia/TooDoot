package com.example.todot;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarDialog extends ButtonsDialog implements DatePickerDialog.OnDateSetListener{

    protected DatePickerDialog datePickerDialog;
    private View.OnClickListener dateButtonListener;
    private Date date = null;


    private void setupDatePicker(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(context, R.style.datepicker, CalendarDialog.this, year, month, day);


    }

    public CalendarDialog(Context context, View view, int idButton){
        super(context, view, idButton);
        dateButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        };
        setupDatePicker();

        setListener(dateButtonListener);
    }

    @Override
    public void onDateSet(final DatePicker datePicker, int year, int month, int day) {
        SimpleDateFormat dateFormat;
        Calendar calendar = Calendar.getInstance();
        if (year ==  calendar.get(Calendar.YEAR))
            dateFormat = new SimpleDateFormat("E, dd MMM");
        else
            dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        calendar.set(year, month, day);
        date = calendar.getTime();

        setChip(R.id.date_chip, -1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipgroup.removeView(chip);
            }
        });

        chip.setText(dateFormat.format(date));


    }




    public Chip getChip(){
        return chip;
    }

    public Date getDate(){
        return date;
    }


}
