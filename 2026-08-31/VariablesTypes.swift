// LET vs VAR slide
// ============================
// 20. constants
let appName = "PNC Mobile"

// appName = "Changed"

var loginCounter = 0
loginCounter += 1


//25. type inference
let balance = 4_250.75

// 26. explicit type annotation
let rate: Double = 0.035


// Type Safety slide
// ============================

let count: Int = 47
let total: Double = 12_309.88

// compile error:
// let avg = total / count

let avg = total / Double(count)

// introduction to conditionals:
if count > 20 {
    print("Count is large")
}

// safe conversion creates Optionals (nillable)
let parsed = Int("2500")
let bad = Int("ABC")

// if combined with let creates a safe way of unwrapping nil
if let amount = parsed {
    // string interpolation involves \()
    print("amount: \(amount)")
} else {
    print("Invalid input")
}

if let temp = bad {
    print("ABC should not be considered an Int")
} else {
    print("You entered a non-numeric value")
}


// the switch slide
// ============================
let transactionType = "transfer"

switch transactionType {
case "deposit":
    print("Deposit transaction")
case "withdrawal":
    print("Withdrawal transaction")
case "transfer":
    print("Transfer transaction")
default:
    print("Unknown transaction")
}

// remove the default: case and point out the error
let myScore = 785


// demonstrate ranges
switch myScore {
case 800...850:
    print("Excellent")
case 740...799:
    print("Very Good")
case 670...739:
    print("Good")
case 580...669:
    print("Fair")
default:
    print("Poor")
}

// demonstrate enums
enum TransactionType {
    case deposit
    case withdrawal
    case fee
}

// demonstrate Implicit Member Expression
// (aka leading dot syntax)
let transactType: TransactionType = .withdrawal

switch transactType {
case .deposit:
    print("depositing")
case .withdrawal:
    print("withdrawing")
case .fee:
    print("charging fee")
}

// demonstrate "clever" twist that allows almost any criteria
let salesTotal = 12_000
switch true {
case salesTotal > 10_000:
    print("You earned the Gold Bonus")
    // add "fallthrough" to demonstrate it
case salesTotal > 7_000:
    print("You earned the Silver Bonus")
default:
    print("Try better next time")
}


// enum cases can have different associated values
enum ServerResponse {
    case success(statusCode: Int, message: String)
    case failure(error: Error)
}

let response = ServerResponse.success(statusCode: 404, message: "Not Found")

// you can unpack values into variables using "let"
switch response {
case .success(statusCode: let code, message: let msg):
    print("Status code: \(code), Message: \(msg)")
// OR
// case: let .success(statusCode, message) and constants are "statusCode" and "message")
case .failure(error: let err):
    print("Error: \(err)")
}

// but what if you wanted to handle different codes from the server differently?
switch response {
case let .success(statusCode, message) where statusCode >= 200 && statusCode < 300:
    print("Success! Response: \(message)")
case let .success(statusCode, message) where statusCode >= 400:
    print("Warning: Server returned status \(statusCode) with message: \(message)")
case let .success(statusCode, _):
    print("Received unexpected success status code: \(statusCode)")
case .failure(error: let err):
    print("API Error: \(err)")
}

// tuple matching in switch
let coordinates = (0, 5)

switch coordinates {
case (0, 0):
    print("At the origin")
case (_, 0):
    print("On the X axis")
case (0, _):
    print("On the Y axis")
default:
    print("Somewhere else in space")
}


// demo function syntax
// =======================
func sayHello() {
    print("Hello world")
}

sayHello()

// with parameters
func greet(name: String) {
    print("Hello \(name)")
}

// demo the problem with just passing a value
// greet("Drew")
greet(name: "Drew")




// the guard slide
// =====================

// demo "Pyramid of Doom" - i.e. work is nested deeply
func processPoorly(amount: Double?, balance: Double) {
    // only work if we have an amount
    if let amt = amount {
        // only work if amount is positive
        if amt > 0 {
            // only work if balance is sufficient
            if amt <= balance {
                // our work is buried 3 levels deep
                print("Executing transfer")
            } else {
                print("Insufficient funds")
            }
        } else {
            print("Invalid amount")
        }
    } else {
        print("No amount specified")
    }
}

// rewritten using guard statements
func processWell(amount: Double?, balance: Double) {
    // only work if we have an amount
    guard let amt = amount else {
        print("No amount")
        return      // must exit the current scope (return, throw, break, continue
    }
    
    // only work if amount is positive
    guard amt > 0 else {
        print("Invalid amount")
        return      // exit the function
    }
    
    // only work if balance is sufficient
    guard amt <= balance else {
        print("Insufficient funds")
        return      // exit the function
    }
    
    // work is not nested at all
    print("Executing transfer")
    
}

let separator = "----------------------"

// basic while loop
var counter = 3

while counter > 0 {
    print("T-minus \(counter)")
    counter -= 1
}
print("Liftoff!!")
print(separator)

// an alternative that is guaranteed to run at least once
var energy = 0

repeat {
    print("Working hard. . . ")
    energy -= 1
} while energy > 0
            
print("I am SOOOO tired!")
print(separator)
            
// mention logical and (&&) logical or (||) logical not (!)

// for loop variations
for number in 1...3 {
    print("Number: \(number)")
    // try the following line to demonstrate that the range variable is a let constant
    // number += 17
}
print(separator)

// using a half open range
for number in 1..<3 {
    print("Number again: \(number)")
}
print(separator)


// looping through an array
let fruits = ["Apple", "Banana", "Watermelon"]

for fruit in fruits {
    print(fruit)
}
print(separator)


// what if we wanted the index number?
for (index, fruit) in fruits.enumerated() {
    print("Index \(index) is \(fruit)")
}
print(separator)


// you don't have to use the loop variable (constant), but Swift will still
// create it
for num in 1...3 {
    print("Hello")
}
print(separator)

// so to improve performance, use an underscore (Swift will not create
// or manage a constant)
for _ in 1...3 {
    print("Hello")
}

print(separator)



