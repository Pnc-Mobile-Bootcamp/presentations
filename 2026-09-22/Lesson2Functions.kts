
// fun displayWelcome() {
//     println("Welcome to PNC Mobile")
// }

// displayWelcome()
// displayWelcome()


// fun displayWelcome(customerName: String) {
//     println("Welcome, $customerName")
// }

// displayWelcome("Jordan")
// displayWelcome("Morgan")


// fun displayWelcome(customerName: String, 
//                    accountType: String) {
//     println("Welcome, $customerName")
//     println("Account type: $accountType")
// }

// displayWelcome("Jordan", "Checking")
// displayWelcome("Morgan", "Savings")


// fun displayWelcome(customerName: String, 
//                    accountType: String) {
//     println("Welcome, $customerName")
//     println("Account type: $accountType")
// }

// displayWelcome("Jordan")
// displayWelcome(12)



// DEMO: return a value

// fun calculateFee(amount: Double, feeRate: Double): Double {
//     val fee = amount * feeRate
//     return fee
// }

// val transactionFee = calculateFee(200.0, 0.03)
// println("Fee: $$transactionFee")

// variations: turn this into a single-expression function
// fun calculateFee(amount: Double, feeRate: Double): Double = amount * feeRate

// val transactionFee = calculateFee(200.0, 0.03)
// println("Fee: $$transactionFee")

// one step further: allow the compiler to infer the return type
// fun calculateFee(amount: Double, feeRate: Double) = amount * feeRate

// val transactionFee = calculateFee(200.0, 0.03)
// println("Fee: $$transactionFee")


// // another example
// fun isLargeTransaction(amount: Double) = amount >= 1000

// if (isLargeTransaction(10_000.0)) {
//     print("Large transaction")
// } else {
//     print("Small transaction")
// }



// DEMO: default value for parameter
// fun calculateFee(amount: Double,
//                  feeRate: Double = 0.03): Double {
//     return amount * feeRate
// }

// val standardFee = calculateFee(200.0)
// val specialFee = calculateFee(200.0, 0.01)

// println("Standard fee: $$standardFee")
// println("Specifal fee: $$specialFee")



// // function with multiple default value parameters
// fun createAccountMessage(
//     customerName: String,
//     accountType: String = "Checking",
//     paperless: Boolean = true
// ): String {
//     return "$customerName opened a $accountType account. Paperless: $paperless"
// }

// println(createAccountMessage("Julie"))
// println(createAccountMessage("Charles", "Savings", false))

// // DEMO: named arguments
// println(createAccountMessage(
//     accountType = "Savings",
//         paperless = false,
//     customerName = "Jordan"
    
// ))


// fun configureAccount(
//     allowTransfers: Boolean,
//     allowOverdraft: Boolean,
//     sendAlerts: Boolean
// ) {
    
// }

// configureAccount(true, false, true)

// configureAccount(
//     allowTransfers = true,
//     allowOverdraft = false,
//     sendAlerts = true
// )


// // DEMO: value of combining named and default arguments
// fun formatTransaction(
//     description: String,
//     amount: Double,
//     showCurrency: Boolean = true,
//     includeStatus: Boolean = false
// ): String {
//     val formattedAmount = if (showCurrency) "$$amount" else amount.toString()
    
//     val status = if (includeStatus) " - Completed" else ""
    
//     return "$description: $formattedAmount$status"
// }

// val result = formatTransaction(
//     description = "Grocery store",
//     amount = 84.25,
//     includeStatus = true
// )
// println(result)


// // DEMO: functions are values

// val calculateTax: (Double) -> Double = { amount -> amount * 0.07 }      // lambda expression

// val tax = calculateTax(100.00)
// println("Tax: $$tax")


// val calculateDiscount: (Double, Double) -> Double = { price, discount -> 
//     price * discount
// }

// val discount = calculateDiscount(100.0, 0.2)
// println("Discount: $$discount")


// DEMO: lambda expressions

// a lambda expression IS a function
// they are always enclosed in curly braces { }

// // basic syntax:
// val sum1: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
// println(sum1(4, 7))


// // first variation: inferred type
// val sum2 = { x: Int, y: Int -> x + y }
// println(sum2(4, 7))


// // the inferred type can go either direction
// val sum3: (Int, Int) -> Int = { x, y -> x + y }
// println(sum3(4, 7))


// val square1: (Int) -> Int = { x -> x * x }
// println(square1(4))

// // if there is EXACTLY one parameter, 
// // the parameter and the arrow can be omitted
// // - if they are omitted, you can refer to the
// // parameter value using the keyword "it"
// val square2: (Int) -> Int = { it * it }
// println(square2(4))


