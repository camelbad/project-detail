//
//  AboutViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 7/9/18.
//
//  ViewController class that displays the about app screen

import UIKit

class AboutViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Navigation
    /**
     *  Dismisses the current view and returns to Settings screen.
     *
     * - Parameter sender : Sender object of type UIBarButtonItem
     */
     @IBAction func doneButtonTapped(_ sender: UIBarButtonItem) {
        self.dismiss(animated: true, completion: nil)
     }
    
}
