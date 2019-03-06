package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;

import com.mobilewellbeingkit.R;

public class AboutActivity extends AppCompatActivity {

    FloatingActionButton fabSettings;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         *set fab button's to their id listener's
         */
        setContentView(R.layout.activity_about_application);
        fabSettings = findViewById(R.id.settings);
        /*
         * When the user clicks on one of the following buttons, start that buttons activity (based on buttons id, text and image)
         */
        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(i);
            }
        });

        /*
         * This is the function of icon link to the website page to order physical kit,
         * user can click the icon of the clarence to link to the website
         */
        ImageView logocity = (ImageView)findViewById(R.id.image_view);
        logocity.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.liveclarence.com.au/talk-to-us/"));
                startActivity(intent);
            }
        });


    }
}
