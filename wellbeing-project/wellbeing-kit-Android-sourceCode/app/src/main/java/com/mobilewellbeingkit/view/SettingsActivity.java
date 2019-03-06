package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


import com.mobilewellbeingkit.R;

public class SettingsActivity extends AppCompatActivity{

    // Logcat tag for debugging
    private static final String TAG = "HelpfulContactsActivity";
    FloatingActionButton fabCards, fabJournal, fabMenu, fabHelpful, fabAddResource;
    Switch passwordSwitch;
    Switch grid_model_cardview;
    Toolbar toolbar;

    boolean isFABOpen = false;
    String password_status;
    String password;

    TextView howToUse;
    boolean show_as_slide=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  Needs a blank string or it will put name of the app up top
        getSupportActionBar().setTitle("");

        howToUse = (TextView)findViewById(R.id.tutorial);
        howToUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), TutorialActivity.class);
                startActivity(i);
            }
        });

        passwordSwitch = (Switch) findViewById(R.id.switch_pass);

        //load the password_status
        SharedPreferences settings= getSharedPreferences("PREFS",0);
        password_status=settings.getString("password_status","false");
        password=settings.getString("password","");

        //the status of the password icon is default turn off
        if(password_status.equals("false")) {
            passwordSwitch.setChecked(false);
        }else if(password.equals("")){

            passwordSwitch.setChecked(false);

            SharedPreferences.Editor editor = settings.edit();
            editor.putString("password_status", "false");
            editor.apply();


        }else{
            passwordSwitch.setChecked(true);

        }


        /*
        * checking the status of the password icon is turn on or turn of in the setting screen,
        * if that is turns on, the CreatePasswordActivity would start up
        * if that is turns off, the DisablePasswordActivity activity would start up
        * */
        passwordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                     //load the password_status
                    SharedPreferences settings= getSharedPreferences("PREFS",0);

                    password=settings.getString("password","");

                    //when the user enter the password interface first time, the createpassword activity would start up
                    if(password.equals("")) {
                        Intent i=new Intent(getApplicationContext(),CreatePasswordActivity.class);
                        startActivityForResult(i, 10);
                    }
                    else {

                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password_status", "true");
                        editor.apply();
                    }
                }
                else {
                    if (password.equals("")){
                        passwordSwitch.setChecked(false);
                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password_status", "false");
                        editor.apply();
                    }
                    //
                    else {
                        Intent i = new Intent(getApplicationContext(), DisablePasswordActivity.class);
                        startActivityForResult(i, 20);
                    }

                }
            }
        });

        /*
         * grid model click listener
         */
        grid_model_cardview=findViewById(R.id.switch_grid);
        show_as_slide=settings.getBoolean("display_as_slide",true);

        //checking the grid model
        if(show_as_slide){
            grid_model_cardview.setChecked(false);
        }else{
            grid_model_cardview.setChecked(true);
        }

        /*
        * checking the grid model, if that is turns on, it would display as grid
        * */

        grid_model_cardview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    show_as_slide=false;

                    SharedPreferences settings =getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("display_as_slide", show_as_slide);
                    editor.apply();

                    grid_model_cardview.setChecked(true);

                }else{
                    show_as_slide=true;
                    SharedPreferences settings = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("display_as_slide", show_as_slide);
                    editor.apply();
                }
            }
        });

        /*
        *set fab button's to their id listener's
        */
        fabAddResource = findViewById(R.id.fabAddResource);
        fabHelpful = findViewById(R.id.helpful);
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
    }

    /**
     * Overrides the back button so that it always returns user to the main menu
     * Solves the problem of going through a bunch of other screens to get back to menu
     */
    @Override
    public void onBackPressed() {
        Intent menu = new Intent(getApplicationContext(), WellbeingHomeScreen.class);
        menu.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        menu.addCategory(Intent.CATEGORY_HOME);
        startActivity(menu);
    }

    /**
    * when user clicking the above buttons, the about activity would start up
    * */
    public void onClick(View v){
        //if(v)
        Intent i = new Intent(getApplicationContext(), AboutActivity.class);
        startActivity(i);
    }

    /**
    * save the password status, and the turn status of the password to off in the setting page
    * */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            //load the password_status
            SharedPreferences settings= getSharedPreferences("PREFS",0);
            password_status=settings.getString("password_status","false");
            password=settings.getString("password","");

            if(password_status.equals("false")) {
                passwordSwitch.setChecked(false);
            }else if(password.equals("")){

                passwordSwitch.setChecked(false);

                SharedPreferences.Editor editor = settings.edit();
                editor.putString("password_status", "false");
                editor.apply();


            }else{
                passwordSwitch.setChecked(true);

            }



          
        }
        if(resultCode==20) {
            SharedPreferences settings = getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("password_status", "false");
            editor.putString("password", "");
            editor.apply();

           passwordSwitch.setChecked(false);
        }else if (resultCode==10){
            SharedPreferences settings = getSharedPreferences("PREFS", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("password_status", "true");
            editor.apply();
            passwordSwitch.setChecked(true);
        }
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
    }
    /**
     * if menu button is pressed blow out menu collapses to specified dimensions
     */
    private void closeFABMenu(){
        isFABOpen=false;
        fabMenu.animate().rotation(0);
        fabCards.animate().translationX(0);
        fabJournal.animate().translationX(0);
        fabHelpful.animate().translationX(0);
    }

    
}