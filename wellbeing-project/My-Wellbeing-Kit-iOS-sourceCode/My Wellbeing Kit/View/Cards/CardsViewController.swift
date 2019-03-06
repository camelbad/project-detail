//
//  CardsViewController.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 28/7/18.
//
//  ViewController class the displays the main cards screen of the application.

import UIKit


class CardsViewController: UIViewController {
    
    let db = databaseHelper() /// Database access variable
    let defaults = UserDefaults.standard /// Provides access to the user defaults storage
    var cellScaling: CGFloat = 0.75 /// Card scale factor compared to screen size (eg. 0.5 = 50%)
    var cards = [Card]() /// Local ordered collection of cards to display in UICollectionView
    
    // Dictionary storing default card names and exact image names in assets folder
    let defaultCardImages = [
        "achieve_something" : "Achieve Something",
        "eat_the_good_stuff" : "Eat The Good Stuff",
        "embrace_the_beauty_in_nature" : "Embrace The Beauty In Nature",
        "find_a_furry_friend" : "Find a Furry Friend",
        "find_comfort" : "Find Comfort",
        "find_my_safe_place" : "Find My Safe Place",
        "get_some_help" : "Get Some Help",
        "have_a_good_sleep_routine" : "Have A Good Sleep Routine",
        "have_a_laugh" : "Have A Laugh",
        "just_breathe" : "Just Breathe",
        "keep_moving" : "Keep Moving",
        "take_care_of_myself" : "Take Care Of Myself",
        "take_time_out_for_me" : "Take Time Out For Me"
    ]

    
    @IBOutlet weak var collectionView: UICollectionView! // Collectionview outlet
    @IBOutlet weak var addBarButtonItem: UIBarButtonItem! // Add bar button outlet
    
    //MARK: Initialisation
    override func viewDidLoad() {
        super.viewDidLoad()

        collectionView.allowsMultipleSelection = true // Used for changing multiple cell states at once (edit mode)
        db.connectDB() // Open database connection

        // Gather screen information for correct alignmnet of cards in collectionview
        let screenSize = UIScreen.main.bounds.size
        var cellWidth = floor(screenSize.width * cellScaling)
        var cellHeight = floor(screenSize.height * cellScaling)
        var insetX = (collectionView.bounds.width - cellWidth) / 2.0
        var insetY = (collectionView.bounds.height - cellHeight) / 2.0
        let layout = collectionView!.collectionViewLayout as! UICollectionViewFlowLayout
        
        // Show cards in grid layout if in grid view and change scroll direction
        if defaults.bool(forKey: "gridViewStatus") {
            layout.scrollDirection = .vertical
            cellScaling = 0.4
            cellWidth = floor(screenSize.width * cellScaling)
            cellHeight = floor(screenSize.height * cellScaling)
            insetX = (collectionView.bounds.width - cellWidth) / 8.0
            insetY = (collectionView.bounds.height - cellHeight) / 8.0
            layout.itemSize = CGSize(width: cellWidth, height: cellHeight)
            layout.minimumLineSpacing = 30
            layout.minimumInteritemSpacing = 10
            layout.sectionInset.right = 0
            layout.sectionInset.top = 30
            collectionView?.contentInset = UIEdgeInsets(top: insetY, left: insetX, bottom: insetY, right: insetX)

        } else {
            layout.itemSize = CGSize(width: cellWidth, height: cellHeight)
            collectionView?.contentInset = UIEdgeInsets(top: insetY, left: insetX, bottom: insetY, right: insetX)
        }
        
        // If the database is empty then populate it with reuired tables and default card collection
        if db.getAllCards().count == 0 {
            db.createTables() // Create tables if they don't already exist
            createDirectory() // Setup document directory if it doesn't already exist
            createDefaultCards() // Init default cards
        }
        
        // Retrieve cards from DB, ordered by Favourites first
        cards = db.getAllCardsOrderedByFav()
        // Assign collectionview data source as this view controller
        collectionView?.dataSource = self
        // Assign collectionview delegate as this view controller
        collectionView?.delegate = self
        // Add edit button to nav bar
        navigationItem.leftBarButtonItem = editButtonItem
    }
    
