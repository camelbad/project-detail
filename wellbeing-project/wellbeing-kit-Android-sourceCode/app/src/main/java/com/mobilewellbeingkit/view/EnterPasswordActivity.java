package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.mobilewellbeingkit.R;


public class EnterPasswordActivity extends AppCompatActivity {

    EditText enterPass;
    Button submitPass;
    Button forgetButton;
    String password;

    FloatingActionButton fabCards, fabJournal, fabMenu, fabHelpful, fabSettings;

    boolean isFABOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_lockscreen);

        //load the password
        SharedPreferences settings= getSharedPreferences("PREFS",0);
        password=settings.getString("password","");

        //set  passoword submit button and forget button's to their id listener's
        enterPass =(EditText) findViewById(R.id.enter_pass);
        submitPass=(Button) findViewById(R.id.sub_pass);
        forgetButton=findViewById(R.id.forget_password_button);

        /*
        * if the user typed the correct answer, start journal activity,
        * if that is wrong, show the error message
        * */
        submitPass.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String text = enterPass.getText().toString();
                //check if the typed password equal the stored password
                if(text.equals(password)){
                    //enter the app
                    Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(EnterPasswordActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        // when user clicks the forget button, the forget activity will start
        forgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

        /*
         *set fab button's to their id listener's
         */
        fabHelpful = findViewById(R.id.helpful);
        fabSettings = findViewById(R.id.settings);
        fabJournal = findViewById(R.id.journal);
        fabCards = findViewById(R.id.cards);
        fabMenu = findViewById(R.id.bMenu);
        fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        /*
         * When the user clicks on one of the following buttons, start that buttons activity (based on buttons id, text and image)
         */
        fabJournal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(i);
                closeFABMenu();
            }
        });
        fabHelpful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HelpfulContactsActivity.class);
                startActivity(i);
                closeFABMenu();
            }
        });
        fabCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(i);
                closeFABMenu();
            }
        });
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
                closeFABMenu();
            }
        });


    }

    /**
     * if menu button is pressed blow out menu to specified dimensions
     */
    private void showFABMenu(){
        isFABOpen=true;
        fabMenu.animate().rotation(90);
        fabCards.animate().translationX(-getResources().getDimension(R.dimen.standard_neg75));
        fabJournal.animate().translationX(-getResources().getDimension(R.dimen.standard_neg150));
        fabHelpful.animate().translationX(-getResources().getDimension(R.dimen.standard_neg225));
        fabSettings.animate().translationY(-getResources().getDimension(R.dimen.standard_pos75));
    }
    /**
     * if menu button is pressed close blow out menu to home position
     */
    private void closeFABMenu(){
        isFABOpen=false;
        fabMenu.animate().rotation(0);
        fabCards.animate().translationX(0);
        fabJournal.animate().translationX(0);
        fabHelpful.animate().translationX(0);
        fabSettings.animate().translationY(0);
    }
}
