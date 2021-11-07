package com.example.taskmaster2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button goHomeButtonSetting = findViewById(R.id.goToHome);
        goHomeButtonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goHomeSetting = new Intent(Settings.this, MainActivity.class);
                startActivity(goHomeSetting);
            }
        });

        Button saveButton = findViewById(R.id.Save);
        saveButton.setOnClickListener((View -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
            SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
            EditText usernameInput = findViewById(R.id.settingLabel);
            String username = usernameInput.getText().toString();
            sharedPreferencesEditor.putString("username",username);
            sharedPreferencesEditor.apply();
        }));
    }
}