    override func viewWillAppear(_ animated: Bool) {
        let oldCardCount = cards.count // store number of cards
        // Reload collectionview incase changes have been made to favourite cards
        cards = db.getAllCardsOrderedByFav()
        collectionView?.reloadData()
        
        // Scroll to new card if one has been added
        if cards.count != oldCardCount {
            collectionView?.scrollToItem(at: (collectionView?.lastIndexpath())!, at: UICollectionView.ScrollPosition.right, animated: false)
        }
        
        // Gather screen dimensions
        let screenSize = UIScreen.main.bounds.size
        var cellWidth = floor(screenSize.width * cellScaling)
        var cellHeight = floor(screenSize.height * cellScaling)
        var insetX = (collectionView.bounds.width - cellWidth) / 2.0
        var insetY = (collectionView.bounds.height - cellHeight) / 2.0
        let layout = collectionView!.collectionViewLayout as! UICollectionViewFlowLayout
        
        // Show cards in grid layout if in grid view and change scroll direction if settings changed
        if defaults.bool(forKey: "gridViewStatus") {
            layout.scrollDirection = .vertical
            cellScaling = 0.4
            cellWidth = floor(screenSize.width * cellScaling)
            cellHeight = floor(screenSize.height * cellScaling)
            insetX = (collectionView.bounds.width - cellWidth) / 8.0
            insetY = (collectionView.bounds.height - cellHeight) / 8.0
            layout.itemSize = CGSize(width: cellWidth, height: cellHeight)
            layout.minimumLineSpacing = 30
            layout.minimumInteritemSpacing = 10
            layout.sectionInset.right = 0
            layout.sectionInset.top = 30
            collectionView?.contentInset = UIEdgeInsets(top: insetY, left: insetX, bottom: insetY, right: insetX)
            
        } else {
            cellScaling = 0.75
            cellWidth = floor(screenSize.width * cellScaling)
            cellHeight = floor(screenSize.height * cellScaling)
            insetX = (collectionView.bounds.width - cellWidth) / 2.0
            insetY = (collectionView.bounds.height - cellHeight) / 2.0
            layout.scrollDirection = .horizontal
            layout.minimumLineSpacing = 15
            layout.minimumInteritemSpacing = 40
            layout.sectionInset.right = 20
            layout.sectionInset.top = 0
            layout.itemSize = CGSize(width: cellWidth, height: cellHeight)
            collectionView?.contentInset = UIEdgeInsets(top: insetY, left: insetX, bottom: insetY, right: insetX)
        }
        
    }
    
    // MARK: Initialise default cards
    /**
     *  Initialises the set of 13 default cards into the database.
     */
    func createDefaultCards() {
        for (path, name) in defaultCardImages {
            let card = Card(name: name, note: "", favourite: 0, custom: 0, image: "")
            let imageUri = saveImage(image: UIImage(named: path)!, name: path)
            card?.cardImage = imageUri!
            db.insertCard(card: card!)
        }
    }
    
