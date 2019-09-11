package com.example.todot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

import model.Task;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        final TextInputEditText editText = findViewById(R.id.textInputEditText);
        editText.requestFocus();

        //set up button
        MaterialButton button = findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();

                Task task = new Task(editText.getText().toString(), "", cal.getTime(), 0, new ArrayList<String>());
                task.addTaskInFile(getBaseContext());

                Intent i = new Intent(AddTaskActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(i, 0);
                finish();
            }
        });
    }

}
