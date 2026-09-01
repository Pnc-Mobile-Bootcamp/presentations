import Foundation

// STRUCT as value type demo
struct Transaction {
    let id: String
    let amount: Double
    var description: String
}

let t1 = Transaction(id: "1001", amount: 475.00, description: "Test transaction")
// start with a let constant and see the error when changing description
// let t2 = t1
var t2 = t1

t2.description = "Updated transaction"

print("t1.description = \(t1.description)")
print("t2.description = \(t2.description)")


// mutating method demo
// first try it w/o "mutating" and demo the error
struct Transaction2 {
    let id: String
    let amount: Double
    var description: String
    
    mutating func addNote(note: String) {
        description = "\(description). \(note)"
    }
}

// have to use var b/c structs are effectively immutable, so a copy is created
// every time a property is mutated, and the copy will then be used by the variable
var t3 = Transaction2(id: "ABC", amount: 500.00, description: "")
t3.addNote(note: "Some comment")


let divider = "--------------------------"
print(divider)


// CLASS demo
// demo the error before you add an initializer
// after adding the initializer, comment it out and show that you
// could instead initialize all properties to a default value
class BankAccount {
    let id: String
    let accountNumber: String
    var balance: Double
    let owner: String
    
    init(id: String, accountNumber: String, balance: Double, owner: String) {
        self.id = id
        self.accountNumber = accountNumber
        self.balance = balance
        self.owner = owner
    }
    
    func deposit(amount: Double) {
        guard amount > 0 else {
            return
        }
        
        balance += amount
    }
    
    func withdraw(amount: Double) -> Bool {
        guard amount > 0, amount <= balance else {
            return false
        }
        balance -= amount
        return true
    }
    
}

let acc1 = BankAccount(id: "1001", accountNumber: "ABC123", balance: 500, owner: "Drew")
let acc2 = acc1

acc2.deposit(amount: 500)

print(acc1.balance)

print(divider)



// ENUM demo the error below
// we want the enum to map to strings for compatability with an external API, for example
//enum AccountType {
//    case checking = "CHECKING"
//}

enum AccountType: String {
    case checking = "CHECKING"
    case savings = "SAVINGS"
    case investment = "INVESTMENT"
    case credit = "CREDIT"
    
    var displayName: String {
        switch self {
        case .checking: return "Checking Account"
        case .savings: return "Savings Account"
        case .investment: return "Investment Account"
        case .credit: return "Credit Card"
            // it is usually recommended to NOT have default: for an enum
        }
    }
}

// try removing one of the cases from the switch {} above and demo the error


// create an instance from the raw value:
var myType = AccountType(rawValue: "SAVINGS")!


// Enum cases can have different typed data
enum AccountError {
    case insufficientFunds(available: Double, requested: Double)
    case accountInactive
    case dailyLimitExceeded(limit: Double)
    case invalidAmount(requested: Double)
}


class BankAccount2 {
    let id: String
    let accountNumber: String
    var balance: Double
    let owner: String
    var inactive: Bool = false
    
    init(id: String, accountNumber: String, balance: Double, owner: String) {
        self.id = id
        self.accountNumber = accountNumber
        self.balance = balance
        self.owner = owner
    }
    
    func deposit(amount: Double) {
        guard amount > 0 else {
            return
        }
        
        balance += amount
    }
    
    func withdraw(amount: Double) -> (success: Bool, error: AccountError?) {
        guard inactive == false else {
            return (false, .accountInactive)
        }
        guard amount > 0 else {
            return (false, .invalidAmount(requested: amount))
        }
        guard amount <= balance else {
            return (false, .insufficientFunds(available: balance, requested: amount))
        }
        balance -= amount
        return (true, nil)
    }
    
}

let acc3 = BankAccount2(id: "12345", accountNumber: "ABC243", balance: 1000, owner: "Alice")

let result = acc3.withdraw(amount: 1500)

switch result.error {
case nil:
    print("Successful withdrawal!")
case .accountInactive:
    print("Cannot withdraw from inactive account")
case .insufficientFunds(available: let avail, requested: let req):
    print("Not enough funds. You only have \(avail)")
case .invalidAmount(requested: let req):
    print("Invalid amount requested: \(req)")
case .dailyLimitExceeded(limit: let limit):
    print("You have exeeded your daily limit of \(limit)")
}
print(divider)


// PROTOCOL demo
protocol Describable {
    var description: String { get }
}

// copy and paste from above, adding the protocol
struct Transaction3 : Describable {
    let id: String
    let amount: Double
    var description: String
    
    mutating func addNote(note: String) {
        description = "\(description). \(note)"
    }
}

// copy and paste from above, adding the protocol and the property getter
class BankAccount3: Describable {
    let id: String
    let accountNumber: String
    var balance: Double
    let owner: String
    var inactive: Bool = false
    
    var description: String {
        return "BankAccount #\(accountNumber) owned by \(owner)"
    }
    
    init(id: String, accountNumber: String, balance: Double, owner: String) {
        self.id = id
        self.accountNumber = accountNumber
        self.balance = balance
        self.owner = owner
    }
    
    func deposit(amount: Double) {
        guard amount > 0 else {
            return
        }
        
        balance += amount
    }
    
    func withdraw(amount: Double) -> (success: Bool, error: AccountError?) {
        guard inactive == false else {
            return (false, .accountInactive)
        }
        guard amount > 0, amount <= balance else {
            return (false, .insufficientFunds(available: balance, requested: amount))
        }
        balance -= amount
        return (true, nil)
    }
    
}


func printAll(_ items: [Describable]) {
    items.forEach { print($0.description) }
}

let things: [Describable] = [
    Transaction3(id: "X123", amount: 150.00, description: "Traveler's Cheque Order"),
    BankAccount3(id: "A447", accountNumber: "A447", balance: 894.00, owner: "Stuart"),
    Transaction3(id: "X448", amount: 99.00, description: "Utility Payment")
]
printAll(things)

print(divider)



// Protocol EXTENSION demo
extension Describable {
    func printDescription() {
        print(description)
    }
}

things.forEach { $0.printDescription() }


// try the following code and demo the error
// the protocol must exist before you create an extension for it
//extension SomethingElse {
//    func printDescription() {
//        print(description)
//    }
//}







