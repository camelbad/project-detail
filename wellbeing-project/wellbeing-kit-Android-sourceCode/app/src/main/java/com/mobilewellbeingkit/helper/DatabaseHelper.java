package com.mobilewellbeingkit.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mobilewellbeingkit.model.Card;
import com.mobilewellbeingkit.model.JournalEntry;
import com.mobilewellbeingkit.model.UserResource;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "wellbeingdb";

    // Table Names
    private static final String TABLE_CARD = "cards";
    private static final String TABLE_JOURNAL_ENTRY = "journal_entries";
    private static final String TABLE_USER_RESOURCES = "user_resources";


    // Common column names
    private static final String KEY_ID = "id";

    // CARD Table - column names
    private static final String KEY_CARD_NAME = "card_name";
    private static final String KEY_CARD_IMAGE = "card_image";
    private static final String KEY_CARD_NOTE = "card_note";
    private static final String KEY_CARD_FAVOURITE = "card_favourite";
    private static final String KEY_CARD_DUPLICATE = "card_duplicate";

    // JOURNAL_ENTRY Table - column names
    private static final String KEY_ENTRY_DATE = "entry_date";
    private static final String KEY_ENTRY_NOTE = "entry_note";
    private static final String KEY_ENTRY_CATEGORY = "entry_category";

    // USER_RESOURCES Table - column names
    private static final String KEY_RESOURCE_TEXT = "resource_text";


    //Table Create Statements
    /* CARD table create statement
     *  note: SQLite does not support boolean column types, instead uses an integer,
     *  0 = false, 1 = true. */
    private static final String CREATE_TABLE_CARD = "CREATE TABLE "
            + TABLE_CARD + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_CARD_NAME + " TEXT,"
            + KEY_CARD_NOTE + " TEXT,"
            + KEY_CARD_IMAGE + " TEXT,"//modified
            + KEY_CARD_FAVOURITE + " INTEGER,"
            + KEY_CARD_DUPLICATE + " INTEGER"
            + ")";

    /* JOURNAL_ENTRY table create statement
     * SQLite does not support DATETIME format, instead allows for them to be stored
     * as an Integer, Real or Text type. Functions for converting input to correct
     * string format. See www.sqlite.org/lang_datefunc.html
     */
    private static final String CREATE_TABLE_JOURNAL_ENTRY = "CREATE TABLE "
            + TABLE_JOURNAL_ENTRY + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_ENTRY_CATEGORY + " TEXT,"
            + KEY_ENTRY_DATE + " TEXT ,"////////////////
            //+ KEY_ENTRY_CATEGORY+"TEXT,"
            + KEY_ENTRY_NOTE + " TEXT"
            + ")";

    // USER_RESOURCES table create statement
    private static final String CREATE_TABLE_USER_RESOURCES = "CREATE TABLE "
            + TABLE_USER_RESOURCES + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_RESOURCE_TEXT + " TEXT"
            + ")";



    /*constructor*/
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_CARD);
        db.execSQL(CREATE_TABLE_JOURNAL_ENTRY);
        db.execSQL(CREATE_TABLE_USER_RESOURCES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL_ENTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_RESOURCES);

        // create new tables
        onCreate(db);
    }



    /* CRUD (Create, Read, Update and Delete) Operations
     ****************************************************
     *creat a new card and insert it into database
     * Create a card
	 *@para card  a new card
	 *@return insert card into the database
     */
    public long createCard(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_NAME, card.getCardName());
        values.put(KEY_CARD_NOTE, card.getCardNote());
        values.put(KEY_CARD_IMAGE, card.getCardImage());
        values.put(KEY_CARD_FAVOURITE, card.getFavourite());
        values.put(KEY_CARD_DUPLICATE, card.getDuplicate());

        // insert row
        return db.insert(TABLE_CARD, null, values);
    }

    /**
     * Return a single card from the CARD table
	 *@param card_id the id of the card
	 *@return card return the card with given card_id
     */
    public Card getCard(long card_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_CARD + " WHERE "
                + KEY_ID + " = " + card_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Card cd = new Card();
        cd.setCardId(c.getInt(c.getColumnIndex(KEY_ID)));
        cd.setCardName(c.getString(c.getColumnIndex(KEY_CARD_NAME)));
        cd.setCardNote(c.getString(c.getColumnIndex(KEY_CARD_NOTE)));
        cd.setCardImage(c.getString(c.getColumnIndex(KEY_CARD_IMAGE)));//midified
        cd.setFavourite(c.getInt(c.getColumnIndex(KEY_CARD_FAVOURITE)));
        cd.setDuplicate(c.getInt(c.getColumnIndex(KEY_CARD_DUPLICATE)));


        return cd;
    }


    /**
     * Return ALL cards from CARD table
	 *@return List<card> return all cards in the database
     */
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<Card>();
        String selectQuery = "SELECT  * FROM " + TABLE_CARD + " ORDER BY " + KEY_CARD_FAVOURITE + " DESC";

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Card cd = new Card();
                cd.setCardId(c.getInt((c.getColumnIndex(KEY_ID))));
                cd.setCardName((c.getString(c.getColumnIndex(KEY_CARD_NAME))));
                cd.setCardNote(c.getString(c.getColumnIndex(KEY_CARD_NOTE)));
                cd.setCardImage(c.getString(c.getColumnIndex(KEY_CARD_IMAGE)));//modified
                cd.setFavourite(c.getInt(c.getColumnIndex(KEY_CARD_FAVOURITE)));
                cd.setDuplicate(c.getInt(c.getColumnIndex(KEY_CARD_DUPLICATE)));

                // adding to card list
                cards.add(cd);
            } while (c.moveToNext());
        }

        return cards;
    }


    /**
     * Return ALL cards from CARD table and sort by favourite
	 *@return List<Card> return ALL cards from CARD table and sort by favourite
     */
    public List<Card> getAllCardsOrderedByFav() {
        List<Card> cards = new ArrayList<Card>();
        String selectQuery = "SELECT  * FROM " + TABLE_CARD;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Card cd = new Card();
                cd.setCardId(c.getInt((c.getColumnIndex(KEY_ID))));
                cd.setCardName((c.getString(c.getColumnIndex(KEY_CARD_NAME))));
                cd.setCardNote(c.getString(c.getColumnIndex(KEY_CARD_NOTE)));
                cd.setCardImage(c.getString(c.getColumnIndex(KEY_CARD_IMAGE)));//modified
                cd.setFavourite(c.getInt(c.getColumnIndex(KEY_CARD_FAVOURITE)));
                cd.setDuplicate(c.getInt(c.getColumnIndex(KEY_CARD_DUPLICATE)));

                // adding to card list
                cards.add(cd);
            } while (c.moveToNext());
        }

        return cards;
    }


    /**
     * Delete a card by id
	 *@param id the id of the card
     */
    public void delete_Image_By_id(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CARD , KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }







    /**
     * Update a card note
	 *@param card the card with new note
	 *@return int the id of the card 
     */
    public int updateCardNote(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "updateCardNote...");

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_NOTE, card.getCardNote());

        // updating row
        return db.update(TABLE_CARD, values, KEY_ID + " = ?",
                new String[] { String.valueOf(card.getCardId()) });

    }


    /**
     * Update card favourite
	 *@param card the card with new favourite status
     */
    public void updateCardFav(Card card) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.e(LOG, "updateCardFav...");

        ContentValues values = new ContentValues();
        values.put(KEY_CARD_FAVOURITE, card.getFavourite());

        // updating row
        db.update(TABLE_CARD, values, KEY_ID + " = ?",
                new String[] { String.valueOf(card.getCardId()) });
    }



    /**
     * Create a Journal Entry
	 *@param entry the new journal entry
	 *@return the long id of the journal entry
     */
    public long createJournalEntry(JournalEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ENTRY_CATEGORY, entry.getEntryCategory());
        values.put(KEY_ENTRY_NOTE, entry.getEntryNote());
        values.put(KEY_ENTRY_DATE, entry.getEntryDate());//////////////////////

        // insert row
        long entry_id = db.insert(TABLE_JOURNAL_ENTRY, null, values);

        return entry_id;
    }

    /**
     * Return a single journal entry from the JOURNAL_ENTRY table
	 *@param entry_id the id of the journal entry
	 *@return JournalEntry the journal entry with certain id 
     */
    public JournalEntry getJournalEntry(long entry_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_JOURNAL_ENTRY + " WHERE "
                + KEY_ID + " = " + entry_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        JournalEntry je = new JournalEntry();
        je.setEntryId(c.getInt(c.getColumnIndex(KEY_ID)));
        je.setEntryDate(c.getString(c.getColumnIndex(KEY_ENTRY_DATE)));
        je.setEntryCategory(c.getString(c.getColumnIndex(KEY_ENTRY_CATEGORY)));
        // TODO: Release 2. je.setEntryCategory(c.getString(c.getColumnIndex(KEY_ENTRY_FEELING_ID)));
        je.setEntryNote(c.getString(c.getColumnIndex(KEY_ENTRY_NOTE)));



        return je;
    }

    /**
     * Return all journal entries from the JOURNAL_ENTRY table
	 *@return Return all journal entries from the JOURNAL_ENTRY table
     */
    public ArrayList<JournalEntry> getAllJournalEntries() {
        ArrayList<JournalEntry> entries = new ArrayList<JournalEntry>();
        String selectQuery = "SELECT  * FROM " + TABLE_JOURNAL_ENTRY;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                JournalEntry j = new JournalEntry();
                j.setEntryId(c.getInt((c.getColumnIndex(KEY_ID))));
                j.setEntryDate(c.getString(c.getColumnIndex(KEY_ENTRY_DATE)));
                j.setEntryCategory(c.getString(c.getColumnIndex(KEY_ENTRY_CATEGORY)));
                // TODO: Release 2. j.setEntryCategory(c.getString(c.getColumnIndex(KEY_ENTRY_FEELING_ID)));
				
				
                j.setEntryNote(c.getString(c.getColumnIndex(KEY_ENTRY_NOTE)));

                // adding to entries list
                entries.add(j);
            } while (c.moveToNext());
        }
        return entries;
    }
    /**
     * Return all existing categories from the JOURNAL_ENTRY table
	 *@return Return all  existing categories  from the JOURNAL_ENTRY table
     */
    public ArrayList<String> getAllJournalCategory(){

        ArrayList<String> journal_categories = new ArrayList<String>();
        String category_query="SELECT  * FROM " + TABLE_JOURNAL_ENTRY;
        HashMap<String,Integer> hashMap_category= new HashMap<>();

        journal_categories.add("All");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(category_query, null);
        if (c.moveToFirst()) {
            do {
                String category_tem=c.getString(c.getColumnIndex(KEY_ENTRY_CATEGORY));


                if(hashMap_category.containsKey(category_tem)) {
                    hashMap_category.put(category_tem, 1) ;
                }
                else {
                    hashMap_category.put(category_tem, 1);
                }
            } while (c.moveToNext());
        }

        Iterator iterator = hashMap_category.keySet().iterator();
        while (iterator.hasNext()) {
            String category_out = (String) iterator.next();
            journal_categories.add(category_out);

        }


        return journal_categories;

    }



    /**
     * Return certain category journal entries from the JOURNAL_ENTRY table
	 *@param category the category chosen by user
	 *@return Return all journal entries which has given category
     */
    public ArrayList<JournalEntry> get_category_JournalEntries(String category) {
        ArrayList<JournalEntry> entries = new ArrayList<JournalEntry>();

        String selectQuery1 = "SELECT  * FROM " + TABLE_JOURNAL_ENTRY + " WHERE "
                + KEY_ENTRY_CATEGORY + " = "  + category;

        // String selectQuery = "SELECT  * FROM " + TABLE_JOURNAL_ENTRY + " WHERE "
        //       + KEY_ID + " = " + entry_id;

        String selectQuery="SELECT * FROM journal_entries where entry_category = '"+category+"'";



        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery("SELECT * FROM journal_entries where entry_category = '%category%' ", null);
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                JournalEntry j = new JournalEntry();
                j.setEntryId(c.getInt((c.getColumnIndex(KEY_ID))));
                j.setEntryDate(c.getString(c.getColumnIndex(KEY_ENTRY_DATE)));
                j.setEntryCategory(c.getString(c.getColumnIndex(KEY_ENTRY_CATEGORY)));
                j.setEntryNote(c.getString(c.getColumnIndex(KEY_ENTRY_NOTE)));

                // adding to entries list
                entries.add(j);
            } while (c.moveToNext());
        }
        return entries;
    }



    /**
     * Update a journal entry
	 *@param entry journal entry with new note
	 *@return the integer id of the journal entry
     */
    public int updateJournalEntry(JournalEntry entry) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // TODO: Release 2. values.put(KEY_ENTRY_FEELING_ID, entry.getEntryCategory());
		values.put(KEY_ENTRY_CATEGORY,entry.getEntryCategory());
        values.put(KEY_ENTRY_NOTE, entry.getEntryNote());

        // updating row
        return db.update(TABLE_JOURNAL_ENTRY, values, KEY_ID + " = ?",
                new String[] { String.valueOf(entry.getEntryId()) });
    }

    /**
     * Delete a journal entry
	 *@param entry_id the id of the journal entry
     */
    public void deleteJournalEntry(long entry_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JOURNAL_ENTRY, KEY_ID + " = ?",
                new String[] { String.valueOf(entry_id) });
    }




    /**
     * Create a new user resource
	 *@param resource new helpful resource
	 *@return the long id of resource
     */
    public long createUserResource(UserResource resource) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RESOURCE_TEXT, resource.getResourceText());

        // insert row
        long resource_id = db.insert(TABLE_USER_RESOURCES, null, values);

        return resource_id;
    }

    /**
     * Return a single user resource from the USER_RESOURCES table
	 *@param resource_id the id of resource
	 *@return Return a single user resource from the USER_RESOURCES table
     */
    public UserResource getUserResource(long resource_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER_RESOURCES + " WHERE "
                + KEY_ID + " = " + resource_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        UserResource ur = new UserResource();
        ur.setResourceId(c.getInt(c.getColumnIndex(KEY_ID)));
        ur.setResourceText(c.getString(c.getColumnIndex(KEY_RESOURCE_TEXT)));

        return ur;
    }

    /**
     * Return all user resources from the USER_RESOURCES table
	 *@return List<UserResource> , Return all user resources
     */
    public List<UserResource> getAllUserResources() {
        List<UserResource> resources = new ArrayList<UserResource>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_RESOURCES;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                UserResource r = new UserResource();
                r.setResourceId(c.getInt((c.getColumnIndex(KEY_ID))));
                r.setResourceText(c.getString(c.getColumnIndex(KEY_RESOURCE_TEXT)));

                // adding to resources list
                resources.add(r);
            } while (c.moveToNext());
        }
        return resources;
    }


    /**
     * Update a user resource
     * @param resource resource to be updated
     * @return int resource id updated
     */
    public int updateUserResource(UserResource resource) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_RESOURCE_TEXT, resource.getResourceText());

        // updating row
        return db.update(TABLE_USER_RESOURCES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(resource.getResourceId()) });
    }

    /**
     * Delete a user resource
     * @param resource_id long id of resource to be deleted
     */
    public void deleteUserResource(long resource_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_RESOURCES, KEY_ID + " = ?",
                new String[] { String.valueOf(resource_id) });
    }



    /**
	*check if db is empty
	*@return boolean, whether the database is empty or not
	*/
    public boolean isDBEmpty(){
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM " + TABLE_CARD;
        Cursor c = db.rawQuery(count, null);
        c.moveToFirst();
        int i = c.getInt(0);
        if(i > 0){
            return false;
        }else {
            return true;
        }
    }




    /**
	* Close database connection
	*/
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
