import Foundation

// labeled parameters
func transfer1(amount: Double, source: String, destination: String) {
    print("Transferring \(amount) from \(source) to \(destination)")
}

// by default, all parameters must be labeled, this makes the invocation (call site)
// read like English
transfer1(amount: 1000, source: "Checking", destination: "Savings")


// however, sometimes the internal names are not what you would prefer to use
// when invoking the function
// so you can provide separate external name before the internal name
func transfer2(amount: Double, from source: String, to destination: String) {
    print("Transferring \(amount) from \(source) to \(destination)")
}

// then, use the external names when invoking
transfer2(amount: 1000, from: "Checking", to: "Savings")


// finally, sometimes a label is not necessary,
// or the call site may even read better without any label
// in this case, use an underscore as the external name
// this will suppress the external label
func transfer3(_ amount: Double, from source: String, to destination: String) {
    print("Transferring \(amount) from \(source) to \(destination)")
}

transfer3(1000, from: "Checking", to: "Savings")

let separator = "----------------------------------------------------------------"
print(separator)


// another example
func authenticate(_ username: String, with password: String) {
    print("Authenticating user: \(username), with password: \(password)")
}

authenticate("alice", with: "secret")

print(separator)

// parameters can have default values, when appropriate
// this makes them optional to provide
func loadRecords1(limit: Int = 50, offset: Int = 0) {
    print("Loading \(limit) records starting at index \(offset)")
}

loadRecords1()
loadRecords1(limit: 20)
loadRecords1(limit: 20, offset: 40)

print(separator)


// return values
func makeGreeting(name: String) -> String {
    return "Hello \(name)"
}

print(makeGreeting(name: "Justine"))
print(makeGreeting(name: "Julio"))


// return many values
func loadRecords2(limit: Int = 50, offset: Int = 0) -> [String] {
    // start with the following and show the error
    // let result = []
    // then use the following until the error inside the loop
    // let result: [String] = []
    var result: [String] = []
    
    for num in offset..<(offset + limit){
        result.append("Record from index \(num)")
    }
    
    return result
}

print(loadRecords2(limit: 5))

print(separator)

// the previous example works well if returning a collection of similar values
// but when you want to return multiple different values
// (e.g. multiple attributes of some entity)
// a typed tuple is best
// NOTE: try without the "?" on the error String type, first
// to demo how the error message is not very clear
func validate(amount: Double) -> (isValid: Bool, error: String?) {
    guard amount > 0 else {
        return (false, "Must be > 0")
    }
    return (true, nil)
}

// we can access the return value components by name (and they are typed!)
let r = validate(amount: 500)

if r.isValid {
    print("proceeding with calculation")
} else {
    // try without the "!" first, to show the warning
    print(r.error!)
}

print(separator)


// the closure slide
// ==========================

let multiply1 = { (a: Int, b: Int) -> Int in return a * b }

// first invoke as if it were a normal function
// print(multiply1(a: 4, b: 5))

// note the error - argument labels are not to be used
print(multiply1(4, 5))


// the types can be specified on the constant:
let multiply2: (Int, Int) -> Int = { a, b in return a * b }

print(multiply2(4, 5))

// IMPLICIT RETURN
// the keyword "return" is implied, so therefore optional
let multiply3: (Int, Int) -> Int = { a, b in a * b }

print(multiply3(4, 5))



print(separator)

// closures capture constants and variables from their surrounding context
// (and this is by reference, so changes will persist)
func makeCounter() -> () -> Int {
    var current = 0
    return {
        current += 1
        return current
    }
}

let next = makeCounter()
print(next())
print(next())
print(next())

let otherCounter = makeCounter()
print(otherCounter())

print(next())


print(separator)


// we use closures as parameter values when a function needs to be passed
// to another function
// Arrays have many methods that allow us to do this
// the array method embodies the iteration over the array and you pass in
// a function that performs the desired operation on each element

let balances = [3_250.00, 12_000.00, 450.75, 8_900.00, 125.50, 22_450.00]


// suppose we want to print out each balance:
for bal in balances {
    print("The current balance is $\(bal)")
}
print(separator)

// OR, using a closure and the forEach() array method
balances.forEach( { (bal: Double) in
    print("The current balance is $\(bal)")
})

print(separator)

// INFERRED TYPES
// because of the context where the closure is being used, the parameter
// data type will be inferred, so it is optional
balances.forEach( { bal in
    print("The current balance is $\(bal)")
})
print(separator)

// TRAILING CLOSURE - if the closure is the last parameter of the function
// receiving the closure, you can omit the parens and pass the closure
// outside the function call

// NOTE: need to "import Foundation" at the top for .currency() to work
balances.forEach { bal in
    print("The current balance is \(bal.formatted(.currency(code: "USD")))")
}
print(separator)

