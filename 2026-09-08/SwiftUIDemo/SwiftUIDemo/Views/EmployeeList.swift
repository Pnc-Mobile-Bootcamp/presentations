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
            List(employees) { emp in
                NavigationLink(emp.lastName, value: emp)
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
                .accessibilityLabel("Add new employee")
            }
        }
        .task {
            loadData()
        }
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
