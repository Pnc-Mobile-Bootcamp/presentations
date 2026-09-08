//
//  EmployeeDetails.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/8/26.
//

import SwiftUI

struct EmployeeDetails: View {
    
    var employee: Employee
    
    var body: some View {
        @Bindable var empBinding = employee
        
        VStack {
            Text("Employee #\(employee.id)")
                .font(.largeTitle)
                .fontWeight(.bold)
            
            TextField("First name", text: $empBinding.firstName)
                .font(Font.title)
                .textFieldStyle(.roundedBorder)
                .padding(20)
            TextField("Last name", text: $empBinding.lastName)
                .font(Font.title)
                .textFieldStyle(.roundedBorder)
                .padding(20)
            
        }
        .padding()
    }
    
    
}

#Preview {
    ContentView()
}
