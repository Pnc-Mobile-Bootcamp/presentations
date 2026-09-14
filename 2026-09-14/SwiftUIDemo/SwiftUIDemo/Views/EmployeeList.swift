//
//  EmployeeList.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/8/26.
//

import SwiftUI

struct EmployeeList: View {
    
    @State private var employees: [Employee] = []
    
    var body: some View {
        NavigationStack {
            List {
                ForEach(employees) { emp in
                    EmployeeListItem(employee: emp)
                }
                .onDelete(perform: deleteItems)
                .onMove(perform: moveItems)
            }
            .navigationTitle("Employees")
            .navigationDestination(for: Employee.self) {
                selectedItem in
                EmployeeDetails(employee: selectedItem)
            }
            .toolbar {
                Button(action: {}) {
                    Image(systemName: "plus")
                }
                .accessibilityLabel("Press to add new employee")
            }
        }
        .task {
            loadData()
        }
    }
    
    func deleteItems(at offsets: IndexSet) {
        employees.remove(atOffsets: offsets)
    }
    
    func moveItems(from source: IndexSet, to destination: Int) {
        employees.move(fromOffsets: source, toOffset: destination)
    }
    
    func loadData() {
        employees = [
            Employee(id: 101, firstName: "Antonio", lastName: "Banderas"),
            Employee(id: 102, firstName: "Gloria", lastName: "Estefan"),
            Employee(id: 103, firstName: "Tony", lastName: "Orlando"),
            Employee(id: 104, firstName: "Gladys", lastName: "Knight"),
        ]
    }
    
}


#Preview {
    EmployeeList()
}
