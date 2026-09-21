
import kotlin.math.pow


// // This is a comment
// // Kotlin is case-sensitive!

// // DEMO : variables and constants

// // var creates a variable
// var accountBalance = 500.00

// // val creates a constant
// val accountNumber = "123456789"

// // accountNumber = "abc123"     // cannot change the value of a constant

// // print("Hello world\n")
// // print("something else\n")
// // print("this is some \"quoted\" text")

// // combine literal text with variable/constant values
// // we use string interpolation
// println("Account: $accountNumber")
// println("Starting balance: $$accountBalance")

// accountBalance = 425.50
// println("Updated balance: $$accountBalance")

// println("this is money $ in us dollars")


// DEMO: Implicit types

// val customerName = "John Doe"
// val accountCount = 3
// val accountBalance = 1250.75
// val isActive = true

// println(customerName)
// println(accountCount)
// println(accountBalance)
// println(isActive)

// // to determine the data type in a variable/constant 
// // we need to use Kotlin's reflection syntax    :: 
// println(customerName::class.simpleName)
// println(customerName::class.qualifiedName)

// println(accountCount::class.simpleName)
// println(accountBalance::class.simpleName)
// println(isActive::class.simpleName)


// DEMO: can explicitly provide data types
// val customerNam: String = "John Doe"
// var accountCount: Int = 3
// val accountBalance: Double = 1250.75
// val isActive: Boolean = true

// // accountCount = "three"

// var total: Double

// total = 5.0


// DEMO: combining different data types in expressions

// val numberOfTransactions: Int = 5
// val averageTransaction: Double = 42.75

// val estimatedTotal = numberOfTransactions * averageTransaction

// println(estimatedTotal)
// println(estimatedTotal::class.simpleName)

// // when combining two data types in mathematical expressions
// // the result will be the larger of the two data types

// // when combining two values of the same data type
// // in a mathematical expression, the result is the same data type

// print(7.0 / 2)



// DEMO String templates (interpolation)

// val customerName = "Morgan"
// val transactionAmount = 84.25
// val remainingBalance = 915.75

// println("Hello, $customerName")
// println("You spent $$transactionAmount")
// println("Your remaing balance is $$remainingBalance")

// // expressions in string interpolation:
// val price = 20.0
// val quantity = 3

// println("The order total is $${price * quantity}")


// DEMO: a basic if statemtn

// val balance = 725.0

// // if (balance >= 100.0) {
// //     println("Balance is in good standing")
// // } else {
// //     println("Low balance warning")
// // }

// if (balance < 0) {
//     println("Account is overdrawn")
// } else if (balance < 100) {
//     println("Account balance is low")
// } else if (balance < 1_000) {
//     println("Account balance is healthy")
// } else {
//     println("Account balance is excellent")
// }


// DEMO: if as an expression

// in Kotlin, the "if" expression actually returns a value

// val balance = 75.0

// val message = if (balance >= 100.0) "Balance is in good standing"
//      else "Low balance warning"

// println(message)

// // another example
// val number = 17
// var result = if (number % 2 == 0) "Even" else "Odd"


// DEMO: Kotlin's "switch" style branching uses "when"

// val accountType = "S"
// var interestRate: Double = 0.0

// when (accountType) {
//     "C" -> {
//         println("Checking account")
//         interestRate = 0.01
//     }
//     "S" -> {
//         println("Savings account")
//         interestRate = 0.03
//     }
//     "L" -> {
//         println("Loan account")
//         interestRate = -0.08
//     }
//     else -> println("Unknown account type")
// }

// println("Interest rate: $interestRate")

// val accountDescription = when (accountType) {
//     "C" -> "Checking account"
//     "S" -> "Savings account"
//     "L" -> "Loan account"
//     else -> "Unknown account type"
// }

// println(accountDescription)

// println("-------------------------")

// // combining when values

// val dayNumber = 6

// val dayType = when (dayNumber) {
//     1, 2, 3, 4, 5 -> "Weekday"
//     6, 7 -> "Weekend"
//     else -> "Invalid day"
// }
// println("Day number $dayNumber is a $dayType")


// val transactionCode = "ATM"

// val category = when (transactionCode) {
//     "ATM", "CASH" -> "Cash transaction"
//     "POS", "DEBIT" -> "Card purchase"
//     "ACH", "WIRE" -> "Electronic transfer"
//     else -> "Other transaction"
// }
// println("$transactionCode transaction is a $category")

// // ranges with when
// val creditScore = 710

// val rating = when (creditScore) {
//     in 800..850 -> "Exceptional"
//     in 740..<800 -> "Very good"
//     in 670..<740 -> "Good"
//     in 580..669 -> "Fair"
//     in 300..579 -> "Poor"
//     else -> "Invalid score"
// }
// println("Credit rating: $rating")



// // DEMO: a subjectless when (without a value)

// val balance = 725.00

// val accountStatus = when {
//     balance < 0 -> "Overdrawn"
//     balance < 100 -> "Low balance"
//     balance < 1000 -> "Healthy balance"
//     else -> "Excellent balance"
// }
// println(accountStatus)


// // DEMO: pull it all together

// // make a withdrawal decision



val accountNumber = "AW-1025"
var balance = 500.0
val withdrawalAmount = 125.0
val isAccountLocked = false

val result = when {
    isAccountLocked -> 
        "Transaction declined: account is locked"
    
    withdrawalAmount <= 0 -> 
        "Transaction declined: invalid amount"
    
    withdrawalAmount > balance ->
        "Transaction declined: insufficient funds"
        
    else -> {
        balance -= withdrawalAmount
        "Withdrawal approved"
    }
}

println("Account: $accountNumber")
println(result)
println("Remaining balance: $$balance")



// CHALLENGE: 

// Generate a random radius (between 1 and 10) for a circle
// then calculate and output its area

val radius = Math.random() * 9 + 1
// println(radius.toInt())

val area = Math.PI * radius.pow(2)

println("The area of a circle of radius ${String.format("%.2f", radius)} is ${String.format("%.2f", area)}")













