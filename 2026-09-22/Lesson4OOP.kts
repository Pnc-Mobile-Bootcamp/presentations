
// // DEMO: basic class syntax

// class Product {
//     var id = 0
//     var name = ""
//     var price = 0.0
// }

// // create an instance of our Product class
// val p1 = Product()
// p1.id = 1234
// p1.name = "Widget"
// p1.price = 24.95

// DEMO: primary constructor syntax
// class Product(var id: Int, var name: String, var price: Double)
// // adding "var" or "val" in front of the parameters
// // promotes them from just being constructor parameters
// // into full properties on the object!

// val p1 = Product(1234, "Widget", 24.95)

// fun displayProduct(p: Product) {
//     println("${p.name} (${p.id}) costs $${p.price}")
// }

// displayProduct(p1)


// DEMO: Class with properties AND methods
// class Product(
//     val id: Int,
//     val name: String,
//     var price: Double
// ) {
//     init {
//         println("Creating an instance of Product")
//     }
    
//     fun displayLabel(): String = "$name - $$price"
    
//     // percent will be something like "10.0" for a 10% discount
//     fun applyDiscount(percent: Double) {
//         // price = price * (1 - percent / 100)
//         price *= 1 - percent / 100
//     }
// }
// val product = Product(101, "Mountain Bike", 900.0)

// println(product.displayLabel())
// product.applyDiscount(10.0)
// println(product.displayLabel())


// // DEMO: primary constructor WITHOUT val or var
// class User(name: String) {
    
//     // 1. valid: used to initialize a class property
//     val login = name.lowercase()
    
//     // 2. valid: accessible inside init block
//     init {
//         println("Initializing a User with name: $name")
//     }
//     init {
//         println("This block also runs")
//     }
    
//     // fun printName() {
//     //     // invalid: Compiler error: "name" is not accessible here
//     //     println(name)
//     // }
    
// }

// val user1 = User("Drew")


// DEMO: difference between "class" and "data class"

// data class Category(val id: Int, val name: String)

// val first = Category(1, "Bikes")
// val second = Category(1, "Bikes")

// println(first == second)
// println(first)


// // // DEMO: data classes have a copy() method
// // data class ProductSummary(
// //     val id: Int,
// //     val name: String,
// //     val price: Double
// // ) {
// //     fun printSummary() {
// //         println("$name costs $$price")
// //     }
// // }

// // val original = ProductSummary(101, "Mountain Bike", 900.0)
// // // val discounted = original.copy(price = 810.0)

// // println(original)
// // // println(discounted)

// // original.printSummary()

// // // DEMO: destructuring
// // // val id = original.id
// // // val name = original.name
// // // val price = original.price

// // val (x, y, z) = original

// // println("$x: $y costs $$z")




// // When should I use data class vs. "ordinary" class?
// // in general use data class when receiving values from an API
// // (whenever we are just modeling data)
// // and use ordinary classes when it needs to own changing state
// // or major behavior

// // ProductSummary we just built is perfect example of data class

// // "ordinary" class:
// class ShoppingCart {
//     private val items = mutableListOf<ProductSummary>()
    
//     fun add(product: ProductSummary) {
//         items.add(product)
//     }
    
//     fun itemCount(): Int = items.size
    
// }


// DEMO: enums

// in Kotlin, enumerations are instances of a class

// enum class Direction {
//     NORTH,
//     SOUTH,
//     EAST,
//     WEST
// }
// // by convention, the values are uppercased

// val currentDirection = Direction.SOUTH
// println(currentDirection)

// // enum constant is an instance of the enum class
// // because of this, each enum instance could hold data

// enum class ThemeColor(val hex: String) {
//     RED("#FF0000"),
//     GREEN("#00FF00"),
//     BLUE("#0000FF")
// }

// // every Kotlin enum automatically inherits from the base Enum class
// // therefore, they have several built-in properties and methods

// var color = ThemeColor.GREEN

// println(color.name)
// println(color.ordinal)

// println(ThemeColor.entries)

// val colorString = "BLUE"
// color = enumValueOf<ThemeColor>(colorString)
// println(color)


// interface Printable {
//     fun printInfo()
// }

// // since enum values are instances of an enum class,
// // the enum class could implement an interface
// // and each value can have its own implementation of the interface!!!!

// enum class CommunicationProtocolState: Printable {
//     WAITING {
//         override fun printInfo() {
//             println("Waiting for connection . . .")
//         }
//     },
//     TALKING {
//         override fun printInfo() {
//             println("Currently transmitting data.")
//         }
//     }
// }

// var state = CommunicationProtocolState.WAITING
// state.printInfo()
// state = CommunicationProtocolState.TALKING
// state.printInfo()


// DEMO: sealed class

// Kotlin enums are not as flexible as Swift enums
// (where each value can store different kinds of data)
// but, Kotlin sealed classes can do that!

// data class ProductSummary(
//     val id: Int,
//     val name: String,
//     val price: Double
// )

// // we want to model the possible outcomes from querying
// // for a product summary from an API
// sealed class ProductLookupResult {
//     data class Failed(val message: String): ProductLookupResult()
//     data class Found(val product: ProductSummary): ProductLookupResult()
//     data object NotFound: ProductLookupResult()
// }

// // process a product lookup outcome
// fun displayResult(result: ProductLookupResult) {
//     when (result) {
//         is ProductLookupResult.Found ->
//             println("Found: ${result.product.name}")
//         is ProductLookupResult.NotFound ->
//             println("No product matching that ID")
//         is ProductLookupResult.Failed ->
//             println("Could not load product: ${result.message}")
//     }
// }

// val product = ProductSummary(101, "Mountain Bike", 900.0)
// displayResult(ProductLookupResult.Found(product))

// displayResult(ProductLookupResult.NotFound)

// displayResult(ProductLookupResult.Failed("No internet connection"))


// DEMO: another sealed class example

// model the screen state
// data class ProductSummary(
//     val id: Int,
//     val name: String,
//     val price: Double
// )

// sealed class ProductScreenState {
//     data object Loading: ProductScreenState()
//     data class Success(val products: List<ProductSummary>): ProductScreenState()
//     data class Error(val message: String): ProductScreenState()
// }

// fun screenMessage(state: ProductScreenState): String =
//     when (state) {
//         is ProductScreenState.Loading -> "Loading products..."
//         is ProductScreenState.Success -> "Showing ${state.products.size} products"
//         is ProductScreenState.Error -> "Unable to load products: ${state.message}"
//     }

// println(screenMessage(ProductScreenState.Loading))

// println(screenMessage(ProductScreenState.Error("Invalid credentials")))

// val products = listOf<ProductSummary>()
// println(screenMessage(ProductScreenState.Success(products)))



// DEMO: extension methods

data class ProductSummary(
    val id: Int,
    val name: String,
    val price: Double
)

// suppose (from our API) product names arrive with extraneous whitespace
// and all lowercase

// we can implement this behavior for all strings, by adding a method to String
fun String.toDisplayName(): String = trim().replaceFirstChar { it.uppercase() }

val rawName = "   mountain bike     "
println(rawName.toDisplayName())

// add an extension method to our ProductSummary class
fun ProductSummary.isPremium(): Boolean = price >= 1000

val bike = ProductSummary(123, "Mountain Bike", 1250.0)
println("${bike.name} is a premium product: ${bike.isPremium()}")


// why might I use an extension method on a class I own?
//  (usually, Separation of Concerns)
//  - keep the class focused on its core identity and behavior
//  - put supplementary operations or formatting helpers into an extension
//  - e.g. a User class should not need to know how to map itself to a DTO
//  - module-specific behavior separate from the class' core behavior







