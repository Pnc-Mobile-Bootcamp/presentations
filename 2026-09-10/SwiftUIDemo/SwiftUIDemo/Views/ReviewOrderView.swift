//
//  ReviewOrderView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//

import SwiftUI

struct ReviewOrderView: View {
    
    let done: () -> Void
    
    init(done: @escaping () -> Void) {
        self.done = done
    }
    
    var body: some View {
        VStack {
            Text("Review Your Order")
                .font(Font.largeTitle.bold())
            
            // display address and payment details
            
            Button("Confirm") {
                done()
            }
        }
        
    }
}
