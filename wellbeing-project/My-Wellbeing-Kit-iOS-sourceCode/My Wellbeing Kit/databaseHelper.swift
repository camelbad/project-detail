//
//  databaseHelper.swift
//  My Wellbeing Kit
//
//  Created by William Stephenson on 1/8/18.
//
//  Handles all interaction with local SQLite database.
//
//  This class utilises the open source software SQLite.swift by Stephen Celis for better Swift integration of SQL commands.
//  For more information visit https://github.com/stephencelis/SQLite.swift
//  It is freely used without restriction under the MIT license, found below.
//  https://github.com/stephencelis/SQLite.swift/blob/master/LICENSE.txt

import SQLite

class databaseHelper {
    
    var database: Connection!
    
    /// Card table and columns
    let cardsTable = Table("cards")
    let id = Expression<Int>("id")
    let name = Expression<String>("name")
    let note = Expression<String>("note")
    let favourite = Expression<Int>("favourite")
    let custom = Expression<Int>("custom")
    let image = Expression<String>("image")
    
    /// Journal table and columns
    let journalTable = Table("journal")
    let jid = Expression<Int>("id")
    let category = Expression<String>("category")
    let date = Expression<String>("date")
    let jnote = Expression<String>("note")
    
    /// Resources table and columns
    let resourcesTable = Table("resources")
    let rid = Expression<Int>("id")
    let text = Expression<String>("text")
    
    /// Settings table and columns
    let settingsTable = Table("settings")
    let sid = Expression<Int>("id")
    let journalLock = Expression<Int>("locked")
    let password = Expression<String?>("password")
    let question = Expression<String?>("question")
    let answer = Expression<String?>("answer")
    

    //MARK: Estasblish Database Connection
    /**
     * Establishes a database connection.
     */
    func connectDB(){
        do{
            let documentDirectory = try FileManager.default.url(for: .documentDirectory, in: .userDomainMask, appropriateFor: nil, create: true)
            let fileUrl = documentDirectory.appendingPathComponent("wellbeingDB").appendingPathExtension("sqlite3")
            let database = try Connection(fileUrl.path)
            self.database = database
            // print("Database Connection Established")
        } catch{
            print(error)
        }
    }
    
    //MARK: Create Tables
    /**
     * Creates all tables required for storing user data.
     */
    func createTables(){
        
        //Create Cards Table
        let createCardsTable = self.cardsTable.create{ (table) in
            table.column(self.id, primaryKey: true)
            table.column(self.name)
            table.column(self.note)
            table.column(self.favourite)
            table.column(self.custom)
            table.column(self.image)
        }
        do{
            try self.database.run(createCardsTable)
            print("Created Cards Table")
        } catch {
            print(error)
        }
        
        //Create Journal Table
        let createJournalTable = self.journalTable.create{ (table) in
            table.column(self.jid, primaryKey: true)
            table.column(self.category)
            table.column(self.date)
            table.column(self.jnote)
        }
        do{
            try self.database.run(createJournalTable)
            print("Created Journal Table")
        } catch {
            print(error)
        }
        
        //Create Resource Table
        let createResourcesTable = self.resourcesTable.create{ (table) in
            table.column(self.rid, primaryKey: true)
            table.column(self.text)
        }
        do{
            try self.database.run(createResourcesTable)
            print("Created Resources Table")
        } catch {
            print(error)
        }
        
        //Create Settings Table
        let createSettingsTable = self.settingsTable.create{ (table) in
            table.column(self.sid, primaryKey: true)
            table.column(self.journalLock, defaultValue: 0)
            table.column(self.password)
            table.column(self.question)
            table.column(self.answer)
        }
        do{
            try self.database.run(createSettingsTable)
            print("Created Settings Table")
        } catch {
            print(error)
        }
        
        // Create an empty row in settings table with default values
        insertPassword(password: nil, question: nil, answer: nil, journalLocked: 0)

    }
    
    //MARK: Insert Functions
    /**
     * Inserts a card into the Cards table of the database.
     *
     * - Parameter card : The card to be inserted into the database
     */
    func insertCard(card:Card){
        let insertCard = self.cardsTable.insert(self.name <- card.cardName, self.note <- card.cardNote, self.favourite <- card.cardFavourite, self.custom <- card.cardCustom, self.image <- card.cardImage)
        do{
            try self.database.run(insertCard)
            print("Card Created")
        }   catch   {
            print(error)
        }
    }
    
    /**
     *  Inserts a journal entry into journal table of the database.
     *
     * - Parameter journalEntry : Journal entry to be inserted into database
     */
    func insertJournalEntry(journalEntry: JournalEntry) {
        let insertJournalEnty = self.journalTable.insert(self.category <- journalEntry.journalCategory, self.date <- journalEntry.journalDate, self.jnote <- journalEntry.journalNote)
        do{
            try self.database.run(insertJournalEnty)
            print("Journal Entry Created")
        }   catch   {
            print(error)
        }
    }
    
