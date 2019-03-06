package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.mobilewellbeingkit.R;

public class SplashActivity extends AppCompatActivity {

    String password;
    String password_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //load the password
        SharedPreferences settings = getSharedPreferences("PREFS",0);
        password = settings.getString("password","");
        Handler handler = new Handler();

        //load the password_status
        password_status = settings.getString("password_status","false");

       // checking the password status, if that is turn off, the journal activity would startup.
        if(password_status.equals("false")) {
            Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
            startActivity(intent);
            finish();
        }else {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (password.equals("")) {
                        //if there is no password， the createpasswordactivity would start up
                        Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //if there is a password, the enterpasswordactivity would start up
                        Intent intent = new Intent(getApplicationContext(), EnterPasswordActivity.class);
                        startActivity(intent);
                        finish();

                    }

                }
            }, 1);
        }
    }
}
