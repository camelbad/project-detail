package com.mobilewellbeingkit.view;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.helper.DatabaseHelper;
import com.mobilewellbeingkit.model.Card;

public class ViewCardActivity extends AppCompatActivity {
    DatabaseHelper db;
    Intent i;
    TextView cardName;
    EditText cardNote;
    ImageView cardImage;
    ProgressBar progressBar;
    Card card;
    String cardImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.card_flip_in, R.anim.card_flip_out);

        setContentView(R.layout.activity_view_card);
        //connect to the db
        db = new DatabaseHelper(this);
        //set listenner
        cardName = (TextView) findViewById(R.id.lbl_name_card_back);
        cardNote = (EditText) findViewById(R.id.enter_card_note);
        cardImage = (ImageView) findViewById(R.id.img_card_back);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

        // get values passed by intent
        i = getIntent();
        card = db.getCard(i.getLongExtra("cardId", 0));
        cardName.setText(card.getCardName());
        cardNote.setText(card.getCardNote());

        cardImagePath = card.getCardImage();


        /*
        * card orientation rotate
        * */
        if(i.getStringExtra("cardName").equals("just breathe")
                ||i.getStringExtra("cardName").equals("eat the good stuff")
                ||i.getStringExtra("cardName").equals("embrace the beauty in nature")
                ||i.getStringExtra("cardName").equals("get some help")
                ||i.getStringExtra("cardName").equals("have a good sleep routine")
                ||i.getStringExtra("cardName").equals("have a laugh")
                ||i.getStringExtra("cardName").equals("take time out for me")
                ){

            // rotate the default images that need it
            Glide.with(this)
                    .load(cardImagePath)
                    .transform(new RotateTransformation(this, -90f))
                    .into(cardImage);
        } else {

            displayImage(cardImagePath);
        }


        /* Sets a listener for whenever the cardNote field contents is changed and
         * updates the corresponding values in the cards table accordingly
         * NOTE: This currently calls an SQL query every time a character is changed,
         * taking up unnecessary resources. There is almost certainly a better approach but
         * for now this works.
         */
        cardNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            // update card note on database with new string if the string has changed
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //update local card variable with new string
                card.setCardNote(cardNote.getText().toString());
                // update card note in db table
                db.updateCardNote(card);
            }
        });

        /*
        * when user clicking the card image, the CardsActivity would start up
        * */
        cardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewCardActivity.this, CardsActivity.class);
                startActivity(intent);
            }
        });



    }

    /**
    * If activity enters paused state override current animations and close database connection
    * */
    @Override
    protected void onPause() {
        overridePendingTransition(R.anim.card_flip_in, R.anim.card_flip_out);
        super.onPause();
        db.close();
    }


    /**
      * Displays the image in custom card view
      *
      * @param imagePath
     *          the image path as a String
      */
    private void displayImage(String imagePath){
        if(imagePath != null){

            progressBar.setVisibility(View.VISIBLE);

            // Utilising Glide here to load a resized image more memory efficiently.
            Glide.with(this)
                    .load(imagePath)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target,
                                                       boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(cardImage);
        }else{
            Toast.makeText(this,"Invalid image path",Toast.LENGTH_SHORT).show();
        }
    }
}