    /**
     *  Inserts a resource into the resources table of the database.
     *
     * - Parameter resource : The resource to be inserted into the databse
     */
    func insertResource(resource: Resource) {
        let insertResource = self.resourcesTable.insert(self.text <- resource.resourceText)
        do{
            try self.database.run(insertResource)
            print("Resource Created")
        } catch {
            print(error)
        }
    }
    
    /**
     *  Inserts a password, security question, answer and locked status of the journal into the settings table of the database.
     *
     * - Parameter password : String variable holding user password
     * - Parameter question : String variable holding security question
     * - Parameter answer : String variable holding the answer to the security question
     * - Parameter journalLocked : An Integer value holding locked status of the journal 
     */
    func insertPassword(password: String?, question: String?, answer: String?, journalLocked: Int) {
        let insertPass = self.settingsTable.insert(self.journalLock <- journalLocked, self.password <- password, self.question <- question, self.answer <- answer)
    
        do{
            try self.database.run(insertPass)
            listSettings()
        } catch {
            print(error)
        }
    }
    
    //MARK: Print Functions
    /**
     * Lists all cards in the database.
     */
    func listCards(){
        do{
            let cards = try self.database.prepare(self.cardsTable)
            for card in cards {
                print("ID: \(card[self.id]), Name: \(card[self.name]), Note: \(card[self.note]), Fav: \(card[self.favourite]), Custom: \(card[self.custom]), Image: \(card[self.image]),")
            }
        } catch {
            print(error)
        }
    }
    
    /**
     * List all journal entries in the database.
     */
    func listJournalEntries(){
        do{
            let journalEntries = try self.database.prepare(self.journalTable)
            for journalEntry in journalEntries {
                print("ID: \(journalEntry[self.jid]), Category: \(journalEntry[self.category]), Note: \(journalEntry[self.jnote])")
            }
        } catch {
            print(error)
        }
    }
    
    /**
     * List all resources in the database.
     */
    func listResources(){
        do{
            let resources = try self.database.prepare(self.resourcesTable)
            for resource in resources {
                print("ID: \(resource[self.rid]), Text: \(resource[self.text])")
            }
        } catch {
            print(error)
        }
    }
    
    /**
     * List all settings in the database.
     */
    func listSettings(){
        do{
            let settings = try self.database.prepare(self.settingsTable)
            for x in settings {
                print("ID: \(x[self.sid]), JournalLock: \(x[self.journalLock]), Password: \(String(describing: x[self.password] ?? "No Password")), FirstQuestion: \(x[self.question] ?? "No Question"), Answer: \(x[self.answer] ?? "No Answer")")
            }
        } catch {
            print(error)
        }
    }

    //MARK: Getter Functions
    /**
     *  Returns a single Card object from the Cards table on the database.
     *
     * - Parameter ID : Integer Id value of the card to be retrieved.
     * - Returns : Card
     */
    func getCard(ID:Int) -> Card {
        let cardRow = cardsTable.filter(id == ID)
        let cd = Card()
        do{
            for card in try database.prepare(cardRow){
                cd.cardId = card[id]
                cd.cardName = card[name]
                cd.cardNote = card[note]
                cd.cardFavourite = card[favourite]
                cd.cardCustom = card[custom]
                cd.cardImage = card[image]
            }
        }catch{
            print(error)
        }
        return cd
    }
    
    /**
     *  Returns a single Journal Entry object from the database.
     *
     * - Parameter ID : Integer Id value of the entry to be retrieved.
     * - Returns : JournalEntry
     */
    func getJournalEntry(ID:Int) -> JournalEntry {
        let journalEntryRow = journalTable.filter(id == ID)
        let je = JournalEntry()
        do{
            for journalEntry in try database.prepare(journalEntryRow){
                je.entryId = journalEntry[jid]
                je.journalCategory = journalEntry[category]
                je.journalDate = journalEntry[date]
                je.journalNote = journalEntry[jnote]
            }
        } catch {
            print(error)
        }
        return je
    }
    
    /**
     *  Returns a single Resource object from the database.
     *
     * - Parameter ID : Integer Id value of Resource to be retrieved.
     * - Returns : Resource
     */
    func getResource(ID:Int) -> Resource {
        let resourceRow = resourcesTable.filter(id == ID)
        let re = Resource()
        do{
            for resource in try database.prepare(resourceRow){
                re.resourceId = resource[rid]
                re.resourceText = resource[text]
            }
        }catch {
            print(error)
        }
        return re
    }
    
