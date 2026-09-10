//
//  AddressView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//

import SwiftUI


struct AddressView: View {
    
    let done: (ShippingAddress) -> Void
    
    init(done: @escaping (ShippingAddress) -> Void) {
        self.done = done
    }
    
    
    var body: some View {
        VStack {
            Text("Shipping Address")
                .font(Font.largeTitle.bold())
                .padding(20)
            
            // create the form to collect the address data
            
            Button("Continue") {
                done(ShippingAddress(shipTo: "Test Name",
                                     street: "123 Main St",
                                     city: "Anytown",
                                     state: "ID",
                                     zip: "98989"))
            }
        }
    }
    
}

#Preview {
    AddressView(done: { x in
        
    })
}
