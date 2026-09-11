//
//  AddressView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//

import SwiftUI


struct AddressView: View {
    
    @State private var address = ShippingAddress(shipTo: "",
                                                 street: "",
                                                 city: "",
                                                 state: "",
                                                 zip: "")
    
    let done: (ShippingAddress) -> Void
    
    init(done: @escaping (ShippingAddress) -> Void) {
        self.done = done
    }
    
    
    var body: some View {
        VStack {
            Text("Shipping Address")
                .font(Font.largeTitle.bold())
                .padding(20)
            
            TextField("Ship to", text: $address.shipTo)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            TextField("Street", text: $address.street)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            TextField("City", text: $address.city)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            TextField("State", text: $address.state)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            TextField("Zip", text: $address.zip)
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .padding()
            
            
            Button("Continue") {
                done(address)
            }
        }
    }
    
}

#Preview {
    AddressView(done: { x in
        
    })
}
