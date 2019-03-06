//
//  JournalLockViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 29/8/18.
//
//  ViewController class that presents the journal locked screen.

import UIKit

class JournalLockViewController: UIViewController {
    
    let db = databaseHelper()
    let defaults = UserDefaults.standard

    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var unlockButtonBlur: UIVisualEffectView!
    @IBOutlet weak var forgotPasswordButtonBlur: UIVisualEffectView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // hides nav bar
        self.navigationController?.setNavigationBarHidden(true, animated: false)
        
        db.connectDB()
        
        self.hideKeyboard()
        
        unlockButtonBlur.layer.cornerRadius = unlockButtonBlur.bounds.height / 2.0
        forgotPasswordButtonBlur.layer.cornerRadius = forgotPasswordButtonBlur.bounds.height / 2.0
        unlockButtonBlur.clipsToBounds = true
        forgotPasswordButtonBlur.clipsToBounds = true
        
        // Skip this view if journal unlocked
        if !db.isJournalLocked() {
            self.performSegue(withIdentifier: "ForceShowJournal", sender: nil)
        }
    }
    
    
    override func viewWillAppear(_ animated: Bool) {
        if !defaults.bool(forKey: "journalLockedStatus") {
            self.performSegue(withIdentifier: "ForceShowJournal", sender: nil)
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    /**
     *  Checks the input details against database values and either proceeds to unlock journal or notifies user the details are incorrect.
     *
     * - Parameter sender : Sender object of Any type.
     */
    @IBAction func unlockButtonTapped(_ sender: Any) {
        
        let enteredPass = passwordTextField.text
        let actualPass = db.getPassword()
        
        if enteredPass == actualPass {
            // Password is correct, show journal
            performSegue(withIdentifier: "Show My Journal", sender: self)
        } else {
            // If password was incorrect clear entered text and notify user by shaking
            passwordTextField.shake()
            passwordTextField.text = ""
        }
    }
    

    @IBAction func forgotPasswordButtonTapped(_ sender: Any) {
        // Do something with security question and answer here 
    }

}
