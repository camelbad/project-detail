package com.mobilewellbeingkit.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.adapter.ResourcesListAdapter;
import com.mobilewellbeingkit.helper.DatabaseHelper;
import com.mobilewellbeingkit.model.UserResource;

import java.util.ArrayList;
import java.util.List;

public class HelpfulContactsActivity extends AppCompatActivity{

    // Logcat tag for debugging
    private static final String TAG = "HelpfulContactsActivity";
    FloatingActionButton fabCards, fabJournal, fabMenu, fabHelpful, fabAddResource, fabSettings;
    Toolbar toolbar;
    boolean isFABOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpful_contacts);

        //Set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(""); //  Needs a blank string or it will put name of the app up top

        //Open the database, so that we can read and write
        final DatabaseHelper db = new DatabaseHelper(HelpfulContactsActivity.this);

        // Generate a list of resources from db
        final List<UserResource> resources = new ArrayList<>(db.getAllUserResources());

        // generate the helpfu;contact activity from db
        final ResourcesListAdapter myResourceAdapter = new ResourcesListAdapter(HelpfulContactsActivity.this,
                R.layout.list_item_resource, resources);

        final ListView myList = findViewById(R.id.user_links);

        myList.setAdapter(myResourceAdapter);

        registerForContextMenu(myList);

        /*
         * On long press of a list item present dialog box with option to edit or delete item.
         * all editing would display in the helpful contact activity
         */
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HelpfulContactsActivity.this);

                final UserResource r = resources.get(i);

                builder.setTitle("Edit Resource");

                final EditText input = new EditText(HelpfulContactsActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(r.getResourceText());
                builder.setView(input);

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // delete item entry from table
                        db.deleteUserResource(r.getResourceId());
                        // remove item from list
                        myResourceAdapter.remove(r);
                        // notify view adapter that list has changed
                        myResourceAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Resource Successfully Removed", Toast.LENGTH_SHORT).show();

                    }
                });

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        r.setResourceText(input.getText().toString());
                        // update list item in table
                        db.updateUserResource(r);
                        // notify view adapter that list has changed
                        myResourceAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Changes Saved", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
            }
        });


        /*
         *set fab button's to their id listener's
         */
        fabAddResource = findViewById(R.id.fabAddResource);
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
                    fabAddResource.setVisibility(View.GONE);
                }else{
                    closeFABMenu();
                    fabAddResource.setVisibility(View.VISIBLE);
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

        /*
         * Presents dialog box with option to add a new resource on add fab button press.
         */
        fabAddResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add button pressed...");
                AlertDialog.Builder builder = new AlertDialog.Builder(HelpfulContactsActivity.this);

                final UserResource r = new UserResource();

                builder.setTitle("Add Resource");
                final EditText input = new EditText(HelpfulContactsActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("eg. www.lifeline.org.au");
                builder.setView(input);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        r.setResourceText(input.getText().toString());
                        // update list item in table
                        db.createUserResource(r);
                        // notify view adapter that list has changed
                        myResourceAdapter.add(r);
                        myResourceAdapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "Resource Successfully Added",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
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
     * if menu button is pressed blow out menu collapses to specified dimensions
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
