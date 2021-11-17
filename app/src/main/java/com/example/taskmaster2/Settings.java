package com.example.taskmaster2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.HashMap;
import java.util.Map;


public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();


        findViewById(R.id.SaveButton).setOnClickListener(view -> {

            TextView text = findViewById(R.id.EnteredName);

            String name =text.getText().toString();

////////////////////////////////////////////////////

            RadioButton b1=findViewById(R.id.radioButtonSet1);
            RadioButton b2=findViewById(R.id.radioButtonSet2);
            RadioButton b3=findViewById(R.id.radioButtonSet3);


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

            editor.putString("Team",id);
            editor.putString("EnteredText",name);
            editor.apply();
//            Context context = getApplicationContext();
//            Toast.makeText(context, "Saved!", Toast.LENGTH_LONG).show();
//

        });

    }
}