
fun printHeader(title: String) {
    val divider = "-------------------------------------------"
    println("\n\n$divider")
    println(title)
    println("$divider\n")
}

// DEMO: a list

printHeader("List")

val fruits = listOf("Apple", "Banana", "Cherry", "Dragonfruit")

println(fruits)
println("The first fruit is ${fruits[0]}")

// fruits[0] = "Orange"

// ways of getting values from a list:
var firstFruit = fruits[0]
firstFruit = fruits.get(0)
firstFruit = fruits.first()

println("The first fruit is $firstFruit")

var maybeFirstFruit = fruits.firstOrNull()


// DEMO: mutable list

printHeader("Mutable List")

val languages = mutableListOf("Kotlin")

languages.add("Python")
println(languages)
languages.add(0, "Swift")
println(languages)

languages[1] = "C++"
println(languages)

languages.add(0, "Python")
println(languages)

languages.removeAll( listOf("Python", "C#", "Java") )
println(languages)

languages.removeAt(0)
println(languages)


// Demo: iteration

printHeader("Iteration")

val tools: List<String> = listOf("Git", "Gradle", "Pip", "Chocolatey")

// standard "for" loop
for (tool in tools) {
    println(tool)
}

// collections, such as List, have a forEach method
println("\nforEach\n")

// trailing lambda syntax, single parameter shorthand
tools.forEach {
    println(it)
}

println("\nindexed\n")

tools.forEachIndexed { index, elt ->
    println("Item at index $index is $elt")
}


// DEMO: Sets

printHeader("Sets")

val frameworks: Set<String> = setOf("Swift", "React", "Angular", "Swift", "Angular")
println(frameworks)

// frameworks.add("Something")

val numberSet = mutableSetOf(1, 2, 3)

numberSet.add(4)
numberSet.add(4)
numberSet.add(4)
numberSet.add(4)

println(numberSet)


// DEMO: Maps

printHeader("Maps")

val userRoles = mapOf(
    "admin" to 1,
    "moderator" to 2,
    "guest" to 3
)

println(userRoles["admin"])
println(userRoles["not-there"])


val scores = mutableMapOf(
    "Alice" to 90,
    "Bob" to 85
)

// add a new entry (key/value pair)
scores["Charlie"] = 95

// update an existing entry
scores["Alice"] = 92

scores.remove("Bob")

println(scores)


// DEMO: useful map functions

printHeader("Map functions")

val counts = mutableMapOf<String, Int>()

counts["Red"] = 3
counts["Blue"] = 1
counts["Yellow"] = 4

// .containsKey
if (counts.containsKey("Yellow")) {
    println("Yellow has come up ${counts["Yellow"]} times.")
}

// .getOrPut
val yellowCount = counts.getOrPut("Yellow") { 1 }
println("Yellow has come up $yellowCount times")

println(counts)

val greenCount = counts.getOrPut("Green") { 2 }
println("Green has come up $greenCount times")
println(counts)


// DEMO: Iterating a map

printHeader("Iterating a map")

for ( (key, value) in counts) {
    println("$key has come up $value times")
}


// DEMO: list methods

printHeader("List methods")

data class Product (
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val stock: Int
)

val products = mutableListOf(
    Product(101, "Mountain Bike", "Bikes", 900.0, 8),
    Product(102, "Helmet", "Accessories", 45.0, 24),
    Product(103, "Road Bike", "Bikes", 1200.0, 3),
    Product(104, "Gloves", "Accessories", 25.0, 0),
)

// the old-style, manual way
val lowStockManual = mutableListOf<Product>()
for (prod in products) {
    if (prod.stock < 5) {
        lowStockManual.add(prod)
    }
}

// filter method
val lowStock = products.filter { it.stock < 5 }


println("\nfilter for low stock")
println(lowStock)


// map method
println("\nprepare screen content with map")

val labels = products.map { product -> "${product.name}: $${product.price}" }

// for (label in labels) {
//     println(label)
// }

// labels.forEach {
//     println(it)
// }

labels.forEach(::println)


// combine methods by chaining them together

// we want the prices only for bikes
val bikePrices = products
    .filter { it.category == "Bikes" }
    .map { it.price }

val totalPrice = bikePrices.reduce { agg, price ->
    agg + price
}
println(totalPrice)

// sumOf is more convenient (if your summary operation is + )
val total = products
    .filter { it.category == "Bikes" }
    .sumOf { it.price }
println(total)



// DEMO: Generic class

printHeader("Generic class")

data class Page<T>(
    val items: List<T>,
    val pageNumber: Int,
    val totalPages: Int
)

val productPage = Page(
    items = products,
    pageNumber = 1,
    totalPages = 4
)

val namePage = Page(
    items = listOf("Jordan", "Morgan", "Drew"),
    pageNumber = 1,
    totalPages = 1
)

println(productPage.items.first().name)
println(namePage.items.first())


// DEMO: generic function

printHeader("Generic function")

fun <T> printPageSummary(page: Page<T>) {
    println("Page ${page.pageNumber} of ${page.totalPages}: ${page.items.size} items")
}

printPageSummary(productPage)
printPageSummary(namePage)



// a couple other examples I thought of that we didn't code in class
// DEMO: try/catch

printHeader("Try/Catch")

fun findProduct(input: String, products: List<Product>): Product? {
    val id = try {
        input.toInt()
    } catch (exception: NumberFormatException) {
        println("Enter a numeric product ID.")
        return null
    }

    return products.firstOrNull { it.id == id }
}

println(findProduct("102", products))    // Helmet product
println(findProduct("helmet", products)) // Message, then null
println(findProduct("999", products))    // null


// DEMO: a more realistic failure boundary

printHeader("Failure boundary")

fun loadProducts(shouldFail: Boolean): List<Product> {
    if (shouldFail) {
        throw IllegalStateException("Product service unavailable")
    }
    return products
}


try {
    val availableProducts = loadProducts(shouldFail = true)
        .filter { it.stock > 0 }
        .map { it.name }

    println(availableProducts)
} catch (exception: IllegalStateException) {
    println("Products could not be loaded. Please try again.")
}


















