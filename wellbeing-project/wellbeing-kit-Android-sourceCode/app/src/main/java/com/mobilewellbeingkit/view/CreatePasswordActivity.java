package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobilewellbeingkit.R;

public class CreatePasswordActivity extends AppCompatActivity {


    EditText textPass, confPass,security_question,security_answer;
    Button creatPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        //Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Needs a blank string or it will put name of the app up top
        getSupportActionBar().setTitle("");

        //passsword, confirmation, question, answer to their id listener's
        textPass = (EditText) findViewById(R.id.text_pass);
        confPass = (EditText) findViewById(R.id.conf_pass);
        creatPass = (Button) findViewById(R.id.creat_pass);

        security_question = (EditText)findViewById(R.id.security_question);
        security_answer = (EditText)findViewById(R.id.question_answer);


        /*
        * Creating the password,confirm password, store the typed question and answer
        * */
        creatPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textpass = textPass.getText().toString();
                String confpass = confPass.getText().toString();

                String security_question_s = security_question.getText().toString();
                String security_answer_s = security_answer.getText().toString();

                //check if the password or confirmation is null
                if(textpass.equals("")||confpass.equals("")){
                    Toast.makeText(CreatePasswordActivity.this,"No password entered!", Toast.LENGTH_SHORT).show();
                }else{
                    //check if the confirmation is same as the created password
                    if(textpass.equals(confpass)){
                        //
                        if(!security_answer_s.equals("") && !security_question_s.equals("")) {
                            SharedPreferences settings = getSharedPreferences("PREFS", 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("password", textpass);
                           
                            editor.putString("security_question", security_question_s);
                            editor.putString("security_answer", security_answer_s);
                        
                            editor.apply();

                            Intent mIntent = new Intent();
                            mIntent.putExtra("correct", "1");
                            setResult(10, mIntent);
                            finish();
                        }
                        else {
                            Toast.makeText(CreatePasswordActivity.this,"PLease input a security question!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //There is no match on the passwords
                        Toast.makeText(CreatePasswordActivity.this,"Passwords doesn't match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
