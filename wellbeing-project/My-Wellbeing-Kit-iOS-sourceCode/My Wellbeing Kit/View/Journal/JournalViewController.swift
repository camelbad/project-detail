//
//  JournalViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 28/7/18.
//
//  ViewController class that handles interaction with the Journal view.

import UIKit

class JournalViewController: UIViewController, UITableViewDataSource, UITableViewDelegate, UIPickerViewDelegate, UIPickerViewDataSource {
    
    //MARK: Properties
    var entries = [JournalEntry]() /// Array of journal entries
    var pickerData: [String] = [String]() /// Options for sorting entries
    let db = databaseHelper() /// Database helper providing access to database functions
    let defaults = UserDefaults.standard /// Access to user defaults storage
    
    //MARK: UI Elements
    @IBOutlet weak var journalTableView: UITableView!
    @IBOutlet weak var addButton: UINavigationItem!
    @IBOutlet weak var categoryField: UITextField!
    @IBOutlet var backgroundView: UIView!
    
    
    //MARK: Initialisation 
    override func viewDidLoad() {
        super.viewDidLoad()
        // Necessary to get Nav Bar to appear in this class
        self.navigationController?.setNavigationBarHidden(false, animated: false)
        // Removes back button from nav bar
        self.navigationItem.hidesBackButton = true
        // Establish database connection
        db.connectDB()
        // Populate journal will all entries (i.e no filter) by default
        self.populateJournal(data: "All")
        
        // Makes the delegate for the tableView this class
        journalTableView.delegate = self
        
        // Set up category picker UI/Delegate/Input
        let categoryPicker = UIPickerView()
        categoryPicker.delegate = self
        categoryField.inputView = categoryPicker
        
        // Set the journal locked status to false by default
        defaults.set(false, forKey: "journalLockedStatus")
        
        // Make the parent navigation controller only hold this view controller
        self.navigationController?.viewControllers = [self]
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        // Update journalLockedStatus value stored in user defaults so that journal will lock itself
        defaults.set(true, forKey: "journalLockedStatus")
    }
    
    override func viewWillAppear(_ animated: Bool) {
        // Skip this view if journal is locked as we don't want to give access
        if defaults.bool(forKey: "journalLockedStatus") {
            // Set new root view controller to locked screen as journal is supposed to be locked
            let rootVC:JournalLockViewController = self.storyboard?.instantiateViewController(withIdentifier: "journalLockViewController") as! JournalLockViewController
            self.navigationController?.viewControllers = [rootVC]
            self.navigationController?.popToRootViewController(animated: false)
        }
        // Populate picker with category if returning from emotion bank
        populatePicker()
        categoryField.text = ""
        categoryField.placeholder = "Select Emotion"
    }
    
    /**
     *  Populate the picker with emotion filtering options relevant to the current list of entries available.
     *  Only show options that currently exist in the journal entries array.
     */
    func populatePicker(){
        // Empties the list if it has already been populated
        if !pickerData.isEmpty{
            pickerData.removeAll()
        }
        
        // Add All option to the start of the list then fills pickerData from the array of entries
        pickerData.append("All")
        for entry in entries{
            // Check that a category isn't already in the list before adding it
            if !pickerData.contains(entry.journalCategory){
                pickerData.append(entry.journalCategory)
            }
        }
    }
    
    /**
     *  Checks if the list of entries is empty and displays text notifiying user if the number of
     *  entries is less than zero.
     */
    func checkForEmpty(){
        if tableView(journalTableView, numberOfRowsInSection: 1) == 0 {
            let emptyStateLabel = UILabel()
            emptyStateLabel.text = "No Saved Journal Entries!" // text to display if list is empty
            emptyStateLabel.textAlignment = .center
            emptyStateLabel.frame = CGRect(x: journalTableView.frame.width / 2, y: journalTableView.frame.height / 2, width: journalTableView.frame.width, height: journalTableView.frame.height)
            emptyStateLabel.textColor = UIColor.darkGray
            emptyStateLabel.font = UIFont.boldSystemFont(ofSize: 20.0)
            
            // style it as necessary
            journalTableView.backgroundView = emptyStateLabel
        } else {
            journalTableView.backgroundView = nil
        }
    }
    
