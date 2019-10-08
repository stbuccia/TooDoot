package com.example.todot;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Task;

public class GraphicFragment extends Fragment {
    private PieChart pieChart;
    private LineChart lineChart;
    private RadioGroup radioGroup;
    private TextView rate;
    private String listSel;
    private String tagSel;
    private ArrayList<Task> tasks;
    private String all = "All";

    @SuppressLint("DefaultLocale")
    public void setPieChart(){

        pieChart.getDescription().setEnabled(false);

        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(90f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(0f);
        pieChart.setDrawEntryLabels(false);
        pieChart.setTouchEnabled(false);

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        ArrayList<PieEntry> values = new ArrayList<>();


        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        boolean noDate = false;

        int index = radioGroup.indexOfChild(radioGroup.findViewById(radioGroup.getCheckedRadioButtonId()));
        if (index >= 1) {
            startDate.set(Calendar.HOUR_OF_DAY, 0);
            endDate.set(Calendar.HOUR_OF_DAY, 23);
            endDate.set(Calendar.MINUTE, 59);
            endDate.set(Calendar.SECOND, 59);
            endDate.set(Calendar.MILLISECOND, 999);
        }
        if (index >= 2){
            startDate.set(Calendar.DAY_OF_WEEK, startDate.getFirstDayOfWeek());
            endDate.set(Calendar.DAY_OF_WEEK, startDate.getFirstDayOfWeek() + 6);
        }
        if (index >= 3){
            startDate.set(Calendar.DAY_OF_MONTH, 1);
            endDate.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
        }
        if (index == 0){
            noDate = true;
        }

        int countCompleted = 0;
        int countUncompleted = 0;

        for (int i = 0; i < tasks.size(); i++){
            if (noDate || tasks.get(i).getCreation_date() == null
                            || (tasks.get(i).getCreation_date().before(endDate.getTime())
                            && tasks.get(i).getCreation_date().after(startDate.getTime())
                    ))
            {
                if (tasks.get(i).isComplete())
                    countCompleted++;
                else if (!tasks.get(i).isComplete()) {
                    countUncompleted++;
                }
            }
        }
        values.add(new PieEntry(countCompleted, "Completed"));
        values.add(new PieEntry(countUncompleted, "Uncompleted"));

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(0f);
        dataSet.setColors(getResources().getColor(R.color.design_default_color_primary), getResources().getColor(R.color.design_default_color_secondary));

        PieData data = new PieData((dataSet));
        data.setValueTextSize(0f);

        pieChart.setData(data);

        String s = "";
        if (countCompleted + countUncompleted != 0)
            s = String.format("%d", (int)(countCompleted * 100 / (countCompleted + countUncompleted)));
        else
            s = String.format("0");
        s += "%";
        rate.setText(s);

    }

    private void setLineChart(){

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
        lineChart.animateY(1000);

        ArrayList<Entry> yValues = new ArrayList<>();

        yValues.add(new Entry(0, 60));
        yValues.add(new Entry(1, 70));
        yValues.add(new Entry(2, 50));
        yValues.add(new Entry(3, 50));
        yValues.add(new Entry(4, 40));
        yValues.add(new Entry(5, 0));
        yValues.add(new Entry(6, 50));

        LineDataSet set1 = new LineDataSet(yValues, "Data set 1");
        set1.setFillColor(getResources().getColor(R.color.fillColor));
        set1.setDrawFilled(true);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);


        set1.setLineWidth(3f);
        set1.setValueTextSize(12f);
        set1.setColor(getResources().getColor(R.color.design_default_color_primary));
        set1.setCircleColor(getResources().getColor(R.color.design_default_color_primary));


        LineData data = new LineData();
        data.addDataSet(set1);
        lineChart.setData(data);

    }

    private void setMenu(View view, ArrayList<String> lists, int dropdown_menu, int dropdown_list, final boolean isList){
        ArrayList<String> suggestion = lists;
        if (!suggestion.contains(all))
            suggestion.add(0, all);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        dropdown_menu, suggestion);


        final AutoCompleteTextView editTextFilledExposedDropdown =
                view.findViewById(dropdown_list);
        editTextFilledExposedDropdown.setText(all);
        if (isList) listSel = all;
        else tagSel = all;
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isList)
                    listSel = editTextFilledExposedDropdown.getText().toString();
                else
                    tagSel = editTextFilledExposedDropdown.getText().toString();
                filter();
                setPieChart();
            }
        });
    }

    private void filter(){
        tasks = Task.getSavedTasks(getContext(), getActivity());
        ArrayList<Task> newTasks = new ArrayList<>();
        for (int i = 0; i < tasks.size(); i++){
            if ((listSel.equals(all) || tasks.get(i).getLists().contains(listSel)) && (tagSel.equals(all) || tasks.get(i).getTags().contains(tagSel)))
                newTasks.add(tasks.get(i));
        }
        tasks = newTasks;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_graphic, null);
        tasks = Task.getSavedTasks(getContext(), getActivity());


        pieChart = (PieChart) view.findViewById(R.id.piechart);
        TextView textView = (TextView) view.findViewById(R.id.piename);
        rate = (TextView) view.findViewById(R.id.rate);

        textView.setText("Completation Rate");

        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.check(R.id.all_radio);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                setPieChart();
            }
        });

        setPieChart();

        lineChart = (LineChart) view.findViewById(R.id.line_chart);
        setLineChart();

        setMenu(view, Task.getAllLists(), R.layout.dropdown_popup_menuitem, R.id.list_dropdown, true);
        setMenu(view, Task.getAllTags(), R.layout.dropdown_popup_menuitem, R.id.tag_dropdown, false);



        return view;
    }
}