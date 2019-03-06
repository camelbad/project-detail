package com.mobilewellbeingkit.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.helper.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalEntryActivity extends AppCompatActivity {
    private static final String TAG = "JournalEntryActivity";
    Boolean newEntry = true;
    int index = -1;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_journal_entry);
        // database connection
        db = new DatabaseHelper(JournalEntryActivity.this);

        /*
         *set save, cancel, button, journalentry, dateview, emotionbank to their id listener's
         */
        final Button save = findViewById(R.id.saveEntryButton);
        final Button cancel = findViewById(R.id.cancelEntryButton);
        final EditText journalEntry = findViewById(R.id.journalText);
        final TextView dateView = findViewById(R.id.lblDateEditView);
        final Button emotionButton = findViewById(R.id.emotion_button);;

        //Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //  Needs a blank string or it will put name of the app up top
        getSupportActionBar().setTitle("");


        if(getIntent().getExtras() != null)
        {
            newEntry = false;
            Log.d(TAG, "XXX Ya boy has extras");
            String tempText = getIntent().getExtras().getString("text");
            String tempCatagory = getIntent().getExtras().getString("catagory");
            String tempDate = getIntent().getExtras().getString("date");
            index = getIntent().getExtras().getInt("indexOfListItem");
            dateView.setText(tempDate);
            journalEntry.setText(tempText);
            emotionButton.setText(tempCatagory);


        }
        else if(getIntent().getExtras() == null){
            Log.d(TAG, "XXX No extras");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date newDate = new Date();
            dateView.setText(formatter.format(newDate));

        }
        else{
            Log.d(TAG, "XXX Help");

        }

        /*
         *When the user clicks on emotion buttons, start that journalentryactivity and expandablelistactivity
         */
        emotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent toy = new Intent(JournalEntryActivity.this, ExpandableListActivity. class);
                startActivityForResult(toy,10);
            }
        });


        /*
         *when user clicking the cancel button, return the previous page
         */
        cancel.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED,returnIntent);
                finish();
            }
        });


        /*
        * When the journalentryactivity run in the system, the user should choose the emotion bank category form the emotion bank.
        * */
        save.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checking if tthat is the new journal entry
                if(newEntry){
                    //checking if the user select the emotion category
                    if(emotionButton.getText().toString().equals("")){
                        Toast.makeText(JournalEntryActivity.this,"Please select an emotion category from the emotion bank",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("catagory", emotionButton.getText().toString());
                        returnIntent.putExtra("text", journalEntry.getText().toString());
                        returnIntent.putExtra("newEntry", "true");
                        returnIntent.putExtra("date", dateView.getText().toString());
                        setResult(Activity.RESULT_OK, returnIntent);


                        finish();
                    }
                }
                if(!newEntry)
                {
                    if(emotionButton.getText().toString().equals("")){
                        Toast.makeText(JournalEntryActivity.this,"Please select an emotion category from the emotion bank",Toast.LENGTH_SHORT).show();
                    }else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("catagory", emotionButton.getText().toString());
                        returnIntent.putExtra("text", journalEntry.getText().toString());
                        returnIntent.putExtra("newEntry", "false");
                        returnIntent.putExtra("index", index);
                        returnIntent.putExtra("date", dateView.getText().toString());
                        setResult(Activity.RESULT_OK, returnIntent);


                        finish();
                    }
                }
            }
        });
    }

  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        String emotion_word;
        if(data==null){
            emotion_word="";
        }else {
           emotion_word = data.getStringExtra("emotion");
        }
        if(requestCode == 10){
            Button emotionButton = findViewById(R.id.emotion_button);
            emotionButton.setText(emotion_word);
        }


    }


}