    //MARK: Segue Override
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "Go To Edit Card"
        {
            let cell = sender as! UICollectionViewCell
            let indexPath = self.collectionView!.indexPath(for: cell)
            let selectedCard: Card = db.getCard(ID: cards[indexPath!.item].cardId!)
            let destinationVC = segue.destination as? UINavigationController
            let vc = destinationVC?.viewControllers.first as? EditCardViewController
            
            // Image must be passed separately as card object stores it as string and we want UIImage type
            vc?.cardImageBack = loadImage(fileName: selectedCard.cardImage)!
            vc?.selectedCard = selectedCard
        }
    }
    
    //MARK: Image Handling
    /**
     *  Creates the document directory for storing images if it doesn't exist.
     */
    func createDirectory(){
        let fileManager = FileManager.default
        let paths = (NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true)[0] as NSString).appendingPathComponent("WellBeingKitImages")
        if !fileManager.fileExists(atPath: paths){
            try! fileManager.createDirectory(atPath: paths, withIntermediateDirectories: true, attributes: nil)
        }else{
            print("A directory already exists!")
        }
    }
    
    /**
     *  Saves a UIImage to the document directory
     *
     * - Parameter image : UIImage to be saved locally
     * - Returns : String path to the stored image.
     */
    func saveImage(image: UIImage) -> String? {
        let currentDate = Date()
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .none
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
        
        // Set file name equal to current date/time for unique ID
        let fileName = dateFormatter.string(from: currentDate)
        let fileURL = getDocumentsUrl.appendingPathComponent(fileName)
        if let imageData = image.jpegData(compressionQuality: 1.0) {
            try? imageData.write(to: fileURL, options: .atomic)
            return fileName // ----> Save fileName
        }
        print("Error saving image")
        return nil
    }
    
    /**
     *  Saves an image to document directory given the UIImage and custom String (Used for default cards as we want particular names).
     *
     * - Parameter image : UIImage to be saved locally
     * - Parameter name : Desired name of image file
     * - Returns : String path to image in document directory
     */
    func saveImage(image: UIImage, name: String) -> String? {
        let fileName = name
        let fileURL = getDocumentsUrl.appendingPathComponent(fileName)
        if let imageData = image.jpegData(compressionQuality: 1.0) {
            try? imageData.write(to: fileURL, options: .atomic)
            return fileName // ----> Save fileName
        }
        print("Error saving image")
        return nil
    }
    
    /**
     *  Documents directory URL
     */
    var getDocumentsUrl: URL {
        return FileManager.default.urls(for: .documentDirectory, in: .userDomainMask).first!
    }
    
    /**
     *  Loads an image from the document directory.
     *
     * - Parameter fileName : Image file name as a String
     * - Returns : UIImage
     */
    func loadImage(fileName: String) -> UIImage? {
        let fileURL = getDocumentsUrl.appendingPathComponent(fileName)
        do {
            let imageData = try Data(contentsOf: fileURL)
            return UIImage(data: imageData)
        } catch {
            print("Error loading image : \(error)")
        }
        return nil
    }
    
    //MARK: Misc card functions
    /**
     *  Duplicates a card object at given IndexPath in the UICollectionView and inserts it into database, updates view accordingly.
     *
     * - Parameter indexPath : IndexPath of Card object in UICollectionView to be duplicated.
     */
    private func duplicate(indexPath: IndexPath) {
        let card = cards[indexPath.item]
        card.cardNote = ""
        card.cardCustom = 1
        card.cardFavourite = 0
        card.cardId = cards.count + 1
        db.insertCard(card: card)
        cards = db.getAllCardsOrderedByFav() // refresh local cards array
        collectionView.reloadData()
        collectionView.scrollToItem(at: collectionView.lastIndexpath(), at: [], animated: true) // scroll to new card
    }
    
    //MARK: Edit Mode
    // Handles editing mode
    override func setEditing(_ editing: Bool, animated: Bool) {
        super.setEditing(editing, animated: animated)
        
        // Select all items in collectionview
        for i in 0...collectionView!.numberOfItems(inSection: 0) {
            collectionView!.selectItem(at: IndexPath(row: i, section: 0), animated: false, scrollPosition: [])
        }
        
        // Disable add button while editing
        addBarButtonItem.isEnabled = !editing
        
        // set cell state to editing
        if let indexPaths = collectionView?.indexPathsForSelectedItems {
            for indexPath in indexPaths {
                if let cell = collectionView?.cellForItem(at: indexPath) as? CardCollectionViewCell {
                    cell.isEditing = editing
                    // If the card is a default card don't allow deletion
                    if editing && cards[indexPath.item].cardCustom == 0 {
                        cell.deleteButton.isEnabled = false
                        cell.deleteButton.tintColor = UIColor.gray
                        cell.deleteButton.alpha = 0.7
                    } else {
                        cell.deleteButton.isEnabled = true
                        cell.deleteButton.tintColor = UIColor(red: 239.0, green: 0.0, blue: 0.0, alpha: 1)
                        cell.deleteButton.alpha = 1.0
                    }
                }
            }
        }
    }
    
    // Do not allow card interaction while editing
    override func shouldPerformSegue(withIdentifier identifier: String, sender: Any?) -> Bool {
        if identifier == "Go To Edit Card" {
            return !isEditing
        }
        return true
    }
}


