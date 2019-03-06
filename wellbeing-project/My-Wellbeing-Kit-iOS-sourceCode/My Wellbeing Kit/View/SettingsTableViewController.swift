//
//  SettingsViewController.swift
//  My Wellbeing Kit
//
//  Created by William Stephenson on 21/8/18.
//
//  ViewController class that displays the Settings view

import UIKit

class SettingsTableViewController: UITableViewController {
    
    let db = databaseHelper() /// Provides access to database functions
    let defaults = UserDefaults.standard /// Provides access to user defaults storage

    @IBOutlet var settingsTableView: UITableView!
    @IBOutlet weak var journalPasswordLockSwitch: UISwitch!
    @IBOutlet weak var gridViewSwitch: UISwitch!
    
    override func viewDidLoad() {
        super.viewDidLoad()        
        settingsTableView.delegate = self
        db.connectDB()
        
        // set state of lock switches
        if db.isJournalLocked() {
            journalPasswordLockSwitch.setOn(true, animated: false)
        } else {
            journalPasswordLockSwitch.setOn(false, animated: false)
        }
        
        // set state of grid view switch
        if defaults.bool(forKey: "gridViewStatus") {
            gridViewSwitch.setOn(true, animated: false)
        } else {
            gridViewSwitch.setOn(false, animated: false)
        }
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        if db.isJournalLocked() {
            journalPasswordLockSwitch.setOn(true, animated: false)
        } else {
            journalPasswordLockSwitch.setOn(false, animated: false)
        }
        
        if defaults.bool(forKey: "gridViewStatus") {
            gridViewSwitch.setOn(true, animated: false)
        } else {
            gridViewSwitch.setOn(false, animated: false)
        }
        
    }
    
    /**
     *  Displays create password screen if switch toggled on or confirm password screen if toggled off
     *
     * - Parameter sender : Sender object of Any type
     */
    @IBAction func journalLockSwitchPressed(_ sender: Any) {
        if db.isJournalLocked() {
            performSegue(withIdentifier: "Confirm Password", sender: nil)
        } else {
            performSegue(withIdentifier: "Create Password", sender: nil)
        }
    }
    
    /**
     *  Enables and disables the cards grid view mode according to switch state
     *
     * - Parameter sender : Sender object of Any type
     */
    @IBAction func gridViewSwitchPressed(_ sender: Any) {
        if gridViewSwitch.isOn {
            defaults.set(true, forKey: "gridViewStatus")
        } else {
            defaults.set(false, forKey: "gridViewStatus")
        }
        
    }
    
    
    
}

extension UIView {
    /**
     *  Shakes the view slightly from left to right. Mainly used for signaling a wrong password.
     */
    func shake() {
        let animation = CAKeyframeAnimation(keyPath: "transform.translation.x")
        animation.timingFunction = CAMediaTimingFunction(name: CAMediaTimingFunctionName.linear)
        animation.duration = 0.6
        animation.values = [-20.0, 20.0, -20.0, 20.0, -10.0, 10.0, -5.0, 5.0, 0.0 ]
        layer.add(animation, forKey: "shake")
    }
}
