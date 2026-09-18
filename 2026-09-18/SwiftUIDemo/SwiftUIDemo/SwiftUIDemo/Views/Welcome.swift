//
//  Welcome.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/14/26.
//
import SwiftUI

struct Welcome: View {
    
    
    var body: some View {
        Text("Groovy SwiftUI Demo")
            .font(.largeTitle)
            .foregroundStyle(Color(.systemBlue))
            .padding()
    }
}

#Preview {
    Welcome()
}
