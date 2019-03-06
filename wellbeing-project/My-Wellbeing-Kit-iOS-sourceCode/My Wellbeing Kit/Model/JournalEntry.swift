//
//  JournalEntry.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 28/7/18.
//
//  Journal Entry model class

class JournalEntry
{
    //MARK: Properties
    var entryId: Int?
    private var category: String?
    private var date: String?
    private var note: String?
    
    // Constructor
    init? (entryID: Int, category: String, date: String, note: String)
    {
        // The name must not be empty
        guard !category.isEmpty else {
            return nil
        }
        
        self.entryId = entryID
        self.category = category
        self.date = date
        self.note = note
        
    }
    
    //Empty constructor
    init(){
        
    }
    
    //MARK: Getters & Setters
    var journalCategory: String {
        get {
            return self.category!
        }
        set {
            category = newValue
        }
    }
    
    // Might not need this as database should handle automatic dating
    var journalDate: String {
        get {
            return self.date!
        }
        set {
            date = newValue
        }
    }
    
    var journalNote: String {
        get {
            return self.note!
        }
        set {
            note = newValue
        }
    }
    
    func printInfo(){
        print("ID: \(Int(self.entryId!)), Category: \(self.category!), Date: \(self.date!), Note: \(self.note!)")
    }
    
}
