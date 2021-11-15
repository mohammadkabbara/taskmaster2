package com.example.taskmaster2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.amplifyframework.datastore.generated.model.TaskClass;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;




public class MainActivity extends AppCompatActivity {

    private TaskAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String enteredName = sharedPreferences.getString("EnteredText","Write the name");


        String Team = sharedPreferences.getString("Team","noTeam");
//        System.out.println(enteredName);
//        System.out.println("-------------------------------------------------------------------");
//        System.out.println(Team);

        String reciveputExtraFromAddTask= getIntent().getStringExtra("STRING_I_NEED");
        TextView personTasks = findViewById(R.id.Message);
        personTasks.setText(enteredName + "'s Tasks");

        ////////
if (reciveputExtraFromAddTask == null){
    configureAmplify();
}
////////

        creatTeams();


        RecyclerView allTasksRecyclerView = findViewById(R.id.TaskID);
        List<TaskClass> tasks= new ArrayList<>();
        if(Team.equals("noTeam")){
            tasks = GetData(allTasksRecyclerView);
        }
        else{
            tasks = GetData2(allTasksRecyclerView);
        }
        Log.i("BLAAAAAAAA",tasks.toString());
        allTasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allTasksRecyclerView.setAdapter(new TaskAdapter(tasks));




        Button button2Page1 = findViewById(R.id.Button3);
        button2Page1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(MainActivity.this, AddTask.class);
                startActivity(intent2);
            }

        });


        SharedPreferences.Editor editor = sharedPreferences.edit();


        Button buttonSettings = findViewById(R.id.TaskViewer);
        buttonSettings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent3 = new Intent(MainActivity.this, Settings.class);
                startActivity(intent3);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String enteredName = sharedPreferences.getString("EnteredText","Write the name");


        String Team = sharedPreferences.getString("Team","noTeam");


        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println(enteredName);

        TextView tasks = findViewById(R.id.Message);
        tasks.setText(enteredName + "'s Tasks");
    }

    private void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("Main", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("Main", "Could not initialize Amplify", error);
        }}

    private  List<TaskClass> GetData( RecyclerView allTaskDataRecyclerView ){
        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                allTaskDataRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
        List<TaskClass> foundTask=new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.TaskClass.class),
                response -> {
                    for (com.amplifyframework.datastore.generated.model.TaskClass todo : response.getData()) {
                        foundTask.add(todo);
                        foundTask.toString();
                        Log.i("MyAmplifyApp", foundTask.toString());
                        Log.i("MyAmplifyApp", "Successful query, found posts.");
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        return  foundTask;
    }



    private  List<TaskClass> GetData2( RecyclerView allTaskDataRecyclerView ){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String Team = sharedPreferences.getString("Team","noTeam");
        System.out.println("-------------------------------------------------------------------");
        System.out.println(Team);
        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                allTaskDataRecyclerView.getAdapter().notifyDataSetChanged();
                return false;
            }
        });

        List<TaskClass> foundTask=new ArrayList<>();
        Amplify.API.query(
                ModelQuery.list(TaskClass.class,TaskClass.TEAM_ID.contains(Team)),
                response -> {
                    for (TaskClass todo : response.getData()) {
                        foundTask.add(todo);
                        foundTask.toString();
                        Log.i("MyAmplifyApp", foundTask.toString());
                        Log.i("MyAmplifyApp", "Successful query, found posts.");
                    }
                    handler.sendEmptyMessage(1);
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        return  foundTask;
    }
    private void creatTeams(){
        AtomicBoolean x= new AtomicBoolean(false);
        Amplify.API.query(
                ModelQuery.list(Team.class),
                response -> {
                    if(response.getData().getRequestForNextResult()==null){
                        System.out.println("alooooolllllllllllllllllllllll");
                        System.out.println(response.getData().getRequestForNextResult());
                        x.set(true);
                        Log.i("Teams", "Successful query, found teams.");
                    }
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );
        if(x.equals(false)){
            Team todo1 = Team.builder()
                    .name("Team 1").id("1")
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(todo1),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );
            Team todo2 = Team.builder()
                    .name("Team 2").id("2")
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(todo2),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );
            Team todo3 = Team.builder()
                    .name("Team 3").id("3")
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(todo3),
                    response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                    error -> Log.e("MyAmplifyApp", "Create failed", error)
            );
        } }


}