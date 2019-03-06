//
//  ResetPasswordViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 5/9/18.
//

import UIKit

class ResetPasswordViewController: UIViewController {

    let db = databaseHelper() /// Provides access to database functions
    
    @IBOutlet weak var newPasswordTextField: UITextField!
    @IBOutlet weak var repeatNewPasswordTextField: UITextField!
    @IBOutlet weak var securityQuestionTextField: UITextField!
    @IBOutlet weak var answerTextField: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.hideKeyboard()
        
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    /**
     *  Checks that passwords entered match and if there is a security question and answer entered before saving data to database.
     *
     * - Parameter sender : Sender object of Any type.
     */
    @IBAction func saveButtonTapped(_ sender: Any) {
        self.dismissKeyboard()
        if newPasswordTextField.text != repeatNewPasswordTextField.text {
            // UIAlertController with okay option
            let alertController = UIAlertController(title: "Passwords Do Not Match", message: "The passwords you entered do not match.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        } else if newPasswordTextField.text == "" {
            // UIAlertController with okay option
            let alertController = UIAlertController(title: "Passwords Cannot Be Empty", message: "A password must be entered.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        } else if (securityQuestionTextField.text == "") {
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
            let newPass = newPasswordTextField.text
            let securityQuestion = securityQuestionTextField.text
            let securityQuestionAnswer = answerTextField.text
            
            db.connectDB()
            db.updatePassword(newPassword: newPass!, firstQuestion: securityQuestion!, firstAnswer: securityQuestionAnswer!, journalLocked: 1)
            
            // Make sure we update the shared storage to lock the journal now that it has a password
            let defaults = UserDefaults.standard
            defaults.set(true, forKey: "journalLockedStatus")

            navigationController?.popToRootViewController(animated: true)
        }
    }
    
}
