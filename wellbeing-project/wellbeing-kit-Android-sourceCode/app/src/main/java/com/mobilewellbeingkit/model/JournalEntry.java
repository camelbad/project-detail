package com.mobilewellbeingkit.model;


public class JournalEntry {
    private static final String TAG = "JournalActivity";

    //private String title;
    private int entryId;
    private String category;
    private String date;
    private String note;

    // Constructors
    public JournalEntry(){
    }

    public JournalEntry(String date, String note, String category){
        this.date = date;
        this.note = note;
        this.category=category;
    }

    public JournalEntry(int id, String category, String date, String note) {
        this.entryId = id;
        this.category = category;
        this.date = date;
        this.note = note;
    }

    // Getters
    public int getEntryId(){
        return entryId;
    }

    public String getEntryCategory() {
        return category;
    }

    public String getEntryDate() {
        return date;
    }

    public String getEntryNote() {
        return note;
    }

    // Setters
    public void setEntryId(int id) {
        this.entryId = id;
    }

    public void setEntryCategory(String category) {
        this.category = category;
    }

    public void setEntryDate(String timestamp) { this.date = timestamp; }

    public void setEntryNote(String note) {
        this.note = note;
    }
}