    /**
     *  Populate journal list with entries retrieved from the local database according to
     *  the filter option given.
     *
     * - Parameter data : Emotion to filter journal entries by in String format
     */
    func populateJournal(data: String){
        if (data == "All"){
            entries = db.getAllJournalEntries()
        }
        else{
            entries = db.getJournalEntriesByCategory(categoryToGet: data)
        }
        checkForEmpty()
        self.journalTableView.reloadData()
    }
    
    
    //MARK: Navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?){
        super.prepare(for: segue, sender: sender)
        
        switch(segue.identifier ?? "") {
            
        case "Add Entry":
                print("add item segue")
            
        case "ShowDetail":
            guard let journalEntryNavigationController = segue.destination as? UINavigationController else {
                fatalError("Unexpected destination: \(segue.destination)")
            }
            
            guard let selectedEntryCell = sender as? JournalTableViewCell else {
                fatalError("Unexpected sender: \(String(describing: sender))")
            }
            
            guard let indexPath = journalTableView.indexPath(for: selectedEntryCell) else {
                fatalError("The selected cell is not being displayed by the table")
            }
            
            // Get the child view controller from navigation controller and set its entry to selected item
            let selectedEntry = entries[indexPath.row]
            let jEntryVC = journalEntryNavigationController.viewControllers[0] as? JournalEntryViewController
            jEntryVC?.entry = selectedEntry
            
        default:
            fatalError("Unexpected Segue Identifier; \(String(describing: segue.identifier))")
        }
    }

    
    //MARK: Picker View functions
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerData.count
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        return pickerData[row]
    }
    
    func pickerView(_ pickerView: UIPickerView, didSelectRow row: Int, inComponent component: Int) {
        categoryField.text = pickerData[row]
        self.view.endEditing(true)
        populateJournal(data: categoryField.text!)
    }
    
    //MARK: Table View functions
    // Support editing the table view.
    func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCell.EditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            let alert = UIAlertController(title: "Delete", message: "Are you sure you wish to delete this journal entry permanently?", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.default, handler: { action in
                // Cancels delete attempt
            }))
            alert.addAction(UIAlertAction(title: "Delete", style: UIAlertAction.Style.destructive, handler: { action in
                // Delete the row from the data source
                let toRemove = self.db.getJournalEntry(ID: self.entries[indexPath.row].entryId!)
                self.db.deleteJournalEntry(ID: toRemove.entryId!)
                self.entries.remove(at: indexPath.row) //This is just temp until I fix database function
                self.journalTableView.deleteRows(at: [indexPath], with: .fade)
                // Refresh picker so that it can remove category 
                self.populatePicker()
            }))

            present(alert, animated: true, completion: nil)
            
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
            
        }
        checkForEmpty()
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    
    // Number of sections to display in table
    func  numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    // Number of rows to display in table (length of entries array)
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return entries.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "journalEntryCell", for: indexPath) as! JournalTableViewCell
        let entry = entries[indexPath.row]
        
        // Set cell data
        cell.entryTextView.text = entry.journalNote
        cell.entryDateLabel.text = entry.journalDate
        cell.entryCategoryLabel.text = entry.journalCategory
        
        return cell
    }
    
    // Set height of each row in tableView
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 80 
    }
    
    //MARK: Actions
    /**
     *  Checks to see whether a new journal entry has been added or an old one has been updated.
     *
     * - Parameter sender : Sender of type UIStoryboardSegue
     */
    @IBAction func unwindToJournalList(sender: UIStoryboardSegue) {
        if let sourceViewController = sender.source as? JournalEntryViewController, let tempEntry = sourceViewController.entry {
            if journalTableView.indexPathForSelectedRow != nil {
                print("Update entry \(String(describing: tempEntry.entryId))")
                db.updateJournalEntry(journalEntry: tempEntry)
                populateJournal(data: "All")
            }
            else{
                print("New entry")
                db.insertJournalEntry(journalEntry: tempEntry)
                populateJournal(data: "All")
            }
        }
    }
    
}















