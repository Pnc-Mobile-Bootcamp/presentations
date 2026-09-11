//
//  BannerError.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/11/26.
//
import SwiftUI

struct BannerError: View {
    
    private let model: Failable
    
    init(model: Failable) {
        self.model = model
    }
    
    var body: some View {
        if model.errorMessage != "" {
            Text(model.errorMessage)
                .foregroundColor(Color(.systemRed))
                .background(Color(.red).opacity(0.1))
                .padding()
        }
    }
}
