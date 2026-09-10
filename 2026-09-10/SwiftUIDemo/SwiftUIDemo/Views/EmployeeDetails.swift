//
//  EmployeeDetails.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/8/26.
//

import SwiftUI

struct EmployeeDetails: View {
    
    var employee: Employee
    @State private var colorIndex = 0
    @State private var scale = 1.0
    
    private let colors = [Color.gray, .green, .yellow, .orange, .pink, .purple, .blue, .red ]
    
    var body: some View {
        @Bindable var empBinding = employee
        
        VStack {
            Text("Employee #\(employee.id)")
                .font(.largeTitle)
                .fontWeight(.bold)
                .onTapGesture {
                    colorIndex = Int.random(in: 0..<colors.count)
                }
            
            TextField("First name", text: $empBinding.firstName)
                .font(Font.title)
                .textFieldStyle(.roundedBorder)
                .padding(20)
            TextField("Last name", text: $empBinding.lastName)
                .font(Font.title)
                .textFieldStyle(.roundedBorder)
                .padding(20)
            
            Image(systemName: "volleyball.fill")
                .foregroundColor(.blue)
                .frame(width: 80, height: 80)
                .scaleEffect(scale)
                .onAppear {
                    let baseAnimation = Animation.easeInOut(duration: 1.3)
                    let repeatingAnimation = baseAnimation.repeatForever(autoreverses: true)
                    withAnimation(repeatingAnimation) {
                        scale = 3.0
                    }
                }
            
        }
        .padding()
        .background(colors[colorIndex].opacity(0.1))
    }
    
    
}

#Preview {
    ContentView()
}
