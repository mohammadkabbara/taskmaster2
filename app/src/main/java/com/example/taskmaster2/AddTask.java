package com.example.taskmaster2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Todo;

public class AddTask extends AppCompatActivity {
    //    AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Button addTaskButton = findViewById(R.id.buttonAddTask);
        EditText title = findViewById(R.id.editTextTaskTitle);
        EditText body = findViewById(R.id.editTextDescription);
        EditText state = findViewById(R.id.editTextTaskState);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {

                Todo todo = Todo.builder()
                        .title(title.getText().toString())
                        .body(body.getText().toString())
                        .state(state.getText().toString())
                        .build();
                Log.i("MyAmplifyApp", "onClick: " + todo.getTitle());



                Amplify.API.mutate(
                        ModelMutation.create(todo),
                        response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );

                Toast.makeText(getApplicationContext(), "submitted!", Toast.LENGTH_SHORT).show();
                Intent goToHome = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToHome);

//                Task newTask = new Task(title.getText().toString(), body.getText().toString(), state.getText().toString());
//                appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasksDatabase").allowMainThreadQueries().build();
//                appDatabase.taskDao().insertAll(newTask);

            }
        });

        //        Button homeButton = findViewById(R.id.homeAddTask);
//        homeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View V) {
//                Intent goToHome = new Intent(AddTask.this, MainActivity.class);
//                startActivity(goToHome);
//            }
//        });

    }
}