// SHORTHAND ARGUMENTS
// you can skip naming the input parameter(s) for the closure and use $n
// to refer to the parameter(s) when context dictates specific parameters
balances.forEach {
    print("The current balance is \($0.formatted(.currency(code: "USD")))")
}
print(separator)


// OTHER METHODS - filter()
// first, do it the old-fashioned way
var largeBalances: [Double] = []
for bal in balances {
    if bal > 5_000.00 {
        largeBalances.append(bal)
    }
}
print(largeBalances)

largeBalances = balances.filter({ (a: Double) in return a > 5_000.00})
print(largeBalances)

// OR
largeBalances = balances.filter { $0 > 5_000.00 }
print(largeBalances)

print(separator)


// MAP - we want to add 3.5% interest to each balance
// first - the old-fashioned way
var withInterest: [Double] = []
for bal in balances {
    withInterest.append(bal * 1.035)
}
print(withInterest)



// now, do it with map()
withInterest = balances.map({ (a: Double) in return a * 1.035 })
print(withInterest)

// OR
withInterest = balances.map { $0 * 1.035 }
print(withInterest)

print(separator)



// REDUCE - what is the total of all of the balances?
// first, the old-fashioned way
var total = 0.0
for bal in balances {
    total += bal
}
print("Total: \(total)")

// now, using reduce()
total = balances.reduce(0.0) { (subtotal: Double, current: Double) in
    return subtotal + current
}
print("Total: \(total)")


// OR
total = balances.reduce(0.0) { $0 + $1 }
print("Total: \(total)")

print(separator)


// Chaining methods
// What would the total be of accounts > $10,000 after we add 4% interest?
// first, the old-fashioned way
total = 0.0
for bal in balances {
    guard bal > 10_000 else {
        continue
    }
    
    total += bal * 1.04
}
print("Total of large accounts with interest: $\(total)")


// now, using the functions
largeBalances = balances.filter { $0 > 10_000 }
withInterest = largeBalances.map { $0 * 1.04 }
total = withInterest.reduce(0.0) { $0 + $1 }
print("Total of large accounts with interest: $\(total)")

// finally, we don't need all the intermediate variables
total = balances.filter { $0 > 10_000 }
    .map { $0 * 1.04 }
    .reduce(0.0) { $0 + $1 }
print("Total of large accounts with interest: $\(total)")

print(separator)



// so far the iteration algorithms have been simple, so we could code
// them the old-fashioned way or using array methods

// what if we want to sort the balances? implementing our own sorting
// algorithm would be not simple, so we'll just jump straight to the array methods
var sortedBalances = balances.sorted(by: { (a:Double, b:Double) in
        return a < b
} )

print(sortedBalances)


// OR
sortedBalances = balances.sorted { $0 < $1 }
print(sortedBalances)

print(balances)




// optional - demo of edge case w/ memory leak
class NetworkManager {
    
    // this property will store a closure
    var onDataLoaded: (() -> Void)?
    
    func fetchData() {
        // simulate a network call
        sleep(2)
        
        onDataLoaded?()
    }
    
}


class LeakingViewController {
    
    let networkManager = NetworkManager()
    let name = "Profile Screen"
    
    init() {
        print("LeakingViewController initialized")
    }
    
    deinit {
        print("LeakingViewController deinitialized (this will never execute)")
    }
    
    func setupNetwork() {
        networkManager.onDataLoaded = {
            print("Data loaded in \(self.name)")
            // this line captures "self" in the closure with a strong reference
            // leading to circular references:
            // NetworkManager -> closure -> LeakingViewController -> NetworkManater -> etc.
        }
        networkManager.fetchData()
    }
    
}


print("---- running Leaking demo ----")
var leakingVC: LeakingViewController? = LeakingViewController()
leakingVC?.setupNetwork()

// even if explicitly setting to nil, the deinitializer does not run
// (the LeakingViewController and the NetworkManager both stay resident in memory)
leakingVC = nil

// this happens because both objects have strong references
// (which add to the refcount for the object, keeping it alive in memory)

// to break the circular references, at least one of them must be a weak reference
// (which do not add to the refcount, so they will not keep an object alive)



class SafeViewController {
    
    let networkManager = NetworkManager()
    let name = "Profile Screen"
    
    init() {
        print("SafeViewController initialized")
    }
    
    deinit {
        print("SafeViewController deinitialized!")
    }
    
    func setupNetwork() {
        networkManager.onDataLoaded = { [weak self] in
            
            guard let self = self else {
                print("SafeViewController already deallocated. Aborting.")
                return
            }
            
            print("Data loaded in \(self.name)")
        }
        networkManager.fetchData()
    }
    
}


print("---- running Safe demo ----")
var safeVC: SafeViewController? = SafeViewController()
safeVC?.setupNetwork()

// setting to nil now allows deallocation
safeVC = nil