//MARK: Collection View functions
extension CardsViewController : UICollectionViewDataSource {
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return cards.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "Cell", for: indexPath) as! CardCollectionViewCell
        cell.delegate = self
       
        // Grab the correct card from local array
        let currentCard: Card = cards[indexPath.item]
        
        // Set card image to matching local image
        cell.cardImageView.image = loadImage(fileName: currentCard.cardImage)
        
        /* Set favourite button to selected state (filled heart) if card is favourite and visa versa.
         * We need to check if the current card is also not a favourite and set accordingly as the UICollectionView
         * will reuse button states if we don't override it. ie. some non faves will have selected button states.
         */
        if currentCard.cardFavourite == 1 {
            cell.favButton.isSelected = true
            cell.favButton.alpha = 1
        } else if currentCard.cardFavourite == 0 {
            cell.favButton.isSelected = false
            cell.favButton.alpha = 0.75
        }
        
        // Called from CardCollectionViewCell class, sets favourite button state and updates database value
        cell.favTapAction = {
            () in
            if (currentCard.cardFavourite == 0) {
                currentCard.cardFavourite = 1
                cell.favButton.isSelected = true
                cell.favButton.alpha = 1
                self.db.updateCardFav(card: currentCard)
                
            } else {
                currentCard.cardFavourite = 0
                cell.favButton.isSelected = false
                cell.favButton.alpha = 0.75
                self.db.updateCardFav(card: currentCard)
            }
        }
        
        // Called from CardCollectionViewCell, runs duplicate function on button tap
        cell.duplicateTapAction = {
            () in
            self.duplicate(indexPath: indexPath)
        }

        return cell
    }
    
    // Set the state of a cell that is about to be visible according to the isEditing boolean value
    func collectionView(_ collectionView: UICollectionView, willDisplay cell: UICollectionViewCell, forItemAt indexPath: IndexPath) {
        let cardCell = cell as! CardCollectionViewCell
        
        if isEditing {
            cardCell.deleteButton.isHidden = false
            cardCell.duplicateButton.isHidden = false
            cardCell.editItemsBackgroundBlur.isHidden = false
            if cards[indexPath.item].cardCustom == 0 {
                cardCell.deleteButton.isEnabled = false
                cardCell.deleteButton.tintColor = UIColor.gray
                cardCell.deleteButton.alpha = 0.7
            } else {
                cardCell.deleteButton.isEnabled = true
                cardCell.deleteButton.tintColor = UIColor(red: 239.0, green: 0.0, blue: 0.0, alpha: 1)
                cardCell.deleteButton.alpha = 1.0
            }
            cardCell.favButton.isHidden = true
            cardCell.favButtonBackgroundBlur.isHidden = true
        } else {
            cardCell.deleteButton.isHidden = true
            cardCell.duplicateButton.isHidden = true
            cardCell.editItemsBackgroundBlur.isHidden = true
            cardCell.favButton.isHidden = false
            cardCell.favButtonBackgroundBlur.isHidden = false
        }
    }
    
}


//MARK: Extensions

// Calculates necessary insets and handles scroll always stopping with a card in center of view
extension CardsViewController : UIScrollViewDelegate, UICollectionViewDelegate
{
    func scrollViewWillEndDragging(_ scrollView: UIScrollView, withVelocity velocity: CGPoint, targetContentOffset: UnsafeMutablePointer<CGPoint>) {
        // Don't apply the scroll changes if collectionview is in gridviewo
        if !defaults.bool(forKey: "gridViewStatus") {
            let layout = self.collectionView?.collectionViewLayout as! UICollectionViewFlowLayout
            let cellWidthIncludingSpacing = layout.itemSize.width + layout.minimumLineSpacing
            
            var offset = targetContentOffset.pointee
            let index = (offset.x + scrollView.contentInset.left) / cellWidthIncludingSpacing
            let roundedIndex = round(index)
            
            offset = CGPoint(x: roundedIndex * cellWidthIncludingSpacing - scrollView.contentInset.left, y: -scrollView.contentInset.top)
            targetContentOffset.pointee = offset
        }
    }
}

extension CardsViewController: CardCollectionViewCellDelegate {
    /**
     *  Deletes a cell from the CollectionView and the object it represents from the database.
     *
     * - Parameter cell : CardCollectionViewCell to be removed from CollectionView
     */
    func delete(cell: CardCollectionViewCell) {
        if let index = collectionView?.indexPath(for: cell) {
            // Only provide option to delete a card if it is custom
            if cards[index.item].cardCustom == 1 {
                // UIAlertController with delete option
                let alertController = UIAlertController(title: "Delete selected card?", message: "This will remove the card permanently.", preferredStyle: .alert)
                alertController.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: { action in
                    
                    self.db.deleteCard(ID: self.cards[index.item].cardId!) // Remove card from database
                    self.cards.remove(at: index.item) // Remove card from local array
                    
                    // Refresh collectionview with updated data
                    self.collectionView.deleteItems(at: [index])
                    
                }))
                alertController.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: nil))
                
                self.present(alertController, animated: true, completion: nil)
                
            }
        }
    }
}

extension UICollectionView {
    /**
     *  Retrieves the last index path value from the collection view
     *
     * - Returns : IndexPath of last item in collection view
     */
    func lastIndexpath() -> IndexPath {
        let section = max(numberOfSections - 1, 0)
        let row = max(numberOfItems(inSection: section) - 1, 0)
        
        return IndexPath(row: row, section: section)
    }
}

// Changes colour of unselected icons in UITabBar now that this cannot be achieved in AppDelegate (post iOS 12 update)
extension UITabBar {
    open override func willMove(toSuperview newSuperview: UIView?) {
        super.willMove(toSuperview: newSuperview)
        self.unselectedItemTintColor = UIColor(red: 255.0/255.0, green: 255.0/255.0, blue: 255.0/255.0, alpha: 0.7)
    }
}
