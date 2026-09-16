//
//  ToDoList.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/16/26.
//

import SwiftUI
internal import CoreData

struct ToDoList: View {
    
    @Environment(\.managedObjectContext) private var viewContext
    
    @State private var viewModel = ViewModel()
    
    var body: some View {
        NavigationView {
            List {
                ForEach(viewModel.items) { item in
                    Text(item.name ?? "--")
                }
                .onDelete(perform: { offsets in
                    viewModel.deleteItems(offsets: offsets,
                                          viewContext: self.viewContext)
                })
            }
            .toolbar {
                Button(action: {
                    viewModel.addItem(viewContext: self.viewContext)
                }) {
                    Label("Add Item", systemImage: "plus")
                }
            }
            .task {
                viewModel.loadData(viewContext: viewContext)
            }
        }
    }
}


extension ToDoList {
    
    class ViewModel {
        
        var items: [ToDoItem] = []
        private let resultsController: NSFetchedResultsController<ToDoItem>? = nil
        
        
        func loadData(viewContext: NSManagedObjectContext) {
            guard resultsController == nil else { return }
            
            let request = ToDoItem.fetchRequest()
            request.sortDescriptors = [
                NSSortDescriptor(keyPath: \ToDoItem.dateAssigned, ascending: false)
            ]
            
            
        }
        
        func addItem(viewContext: NSManagedObjectContext) {
            withAnimation {
                let newItem = ToDoItem(context: viewContext)
                newItem.name = "New Task"
                newItem.dateAssigned = Date()
                
                saveContext(viewContext: viewContext)
            }
        }
        
        func deleteItems(offsets: IndexSet,
                         viewContext: NSManagedObjectContext) {
            withAnimation {
                offsets.map { items[$0] }
                    .forEach(viewContext.delete)
                saveContext(viewContext: viewContext)
            }
        }
        
        
        private func saveContext(viewContext: NSManagedObjectContext) {
            do {
                try viewContext.save()
            } catch {
                let nsError = error as NSError
                print("Unresolved error \(nsError), \(nsError.userInfo)")
            }
        }
        
    }
    
}

