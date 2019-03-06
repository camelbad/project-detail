package com.mobilewellbeingkit.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.mobilewellbeingkit.R;
import android.support.design.widget.FloatingActionButton;

import android.view.View;
import android.widget.TextView;


public class WellbeingHomeScreen extends AppCompatActivity  {

    /**
     * initialise FloatingActionButton's for the menu system (one for each menu)
     */
    FloatingActionButton cardsButton, journalButton, helpfulButton, settingsButton, aboutButton;
    TextView cardsText, journalText, helpfulText, settingsText, aboutText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wellbeing_home_screen);

        checkIsFirstRun();

        /*
         * initialise FloatingActionButton's to their respective buttons by grabbing the buttons id from the 'wellbeing_home_screen.xml'
         */
        cardsButton = findViewById(R.id.menuButtonCards);
        cardsText = findViewById(R.id.menuTextCards);
        journalButton = findViewById(R.id.menuButtonJournal);
        journalText = findViewById(R.id.menuTextJournal);
        helpfulButton = findViewById(R.id.menuButtonHelpful);
        helpfulText = findViewById(R.id.menuTextHelpfuContacts);
        aboutButton = findViewById(R.id.menuButtonAbout);
        aboutText = findViewById(R.id.menuTextAbout);
        settingsButton = findViewById(R.id.menuButtonSetings);
        settingsText = findViewById(R.id.menuTextSettings);

        cardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(i);
            }
        });

        cardsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), CardsActivity.class);
                startActivity(i);
            }
        });

        /*
         * When the user clicks on one of the following buttons, start that buttons activity (based on buttons id, text and image)
         */
        journalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(i);
            }
        });

        journalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(i);
            }
        });

        helpfulButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HelpfulContactsActivity.class);
                startActivity(i);
            }
        });

        helpfulText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), HelpfulContactsActivity.class);
                startActivity(i);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
            }
        });

        aboutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(i);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

        settingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });
    }

    /**
     * Check's if it is the application's first time running
     * If it is, the application will create a dialog prompting the user to view a tutorial
     */
    private void checkIsFirstRun(){
        //Check for first run and prompt user to view tutorial if so
        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isfirstrun", true);

        if(isFirstRun){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("View Tutorial?");
            builder.setMessage("Would you like to view an explanation of this application's features? \nThis can be viewed any time in the Settings Menu");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                    Intent i = new Intent(getApplicationContext(), TutorialActivity.class);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isfirstrun", false).commit();

    }

}
