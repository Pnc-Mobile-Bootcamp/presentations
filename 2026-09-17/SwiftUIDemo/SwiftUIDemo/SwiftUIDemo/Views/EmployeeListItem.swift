//
//  EmployeeListItem.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/9/26.
//

import SwiftUI

struct EmployeeListItem: View {
    
    let employee: Employee
    
    var body: some View {
        HStack {
            Image(systemName: "circle")
                .foregroundColor(.blue)
            NavigationLink("(\(employee.id)) \(employee.lastName)", value: employee)
        }
        .accessibilityElement(children: .combine)
        .accessibilityLabel("employee: \(employee.firstName) \(employee.lastName)")
    }
}


#Preview {
    EmployeeListItem(employee: Employee(id: 0,
                                        firstName: "John",
                                        lastName: "Doe"))
}
