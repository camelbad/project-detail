//
//  Card.swift
//  My Wellbeing Kit
//
//  Created by Sam Fahey on 28/7/18.
//
//  Card model class.

import os.log
import UIKit

class Card {
    
    //MARK: Properties
    var cardId: Int? // Unique card id
    private var name: String? // Card name
    private var note: String? // Card note
    private var favourite: Int? // Is the card a favourite
    private var custom: Int? // Is the card a custom card
    private var image: String? // Copy of local image path uri
    
    //MARK: Initialization
    init? (name: String, note: String, favourite: Int, custom: Int, image: String) {
        
        // The name must not be empty
        guard !name.isEmpty else {
            return nil
        }
        
        // Initialize stored properties
        self.name = name
        self.note = note
        self.favourite = favourite
        self.custom = custom
        self.image = image
    }
    
    init () {
        
    }
    
    //MARK: Getters & Setters
    var cardName: String {
        get {
            return self.name!
        }
        set {
            name = newValue
        }
    }
    
    var cardNote: String {
        get {
            return self.note!
        }
        set {
            note = newValue
        }
    }
    
    var cardFavourite: Int {
        get {
            return self.favourite!
        }
        set {
            favourite = newValue
        }
    }
    
    var cardCustom: Int {
        get {
            return self.custom!
        }
        set {
            custom = newValue
        }
    }
    var cardImage: String {
        get {
            return self.image!
        }
        set {
            image = newValue
        }
    }
    
    func printInfo(){
        print("ID: \(Int(self.cardId!)), Name: \(self.cardName), Note: \(self.cardNote), Favourite: \(Int(self.favourite!)), Custom: \(Int(self.custom!))")
    }
    
}

