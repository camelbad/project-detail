//
//  EmotionBankViewController.swift
//  My Wellbeing Kit
//
//  Created by William Stephenson on 28/8/18.
//
//  Handles all interaction with the emotion bank view.

import UIKit

class EmotionBankViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    //MARK: UI Elements
    @IBOutlet weak var segmentedControl: UISegmentedControl!
    @IBOutlet var emotionTableView: UITableView!
    
    /// Holds the currently selected segment of the segmented control.
    var selectedSegment = 0
    /// Holds the selected category.
    var category: String?
    
    /// Holds all available 'happy' emotions
    let happyEmotions = ["Happy", "Accepted", "Calm", "Capable", "Certain", "Cheerful", "Clear", "Comfortable", "Complete", "Confident", "Content", "Delighted", "Determined", "Ecstatic", "Elated", "Enthusiatic", "Excited", "Fantastic", "Free", "Generous", "Grateful", "Healthy", "Heard", "Hopeful", "Interested", "Invigorated", "Joyful", "Kind", "Knowledgeable", "Light-hearted", "Loved", "Optimistic", "Passionate", "Peaceful", "Playful", "Refreshed", "Relaxed", "Resourceful", "Self-confident", "Successful", "Surprised", "Terrific", "Thankful", "Valued", "Zany"]
    
    /// Holds all available 'sad' emotions
    let sadEmotions = ["Sad", "Abandoned", "Bored", "Broken", "Crushed", "Defeated", "Depressed", "Deserted", "Disappointed", "Discouraged", "Drained", "Excluded", "Fatigued", "Forgotten", "Grieving", "Heartbroken", "Helpless", "Hopeless", "Ignored", "Inferior", "Isolated", "Lonely", "Melancholy", "Numb", "Tearful", "Troubled", "Unappreciated"]
    
    /// Holds all available 'angry' emotions
    let angryEmotions = ["Angry", "Agitated", "Belittled", "Betrayed", "Bewildered", "Bitter", "Cross", "Defiant", "Distrubed", "Disturbed", "Envious", "Evil", "Furious", "Grumpy", "Humiliated", "Hurt", "Insulated", "Jealous", "Mad", "Offended", "Shaken", "Violated", "Wounded"]
    
    /// Holds all available 'fear' emotions
    let fearEmotions = ["Afraid", "Anxious", "Apologetic", "Ashamed", "Awkward", "Burdened", "Concerned", "Confused", "Distressed", "Embarrassed", "Exposed", "Fearful", "Flustered", "Frightened", "Guilty", "Indecisive", "Nervous", "Self-concious", "Sorry", "Trapped", "Worried"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        // Change the color of the segmented control to match app theme.
        segmentedControl.tintColor = UIColor(red: 55.0/255.0, green: 211.0/255.0, blue: 184.0/255.0, alpha: 1.0)
        
        self.emotionTableView.delegate = self
        self.emotionTableView.dataSource = self
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cellA = emotionTableView.dequeueReusableCell(withIdentifier: "emotionA")! as UITableViewCell
        
        if(selectedSegment == 0)
        {
            cellA.textLabel?.text = happyEmotions[indexPath.row]
        }
        else if(selectedSegment == 1)
        {
            cellA.textLabel?.text = sadEmotions[indexPath.row]

        }
        else if(selectedSegment == 2)
        {
            cellA.textLabel?.text = angryEmotions[indexPath.row]
        }
        else if(selectedSegment == 3)
        {
            cellA.textLabel?.text = fearEmotions[indexPath.row]
        }
        else
        {
            print("Invalid Segment Selected")
        }
        return cellA
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if(selectedSegment == 0)
        {
            return happyEmotions.count
        }
        else if(selectedSegment == 1)
        {
            return sadEmotions.count
        }
        else if(selectedSegment == 2)
        {
            return angryEmotions.count
        }
        else if(selectedSegment == 3)
        {
            return fearEmotions.count
        }
        return 3
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if(selectedSegment == 0)
        {
            category = happyEmotions[indexPath.row]
        }
        else if(selectedSegment == 1)
        {
            category = sadEmotions[indexPath.row]
        }
        else if(selectedSegment == 2)
        {
            category = angryEmotions[indexPath.row]
        }
        else if(selectedSegment == 3)
        {
            category = fearEmotions[indexPath.row]
        }
        self.performSegue(withIdentifier: "emotionSegueUnwind", sender: self)
    }
    
    /**
     * Upon interaction with the segemented control, updates the selected index and reloads
     * appropriate data.
     *
     * - Parameter sender : Sender object of type AnyObject
     */
    @IBAction func indexChanged(_ sender: AnyObject){
        switch segmentedControl.selectedSegmentIndex
        {
            case 0:
                selectedSegment = 0
                emotionTableView.reloadData()
            case 1:
                selectedSegment = 1
                emotionTableView.reloadData()
            case 2:
                selectedSegment = 2
                emotionTableView.reloadData()
            case 3:
                selectedSegment = 3
                emotionTableView.reloadData()
            default:
                print("This should really not happen")
            break
        }
    }

}