// NOTE: lambdas can contain many lines of code, if necessary
//      the last statement evaluated in the lambda is implicitly returned

// NOTE: if you use the word "return" inside a lambda - bad things usually happen
//      e.g.     return qty * unitPrice
//      b/c the lambda is not a valid context from which to return
//      instead, Kotlin tries to return from the surrounding function

// if you do need an early return from a lambda,
// you need to use qualified (labeled) return
// val processMessage = mes@{ message: String -> 
//     if (message.isEmpty()) {
//         // we want to return early, with a default message
//         return@mes "Default message"
//     }

//     message.uppercase()     // implicit return
// }


// // b/c a function is a value, it can be passed as a parameter to another function
// fun processAmount(
//     amount: Double,
//     operation: (Double) -> Double
// ): Double {
//     return operation(amount)
// }

// // this is known as a higher-order function

// var result = processAmount( 100.00, { amt -> amt * 0.9 })
// println(result)

// val discounted = processAmount(100.00, { it * 0.9 })
// val taxed = processAmount(100.00, { it * 1.07 })
// val doubled = processAmount(100.00, { it * 2 })

// println(discounted)
// println(taxed)
// println(doubled)


// fun applyFees(balance: Double): Double {
//     return balance * 1.05
// }

// // if we want to use a named function as a parameter
// // (instead of a lambda)
// // we need to use the function reference operator
// result = processAmount(100.00, ::applyFees)
// print(result)

// to summarize:
// applyFees()      invokes (calls) the function
// ::applyFees      refers to the function



// DEMO: trailing lambda syntax
// if the last parameter for a function is itself a function
// then you can pass a lambda to that parameter outside the parenthesis ()
// fun processAmount(
//     amount: Double,
//     operation: (Double) -> Double
// ): Double {
//     return operation(amount)
// }

// // standard invocation:
// var result = processAmount(100.0, { amt -> amt * 0.9 })
// println(result)

// // trailing lambda syntax:
// result = processAmount(100.0) { amt -> amt * 0.9 }
// println(result)

// // trailing lambda with single parameter shorthand
// result = processAmount(100.0) { it * 0.9 }
// println(result)



// DEMO: success and failure callbacks
// callbacks are common in user actions, networking, and asynchronous calls

// fun withdraw(
//     balance: Double,
//     amount: Double,
//     onSuccess: (Double) -> Unit,
//     onFailure: (String) -> Unit
// ) {
//     if (amount <= 0) {
//         onFailure("Amount must be greater than zero")
//     } else if (amount > balance) {
//         onFailure("Insufficient funds")
//     } else {
//         onSuccess(balance - amount)
//     }
// }

// withdraw(
//     balance = 500.0,
//     amount = 125.0,
//     onSuccess = { newBalance ->
//         println("Withdrawal approved")
//         println("New balance: $$newBalance")
//     },
//     onFailure = { message ->
//         println("Withdrawal declined: $message")
//     }
// )

// withdraw(
//     balance = 100.0,
//     amount = 125.0,
//     onSuccess = { newBalance ->
//         println("Withdrawal approved")
//         println("New balance: $$newBalance")
//     },
//     onFailure = { message ->
//         println("Withdrawal declined: $message")
//     }
// )


// fun createSerialNumberGenerator(): () -> Int {
    
//     var counter: Int = 0
    
//     return { 
//         counter += 1
//         counter
//     }
    
// }

// val generator1 = createSerialNumberGenerator()
// val generator2 = createSerialNumberGenerator()

// println(generator1())
// println(generator1())
// println(generator1())

// println(generator2())
// println(generator2())

// println(generator1())
// println(generator1())
// println(generator1())


// CHALLENGE:

// create a function called processTransaction
// it should have parameters:
//      description     (String)
//      amount          (Double)
//      feeRate         (Double)
//      validator       (function that receives a Double and returns a Boolean)
//      onApproved      (function that receives a Double and returns a Unit)

//      Test the function by executing a transaction for 250.0
//      with a validator function that only approves transactions between 0.0 and 500.0
//      on success and failure, just print out the result

fun processTransaction(
    description: String,
    amount: Double,
    feeRate: Double,
    validator: (Double) -> Boolean,
    onApproved: (Double) -> Unit
) {
    if (!validator(amount)) {
        println("$description was declined")
        return
    }
    
    val fee = amount * feeRate
    val total = amount + fee
    
    onApproved(total)
}


processTransaction(
    amount = 250.0,
    feeRate = 0.02,
    description = "Equipment purchase",
    validator = { 
        it > 0.0 && it <= 500.0
    },
    onApproved = { 
        println("Transaction approved")
        println("Total with fee: $$it")
    }
)









