package com.example.todot;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Formatter;

import model.Task;
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

    public void showDialog(){

        setTitle("Postpone Day");
        setContentView(R.layout.postpone_dialog);

        MaterialButton tomorrow_btn = findViewById(R.id.postpone_day);
        MaterialButton week_btn = findViewById(R.id.postpone_week);
        MaterialButton cal_btn = findViewById(R.id.postpone_cal);
        MaterialButton time_btn = findViewById(R.id.postpone_time);

        tomorrow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date newDate = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(newDate);
                c.add(Calendar.DATE, 1);
                newDate = c.getTime();
                adapter.changeDate(context, position, newDate, (fragment.getCalDate() != null && !Utils.isSameDay(fragment.getCalDate(), newDate)));
                dismiss();
            }
        });

        week_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date newDate = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(newDate);
                c.add(Calendar.DATE, 7);
                newDate = c.getTime();
                adapter.changeDate(context, position, newDate, (fragment.getCalDate() != null && !Utils.isSameDay(fragment.getCalDate(), newDate)));
                dismiss();
            }
        });

        cal_btn.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   final Calendar selCalendar = Calendar.getInstance();
                   selCalendar.setTime(new Date());
                   DatePickerDialog StartTime = new DatePickerDialog(getContext(), R.style.datepicker, new DatePickerDialog.OnDateSetListener() {
                       public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                           selCalendar.set(year, monthOfYear, dayOfMonth);
                           Date newDate = selCalendar.getTime();
                           adapter.changeDate(context, position, newDate, (fragment.getCalDate() != null && !Utils.isSameDay(fragment.getCalDate(), newDate)));
                           dismiss();
                       }
                   }, selCalendar.get(Calendar.YEAR), selCalendar.get(Calendar.MONTH), selCalendar.get(Calendar.DAY_OF_MONTH));
                   StartTime.show();
               }
           });

        time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar selCalendar = Calendar.getInstance();
                selCalendar.setTime(new Date());
                TimePickerDialog StartTime = new TimePickerDialog(getContext(), R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hour, int min) {
                        selCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        selCalendar.set(Calendar.MINUTE, min);
                        Date time = selCalendar.getTime();

                        adapter.changeTime(context, position, time);
                        dismiss();
                    }
                }, selCalendar.get(Calendar.HOUR_OF_DAY), 0, true);
                StartTime.show();
            }
        });

        show();

    }
}
