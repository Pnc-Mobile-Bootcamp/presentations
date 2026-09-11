//
//  PaymentView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//

import SwiftUI

struct PaymentView: View {
    
    @State private var payment: PaymentMethod = PaymentMethod(cardNum: "", expDate: "", cvvCode: "")
    
    let done: (PaymentMethod) -> Void
    
    init(done: @escaping (PaymentMethod) -> Void) {
        self.done = done
    }
    
    var body: some View {
        VStack {
            Text("Enter Payment Details")
                .font(.largeTitle.bold())
            
            TextField("Card Number", text: $payment.cardNum)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            TextField("Expiration Date", text: $payment.expDate)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            TextField("CVV Code", text: $payment.cvvCode)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            Button("Continue") {
                done(payment)
            }
        }
    }
    
}
