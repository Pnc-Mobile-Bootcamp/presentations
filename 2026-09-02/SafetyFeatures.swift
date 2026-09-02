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


