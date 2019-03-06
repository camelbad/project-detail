//
//  ForgotPasswordViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 5/9/18.
//
//  ViewController class that displays the forgot my password screen.

import UIKit

class ForgotPasswordViewController: UIViewController {

    let db = databaseHelper() /// Provides access to database functions
    
    @IBOutlet weak var securityQuestionLabel: UILabel!
    @IBOutlet weak var answerTextField: UITextField!
    @IBOutlet weak var submitButtonBlur: UIVisualEffectView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.setNavigationBarHidden(false, animated: false)

        db.connectDB()
        
        self.hideKeyboard()
        
        securityQuestionLabel.text = db.getFirstQuestion()
        
        submitButtonBlur.layer.cornerRadius = submitButtonBlur.bounds.height / 2.0
        submitButtonBlur.clipsToBounds = true
        
    }

    override func viewWillAppear(_ animated: Bool) {
        answerTextField.text = ""
        securityQuestionLabel.text = db.getFirstQuestion()
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    /**
     *  Returns to Journal locked screen if back button is pressed.
     *
     * - Parameter sender : Sender object of Any type
     */
    @IBAction func backBarButtonTapped(_ sender: Any) {
        self.dismissKeyboard()
        dismiss(animated: true, completion: nil)
    }
    
    /**
     *  Checks the security question answer against database values and either proceeds to unlock journal or notifies user.
     *
     * - Parameter sender : Sender object of Any type.
     */
    @IBAction func submitButtonTapped(_ sender: Any) {
        let answer = db.getFirstAnswer()
        if answerTextField.text == answer {
            performSegue(withIdentifier: "Show Reset Password", sender: nil)
        } else {
            // If answer was incorrect clear entered text and notify user
            answerTextField.text = ""
            // Display alert telling user answer was incorrect
            let alertController = UIAlertController(title: "Wrong Answer", message: "The answer you entered was incorrect, please try again.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        }
    }
}
