//
//  CheckoutCoordinator.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/10/26.
//
import SwiftUI

@Observable
class CheckoutCoordinator {
    
    var path: [CheckoutRoute] = []
    
    private(set) var address: ShippingAddress?
    private(set) var paymentMethod: PaymentMethod?
    
    func start() {
//        self.address = nil
        self.paymentMethod = nil
        path = [.address]
    }
    
    func didEnterAddress(_ address: ShippingAddress) {
        self.address = address
        path.append(.payment)
    }
    
    func didEnterPayment(_ paymentMethod: PaymentMethod) {
        self.paymentMethod = paymentMethod
        path.append(.review)
    }
    
    func didCompleteReview() {
        path.append(.confirmation)
    }
    
    
    
}
