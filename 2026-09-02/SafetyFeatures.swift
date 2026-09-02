import Foundation


class Customer1 {
    let name: String
    var account: BankAccount1?
    
    init(name: String) {
        self.name = name
        self.account = nil
    }
    
    deinit {
        print("Customer \(name) is deallocating")
    }
}

class BankAccount1 {
    let number: String
    var owner: Customer1?
    
    init(number: String) {
        self.number = number
        self.owner = nil
    }
    
    deinit {
        print("Bank account \(number) is deallocating")
    }
}


// wrap objects in a do block to create a scope
// objects will go out of scope once the block exits
do {
    let cust = Customer1(name: "Arthur")
    let acct = BankAccount1(number: "12345")
    
    cust.account = acct
    acct.owner = cust
}

print("outside block, objects should be deallocated")


let divider = "---------------------------"
print(divider)

print("Second try:")

class Customer2 {
    let name: String
    var account: BankAccount2?
    
    init(name: String) {
        self.name = name
        self.account = nil
    }
    
    deinit {
        print("Customer \(name) is deallocating")
    }

    func proveReference() {
        print("Customer object has reference to account #\(account!.number)")
    }
}

class BankAccount2 {
    let number: String
    weak var owner: Customer2?
    
    init(number: String) {
        self.number = number
        self.owner = nil
    }
    
    deinit {
        print("Bank account \(number) is deallocating")
    }

    func proveReference() {
        print("Account object has reference to customer \(owner!.name)")
    }
}


// wrap objects in a do block to create a scope
// objects will go out of scope once the block exits
do {
    let cust = Customer2(name: "Arthur")
    let acct = BankAccount2(number: "12345")
    
    cust.account = acct
    acct.owner = cust

    cust.proveReference()
    acct.proveReference()
}

print("outside block, objects should be deallocated")

print(divider)
print("Third try:")


class Customer3 {
    let name: String
    var account: BankAccount3?
    
    init(name: String) {
        self.name = name
        self.account = nil
    }
    
    deinit {
        print("Customer \(name) is deallocating")
    }
}

class BankAccount3 {
    let number: String
    unowned let owner: Customer3?
    
    init(number: String, owner: Customer3) {
        self.number = number
        self.owner = owner
    }
    
    deinit {
        print("Bank account \(number) is deallocating")
    }
}

// wrap objects in a do block to create a scope
// objects will go out of scope once the block exits
do {
    let cust = Customer3(name: "Arthur")
    let acct = BankAccount3(number: "12345", owner: cust)
    
    cust.account = acct
}

print("outside block, objects should be deallocated")


print(divider)
print("Unwrapping Optionals:")




var username: String? = nil
username = "jsmith"

// first pattern for unwrapping: if-let
if let name = username {
    // name is String, not String?
    print("Hello \(name)")
}
// name is not available here - out of scope

// second pattern: guard-let
// guard requires a return or break
// you can break out of a go scope, but only if it has a name
myScope: do {
    guard let name = username else {
        break myScope
    }
    print("Hello \(name)")
}

// third pattern: nil coalescing
let name3 = username ?? "Guest"
// name3 is String, not String?
print("Hello \(name3)")


// fourth pattern: safe access operator
// properties will only be accessed if the object is not nil
// values extracted will be Optional
let length = username?.count
let upper = username?.uppercased()

if let ucase = upper {
    print("Hello \(ucase)")
}

// fifth pattern: force unwrapping
// DANGER: this pattern will throw an error if the value is nil
// use only when a nil value is a programmer error that should crash
// during development
print("Hello \(username!)")


print(divider)


// custom error type
enum TransferError: LocalizedError {
    case invalidAmount
    case insufficientFunds(available: Double)
    case dailyLimitExceeded(limit: Double, attempted: Double)
    case networkUnavailable
    
    var errorDescription: String? {
        switch self {
        case .invalidAmount:
            return "The amount must be greater than zero"
        case .insufficientFunds(let a):
            return "Insufficient funds. Available: $\(String(format:"%.2f", a))"
        case .dailyLimitExceeded(let l, let a):
            return "Daily limit exceeded. Limit: $\(String(format:"%.2f", l)), attempted: $\(String(format:"%.2f", a)))"
        case .networkUnavailable:
            return "Network unavailable. Please try again later."
        }
    }
}



// a function that may throw an error
func executeTransfer(amount: Double, balance: Double) throws -> String {
    // make sure a positive amount was supplied
    guard amount > 0 else {
        throw TransferError.invalidAmount
    }
    
    // make sure there is enough available
    guard amount <= balance else {
        throw TransferError.insufficientFunds(available: balance)
    }
    
    return "Transfer of $\(amount) complete"
    
}

// just try to call the function directly - this executes but has a compiler warning
// let result = executeTransfer(amount: 500, balance: 1000)

// try these parameters and it crashes on a runtime error
// let result = executeTransfer(amount: 500, balance: 100)

// execute the function correctly
do {
    let result = try executeTransfer(amount: 500, balance: 1000)
    print(result)
} catch let e as TransferError {
    print(e.localizedDescription)
} catch {
    print(error)
}

// change the balance above to 100 and run again to see error handling in action

print(divider)

let ints = [3, 1, 7, 2]
let letters = ["a", "b", "c"]
let decimals = [1.414, 3.1415, 2.718]
let words = ["banana", "watermelon", "apple"]

// an example without generics
// NOTE: the "return" keyword is optional in single-line functions
//func first(_ arr: [Int]) -> Int? {
//    arr.first
//}
//
//func first(_ arr: [String]) -> String? {
//    arr.first
//}
//
//print(first(ints))
//print(first(letters))


// now, with Generics
func first<T>(_ arr: [T]) -> T? {
    arr.first
}

if let val = first(ints) {
    print(val)
}

// this will get tiring, so how about a function:
// NOTE: T? is just a synonym for Optional<T>
func printOptional<T>(_ item: Optional<T>) {
    if let val = item {
        print(val)
    }
}

printOptional(first(ints))
printOptional(first(letters))
printOptional(first(decimals))

print(divider)

// the problem with generics is that you don't know anything about the data type
// so you can't do much with the items

// constrained generics allow you to make a trade-off
// you narrow down the range of data types to which the method will apply
// in return, you get to know some capabilities of the data types

// in this case, max() requires that the items conform to Comparable
func findLargest<T: Comparable>(_ arr: [T]) -> T? {
    arr.max()
}

printOptional(findLargest(ints))
printOptional(findLargest(decimals))
printOptional(findLargest(words))

print(divider)

// a generic struct:
struct Stack<T> {
    
    private var items: [T] = []
    
    mutating func push(_ item: T) {
        items.append(item)
    }
    
    mutating func pop() -> T? {
        items.popLast()
    }
    
    var top: T? {
        items.last
    }
    
    var isEmpty: Bool {
        items.isEmpty
    }
    
}


var tasks = Stack<String>()
tasks.push("Wash the car")
tasks.push("Shave the cat")
tasks.push("Fold the laundry")

while !tasks.isEmpty {
    print(tasks.pop()!)
}




