package com.example.taskmaster2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;


public class SignIn extends AppCompatActivity {

    private static final String TAG = "SignInActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        recordEvent();
        Button signIn = findViewById(R.id.btnlogin);
        EditText username = findViewById(R.id.emailLgn);
        EditText password = findViewById(R.id.passwordLgn);
        TextView createNewAccount = findViewById(R.id.createNewAcc);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor preferenceEditor = preferences.edit();

        signIn.setOnClickListener(view -> {
            signIn(username.getText().toString(), password.getText().toString());


            preferenceEditor.putString("userNameAPI",username.getText().toString());
            preferenceEditor.apply();

        });

        createNewAccount.setOnClickListener(view -> {
            Intent goToSignUp = new Intent(SignIn.this, SignUpActivity.class);
            startActivity(goToSignUp);
        });
    }

    void signIn(String username, String password) {
        Amplify.Auth.signIn(
                username, password, success -> {
                    Log.i(TAG, "signIn succeed " + success.toString());
                    Intent goToMain = new Intent(SignIn.this, MainActivity.class);
                    startActivity(goToMain);
                },
                error -> Log.e(TAG, "signInfailed" + error.toString()));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    private void recordEvent(){
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("Launch Join activity")
                .addProperty("Channel", "SMS")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .addProperty("UserAge", 120.3)
                .build();

        Amplify.Analytics.recordEvent(event);
    }
}