    /**
     *  Returns the journal lock status from the settings table in the databse (true or false).
     *
     * - Returns : Bool
     */
    func isJournalLocked() -> Bool {
        let settingsRow = settingsTable.filter(id == 1)
        var lockedStatus = false
        do{
            for x in try database.prepare(settingsRow){
                if (x[journalLock] == 1) {
                    lockedStatus = true
                }
            }
        }catch {
            print(error)
        }
        return lockedStatus
    }
    
    /**
     *  Returns the password String from the settings table in the database.
     *
     * - Returns : String
     */
    func getPassword() -> String {
        let settingsRow = settingsTable.filter(id == 1)
        var result = ""
        do{
            for x in try database.prepare(settingsRow){
                result = x[password]!
            }
        }catch {
            print(error)
        }
        return result
    }
    
    /**
     *  Returns the security question from settings table in database.
     *
     * - Returns : String
     */
    func getFirstQuestion() -> String {
        let settingsRow = settingsTable.filter(id == 1)
        var result = ""
        do{
            for x in try database.prepare(settingsRow){
                result = x[question]!
            }
        }catch {
            print(error)
        }
        return result
    }
    
    /**
     *  Returns the security question answer from the settings table in the database.
     *
     * - Returns : String
     */
    func getFirstAnswer() -> String {
        let settingsRow = settingsTable.filter(id == 1)
        var result = ""
        do{
            for x in try database.prepare(settingsRow){
                result = x[answer]!
            }
        }catch {
            print(error)
        }
        return result
    }
    
    //MARK: Get All Functions
    /**
     *  Returns an array of all Card objects in the database.
     *
     * - Returns : [Card]
     */
    func getAllCards() -> [Card]{
        var allCards: [Card] = []
        let allRows = cardsTable

        do{
            for card in try database.prepare(allRows){
                let cd = Card()
                cd.cardId = card[id]
                cd.cardName = card[name]
                cd.cardNote = card[note]
                cd.cardFavourite = card[favourite]
                cd.cardCustom = card[custom]
                cd.cardImage = card[image]
                allCards.append(cd)
            }
        }catch{
            print(error)
        }
        return allCards
    }
    
    /**
     *  Returns an array of Card objects, ordered by favourite cards first.
     *
     * - Returns : [Card]
     */
    func getAllCardsOrderedByFav() -> [Card]{
        var allCards: [Card] = []
        let allRows = cardsTable.order(favourite.desc) //.desc means it will return the 1's (favs) before 0's (non favs)
        
        do{
            for card in try database.prepare(allRows){
                let cd = Card()
                cd.cardId = card[id]
                cd.cardName = card[name]
                cd.cardNote = card[note]
                cd.cardFavourite = card[favourite]
                cd.cardCustom = card[custom]
                cd.cardImage = card[image]
                allCards.append(cd)
            }
        }catch{
            print(error)
        }
        return allCards
    }
    
    /**
     *  Returns an array of all journal entries in the database.
     *
     * - Returns : [JournalEntry]
     */
    func getAllJournalEntries() -> [JournalEntry]{
        var allJournalEntries: [JournalEntry] = []
        let allRows = journalTable
        
        do{
            for journalEntry in try database.prepare(allRows){
                let je = JournalEntry()
                je.entryId = journalEntry[id]
                je.journalCategory = journalEntry[category]
                je.journalDate = journalEntry[date]
                je.journalNote = journalEntry[note]
                allJournalEntries.append(je)
            }
        }catch{
            print(error)
        }
        return allJournalEntries
    }
    
    /**
     *  Returns an array of all journal entries which match the specified category.
     *
     * - Parameter categoryToGet : String holding the category to filter entries by
     * - Returns : [JournalEntry]
     */
    func getJournalEntriesByCategory(categoryToGet: String) -> [JournalEntry]{
        var journalEntries: [JournalEntry] = []
        let rows = journalTable.filter(category == categoryToGet)
        do{
            for journalEntry in try database.prepare(rows){
                let je = JournalEntry()
                je.entryId = journalEntry[id]
                je.journalCategory = journalEntry[category]
                je.journalDate = journalEntry[date]
                je.journalNote = journalEntry[note]
                journalEntries.append(je)
            }
        }catch{
            print(error)
        }
        
        return journalEntries
    }
    
    /**
     *  Returns an array of all resource objects in the database.
     *
     * - Returns : [Resource]
     */
    func getAllResources() -> [Resource]{
        var allResources: [Resource] = []
        let allRows = resourcesTable
        
        do{
            for resource in try database.prepare(allRows){
                let re = Resource()
                re.resourceId = resource[id]
                re.resourceText = resource[text]
                allResources.append(re)
            }
        } catch {
            print(error)
        }
        return allResources
    }
    
