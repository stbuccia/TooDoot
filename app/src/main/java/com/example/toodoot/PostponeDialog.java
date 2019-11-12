package com.example.toodoot;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;

import model.Utils;

public class PostponeDialog extends Dialog {
    private Context context;
    private int position;
    private TaskAdapter adapter;
    private TodoFragment fragment;

    public PostponeDialog(@NonNull Context context, TaskAdapter taskAdapter, int pos, TodoFragment todoFragment) {
        super(context);
        this.context = context;
        position = pos;
        adapter = taskAdapter;
        fragment = todoFragment;
    }

    private void postponeDay(int days){
        Date newDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(newDate);
        c.add(Calendar.DATE, days);
        newDate = c.getTime();
        adapter.changeDate(context, position, newDate, (fragment.getCalDate() != null && !Utils.isSameDay(fragment.getCalDate(), newDate)));
        dismiss();

    }

    public void showDialog(){

        setTitle("Postpone Day");
        setContentView(R.layout.postpone_dialog);

        MaterialButton tomorrow_btn = findViewById(R.id.postpone_day);
        MaterialButton week_btn = findViewById(R.id.postpone_week);
        MaterialButton cal_btn = findViewById(R.id.postpone_cal);
        MaterialButton time_btn = findViewById(R.id.postpone_time);

        tomorrow_btn.setOnClickListener(view -> postponeDay(1));

        week_btn.setOnClickListener(view -> postponeDay(7));

        cal_btn.setOnClickListener(view -> {
            final Calendar selCalendar = Calendar.getInstance();
            selCalendar.setTime(new Date());
            DatePickerDialog StartTime = new DatePickerDialog(getContext(), R.style.datepicker, (view12, year, monthOfYear, dayOfMonth) -> {
                selCalendar.set(year, monthOfYear, dayOfMonth);
                Date newDate = selCalendar.getTime();
                adapter.changeDate(context, position, newDate, (fragment.getCalDate() != null && !Utils.isSameDay(fragment.getCalDate(), newDate)));
                dismiss();
            }, selCalendar.get(Calendar.YEAR), selCalendar.get(Calendar.MONTH), selCalendar.get(Calendar.DAY_OF_MONTH));
            StartTime.show();
        });

        time_btn.setOnClickListener(view -> {
            final Calendar selCalendar = Calendar.getInstance();
            selCalendar.setTime(new Date());
            TimePickerDialog StartTime = new TimePickerDialog(getContext(), R.style.TimePickerTheme, (view1, hour, min) -> {
                selCalendar.set(Calendar.HOUR_OF_DAY, hour);
                selCalendar.set(Calendar.MINUTE, min);
                Date time = selCalendar.getTime();

                adapter.changeTime(context, position, time);
                dismiss();
            }, selCalendar.get(Calendar.HOUR_OF_DAY), 0, true);
            StartTime.show();
        });

        show();

    }
}
