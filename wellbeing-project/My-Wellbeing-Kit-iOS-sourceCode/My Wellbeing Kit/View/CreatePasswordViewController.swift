//
//  CreatePasswordViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 28/8/18.
//
//  ViewController class that displays the create password screen

import UIKit

class CreatePasswordViewController: UIViewController {

    let db = databaseHelper() /// Provides access to database functions
    
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var confirmPasswordTextField: UITextField!
    @IBOutlet weak var questionTextField: UITextField!
    @IBOutlet weak var answerTextField: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboard()
        
    }
    
    
    /**
     *  Dismisses the current view controller upon cancel button press
     *
     * - Parameter sender : Sender object of type UIBarButtonItem
     */
    @IBAction func cancelButtonTapped(_ sender: UIBarButtonItem) {
        self.dismissKeyboard()
        self.dismiss(animated: true, completion: nil)
    }
    
    
    /**
     *  Checks for inconsistencies in passwords and that a security quesiton has been entered with an answer, then dismisses.
     *
     * - Parameter sender : Sender object of type UIBarButtonItem
     */
    @IBAction func saveButtonTapped(_ sender: UIBarButtonItem) {
        self.dismissKeyboard()
        if (passwordTextField.text == "") {
            // UIAlertController with okay option
            let alertController = UIAlertController(title: "Must Have A Password", message: "Please enter a password into the text field.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        } else if (confirmPasswordTextField.text != passwordTextField.text) {
            // UIAlertController with okay option
            let alertController = UIAlertController(title: "Passwords Do Not Match", message: "Make sure the passwords entered match.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        } else if (questionTextField.text == "") {
            // UIAlertController with okay option
            let alertController = UIAlertController(title: "Must Have A Security Question", message: "Please enter a security question into the text field.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        } else if (answerTextField.text == "") {
            // UIAlertController with okay option
            let alertController = UIAlertController(title: "Answer Cannot Be Empty", message: "Please enter an answer into the text field.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        } else {
            let newPass = passwordTextField.text
            let securityQuestion = questionTextField.text
            let securityQuestionAnswer = answerTextField.text
            
            db.connectDB()
            db.updatePassword(newPassword: newPass!, firstQuestion: securityQuestion!, firstAnswer: securityQuestionAnswer!, journalLocked: 1)
            
            // Make sure we update the shared storage to lock the journal now that it has a password
            let defaults = UserDefaults.standard
            defaults.set(true, forKey: "journalLockedStatus")
            
            self.dismiss(animated: true, completion: nil)
        }
    }
}
