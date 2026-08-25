protocol PaymentProvider {
    func processPayment(amount Double) throws
}

class CreditCardPaymentProvider: PaymentProvider {
    func processPayment(amount Double) {
        // credit card logic
    }
}

class BankTransferPaymentProvider: PaymentProvider {
    func processPayment(amount Double) {
        // bank transfer logic
    }
}

class PayPalPaymentProvider: PaymentProvider {
    func processPayment(amount Double) {
        // PayPal logic <-- added this month
    }
}

class CryptoPaymentProvider: PaymentProvider {
    func processPayment(amount Double) {
        // crypto logic here
    }
}


class PaymentProcessor {

    private let provider: PaymentProvider
    init(provider: PaymentProvider) {
        self.provider = provider
    }

	func processPayment(amount: Double) {
        // add fees, subtract discount
		try provider.processPayment(amount: amount)
        // write to audit log
        // mark customer invoice as paid
	}
}