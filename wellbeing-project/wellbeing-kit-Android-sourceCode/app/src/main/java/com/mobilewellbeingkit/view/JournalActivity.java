package com.mobilewellbeingkit.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mobilewellbeingkit.R;
import com.mobilewellbeingkit.adapter.JournalEntryAdapter;
import com.mobilewellbeingkit.helper.DatabaseHelper;
import com.mobilewellbeingkit.model.JournalEntry;

import java.util.LinkedList;
import java.util.List;
import android.widget.ArrayAdapter;
import android.support.v7.widget.Toolbar;



public class JournalActivity extends AppCompatActivity {

    private static final String TAG = "JournalActivity";
    private static final int NEW_JOURNAL_ENTRY = 1;
    private static final int RESULT_OKAY = 1;
    private static final int RESULT_CANCELLED = 2;
    List<String> categories= new LinkedList<>();////////


    DatabaseHelper db;
    List<JournalEntry> entries;
    JournalEntryAdapter myJournalEntryAdapter;
    ArrayAdapter<String> adapter;
    Toolbar toolbar;

    ListView journalEntryList;

    FloatingActionButton fabCards, fabJournal, fabMenu, fabHelpful, fabSettings;

    boolean isFABOpen = false;
    Spinner btnPopupMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Open the database, so that we can read and write

        super.onCreate(savedInstanceState);
        /*
         *set journal activity to it id listener's
         */
        setContentView(R.layout.activity_journal);
        //All the change in the activity would store in the database
        db = new DatabaseHelper(JournalActivity.this);

        //Set up toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  Needs a blank string or it will put name of the app up top
        getSupportActionBar().setTitle("");


        //get the category data
        categories= db.getAllJournalCategory();
        //get the journal entry data
        entries = db.getAllJournalEntries();
        myJournalEntryAdapter = new JournalEntryAdapter(JournalActivity.this, R.layout.list_item_journal, entries);
        /*
         *set journal list, and journal entry to their id listener's
         */
        journalEntryList = findViewById(R.id.journalList);
        final FloatingActionButton addEntry = findViewById(R.id.newJournalEntry);
        journalEntryList.setAdapter(myJournalEntryAdapter);

        btnPopupMenu = (Spinner) findViewById(R.id.idPopupMenu);
        //final PopupMenu popupMenu = new PopupMenu(JournalActivity.this,btnPopupMenu);
        adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btnPopupMenu.setSelection(0);
        btnPopupMenu.setAdapter(adapter);



        /*
        * Delete the journal entry from the J file of the database.
        * and also get the entrycategory as the journal category
        * */
        journalEntryList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(JournalActivity.this);
                final JournalEntry j = entries.get(i);

                //Builder Debug Code
                builder.setTitle("Delete Journal Entry?");

                //
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        db.deleteJournalEntry(j.getEntryId());

                        myJournalEntryAdapter.remove(j);
                        myJournalEntryAdapter.notifyDataSetChanged();


                        if(db.getAllJournalCategory().contains(j.getEntryCategory())){


                        }else {
                           adapter.remove(j.getEntryCategory());
                            adapter.notifyDataSetChanged();
                            // check if there are no entries, if so show message
                            TextView txtViewtest2 = (TextView)findViewById(R.id.Journal_info);
                            //Toggle
                            if(entries.size()==0) {
                                txtViewtest2.setVisibility(TextView.VISIBLE);
                            }else{
                                txtViewtest2.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });
        //display the text

        TextView txtView = (TextView)findViewById(R.id.Journal_info);
        //Toggle
        if(entries.size()==0) {
            txtView.setVisibility(TextView.VISIBLE);
        }else{
            txtView.setVisibility(View.INVISIBLE);
        }

        /*
         *Tapping lets you update journal entry, when the user click the journalentrylist, the journalentryactivity would start up.
         **/
        journalEntryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                final JournalEntry j = entries.get(i);
                String entryCatagory = j.getEntryCategory();
                String entryText = j.getEntryNote();
                String entryDate = j.getEntryDate();


                Intent intent = new Intent(getApplicationContext(), JournalEntryActivity.class);
                Bundle extras = new Bundle();
                extras.putString("catagory", entryCatagory);
                extras.putString("text", entryText);
                extras.putString("date", entryDate);
                extras.putInt("indexOfListItem", i);
                intent.putExtras(extras);

