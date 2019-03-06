//
//  EditCardViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 6/8/18.
//
//  ViewController class that displays the edit/view card screen

import UIKit

class EditCardViewController: UIViewController, UITextViewDelegate {
    
    let db = databaseHelper() /// Database access variable
    var selectedCard:Card = Card() /// Local copy of card that has been selected from collectionview
    var cardImageBack:UIImage = #imageLiteral(resourceName: "defaultPhoto") /// Local storage of card image
    
    @IBOutlet weak var cardLabel: UILabel! // Card title
    @IBOutlet weak var cardBackImageView: UIImageView! // Card image with correct rotation
    @IBOutlet weak var cardTextView: UITextView! // Card note is stored/editable here
    @IBOutlet weak var placeholderLabel: UILabel! // Temp 'hint' text in card note text field
    @IBOutlet var backgroundView: UIView!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        cardTextView.delegate = self
        self.hideKeyboard()
        textViewDidChange(cardTextView)
        
        
        // Values passed from CardViewController
        cardLabel.text = selectedCard.cardName
        cardTextView.text = selectedCard.cardNote
        cardBackImageView.image = cardImageBack
        
        // Check if image thumbnail needs to be rotated
        switch selectedCard.cardName {
        case "Eat The Good Stuff":
            cardBackImageView.transform = cardBackImageView.transform.rotated(by: -CGFloat.pi/2)
        case "Embrace The Beauty In Nature":
            cardBackImageView.transform = cardBackImageView.transform.rotated(by: -CGFloat.pi/2)
        case "Get Some Help":
            cardBackImageView.transform = cardBackImageView.transform.rotated(by: -CGFloat.pi/2)
        case "Have A Good Sleep Routine":
            cardBackImageView.transform = cardBackImageView.transform.rotated(by: -CGFloat.pi/2)
        case "Have A Laugh":
            cardBackImageView.transform = cardBackImageView.transform.rotated(by: -CGFloat.pi/2)
        case "Just Breathe":
            cardBackImageView.transform = cardBackImageView.transform.rotated(by: -CGFloat.pi/2)
        case "Take Time Out For Me":
            cardBackImageView.transform = cardBackImageView.transform.rotated(by: -CGFloat.pi/2)
        default:
            break
        }
        
        // Smooth corners of card to be round
        cardBackImageView.roundCornersForAspectFit(radius: 5.0)
        
        // Place border around UITextView (Card Note)
        cardTextView.layer.cornerRadius = 10
        cardTextView.layer.borderColor = UIColor(red: 127.0/255.0, green: 107.0/255.0, blue: 220/255.0, alpha: 1.0).cgColor
        cardTextView.layer.borderWidth = 3
    }
    
    override func viewWillAppear(_ animated: Bool) {
        textViewDidChange(cardTextView)
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    /**
     *  Save changes to selected Card object and return to main cards list view.
     *
     * - Parameter sender : Sender object of type UIBarButton
     */
    @IBAction func saveButtonTapped(_ sender: UIBarButtonItem) {
        db.connectDB() // Open database conneciton
        
        self.dismissKeyboard()
        
        // Copy new text into local card variable
        selectedCard.cardNote = cardTextView.text
        
        // Insert new card into database
        db.updateCard(card: selectedCard)
        
        // Dismiss this view and return to cards view
        self.dismiss(animated: true, completion: nil)
    }
    
    /**
     *  Dismiss the view upon cancel press, notify user if changes have been made that they will be lost.
     *
     * - Parameter sender : Sender object of type UIBarButtonItem
     */
    @IBAction func cancelButtonTapped(_ sender: UIBarButtonItem) {
        self.dismissKeyboard()
        if (selectedCard.cardNote != cardTextView.text) {
            // UIAlertController with confirm option
            let alertController = UIAlertController(title: "Changes will be lost", message: "Are you sure you want to cancel?", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "Yes", style: .destructive, handler: { action in
                self.dismiss(animated: true, completion: nil)
            }))
            alertController.addAction(UIAlertAction(title: "No", style: .cancel, handler: nil))
            
            self.present(alertController, animated: true, completion: nil)
        } else {
            self.dismiss(animated: true, completion: nil)
        }
    }
    
    /**
     *  Hide temporary label acting as text view hint text once user enters text.
     *
     * - Parameter textView : UITextView to monitor for change.
     */
    func textViewDidChange(_ textView: UITextView) {
        let newAlpha = textView.text.isEmpty ? 1.0 : 0.0
        if placeholderLabel.alpha != CGFloat(newAlpha) {
            UIView.animate(withDuration: 0.3) {
                self.placeholderLabel.alpha = CGFloat(newAlpha)
            }
        }
    }
}

extension UIImageView
{
    /**
     *  Rounds the corners of an image with specified radius.
     *
     * - Parameter radius : CGFloat radius of corners
     */
    func roundCornersForAspectFit(radius: CGFloat)
    {
        if let image = self.image {

            //calculate drawingRect
            let boundsScale = self.bounds.size.width / self.bounds.size.height
            let imageScale = image.size.width / image.size.height

            var drawingRect: CGRect = self.bounds

            if boundsScale > imageScale {
                drawingRect.size.width =  drawingRect.size.height * imageScale
                drawingRect.origin.x = (self.bounds.size.width - drawingRect.size.width) / 2
            } else {
                drawingRect.size.height = drawingRect.size.width / imageScale
                drawingRect.origin.y = (self.bounds.size.height - drawingRect.size.height) / 2
            }
            let path = UIBezierPath(roundedRect: drawingRect, cornerRadius: radius)
            let mask = CAShapeLayer()
            mask.path = path.cgPath
            self.layer.mask = mask
        }
    }
}

