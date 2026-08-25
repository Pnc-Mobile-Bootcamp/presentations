class PaymentProcessor {
	func processPayment(amount: Double, type: String) {
		if type == "credit_card" {
			// credit card logic
		} else if type == "bank_transfer" {
			// bank transfer logic
		} else if type == "paypal" {
			// PayPal logic <-- added this month
		}
		// next month: else if type == "crypto" { . . . }
	}
}