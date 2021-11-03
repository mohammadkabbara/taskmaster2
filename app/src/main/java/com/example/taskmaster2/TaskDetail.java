package com.example.taskmaster2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class TaskDetail extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        String title = intent.getExtras().getString("taskNameClickListener");
        TextView taskTitleView = findViewById(R.id.taskTitleId);
        taskTitleView.setText("taskTitle:  " + title );
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        Intent intent = getIntent();
//        String taskName = intent.getExtras().getString("tasks");
//        TextView taskTitle = findViewById(R.id.taskTitleId);
//        taskTitle.setText(taskName);
//    }

}