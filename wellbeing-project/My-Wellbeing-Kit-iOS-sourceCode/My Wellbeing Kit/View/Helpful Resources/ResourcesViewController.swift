//
//  ResourcesViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 29/7/18.
//
//  Displays helpful contacts (resources) view and handles user interaction.

import UIKit

class ResourcesViewController: UIViewController, UITableViewDataSource, UITableViewDelegate {
    
    //MARK: Properties
    /// Array of user resources displayed in table
    var resources = [Resource]()
    
    /// Database helper object, provides access to database interaction
    let db = databaseHelper()
    
    //MARK: UI Elements
    @IBOutlet weak var resourceTableView: UITableView!
    @IBOutlet weak var addButton: UIBarButtonItem!
    @IBOutlet var backgroundView: UIView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Get database connection
        db.connectDB()
        
        // Populate resources array from db
        populateResources()
    }

    /**
     *  Populates local resources array with data from SQLite database.
     */
    private func populateResources(){
        resources = db.getAllResources()
        self.resourceTableView.reloadData()
    }
    
    //MARK: Table View functions
    // Support editing the table view.
    func tableView(_ tableView: UITableView, editActionsForRowAt indexPath: IndexPath) -> [UITableViewRowAction]? {
        let edit = UITableViewRowAction(style: .normal, title: "Edit") {action, index in
            let editedResource = self.db.getResource(ID: self.resources[indexPath.row].resourceId!)
            let alert = UIAlertController(title: "Edit Resource", message: nil, preferredStyle: .alert)
            alert.addTextField { (tf) in tf.text = editedResource.resourceText }
            let save = UIAlertAction(title: "Save", style: .default) { (_) in
                editedResource.resourceText = (alert.textFields?.first?.text)!
                self.db.updateResource(resource: editedResource)
                self.populateResources()
            }
            alert.addAction(save)
            alert.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
            self.present(alert, animated: true, completion: nil)
        }
        
        // Change background colour of edit option
        edit.backgroundColor = UIColor.orange
        
        let delete = UITableViewRowAction(style: .destructive, title: "Delete") {action, index in
            let alert = UIAlertController(title: "Delete Resource", message: "Are you sure you wish to delete this resource permanently?", preferredStyle: .alert)
            alert.addAction(UIAlertAction(title: "Cancel", style: UIAlertAction.Style.default, handler: { action in
                // Cancels delete attempt
            }))
            alert.addAction(UIAlertAction(title: "Delete", style: UIAlertAction.Style.destructive, handler: { action in
                // Delete the row from the data source
                let toRemove = self.db.getResource(ID: self.resources[indexPath.row].resourceId!)
                self.db.deleteResource(ID: toRemove.resourceId!)
                self.resources.remove(at: indexPath.row)
                self.resourceTableView.deleteRows(at: [indexPath], with: .fade)
            }))
            
            self.present(alert, animated: true, completion: nil)
        }
        
        return [delete, edit]
    }
    
    func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    
    /**
     * Number of sections to display in table
     */
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    
    /**
     * Number of rows to display in table (length of resources array)
     */
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return resources.count
    }
    
    /**
     * Create cell based on custom ResourceTableViewCell type
     */
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "resourceCell", for: indexPath) as! ResourceTableViewCell
        
        let resource = resources[indexPath.row]
        cell.resourceTextView.text = resource.resourceText
        
        return cell
    }

    /**
     *  Launches an alert dialog containing a TextField and Submit button
     *  whereby the user can input a new resource and add it to the databse.
     *
     * - Parameter sender : Sender object of type Any
     */
    @IBAction func addButton(_ sender: Any) {
        let alert = UIAlertController(title: "Add Resource", message: nil, preferredStyle: .alert)
        alert.addTextField { (tf) in tf.placeholder = "Enter Resource..." }
        let action = UIAlertAction(title: "Submit", style: .default) { (_) in
            guard let newResource = alert.textFields?.first?.text
                else { return }
            if (newResource != ""){
                let tempResource = Resource(resourceID: 0, text: newResource)
                self.db.insertResource(resource: tempResource!)
            }
            self.populateResources()
        }
        
        alert.addAction(action)
        present(alert, animated: true, completion: nil)
    }
    
    
}
