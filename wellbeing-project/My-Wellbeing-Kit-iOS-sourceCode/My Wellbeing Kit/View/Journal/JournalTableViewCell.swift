//
//  JournalTableViewCell.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 3/8/18.
//
//  Custom UITableViewCell class for journal entries

import UIKit

class JournalTableViewCell: UITableViewCell {

    //MARK: Properties
    @IBOutlet weak var entryDateLabel: UILabel!
    @IBOutlet weak var entryTextView: UITextView!
    @IBOutlet weak var entryCategoryLabel: UILabel!
    
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }

}
