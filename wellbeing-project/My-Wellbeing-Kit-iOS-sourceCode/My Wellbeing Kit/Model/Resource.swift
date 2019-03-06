//
//  Resource.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 28/7/18.
//
//  Resource model class

class Resource {
    
    //MARK: Properties
    var resourceId: Int? // Unique identifier for resource
    private var text: String? // Resource link/information
    
    // Constructor
    init? (resourceID: Int, text: String)
    {
        // The resource must not be empty
        guard !text.isEmpty else {
            return nil
        }
        
        self.resourceId = resourceID
        self.text = text
        
    }
    
    init(){
        
    }
    
    //MARK: Getters & Setters
    var resourceText: String {
        get {
            return self.text!
        }
        set {
            text = newValue
        }
    }
    
}
