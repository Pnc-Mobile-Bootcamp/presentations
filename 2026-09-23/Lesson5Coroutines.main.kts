// in full Kotlin program, importing external libraries requires 
// a Gradle or Maven build structure
// However, Kotlin scripts don't have that build structure
// What they do have is a built-in feature to let us tell the
// compiler about our depdency and how to get it

// step 1: the filename must end in .main.kts not just .kts
// step 2: on each import, add the @file:DependsOn annotation

@file:DependsOn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
import kotlinx.coroutines.*

import kotlin.system.measureTimeMillis


// DEMO: suspend function

suspend fun loadProductName(): String {
    println("Requesting product ...")
    // simulate waiting for an API response
    delay(3000)
    return "Mountain Bike"
}

// fun failing_do_work() = {
//     println("Before request")
//     val name = loadProductName()
//     println("Loaded: $name")
// }

// failing_do_work()

fun doWork() = runBlocking {    // runBlocking creates a coroutine then executes our lambda
    println("Before request")
    val name = loadProductName()
    println("Loaded: $name")
}

// doWork()

// DEMO: "launch" starts work
fun launchThings() = runBlocking {
    // first task
    launch {
        println("Product request starting")
        delay(1000)
        // Thread.sleep(1000)
        println("Product request finished")
    }
    
    // second task
    launch {
        repeat(3) { step -> 
            println("UI can do other work: $step")
            delay(250)
        }
    }
    
    Unit        // make sure the lambda return absolutely nothing
                // so we don't get that extraneous line of output
}

// launchThings()


// DEMO: "launch" starts work; "join" waits for its completion

fun joinDemo() = runBlocking {
    
    val job = launch {
        delay(1500)     // simulate querying an API
        println("Inventory refresh complete.")
    }
    
    println("Refresh started")
    // wait here (don't do anything more) until the refresh is done
    job.join()
    println("Safe to continue")
}

// joinDemo()


// DEMO: "async" returns a result and can shorten independent work

suspend fun loadProduct(): String {
    delay(800)
    return "Mountain Bike"
}

suspend fun loadInventory(): Int {
    delay(600)
    return 12
}

fun demoAsync() = runBlocking {
    
    val sequentialTime = measureTimeMillis {
        val product = loadProduct()
        val inventory = loadInventory()
        println("$product: $inventory in stock")
    }
    
    val concurrentTime = measureTimeMillis {
        // cannot use "launch" here, b/c it does not return a value
        // and we want the value returned by loadProduct() and loadInventory()
        
        val product = async {
            loadProduct()
        }
        
        val inventory = async {
            loadInventory()
        }
        
        println("${product.await()}: ${inventory.await()} in stock")
        
    }
    
    println("Sequential: $sequentialTime ms")
    println("Concurrent: $concurrentTime ms")
    
}

// demoAsync()


// DEMO: coroutineScope - creating it directly

// simulating a common pattern when loading screen (view)

data class ProductScreenData(
    val name: String,
    val quantity: Int
)

suspend fun loadProductScreen(): ProductScreenData = coroutineScope {
    
    val productTask = async { loadProduct() }
    val quantityTask = async { loadInventory() }
    
    ProductScreenData(
        name = productTask.await(),
        quantity = quantityTask.await()
    )
    
}

fun demoScope() = runBlocking {
    println("Opening the product screen")
    val screen = loadProductScreen()
    println("Ready: $screen")
}

// demoScope()


// DEMO: cancelling an operation (e.g. when user leaves a screen)
fun cancelWhenLeaving() = runBlocking {
    
    val loadingJob = launch {
        // try {
            println("Loading the product screen ...")
            delay(5000)
            println("Product screen displayed")
        // }
        // catch (e: Exception) {
        //     println("An error occurred: ${e.message}")
        // }
        // finally {
        //     println("Screen work has ended")
        // }
    }
    
    delay(500)
    println("User navigated away")
    loadingJob.cancelAndJoin()
    println("Navigation complete")
    
}

cancelWhenLeaving()



