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
                    Section {
                        HStack {
                            Text(item.name ?? "--")
                            Spacer()
                            Button(action: {
                                viewModel.beginAddingTask(category: item)
                            }) {
                                Label("", systemImage: "plus")
                            }
                        }
                    }
                    let tasks = (item.tasks?.allObjects as? [ToDoItem]) ?? []
                    ForEach(tasks) { task in
                        Text(task.name ?? "--")
                    }
                }
                .onDelete(perform: viewModel.deleteItems)
            }
            .toolbar {
                Button(action: viewModel.beginAddingCategory ) {
                    Label("Add Item", systemImage: "plus")
                }
            }
            .sheet(isPresented: $viewModel.showingAddSheet) {
                NavigationStack {
                    Form {
                        TextField("New Item Name", text: $viewModel.newItemText)
                    }
                    .navigationTitle("Add Item")
                    .toolbar {
                        ToolbarItem(placement: .cancellationAction) {
                            Button("Cancel") {
                                viewModel.showingAddSheet = false
                                viewModel.newItemText = ""
                            }
                        }
                        ToolbarItem(placement: .confirmationAction) {
                            Button("Save") {
                                if !viewModel.newItemText.isEmpty {
                                    viewModel.addItem()
                                    viewModel.showingAddSheet = false
                                    viewModel.newItemText = ""
                                }
                            }
                        }
                    }
                }
            }
            .task {
                viewModel.loadData(viewContext: viewContext)
            }
        }
    }
}


extension ToDoList {
    
    @Observable
    class ViewModel: NSObject, NSFetchedResultsControllerDelegate {
        
        var items: [ToDoCategory] = []
        var showingAddSheet = false
        var newItemText = ""
        var selectedCategory: ToDoCategory? = nil
        
        private var resultsController: NSFetchedResultsController<ToDoCategory>? = nil
        private var viewContext: NSManagedObjectContext?
        
        
        
        func loadData(viewContext: NSManagedObjectContext) {
            self.viewContext = viewContext
            guard resultsController == nil else { return }
            
            let request = ToDoCategory.fetchRequest()
            request.sortDescriptors = [
                NSSortDescriptor(keyPath: \ToDoCategory.name, ascending: true)
            ]
            
            resultsController = NSFetchedResultsController(
                fetchRequest: request,
                managedObjectContext: viewContext,
                sectionNameKeyPath: nil,
                cacheName: nil
            )
            
            // sign up to receive notifications when the controller's content results change
            resultsController?.delegate = self
            
            do {
                try resultsController?.performFetch()
                items = resultsController?.fetchedObjects ?? []
            }
            catch {
                print("Failted to fetch To Do Items: \(error)")
            }
            
        }
        
        // automatically update the published items when Core Data changes
        func controllerDidChangeContent(_ controller: NSFetchedResultsController<any NSFetchRequestResult>) {
            if let updatedItems = controller.fetchedObjects as? [ToDoCategory] {
                items = updatedItems
            }
        }
        
        
        func beginAddingCategory() {
            showingAddSheet = true
            selectedCategory = nil
        }
        
        func beginAddingTask(category: ToDoCategory) {
            selectedCategory = category
            showingAddSheet = true
        }
        
        
        func addItem() {
            guard let viewContext else { return }
            
            withAnimation {
                if let cat = selectedCategory {
                    let newItem = ToDoItem(context: viewContext)
                    newItem.name = newItemText
                    newItem.dateAssigned = Date()
                    newItem.category = cat
                    
                } else {
                    let newCat = ToDoCategory(context: viewContext)
                    newCat.name = newItemText
                    
                }
                saveContext()
            }
        }
        
        func deleteItems(offsets: IndexSet) {
            guard let viewContext else { return }
            
            withAnimation {
                offsets.map { items[$0] }
                    .forEach(viewContext.delete)
                saveContext()
            }
        }
        
        
        private func saveContext() {
            guard let viewContext else { return }
            
            do {
                try viewContext.save()
            } catch {
                let nsError = error as NSError
                print("Unresolved error \(nsError), \(nsError.userInfo)")
            }
        }
        
    }
    
}

