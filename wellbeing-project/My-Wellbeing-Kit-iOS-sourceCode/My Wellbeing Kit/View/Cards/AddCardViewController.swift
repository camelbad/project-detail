//
//  AddCardViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 10/8/18.
//
//  ViewController class that displays the add new card view

import UIKit

class AddCardViewController: UIViewController, UIImagePickerControllerDelegate, UINavigationControllerDelegate {

    let db = databaseHelper() /// Used to handle
    let cardController = CardsViewController() /// Used for acces to image functions
    let imagePicker = UIImagePickerController() /// Handles user selection of image from gallery
    let newCard:Card = Card() /// New Card object
    
    
    //MARK: UI Outlets
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var cardImageView: UIImageView!
    @IBOutlet weak var takePhotoBlur: UIVisualEffectView!
    @IBOutlet weak var pickImageBlur: UIVisualEffectView!
    @IBOutlet weak var baseShadowView: UIView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        db.connectDB() // Open database connection
        imagePicker.delegate = self
        self.imagePicker.allowsEditing = false
        self.hideKeyboard()
        
        // Below adjustments to imageview and its container view simply add drop shadow and rounded corners
        cardImageView.layer.cornerRadius = 10.0
        cardImageView.clipsToBounds = true
        baseShadowView.layer.shadowRadius = 4.0
        baseShadowView.layer.cornerRadius = 10.0
        baseShadowView.layer.shadowOpacity = 0.4
        baseShadowView.layer.shadowOffset = CGSize(width: 1, height: 1)
        baseShadowView.clipsToBounds = false
        
        // More rounded corners of buttons
        takePhotoBlur.layer.cornerRadius = takePhotoBlur.bounds.height / 2.0
        pickImageBlur.layer.cornerRadius = pickImageBlur.bounds.height / 2.0
        pickImageBlur.clipsToBounds = true
        takePhotoBlur.clipsToBounds = true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    
    /**
     *  Launches image picker upon button press
     *
     * - Parameter sender : Sender object of Any type
     */
    @IBAction func pickImageButtonTapped(_ sender: Any) {
        self.dismissKeyboard()
        imagePicker.sourceType = .photoLibrary
        present(imagePicker, animated: true, completion: nil)
    }
    
    /**
     *  Launches the phones default camera application upon button press
     *
     * - Parameter sender : Sender object of Any type
     */
    @IBAction func takePhotoButtonTapped(_ sender: Any) {
        self.dismissKeyboard()
        imagePicker.sourceType = .camera
        present(imagePicker, animated: true, completion: nil)
    }
    
    /**
     *  Checks that the new Card has required data before saving to database and returning to Cards list view.
     *
     * - Parameter sender : Sender object of type UIBarButtonItem
     */
    @IBAction func saveButtonTapped(_ sender: UIBarButtonItem) {
        self.dismissKeyboard()
        if nameTextField.text == "" {
            // UIAlertController with okay option
            let alertController = UIAlertController(title: "Must Have A Name", message: "Please enter a card name into the text field.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        } else if cardImageView.image == #imageLiteral(resourceName: "defaultPhoto") {
            // UIAlertController with okay option
            let alertController = UIAlertController(title: "Must Have An Image", message: "Please select an image to use.", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Okay", style: .default, handler: { action in
            }))
            self.present(alertController, animated: true, completion: nil)
        } else {
            newCard.cardName = nameTextField.text!
            newCard.cardNote = ""
            newCard.cardCustom = 1
            newCard.cardFavourite = 0
            newCard.cardImage = cardController.saveImage(image: cardImageView.image!)!
            
            // Insert new card into database
            db.insertCard(card: newCard)
            
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    /**
     *  Dismisses the view upon cancel bar button press
     *
     * - Parameter sender : Sender object of type UIBarButtonItem
     */
    @IBAction func cancelButtonTapped(_ sender: UIBarButtonItem) {
        self.dismissKeyboard()
        self.dismiss(animated: true, completion: nil)
    }
    
    //MARK: UIImgePickerControllerDelegate Methods
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [UIImagePickerController.InfoKey : Any]) {
        // The info dictionary may contain multiple representations of the image. We just want the original.
        guard let selectedImage = info[.originalImage] as? UIImage else {
            fatalError("Expected a dictionary containing an image, but was provided the following: \(info)")
        }
        
        // Set cardImageView to display the selected image.
        cardImageView.image = selectedImage
        
        // Dismiss the picker.
        picker.dismiss(animated: true, completion: nil)
    }
}
