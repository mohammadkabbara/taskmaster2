package com.example.taskmaster2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Todo;

public class AddTask extends AppCompatActivity {
    //    AppDatabase appDatabase;
    private static final String TAG = "AddTask";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);


        Button addTaskButton = AddTask.this.findViewById(R.id.addTextTask);
        addTaskButton.setOnClickListener(view -> {

            EditText studentTitle = findViewById(R.id.editTextTaskTitle);
            String title = studentTitle.getText().toString();

            EditText Body = findViewById(R.id.editTextDescription);
            String BodyTodo = (Body.getText().toString());

            EditText State = findViewById(R.id.editTextTaskState);
            String StateTodo = (State.getText().toString());

            dataStore(title, BodyTodo, StateTodo);
            Toast messageToast = Toast.makeText(getApplicationContext(), "YOU CLICK ME!!", Toast.LENGTH_SHORT);
            messageToast.show();
            Intent goToHome = new Intent(AddTask.this, MainActivity.class);
                startActivity(goToHome);
        });

    }

    private void dataStore(String title, String body, String state) {
        Todo task = Todo.builder().title(title).state(state).body(body).build();

        // save with the datastore
        Amplify.DataStore.save(task, result -> {
            Log.i(TAG, "Task Saved");
        }, error -> {
            Log.i(TAG, "Task Not Saved");
        });
    }
}
//        EditText title = findViewById(R.id.editTextTaskTitle);
//        EditText body = findViewById(R.id.editTextDescription);
//        EditText state = findViewById(R.id.editTextTaskState);
//        button2.setOnClickListener(new View.OnClickListener(){

//            @Override
//            public void onClick(View view) {
//
//
//                Todo todo = Todo.builder()
//                        .title(title.getText().toString())
//                        .body(body.getText().toString())
//                        .state(state.getText().toString())
//                        .build();
//
//
//
//                System.out.println("/////////////////////////////////////////////////////"+
//                        title +    "/////////////////////////////////////////////////////" );
//
//
//                Amplify.API.mutate(
//                        ModelMutation.create(todo),
//                        response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
//                        error -> Log.e("MyAmplifyApp", "Create failed", error)
//                );
////                Task newTask = new Task(title.getText().toString(), body.getText().toString(), state.getText().toString());
// //               appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tasksDatabase").allowMainThreadQueries().build();
////                appDatabase.taskDao().insertAll(newTask);
//                Toast messageToast = Toast.makeText(getApplicationContext(), "YOU CLICK ME!!", Toast.LENGTH_SHORT);
//                messageToast.show();
//                Intent goToHome = new Intent(AddTask.this, MainActivity.class);
//                startActivity(goToHome);
//
//
//            }
//        });




