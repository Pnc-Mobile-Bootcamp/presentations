//
//  PaymentView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//

import SwiftUI

struct PaymentView: View {
    
    let done: (PaymentMethod) -> Void
    
    init(done: @escaping (PaymentMethod) -> Void) {
        self.done = done
    }
    
    var body: some View {
        VStack {
            Text("Enter Payment Details")
                .font(.largeTitle.bold())
            
            // skip building the credit card form
            
            Button("Continue") {
                done(PaymentMethod(cardNum: "123456789876543210", expDate: "12/27", cvvCode: "123"  ))
            }
        }
    }
    
}
