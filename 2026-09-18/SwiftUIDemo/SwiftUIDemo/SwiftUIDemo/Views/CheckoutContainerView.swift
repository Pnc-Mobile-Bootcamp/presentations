//
//  CheckoutContainerView.swift
//  SwiftUIDemo
//
//  Created by user286430 on 9/11/26.
//

import SwiftUI

struct CheckoutContainerView: View {
    
    @State private var coordinator = CheckoutCoordinator()
    
    var body: some View {
        @Bindable var coordinator = coordinator
        
        NavigationStack(path: $coordinator.path) {
            Color.clear
                .navigationDestination(for: CheckoutRoute.self) { route in
                    switch route {
                    case .address:
                        AddressView { address in
                            coordinator.didEnterAddress(address)
                        }
                    case .payment:
                        PaymentView { payment in
                            coordinator.didEnterPayment(payment)
                        }
                    case .review:
                        ReviewOrderView(address: coordinator.address!, payment: coordinator.paymentMethod!) {
                            coordinator.didCompleteReview()
                        }
                    case .confirmation:
                        OrderConfirmationView()
                    }
                }
        }
        .task {
            coordinator.start()
        }
    }
}

#Preview {
    CheckoutContainerView()
}
