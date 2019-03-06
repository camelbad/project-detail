//
//  JournalEntryViewController.swift
//  My Wellbeing Kit
//
//  Created by William Stephenson on 6/8/18.
//

import UIKit

class JournalEntryViewController: UIViewController, UITextViewDelegate {
    
    @IBOutlet weak var emotionButton: UIButton!
    @IBOutlet weak var dateField: UILabel!
    @IBOutlet weak var textView: UITextView!
    @IBOutlet weak var saveButton: UIBarButtonItem!
    @IBOutlet weak var cancelButton: UIBarButtonItem!
    @IBOutlet var backgroundView: UIView!
    @IBOutlet weak var emotionButtonBlur: UIVisualEffectView!
    
    let date = Date() /// Used to get current date/time
    let formatter = DateFormatter() /// Used to format date string as required
    
    var category: String? /// Category of the selected journal entry
    var entry: JournalEntry? /// Selected journal entry object
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        updateSaveButtonState()

        // Works with UIViewController extension to dismiss keyboard when tapping anywhere else on screen
        self.hideKeyboard()
        
        // Set up textField
        textView.delegate = self
        // Place border around UITextView (Journal)
        textView.layer.cornerRadius = 10
        textView.layer.borderColor = UIColor(red: 127.0/255.0, green: 107.0/255.0, blue: 220/255.0, alpha: 1.0).cgColor
        textView.layer.borderWidth = 3
        
        // Set up date label
        formatter.dateFormat = "dd.MM.yyyy"
        let result = formatter.string(from: date)
        dateField.text = result
        
        // Populate UI if updating existing entry
        if let entry = entry {
            textView.text = entry.journalNote
            emotionButton.setTitle(entry.journalCategory, for: UIControl.State.normal)
            dateField.text = entry.journalDate
        }
        updateSaveButtonState()
        
        emotionButtonBlur.layer.cornerRadius = emotionButtonBlur.bounds.height / 2.0
        emotionButtonBlur.clipsToBounds = true

    }
    
    
    override func viewWillDisappear(_ animated: Bool) {
        // Change the shared value stored in user defaults to be false so we don't get locked out of journal
        let defaults = UserDefaults.standard
        defaults.set(false, forKey: "journalLockedStatus")
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    internal func textViewDidBeginEditing(_ textView: UITextView) {
        // Disable the Save button while editing.
        updateSaveButtonState()

    }
    
    internal func textViewDidEndEditing(_ textView: UITextView) {
        // Enable save button
        updateSaveButtonState()
    }
    
    // MARK: - Navigation
    /**
     *  Returns to journal view controller if cancel button is pressed.
     *
     * - Parameter sender : Sender object of type UIBarButtonItem
     */
    @IBAction func cancel(_ sender: UIBarButtonItem) {
        self.dismissKeyboard()
        // Depending on style of presentation (modal or push presentation), this view controller needs to be dismissed in two different ways.
        let isPresentingInAddEntryMode = presentingViewController is UINavigationController
        if isPresentingInAddEntryMode {
            dismiss(animated: true, completion: nil)
        }
        else if let owningNavigationController = navigationController{
            owningNavigationController.popViewController(animated: true)
        }
        else {
            fatalError("The JournalEntryViewController is not inside a navigation controller.")
        }
        dismiss(animated: true, completion: nil) //I know this line shouldn't be here and in first if statement but doesn't work without it
    }
    
    /**
     *  Checks if view has returned from EmotionBankViewController and sets category accordingly.
     *
     * - Parameter sender : UIStoryboardSegue
     */
    @IBAction func didUnwindFromEmotionBank(_ sender: UIStoryboardSegue){
        guard let emotion = sender.source as? EmotionBankViewController else{ return }
        emotionButton.setTitle(emotion.category, for: UIControl.State.normal)
    }
    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        super.prepare(for: segue, sender: sender)
        // Configure the destination view controller only when the save button is pressed.
        if(segue.identifier == "emotionSegue")
        {
            print("emotionSegue")
        }
        
        guard let button = sender as? UIBarButtonItem, button === saveButton else {
            print("The save button was not pressed, cancelling")
            return
        }
        let text = textView.text ?? ""
        let date = dateField.text
        let category = emotionButton.currentTitle ?? ""
        // Set the journalEntry to be passed to JournalViewController after the unwind segue.
        if(entry?.entryId != nil){  //Executes only when it is an existing entry being updated
            entry = JournalEntry(entryID: (entry?.entryId)!, category: category, date: date!, note: text)
        }
        else{   // Executes on new entries (to avoid issue with accessing entryID of existing entry)
            entry = JournalEntry(entryID: 0, category: category, date: date!, note: text)
        }
    }
    
    //MARK: Private Methods
    /**
     *  Updates the state of the save button based upon journal text field data.
     */
    private func updateSaveButtonState() {
        // Disable the Save button if the text field or category field is empty
        let text = textView.text ?? ""
        //let category = categoryField.text ?? ""
        saveButton.isEnabled = !text.isEmpty //&& !category.isEmpty
    }
    
    
}

// Enables clicking off the keyboard to dismiss
extension UIViewController
{
    /**
     *  Enables tapping on screen to dismiss on-screen keyboard easily.
     */
    func hideKeyboard()
    {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(
            target: self,
            action: #selector(UIViewController.dismissKeyboard))
        
        view.addGestureRecognizer(tap)
    }
    
    
    
    
    @objc func dismissKeyboard()
    {
        view.endEditing(true)
    }
}
