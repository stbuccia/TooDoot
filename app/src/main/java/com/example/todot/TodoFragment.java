package com.example.todot;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import model.Task;

import static model.Task.getSavedTasks;


public class TodoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    FloatingActionButton button;
    ArrayList<Task> myTasks = new ArrayList<Task>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, null);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.cardRV);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        myTasks = getSavedTasks(getContext(), getActivity());
        mAdapter = new TaskAdapter(myTasks);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        //set up button
        button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //get start and end date
                Calendar cal = Calendar.getInstance();
                Date startDate = cal.getTime();

                Task task = new Task("Fare un esempio", "", startDate, 0, new ArrayList<String>());
                task.addTaskInFile(getContext(), getActivity());
                myTasks.add(task);

                mAdapter.notifyItemInserted(myTasks.size() + 1);
            }
        });
        return view;
    }
}