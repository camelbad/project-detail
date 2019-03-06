package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilewellbeingkit.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView security_question_stored;
    EditText security_answer_inputU;
    Button submit_answer;
    String security_question_SPrf;
    String security_answer_SPrf;
    String security_answer_input_user;

    /*
     *load the existing security question and display
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_question);

        //set stored question and input question, and submit answer to their id listener's
        security_question_stored= findViewById(R.id.security_question_r);
        security_answer_inputU=findViewById(R.id.security_answer_input);
        submit_answer=findViewById(R.id.submit_security_answer);
        // load the stored question
        SharedPreferences settings= getSharedPreferences("PREFS", 0);

        //load the security question and answer
        security_question_SPrf=settings.getString("security_question",null);
        security_answer_SPrf=settings.getString("security_answer",null).toLowerCase();

        //the stored security question displays in the screen
        security_question_stored.setText(security_question_SPrf);

        /*
        *checking the input answer, if that is same as the stored answer, the create password activity would start,
        * if that is not correct, the error would display
        * */
        submit_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                security_answer_input_user=security_answer_inputU.getText().toString().toLowerCase();
                //checking if the input answer is correct
                if(security_answer_input_user.equals(security_answer_SPrf)){
                    Intent intent = new Intent(getApplicationContext(), CreatePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(ForgotPasswordActivity.this, "Wrong answer!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}
