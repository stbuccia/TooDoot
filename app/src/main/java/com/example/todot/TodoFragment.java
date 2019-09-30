package com.example.todot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import model.Task;

import static model.Task.getSavedTasks;


public class TodoFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
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
        ((View) button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AddTaskFragment bottomSheetFragment = new AddTaskFragment();

                bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

            }
        });
        return view;
    }

    public void filter(String sequence){
       mAdapter.getFilter().filter(sequence);
    }

    public void insertTask(Task task){
        mAdapter.insertItem(getActivity(), task);
        filter(mAdapter.getCharString());
    }

    public void deleteTask(Task task, int pos){
        mAdapter.deleteItem(getActivity(), pos);

    }
}