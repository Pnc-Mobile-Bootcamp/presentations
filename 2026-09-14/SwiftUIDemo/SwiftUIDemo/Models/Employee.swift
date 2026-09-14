//
//  Employee.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/8/26.
//

import SwiftUI

@Observable
class Employee: Identifiable, Hashable {
    
    var id: Int
    var firstName: String
    var lastName: String
    
    init(id: Int, firstName: String, lastName: String) {
        self.id = id
        self.firstName = firstName
        self.lastName = lastName
    }
    
    static func == (lhs: Employee, rhs: Employee) -> Bool {
        lhs.id == rhs.id
    }
    
    func hash(into hasher: inout Hasher) {
        hasher.combine(id)
    }
    
}
