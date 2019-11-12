package com.example.toodoot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import model.Task;
import model.Utils;

public class GraphicFragment extends Fragment {

    private PieChart pieChart;
    private LineChart lineChart;
    private RadioGroup radioGroupPie;
    private TextView rate;
    private String listSel;
    private String tagSel;
    private ArrayList<Task> tasks;
    private String all;
    private RadioGroup radioGroupLine;
    private ArrayList<Date> xValues = new ArrayList<>();
    private ArrayList<Entry> yValues = new ArrayList<>();


    private boolean isTaskinDate(Task task, Date start, Date end){
        return ((task.getDate().equals(end) ||task.getDate().before(end)) && (task.getDate().equals(start) || task.getDate().after(start)));
    }

    @SuppressLint("DefaultLocale")
    public void setPieChart(){

        pieChart.getDescription().setEnabled(false);

        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(90f);
        pieChart.setHoleColor(getResources().getColor(R.color.design_default_color_background));
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.setTouchEnabled(false);

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        ArrayList<PieEntry> values = new ArrayList<>();


        Calendar startDate = Utils.initCalendar();

        Calendar endDate = Utils.initCalendar();
        boolean noDate = false;

        int index = radioGroupPie.indexOfChild(radioGroupPie.findViewById(radioGroupPie.getCheckedRadioButtonId()));
        if (index >= 1) {
            startDate = Utils.setStartDay(startDate);
            endDate = Utils.setEndDay(endDate);
        }
        if (index >= 2){
            startDate = Utils.setStartWeek(startDate);
            endDate = Utils.setEndWeek(endDate);
        }
        if (index >= 3){
            startDate = Utils.setStartMonth(startDate);
            endDate = Utils.setEndMonth(endDate);
        }
        if (index == 0){
            noDate = true;
        }

        int countCompleted = 0;
        int countUncompleted = 0;

        for (int i = 0; i < tasks.size(); i++){
            if (noDate  || isTaskinDate (tasks.get(i), startDate.getTime(), endDate.getTime())) {
                if (tasks.get(i).isComplete())
                    countCompleted++;
                else if (!tasks.get(i).isComplete()) {
                    countUncompleted++;
                }
            }
        }
        if (countCompleted + countUncompleted == 0)
            countUncompleted = 1;

        values.add(new PieEntry(countCompleted, getResources().getString(R.string.completed)));
        values.add(new PieEntry(countUncompleted, getResources().getString(R.string.uncompleted)));

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(0f);
        dataSet.setColors(getResources().getColor(R.color.design_default_color_primary), getResources().getColor(R.color.design_default_color_secondary));

        PieData data = new PieData((dataSet));
        data.setValueTextSize(0f);

        pieChart.setData(data);

        String s;
        s = String.format("%d", (int)(countCompleted * 100 / (countCompleted + countUncompleted)));
        s += "%";
        rate.setText(s);

    }

    private int countComplete(Calendar start, Calendar end){
        int countTask = 0;
        for (int j = 0; j < tasks.size(); j++) {
            if (isTaskinDate(tasks.get(j), start.getTime(), end.getTime()) && tasks.get(j).isComplete())
                countTask++;
        }
        return countTask;
    }

    private void setLineXYValues(int index) {

        int numberOfX = 7;

        int countTask;
        Calendar start = Utils.initCalendar();
        Calendar end = Utils.initCalendar();
        if (index == 0) {
            start.add(Calendar.DAY_OF_MONTH, (-1) * (numberOfX - 1));
            end.add(Calendar.DAY_OF_MONTH, (-1) * (numberOfX - 1));
        }
        else if (index == 1) {
            start.add(Calendar.WEEK_OF_YEAR, (-1) * (numberOfX - 1));
            end.add(Calendar.WEEK_OF_YEAR, (-1) * (numberOfX - 1));
        }
        else {
            start.add(Calendar.MONTH, (-1) * (numberOfX - 1));
            end.add(Calendar.MONTH, (-1) * (numberOfX - 1));

        }

        for (int i = 0; i < numberOfX; i++) {
            if (index == 0) {
                start = Utils.setStartDay(start);
                end = Utils.setEndDay(end);
                countTask = countComplete(start, end);
                xValues.add(start.getTime());
                start.add(Calendar.DAY_OF_MONTH, 1);
                end.add(Calendar.DAY_OF_MONTH, 1);

            } else if (index == 1) {
                start = Utils.setStartWeek(start);
                end = Utils.setEndWeek(end);
                countTask = countComplete(start, end);
                xValues.add(start.getTime());
                start.add(Calendar.WEEK_OF_YEAR, 1);
                end.add(Calendar.WEEK_OF_YEAR, 1);

            } else {
                start = Utils.setStartMonth(start);
                end = Utils.setEndMonth(end);
                countTask = countComplete(start, end);
                xValues.add(start.getTime());
                start.add(Calendar.MONTH, 1);
                end.add(Calendar.MONTH, 1);
            }
            yValues.add(new Entry(i, countTask));

        }

    }

    public class XAxisValueFormatter extends ValueFormatter {
        private ArrayList<Date> values;
        private int index;

        public XAxisValueFormatter(ArrayList<Date>  values, int i){
            index = i;
            this.values = values;
        }

