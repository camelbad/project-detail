//
//  ResourceTableViewCell.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 1/8/18.
//
//  Simple TableCell class used by ResourcesViewController table.

import UIKit

class ResourceTableViewCell: UITableViewCell {

    //MARK: Properties
    @IBOutlet weak var resourceTextView: UITextView!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }

}
