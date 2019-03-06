package com.mobilewellbeingkit.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.KeyEvent;

import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.adapter.CardsListAdapter;
import com.mobilewellbeingkit.helper.DatabaseHelper;
import com.mobilewellbeingkit.model.Card;

import java.util.ArrayList;
import java.util.List;


public class CardsActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_CARD = 1;

    private DatabaseHelper db;
    List<Card> cards;
    RecyclerView myRV;
    SnapHelper sHelper;
    Toolbar toolbar;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    LinearLayoutManager layoutManager;

    FloatingActionButton fabCards, fabJournal, fabMenu, fabHelpful, fabSettings, customCard;

    boolean isFABOpen = false;

    boolean show_as_slide = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("onCreate()", "called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        //Set up toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  Needs a blank string or it will put name of the app up top
        getSupportActionBar().setTitle("");

        // database connection
        db = new DatabaseHelper(this);

        // initialise the arraylist of cards
        cards = new ArrayList<>();

        //if the db does not have any cards then insert the default cards with empty notes
        if(db.isDBEmpty()){
           initDefaultCards();
        }
        else {
            cards.addAll(db.getAllCards());
            Log.d("cards.addAll", "called");
        }


        SharedPreferences settings = getSharedPreferences("PREFS",0);
        show_as_slide = settings.getBoolean("display_as_slide",true);

        myRV = (RecyclerView) findViewById(R.id.recyclerview_id);
        myRV.setAdapter(new CardsListAdapter(this, cards, show_as_slide));
        int numElements = myRV.getAdapter().getItemCount();

        //Performance enhancing functions
        myRV.setItemViewCacheSize(numElements);
        myRV.setDrawingCacheEnabled(true);
        myRV.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //set display model depend on the display status
        if(show_as_slide) {
            // snaphelper centers a list item
            sHelper = new LinearSnapHelper();
            // apply snaphelper, adapter layout manager to recycler view
            sHelper.attachToRecyclerView(myRV);

            layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            myRV.setLayoutManager(layoutManager);
        }else {
            int orientation=getResources().getConfiguration().orientation;
            if(orientation== Configuration.ORIENTATION_LANDSCAPE) {
                StaggeredGridLayoutManager grid_layout_manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
                myRV.setLayoutManager(grid_layout_manager);
            }else if(orientation==Configuration.ORIENTATION_PORTRAIT){
                StaggeredGridLayoutManager grid_layout_manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                myRV.setLayoutManager(grid_layout_manager);
            }
        }


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

                    customCard=findViewById(R.id.customCard);
                    customCard.setVisibility(View.GONE);
                }else{
                    closeFABMenu();
                    customCard.setVisibility(View.VISIBLE);
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
        /*Menu system end*/

        /*
        * after click the above button, the customcard activity would start up
        * */
        FloatingActionButton customCard=findViewById(R.id.customCard);
        customCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CardsActivity.this, CustomCardActivity.class);
                startActivityForResult(i, REQUEST_ADD_CARD);
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
    * Duplicate card, favorite card
    *  Check which request it is that we're responding to and that it was successful
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("onActivityResult", "function called...");

        if (requestCode == REQUEST_ADD_CARD && resultCode == Activity.RESULT_OK) {
            Log.d("onActivityResult", "request code is 1 and okay");
            Card newCard = new Card(data.getStringExtra("newCardName"),
                    null,
                    0,
                    0,
                    data.getStringExtra("newCardImage")
            );
            // insert and get id
            long id = db.createCard(newCard);
            Log.d("onActivityResult", newCard.getCardName() + " Just Added...");
            cards = db.getAllCardsOrderedByFav();
            myRV.getAdapter().notifyItemInserted(myRV.getAdapter().getItemCount() - 1);

            // Scrolling to end of list to display new card has to be delayed for some reason
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    myRV.getLayoutManager().scrollToPosition(myRV.getAdapter().getItemCount()-1);
                }
            }, 10);
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.d("onActivityResult", "result cancelled");
        }
    }

    /**
    *  close db connection when activity changes to free up resources
    * */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("onPause db.close()", "close");

        db.close();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = myRV.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }


    @Override
    public void onResume(){
        super.onResume();
        Log.d("onResume: ", "on resume called");

        // initialise the array list of cards
        SharedPreferences settings= getSharedPreferences("PREFS",0);
        show_as_slide=settings.getBoolean("display_as_slide",true);
        cards = new ArrayList<>();
        cards.addAll(db.getAllCards());
        myRV.setAdapter(new CardsListAdapter(this, cards,show_as_slide));

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            myRV.getLayoutManager().onRestoreInstanceState(listState);
        }
    }




    /**
    *  initialise default cards if cards table is empty
    */
    private void initDefaultCards(){
        //get the Uri from R.id
        String achieve_something_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.achieve_something).toString();
        String eat_the_good_stuff_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.eat_the_good_stuff).toString();
        String embrace_the_beauty_in_nature_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.embrace_the_beauty_in_nature).toString();
        String find_a_furry_friend_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.find_a_furry_friend).toString();
        String find_comfort_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.find_comfort).toString();
        String find_my_safe_place_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.find_my_safe_place).toString();
        String get_some_help_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.get_some_help).toString();
        String have_a_good_sleep_routine_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.have_a_good_sleep_routine).toString();
        String have_a_laugh_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.have_a_laugh).toString();
        String just_breathe_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.just_breathe).toString();
        String keep_moving_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.keep_moving).toString();
        String take_care_of_myself_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.take_care_of_myself).toString();
        String take_time_out_for_me_uri=Uri.parse("android.resource://"+getApplicationContext().getPackageName()+
                "/"+R.drawable.take_time_out_for_me).toString();


        cards.add(new Card("achieve something", "", 0, 0, achieve_something_uri));
        cards.add(new Card("eat the good stuff", "", 0, 0, eat_the_good_stuff_uri));
        cards.add(new Card("embrace the beauty in nature", "", 0, 0, embrace_the_beauty_in_nature_uri));
        cards.add(new Card("find a furry friend", "", 0, 0, find_a_furry_friend_uri));
        cards.add(new Card("find comfort", "", 0, 0, find_comfort_uri));
        cards.add(new Card("find my safe place", "", 0, 0, find_my_safe_place_uri));
        cards.add(new Card("get some help", "", 0, 0, get_some_help_uri));
        cards.add(new Card("have a good sleep routine", "", 0, 0, have_a_good_sleep_routine_uri));
        cards.add(new Card("have a laugh", "", 0, 0, have_a_laugh_uri));
        cards.add(new Card("just breathe", "", 0, 0, just_breathe_uri));
        cards.add(new Card("keep moving", "", 0, 0, keep_moving_uri));
        cards.add(new Card("take care of myself", "", 0, 0, take_care_of_myself_uri));
        cards.add(new Card("take time out for me", "", 0, 0, take_time_out_for_me_uri));


        for (int i = 0; i < cards.size(); i++){
            db.createCard(cards.get(i));
            Log.d("initDefaultCards(): ", "created" + cards.get(i).getCardName() + "in table...");
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
    /*Menuing system*/
}
