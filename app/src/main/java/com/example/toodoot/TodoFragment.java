package com.example.toodoot;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

import model.Task;
import model.Utils;


public class TodoFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private TaskAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Date calDate = null;

    private FloatingActionButton button;
    private ArrayList<Task> myTasks = new ArrayList<>();

    public void setCalDate(Date date){
        calDate = date;
    }

    public Date getCalDate(){
        return calDate;
    }

    public ArrayList<Task> filterCompleted(ArrayList<Task> initTasks){
        ArrayList<Task> tasks = new ArrayList<>();
        if (PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("hideCompleted", false)){
            for (Task task : initTasks){
                if (!task.isComplete())
                    tasks.add(task);
            }
        }
        else {
            tasks = initTasks;
        }
        return tasks;
    }
    public TodoFragment(ArrayList<Task> initTasks){
        myTasks = initTasks;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_todo, null);

        mRecyclerView = view.findViewById(R.id.cardRV);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        myTasks = filterCompleted(myTasks);
        mAdapter = new TaskAdapter(myTasks);

        mRecyclerView.setAdapter(mAdapter);

        CheckTaskTouchHelper checkTaskTouchHelper = new CheckTaskTouchHelper(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mAdapter.notifyItemChanged(position);
                mAdapter.checkTask(getContext(), position, true);
            }
        };
        new ItemTouchHelper(checkTaskTouchHelper).attachToRecyclerView(mRecyclerView);



        PostponeTaskTouchHelper postponeTaskTouchHelper = new PostponeTaskTouchHelper(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                PostponeDialog dialog = new PostponeDialog(getContext(), mAdapter, position, TodoFragment.this);
                dialog.showDialog();
                mAdapter.notifyItemChanged(position);
                mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), position);
            }
        };
        new ItemTouchHelper(postponeTaskTouchHelper).attachToRecyclerView(mRecyclerView);



        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));

        //set up button
        button = view.findViewById(R.id.floatingActionButton);
        button.setOnClickListener(v -> {


            AddTaskFragment bottomSheetFragment = new AddTaskFragment();

            bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());

        });

        return view;
    }


    public void filter(String sequence){
        mAdapter.getFilter().filter(sequence);
    }

    public void insertTask(Task task){
        task.addTaskInFile(getActivity());
        if (task.getName().toLowerCase().equals("lightbulb"))
            lightbulb();
        if (calDate == null || Utils.isSameDay(calDate, task.getDate())) {
            mAdapter.insertItem(task, ((TextView)((MainActivity)getActivity()).getSearchItem().getActionView().findViewById(R.id.search_src_text)).getText().toString());
        }
    }
    private void lightbulb(){
        button.setImageResource(R.drawable.ic_lightbulb);
    }


}