        @Override
        public String getFormattedValue(float value) {
            DateFormat dateFormat;
            String s;
            if (index == 0) {
                dateFormat = new SimpleDateFormat("dd");
                s = dateFormat.format(values.get((int)value));
            }
            else if (index == 1) {
                dateFormat = new SimpleDateFormat("dd-");
                s = dateFormat.format(values.get((int)value));

                dateFormat = new SimpleDateFormat("dd");
                Calendar calendar = Utils.initCalendar();
                calendar.setTime(values.get((int)value));
                calendar.add(Calendar.DAY_OF_MONTH, 6);
                s += dateFormat.format(calendar.getTime());

            }
            else {
                dateFormat = new SimpleDateFormat("MMM");
                s = dateFormat.format(values.get((int)value));
            }
            return s;
        }
    }

    public class YAxisValueFormatter extends  ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return (int)value + "";
        }
    }

    private void setLineChart(){
        xValues = new ArrayList<>();
        yValues = new ArrayList<>();

        //set up graphic chart
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getXAxis().setDrawAxisLine(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setTextSize(10f);

        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawLabels(false);
        lineChart.getAxisLeft().setDrawAxisLine(false);
        lineChart.getAxisLeft().setAxisMinimum(0f);


        lineChart.getAxisRight().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawLabels(false);
        lineChart.getAxisRight().setDrawAxisLine(false);

        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setDescription(null);
        lineChart.setMinimumHeight(0);
        lineChart.setClickable(false);
        lineChart.setDrawMarkers(false);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.animateY(500);

        int index = radioGroupLine.indexOfChild(radioGroupLine.findViewById(radioGroupLine.getCheckedRadioButtonId()));

        setLineXYValues(index);



        LineDataSet set = new LineDataSet(yValues, getResources().getString(R.string.task_completed));
        set.setFillColor(getResources().getColor(R.color.fillColor));
        set.setDrawFilled(true);
        set.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);


        set.setLineWidth(3f);
        set.setValueTextSize(12f);
        set.setColor(getResources().getColor(R.color.design_default_color_primary));
        set.setCircleColor(getResources().getColor(R.color.design_default_color_primary));
        set.setCircleHoleColor(getResources().getColor(R.color.design_default_color_primary));
        set.setCircleRadius(3f);

        set.setValueFormatter(new YAxisValueFormatter());

        LineData data = new LineData();
        data.addDataSet(set);
        lineChart.setData(data);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new XAxisValueFormatter(xValues, index));
        xAxis.setGranularity(1);


        YAxis yAxisL = lineChart.getAxisLeft();
        yAxisL.setValueFormatter(new YAxisValueFormatter());
        YAxis yAxisR = lineChart.getAxisRight();
        yAxisR.setValueFormatter(new YAxisValueFormatter());
    }

    private void setMenu(View view, ArrayList<String> lists, int dropdown_list, final boolean isList){
        ArrayList<String> suggestion = new ArrayList<>(lists);
        if (!suggestion.contains(all))
            suggestion.add(0, all);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        Objects.requireNonNull(getContext()),
                        R.layout.dropdown_popup_menuitem, suggestion);


        final AutoCompleteTextView editTextFilledExposedDropdown =
                view.findViewById(dropdown_list);
        editTextFilledExposedDropdown.setText(all);
        if (isList) listSel = all;
        else tagSel = all;
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (isList)
                listSel = editTextFilledExposedDropdown.getText().toString();
            else
                tagSel = editTextFilledExposedDropdown.getText().toString();
            filter();
            setPieChart();
            setLineChart();
        });
    }

    private void filter(){
        tasks = Task.getSavedTasks(getContext());
        ArrayList<Task> newTasks = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++){
            if ((listSel.equals(all) || tasks.get(i).getLists().contains(listSel)) && (tagSel.equals(all) || tasks.get(i).getTags().contains(tagSel)))
                newTasks.add(tasks.get(i));
        }
        tasks = newTasks;
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_graphic, null);
        all =  getResources().getString(R.string.all);
        tasks = Task.getSavedTasks(getContext());

        //Chart Pie
        pieChart = view.findViewById(R.id.piechart);
        TextView textViewPie = view.findViewById(R.id.piename);
        rate = view.findViewById(R.id.rate);

        textViewPie.setText(getResources().getString(R.string.completation_rate));

        radioGroupPie = view.findViewById(R.id.radioGroup);
        radioGroupPie.check(R.id.all_radio);

        radioGroupPie.setOnCheckedChangeListener((radioGroup, i) -> setPieChart());

        setPieChart();


        //Chart Line
        lineChart = view.findViewById(R.id.line_chart);
        TextView textViewLine = view.findViewById(R.id.linename);

        textViewLine.setText(getResources().getString(R.string.completation_dates));

        radioGroupLine = view.findViewById(R.id.radioGroup_line);
        radioGroupLine.check(R.id.day_radio_line);

        radioGroupLine.setOnCheckedChangeListener((radioGroup, i) -> setLineChart());


        setLineChart();


        //List and tags dropdown
        setMenu(view, Task.getAllLists(), R.id.list_dropdown, true);
        setMenu(view, Task.getAllTags(), R.id.tag_dropdown, false);


        return view;
    }
}