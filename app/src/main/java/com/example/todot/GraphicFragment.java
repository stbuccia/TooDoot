package com.example.todot;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Task;

public class GraphicFragment extends Fragment {
    private PieChart pieChart;
    private RadioGroup radioGroup;
    private TextView rate;

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

        pieChart.animateY(1000, Easing.EaseInOutCubic);

        ArrayList<PieEntry> values = new ArrayList<>();

        ArrayList<Task> tasks = Task.getSavedTasks(getContext(), getActivity());


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

        String s = String.format("%d", (int)(countCompleted * 100 / tasks.size()));
        s += "%";
        rate.setText(s);

    }

    private void setMenu(View view, ArrayList<String> lists, int dropdown_menu, int dropdown_list){
        if (!lists.contains("All"))
            lists.add(0, "All");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        dropdown_menu, lists);


        AutoCompleteTextView editTextFilledExposedDropdown =
                view.findViewById(dropdown_list);
        editTextFilledExposedDropdown.setText("All");
        editTextFilledExposedDropdown.setAdapter(adapter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_graphic, null);

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

        setMenu(view, Task.getAllLists(), R.layout.dropdown_popup_menuitem, R.id.list_dropdown);
        setMenu(view, Task.getAllTags(), R.layout.dropdown_popup_menuitem, R.id.tag_dropdown);

        return view;
    }
}