//
//  ReviewOrderView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//

import SwiftUI

struct ReviewOrderView: View {
    
    let address: ShippingAddress
    let payment: PaymentMethod
    let done: () -> Void
    
    init(address: ShippingAddress, payment: PaymentMethod, done: @escaping () -> Void) {
        self.address = address
        self.payment = payment
        self.done = done
    }
    
    var body: some View {
        VStack {
            Text("Review Your Order")
                .font(Font.largeTitle.bold())
            
            Text("Shipping Address")
                .font(Font.title)
            Text(address.shipTo)
            Text(address.street)
            Text(address.city)
            Text(address.state)
            Text(address.zip)
            
            Text("Payment Method")
                .font(Font.title)
            Text(payment.cardNum)
            Text(payment.expDate)
            
            Button("Confirm") {
                done()
            }
        }
        
    }
}
