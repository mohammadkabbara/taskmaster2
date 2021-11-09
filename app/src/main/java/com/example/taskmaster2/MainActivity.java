package com.example.taskmaster2;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Todo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureAmplify();


        List<Todo> tasks  = new ArrayList<Todo>();
        tasks=GetData();

        RecyclerView allTasksRecyclerView = findViewById(R.id.taskRecyclerView);
        allTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTasksRecyclerView.setAdapter(new TaskAdapter((ArrayList<Todo>) tasks));


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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();
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



    private void configureAmplify() {

        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }}
    private  List<Todo> GetData(){
        List<Todo> foundExpense=new ArrayList<>();

        Amplify.DataStore.query(
                Todo.class,
                queryMatches -> {
                    while (queryMatches.hasNext()) {
                        Log.i(TAG, "We got the record successfully");
                        foundExpense.add(queryMatches.next());
                    }
                },
                error -> {
                    Log.i(TAG,  "We got an error", error);
                });
        return  foundExpense;
    }
}