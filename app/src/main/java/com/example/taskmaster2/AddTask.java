package com.example.taskmaster2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.TaskClass;


public class AddTask extends AppCompatActivity {
    //    AppDatabase appDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
//        Button addToDbButton = findViewById(R.id.AddToDB);

        Button addToDbButton = findViewById(R.id.AddToDB);
        addToDbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){


                EditText titleField = findViewById(R.id.Field1ID);
                String title = titleField.getText().toString();

                EditText bodyField = findViewById(R.id.Field2ID);
                String body = bodyField.getText().toString();

                EditText stateField = findViewById(R.id.Field3ID);
                String state = stateField.getText().toString();


                RadioButton b1=findViewById(R.id.radioButton1);
                RadioButton b2=findViewById(R.id.radioButton2);
                RadioButton b3=findViewById(R.id.radioButton3);


                String id = null;
                if(b1.isChecked()){
                    id="1";
                }
                else if(b2.isChecked()){
                    id="2";
                }
                else if(b3.isChecked()){
                    id="3";
                }

                dataStore(title, body, state,id);
                System.out.println(  "Task ID is " + title
                );

                Intent intent = new Intent(AddTask.this, MainActivity.class);
                startActivity(intent);

            }

        });

    }

    private void dataStore(String title, String body, String state,String id) {
        TaskClass task = TaskClass.builder().teamId(id).title(title).body(body).state(state).build();


        Amplify.API.mutate(ModelMutation.create(task),succuess-> {
            Log.i("Add Task", "Saved to DYNAMODB");
        }, error -> {
            Log.i("Add Task", "error saving to DYNAMODB");
        });

    }

}