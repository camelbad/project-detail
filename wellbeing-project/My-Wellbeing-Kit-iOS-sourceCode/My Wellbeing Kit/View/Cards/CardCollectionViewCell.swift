//
//  CardCollectionViewCell.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 6/8/18.
//
//  Custom UICollectionViewCell class for representing Card objects in a UICollectionView

import UIKit

protocol CardCollectionViewCellDelegate: AnyObject {
    func delete(cell: CardCollectionViewCell)
}

class CardCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var cardImageView: UIImageView!
    @IBOutlet weak var favButton: UIButton!
    @IBOutlet weak var deleteButton: UIButton!
    @IBOutlet weak var duplicateButton: UIButton!
    @IBOutlet weak var editItemsBackgroundBlur: UIVisualEffectView!
    @IBOutlet weak var favButtonBackgroundBlur: UIVisualEffectView!
    
    
    weak var delegate: CardCollectionViewCellDelegate?
    
    @IBAction func favButtonTapped(_ sender: UIButton) {
        favTapAction?()
    }
    
    @IBAction func deleteButtonTapped(_ sender: Any) {
        delegate?.delete(cell: self)
    }
    
    @IBAction func duplicateButtonTapped(_ sender: Any) {
        duplicateTapAction?()
        
    }
    
    /**
     *  Call back function to CardsViewController when Favourite button is tapped. Updates the favourite status of the card.
     */
    var favTapAction : (()->())?
    
    /**
     *  Call back function to CardsViewController when Duplicate button is tapped. Duplicates the card
     */
    var duplicateTapAction : (()->())?
    
    // Create a small drop shadow below cards in the collectionview and round corners
    override func layoutSubviews() {
        super.layoutSubviews()
        
        cardImageView.layer.cornerRadius = 10.0
        layer.shadowRadius = 10.0
        layer.shadowOpacity = 0.4
        layer.shadowOffset = CGSize(width: 5, height: 10)
        self.clipsToBounds = false
        
        let radius = favButtonBackgroundBlur.bounds.width / 2
        editItemsBackgroundBlur.layer.cornerRadius = 10.0
        favButtonBackgroundBlur.layer.cornerRadius = radius
        editItemsBackgroundBlur.clipsToBounds = true
        favButtonBackgroundBlur.clipsToBounds = true
    }
    
    /**
     *  Holds a true or false value determining if the view is in edit mode
     */
    var isEditing: Bool = false {
        didSet {
            deleteButton.isHidden = !isEditing
            duplicateButton.isHidden = !isEditing
            editItemsBackgroundBlur.isHidden = !isEditing
            favButton.isHidden = isEditing
            favButtonBackgroundBlur.isHidden = isEditing
        }
    }
    
    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
    }
    
}
