package com.example.todot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;

import model.Task;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        final EditText editText = findViewById(R.id.textInputEditText);
        editText.requestFocus();

        //set up button
        Button button = findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                Task task = new Task(editText.getText().toString(), "", cal.getTime(), 0, new ArrayList<String>());
                task.addTaskInFile(getBaseContext());

                Intent i = new Intent(AddTaskActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}
