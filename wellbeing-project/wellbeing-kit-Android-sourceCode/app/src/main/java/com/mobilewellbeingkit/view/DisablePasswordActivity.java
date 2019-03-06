package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilewellbeingkit.R;

public class DisablePasswordActivity extends AppCompatActivity {
    EditText editText;
    Button submitBtn;
    Button forgetButton;
    String password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disable_password);

        //load the password
        SharedPreferences settings= getSharedPreferences("PREFS",0);
        password=settings.getString("password","");

        //set  passoword submit button and forget button's to their id listener's
        editText=(EditText) findViewById(R.id.password_for_journal);
        submitBtn=(Button) findViewById(R.id.password_submit);
        forgetButton=findViewById(R.id.password_journal_for);

        /*
        * When the user clicks on one of the setting buttons, start that setting activity
        */
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();

                if(text.equals(password)){
                    //enter the app
                    Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    setResult(20,intent);
                    finish();
                }else{
                    Toast.makeText(DisablePasswordActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
        * when user clicks the forget button, the forget activity will start
        * */
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

    }
}