                startActivityForResult(intent, NEW_JOURNAL_ENTRY);

            }
        });

        journalEntryList.setAdapter(myJournalEntryAdapter);

        /*
        * when the user clicks the add entry button, the journalentryactivity would start up.
        * */
        addEntry.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), JournalEntryActivity.class);
                startActivityForResult(intent, NEW_JOURNAL_ENTRY);
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
                    addEntry.setVisibility(View.GONE);
                }else{
                    closeFABMenu();
                    addEntry.setVisibility(View.VISIBLE);
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
                 *the pop up spinner for the categories
                 */
                btnPopupMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        entries = db.get_category_JournalEntries(adapter.getItem(position));

                        String select_item=adapter.getItem(position);
                        //checking if the category spinner is all, if it is all, it would display alll journal entries
                        if(select_item.equals("All")){
                            entries = db.getAllJournalEntries();
                            myJournalEntryAdapter = new JournalEntryAdapter(JournalActivity.this, R.layout.list_item_journal, entries);
                            journalEntryList = findViewById(R.id.journalList);

                            final FloatingActionButton addEntry = findViewById(R.id.newJournalEntry);

                            journalEntryList.setAdapter(myJournalEntryAdapter);


                        }else {

                            myJournalEntryAdapter = new JournalEntryAdapter(JournalActivity.this, R.layout.list_item_journal, entries);
                            journalEntryList = findViewById(R.id.journalList);

                            final FloatingActionButton addEntry = findViewById(R.id.newJournalEntry);

                            journalEntryList.setAdapter(myJournalEntryAdapter);


                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {


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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == NEW_JOURNAL_ENTRY)
        {
            Log.d(TAG, "XXX Request Code = NEW_JOURNAL_ENTRY");
            if(resultCode == RESULT_OK)
            {
                Log.d(TAG, "XXX RESULT_OKAY");
                String catagory = data.getStringExtra("catagory");
                String text = data.getStringExtra("text");
                String newEntry = data.getStringExtra("newEntry");
                String entry_date=data.getStringExtra("date");

                Log.d(TAG, "XXX newEntry = " + newEntry);

                if(newEntry.equals("false")) {
                    Log.d(TAG, "XXX Updating Existing Entry");

                    final JournalEntry j = entries.get(data.getIntExtra("index",0));
                    j.setEntryCategory(catagory);
                    j.setEntryNote(text);
                    db.updateJournalEntry(j);
                    myJournalEntryAdapter.notifyDataSetChanged();

                    categories= db.getAllJournalCategory();
                    adapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.spinner_item,categories);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    btnPopupMenu.setSelection(0);
                    btnPopupMenu.setAdapter(adapter);



                    if(categories.contains(catagory)) {


                    }else {
                        adapter.add(catagory);
                        adapter.notifyDataSetChanged();
                    }


                }
                // if create a new entry, note, category, entrydate, journalentry would stored in the j file of the db.
                if(newEntry.equals("true")){
                    Log.d(TAG, "XXX Creating New Entry");

                    final JournalEntry j = new JournalEntry();
                    j.setEntryNote(text);
                    j.setEntryCategory(catagory);
                    j.setEntryDate(entry_date);
                    long id = db.createJournalEntry(j);
                    j.setEntryDate(db.getJournalEntry(id).getEntryDate());

                    db = new DatabaseHelper(JournalActivity.this);
                    entries = db.getAllJournalEntries();
                    myJournalEntryAdapter = new JournalEntryAdapter(JournalActivity.this, R.layout.list_item_journal, entries);
                    journalEntryList.setAdapter(myJournalEntryAdapter);
                    myJournalEntryAdapter.notifyDataSetChanged();

                    if(categories.contains(catagory)) {


                    }else {
                        adapter.add(catagory);
                        adapter.notifyDataSetChanged();
                    }
                }


            }
        }

        TextView txtViewtest2 = (TextView)findViewById(R.id.Journal_info);
        //Toggle
        if(entries.size()==0) {
            txtViewtest2.setVisibility(TextView.VISIBLE);
        }else{
            txtViewtest2.setVisibility(View.INVISIBLE);
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
