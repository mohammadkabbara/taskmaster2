package com.example.taskmaster2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //////////////lab28//////////////
        AppDatabase appDatabase;
//        ArrayList<Task> tasksList = new ArrayList<Task>();
//        tasksList.add(new Task("Task1" , "This is the Task1" , "assigned"));
//        tasksList.add(new Task("Task2" , "This is the Task2" , "complete"));
//        tasksList.add(new Task("Task3" , "This is the Task3" , "   new "));
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasksDatabase").allowMainThreadQueries().build();
        List<Task> tasksList = appDatabase.taskDao().getAll();

        RecyclerView tasksListRecyclerView = findViewById(R.id.taskRecyclerView);
        tasksListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksListRecyclerView.setAdapter(new TaskAdapter((ArrayList<Task>) tasksList));

        //////////////finish lab28////////////////



        Button button2 = findViewById(R.id.addTask2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent2);
            }
        });

        Button button1 = findViewById(R.id.allTask);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, AllTasks.class);
                startActivity(intent1);
            }
        });

        Button buttonStings = findViewById(R.id.Settings);
        buttonStings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(MainActivity.this, Settings.class);
                startActivity(intent3);
            }
        });



//        Button task1 = findViewById(R.id.task1);
//        task1.setOnClickListener((view -> {
//            String taskTitle = task1.getText().toString();
//            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
//            goToTaskDetail.putExtra("tasks", taskTitle);
//            startActivity(goToTaskDetail);
//        }));
//
//        Button task2 = findViewById(R.id.task2);
//        task2.setOnClickListener((view -> {
//            String taskTitle = task2.getText().toString();
//            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
//            goToTaskDetail.putExtra("tasks", taskTitle);
//            startActivity(goToTaskDetail);
//        }));
//
//        Button task3 = findViewById(R.id.task3);
//        task3.setOnClickListener((view -> {
//            String taskTitle = task3.getText().toString();
//            Intent goToTaskDetail = new Intent(MainActivity.this , TaskDetail.class);
//            goToTaskDetail.putExtra("tasks", taskTitle);
//            startActivity(goToTaskDetail);
//        }));



    }
    @Override
    protected void onResume() {
        super.onResume();
        String usernameTasks = "’s tasks";
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = sharedPreferences.getString("username", "Your");
        TextView userTasks = findViewById(R.id.myTask);
        userTasks.setText(username+usernameTasks);
    }
}