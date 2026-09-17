//
//  PersistenceController.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/16/26.
//
internal import CoreData

struct PersistenceController {
    static let shared = PersistenceController()
    
    let container: NSPersistentContainer
    
    init() {
        container = NSPersistentContainer(name: "DataModel")
        container.loadPersistentStores { (storeDescription, error) in
            if let error = error as NSError? {
                // cannot proceed without Core Data
                fatalError("Unresolved error \(error), \(error.userInfo)")
            }
        }
    }
    
    
}