    //MARK: Update Row Functions
    /**
     *  Updates a card objects details.
     *
     * - Parameter card : Card object to be updated in the database.
     */
    func updateCard(card: Card){
        let newName = card.cardName
        let newNote = card.cardNote
        let cardRow = cardsTable.filter(id == card.cardId!)
        
        do{
            try self.database.run(cardRow.update(note <- newNote, name <- newName))
            print(card.cardNote)
        }catch{
            print(error)
        }

    }
    
    /**
     *  Updates a cards favourite status in the database.
     *
     * - Parameter card : Card object to be updated.
     */
    func updateCardFav(card: Card){
        let newFavourite = card.cardFavourite
        if(newFavourite == 0 || newFavourite == 1){ //Ensures newFav is valid Int
            let cardRow = cardsTable.filter(id == card.cardId!)
            do{
                try self.database.run(cardRow.update(favourite <- newFavourite))
            }catch{
                print(error)
            }
        }
        else{
            print("Favourite must be between 0 - 1")
        }
    }
    
    /**
     *  Updates a journal entry's details in the databse.
     *
     * - Parameter journalEntry : JournalEntry object to be updated.
     */
    func updateJournalEntry(journalEntry: JournalEntry){
        let newNote = journalEntry.journalNote
        let newCategory = journalEntry.journalCategory
        let journalRow = journalTable.filter(id == journalEntry.entryId!)
        do{
            try self.database.run(journalRow.update(note <- newNote, category <- newCategory))
        } catch {
            print(error)
        }
    }
    
    /**
     *  Updates a Resource objects details in the database.
     *
     * - Parameter resource : Resource object to be updated.
     */
    func updateResource(resource: Resource){
        let newText = resource.resourceText
        let resourceRow = resourcesTable.filter(id == resource.resourceId!)
        do{
            try self.database.run(resourceRow.update(text <- newText))
        } catch {
            print(error)
        }
    }
    
    
    /**
     *  Updates the users journal lock password in the database.
     *
     * - Parameter newPassword : String holding the new journal password.
     * - Parameter firstQuestion : String holding the new journal security question.
     * - Parameter firstAnswer : String holding the new journal security question answer.
     * - Parameter journalLocked : Integer of either 1(true) or 0(false) holding the locked status of the journal.
     */
    func updatePassword(newPassword: String?, firstQuestion: String?, firstAnswer: String?, journalLocked: Int) {
        let settingsRow = settingsTable.filter(id == 1)
        do{
            try self.database.run(settingsRow.update(journalLock <- journalLocked, password <- newPassword, question <- firstQuestion, answer <- firstAnswer))
            print("Password Updated")
        } catch {
            print(error)
        }
    }
    
    //MARK: Delete Functions
    /**
     *  Deletes a single Card object from the database.
     *
     * - Parameter ID : Integer Id of the Card to be removed.
     */
    func deleteCard(ID: Int){
        let toRemove = self.cardsTable.filter(id == ID)
        let deleteCard = toRemove.delete()
        do{
            try self.database.run(deleteCard)
        } catch {
            print(error)
        }
    }
    
    /**
     *  Deletes all Card objects from the database.
     */
    func deleteAllCards(){
        do{
            try self.database.run(cardsTable.delete())
        } catch {
            print(error)
        }
    }
    
    /**
     *  Deletes a single JournalEntry object from the database.
     *
     * - Parameter ID : Integer Id of the JournalEntry object to be deleted.
     */
    func deleteJournalEntry(ID: Int){
        let toRemove = self.journalTable.filter(id == ID)
        let deleteJournal = toRemove.delete()
        do{
            try self.database.run(deleteJournal)
        } catch {
            print(error)
        }
    }
    
    /**
     *  Deletes all JournalEntry objects from the database.
     */
    func deleteAllJournalEntries(){
        do{
            try self.database.run(journalTable.delete())
        } catch {
            print(error)
        }
    }
    
    /**
     *  Deletes a single Resource object from the database.
     *
     * - Parameter ID : Integer Id of the Resource to be removed.
     */
    func deleteResource(ID: Int){
        let toRemove = self.resourcesTable.filter(id == ID)
        let deleteResource = toRemove.delete()
        do{
            try self.database.run(deleteResource)
        } catch {
            print(error)
        }
    }
    
    /**
     *  Deletes all Resource objects from the databse.
     */
    func deleteAllResources(){
        do{
            try self.database.run(resourcesTable.delete())
        } catch {
            print(error)
        }
    }
    
    /**
     *  Deletes the users journal lock password from the database and sets lock status to false.
     */
    func deletePassword() {
        // insert blank password related settings into row, set lock status to false (0)
        updatePassword(newPassword: nil, firstQuestion: nil, firstAnswer: nil, journalLocked: 0)
    }
}
