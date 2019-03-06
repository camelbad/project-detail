//
//  ConfirmPasswordViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 5/9/18.
//
//  ViewController class that displays the confirm password screen

import UIKit

class ConfirmPasswordViewController: UIViewController {

    let db = databaseHelper() // Provides access to database functions
    
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var confirmButtonBlur: UIVisualEffectView!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        self.hideKeyboard()
        
        confirmButtonBlur.layer.cornerRadius = confirmButtonBlur.bounds.height / 2.0
        confirmButtonBlur.clipsToBounds = true
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    /**
     *  Dismisses the current view controller upon cancel button press
     *
     * - Parameter sender : Sender object of type UIBarButtonItem
     */
    @IBAction func cancelButtonTapped(_ sender: Any) {
        self.dismissKeyboard()
        self.dismiss(animated: true, completion: nil)
    }
    
    /**
     *  Checks password entered against database value then either disables password lock or notifies user of incorrect password.
     *
     * - Parameter sender : Sender object of Any type.
     */
    @IBAction func confirmButtonTapped(_ sender: Any) {
        db.connectDB()
        self.dismissKeyboard()
        let enteredPass = passwordTextField.text
        if enteredPass == db.getPassword() {
            db.deletePassword()
            let defaults = UserDefaults.standard
            defaults.set(false, forKey: "journalLockedStatus")
            self.dismiss(animated: true, completion: nil)
        } else {
            passwordTextField.shake()
            passwordTextField.text = ""
        }
    }
    

}
