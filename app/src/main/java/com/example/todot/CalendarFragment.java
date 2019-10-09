package com.example.todot;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Task;
import model.Utils;


public class CalendarFragment extends Fragment {
    private CollapsibleCalendar collapsibleCalendar;
    private TodoFragment todoFragment;
    private  TextView textView;

    private void setCalToDate(Day day){
        collapsibleCalendar.select(day);
        int calMonth = collapsibleCalendar.getMonth();
        int calYear = collapsibleCalendar.getYear();
        int selectedMonth = collapsibleCalendar.getSelectedItem().getMonth();
        int selectedYear = collapsibleCalendar.getSelectedItem().getYear();

        if ((selectedMonth > calMonth && selectedYear == calYear) || (selectedYear > calYear)){
            int monthdist = (selectedMonth - calMonth) + (12 * (selectedYear - calYear));
            for ( int i = 0; i < monthdist; i++) {
                collapsibleCalendar.nextMonth();
            }
        }
        else{

            int monthdist = (calMonth - selectedMonth) + (12 * (calYear - selectedYear));
            for ( int i = 0; i < monthdist; i++) {
                collapsibleCalendar.prevMonth();
            }
        }
        //collapse
        collapsibleCalendar.expand(300);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                collapsibleCalendar.collapse(300);
            }
        }, 301);
    }

    private Day getToday(){
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Day today = new Day(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        return today;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_calendar, null);

        collapsibleCalendar = view.findViewById(R.id.calendarView);
        //collapsibleCalendar.setFirstDayOfWeek(1);

        todoFragment = new TodoFragment(Task.getTasksWithDate(getContext(), getActivity(), new Date()));
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, todoFragment)
                .commit();
        collapsibleCalendar.select(getToday());
        todoFragment.setCalDate(new Date());

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();

                Calendar myCal = Calendar.getInstance();
                myCal.set(Calendar.YEAR, day.getYear());
                myCal.set(Calendar.MONTH, day.getMonth());
                myCal.set(Calendar.DAY_OF_MONTH, day.getDay());

                Date date = myCal.getTime();
                todoFragment.setCalDate(date);
                ArrayList<Task> tasks = Task.getTasksWithDate(getContext(), getActivity(), date);
                todoFragment.setTasks(tasks);

                textView.setText(Utils.getStringDate(date));

            }

            @Override
            public void onItemClick(@NotNull View view) {

            }

            @Override
            public void onDataUpdate() {

            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int i) {

            }

        });

        MaterialButton todayButton = view.findViewById(R.id.today_button);
        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCalToDate(getToday());
            }
        });

        collapsibleCalendar.setSelected(true);

        textView = view.findViewById(R.id.textView);
        textView.setText(Utils.getStringDate(new Date()));
        textView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                setCalToDate(collapsibleCalendar.getSelectedDay());
            }
        });

        return view;
    }

}