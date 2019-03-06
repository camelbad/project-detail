package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;

import com.mobilewellbeingkit.R;


public class ChangePasswordActivity extends AppCompatActivity {
    EditText passWord_old;
    EditText passWord_new_input_1;
    EditText passWord_new_input_2;
    Button submit_new_passWord;
    Button forgot_passWord;
    String password_storded;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        //set old password, new password, confirmation to their id listener'z
        SharedPreferences settings= getSharedPreferences("PREFS",0);
        password_storded=settings.getString("password","");
        passWord_old=findViewById(R.id.old_password);
        passWord_new_input_1=findViewById(R.id.new_password);
        passWord_new_input_2=findViewById(R.id.confirm_password);
        submit_new_passWord=findViewById(R.id.submit_password_change);


        //When the user clicks the submit new password button, start that journal activity
        submit_new_passWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password_old_input_s=passWord_old.getText().toString();

                //check the old question
                if(password_old_input_s.equals(password_storded)){



                    String password_new_input=passWord_new_input_1.getText().toString();
                    //save the new status of the setting acticity and input new password
                    SharedPreferences settings =getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("password", password_new_input);
                    editor.apply();


                    //enter the app
                    Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        /*
         *when user clicks the forget button, the forget activity will start
         * */
        forgot_passWord=findViewById(R.id.forget_password);
        forgot_passWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //enter the app
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);
                finish();

            }
        });



    }
}
