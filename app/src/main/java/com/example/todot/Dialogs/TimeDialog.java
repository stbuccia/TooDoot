package com.example.todot;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import model.Utils;

public class TimeDialog extends ButtonsDialog implements TimePickerDialog.OnTimeSetListener{
    protected TimePickerDialog timePickerDialog;
    private View.OnClickListener timeButtonListener;
    private Date time = null;


    private void setupTimePicker(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        timePickerDialog = new TimePickerDialog(context, R.style.TimePickerTheme, TimeDialog.this, hour, 0, true);


    }

    public TimeDialog(Context context, View view, int idButton) {
        super(context, view, idButton);
        timeButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        };

        setupTimePicker();
        setListener(timeButtonListener);
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        time = calendar.getTime();
        chip_id = R.id.time_chip;

        setChip(chip_id, -1, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerDialog.show();
            }
        });
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipgroup.removeView(chip);
            }
        });

        chip.setText(Utils.timeFormat().format(time));



    }

    public Date getTime() {
        return time;
    }
}
