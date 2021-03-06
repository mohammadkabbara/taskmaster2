package com.example.taskmaster2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.datastore.generated.model.TaskClass;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Team;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;
//import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;




public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static PinpointManager pinpointManager;
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
//if (reciveputExtraFromAddTask == null){
    configureAmplify();
        recordEvents();

//        assignUserIdToEndpoint();
        getPinpointManager(getApplicationContext());
//}
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

        Button button2Page = findViewById(R.id.TaskDetailBtn);
        button2Page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent2 = new Intent(MainActivity.this, TaskDetail.class);
                startActivity(intent2);
            }

        });


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


        Button signOut=findViewById(R.id.logoutBtn);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amplify.Auth.signOut(
                        () -> Log.i("AuthQuickstart", "Signed out successfully"),
                        error -> Log.e("AuthQuickstart", error.toString())
                );
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        createNotificationChannelForMe();

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
//    private void configureAmplify() {
//        try {
//            Amplify.addPlugin(new AWSDataStorePlugin());
//            Amplify.addPlugin(new AWSApiPlugin());
//            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            Amplify.addPlugin(new AWSS3StoragePlugin());
//            Amplify.configure(getApplicationContext());
//            Log.i(TAG, "Initialized Amplify");
//        } catch (AmplifyException error) {
//            Log.e(TAG, "Could not initialize Amplify", error);
//        }
//    }
    private void configureAmplify() {
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSDataStorePlugin());
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));

            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
           Amplify.configure(getApplicationContext());
            Log.i("Main", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("Main", "Could not initialize Amplify", error);
       }}


//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//          String name = "mohammad";
//            String description = "kabbara";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(PushListenerService.CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }





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


    public static PinpointManager getPinpointManager (final Context applicationContext){
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i("INIT", userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            final String token = task.getResult();
                            Log.d("TAG", "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }

    //    public void assignUserIdToEndpoint() {
//        TargetingClient targetingClient = pinpointManager.getTargetingClient();
//        EndpointProfile endpointProfile = targetingClient.currentEndpoint();
//        EndpointProfileUser endpointProfileUser = new EndpointProfileUser();
//        endpointProfileUser.setUserId("UserIdValue");
//        endpointProfile.setUser(endpointProfileUser);
//        targetingClient.updateEndpointProfile(endpointProfile);
//        Log.d(TAG, "Assigned user ID " + endpointProfileUser.getUserId() +
//                " to endpoint " + endpointProfile.getEndpointId());
//    }
    private void createNotificationChannelForMe() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "abrar";
            String description = "algour";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(PushListenerService.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//    public void assignUserIdToEndpoint() {
//        TargetingClient targetingClient = pinpointManager.getTargetingClient();
//        EndpointProfile endpointProfile = targetingClient.currentEndpoint();
//        EndpointProfileUser endpointProfileUser = new EndpointProfileUser();
//        endpointProfileUser.setUserId("UserIdValue");
//        endpointProfile.setUser(endpointProfileUser);
//        targetingClient.updateEndpointProfile(endpointProfile);
//        Log.d("TAG", "Assigned user ID " + endpointProfileUser.getUserId() +
//                " to endpoint " + endpointProfile.getEndpointId());
//    }
public void recordEvents() {
    AnalyticsEvent event = AnalyticsEvent.builder()
            .name("PasswordReset")
            .addProperty("Channel", "SMS")
            .addProperty("Successful", true)
            .addProperty("ProcessDuration", 792)
            .addProperty("UserAge", 120.3)
            .build();

    Amplify.Analytics.recordEvent(event);
}




}