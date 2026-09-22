
// DEMO: why nullable types exist

// var phoneNumber: String? = "555-4444"

// phoneNumber = null
// println(phoneNumber)


// DEMO: the compiler stops at unsafe access

// var phoneNumber: String? = "555-0412"

// println(phoneNumber?.length)

// phoneNumber = null
// println(phoneNumber?.length)        // String?.length will be Int?
//                                     // whereas 
//                                     // String.length will be Int


// DEMO: The Elvis operator
// val phoneNumber: String? = null //"555-0412"

// val displayNumber = phoneNumber ?: "No phone number on file"
// val digitCount = phoneNumber?.length ?: 0

// println(displayNumber)
// println(digitCount)


// DEMO: a more realistic safe call
// chaining across object graph
class Department(val name: String)

// val department = Department("Sales")

class Employee(
    val name: String,
    val department: Department?
)

// val employee = Employee("Jordan", department)
// val departmentName = employee.department?.name ?: "Unassigned"

// println("${employee.name}: $departmentName")

// val employee2 = Employee("Alicia", null)
// val dept2 = employee2.department?.name ?: "Unassigned"

// println("${employee2.name}: $dept2")

// // extending the chain:
// val deptNameLength = employee.department?.name?.length ?: 0


// DEMO: Smart Casts 
//      (an explicit null check gives the compiler enough info
//      to treat a nullable value as non-null within the branch)
// fun printDepartment(departmentName: String?) {
    
//     if (departmentName != null) {
//         // within this branch, we can work with departmentName 
//         // as if it were String
//         println(departmentName.uppercase())
//     } else {
//         println("Department not assigned")
//     }
    
// }

// val deptName: String? = "Sales"
// printDepartment(deptName)


// DEMO: Smart casting variation
//      (a null check with an early return can cast the rest of the function)

// fun printDepartment(departmentName: String?) {
    
//     if (departmentName == null) {
//         println("Department not assigned")
//         return
//     }
    
//     // for the rest of the function, departmentName is not nullable
//     println(departmentName.uppercase())
    
// }

// val deptName: String? = "Sales"
// printDepartment(deptName)

// class EmployeeProfile {
//     var phoneNumber: String? = null
// }

// fun printPhone(profile: EmployeeProfile) {
//     if (profile.phoneNumber != null) {
//         println(profile.phoneNumber.length)
//     }
// }

// val profile = EmployeeProfile()
// profile.phoneNumber = "555-4231"
// printPhone(profile)


// // DEMO: force unwrapping  !!       - avoid this!!
// //          (non-null assertion operator)
// val managerName: String? = null

// // println(managerName!!.uppercase())
// println(managerName?.uppercase() ?: "No manager assigned")


class EmployeeProfile(
    val name: String,
    val title: String?,
    val phoneNumber: String?,
    val department: Department?
)

fun displayProfile(profile: EmployeeProfile) {
    
    println(profile.name)
    
    val title = profile.title ?: "Title unavailable"
    println("Title: $title")
    
    val dept = profile.department?.name ?: "Unassigned"
    println("Department: $dept")
    
    if (profile.phoneNumber != null) {
        println("Phone: ${profile.phoneNumber}")
    } else {
        print("Phone: Not provided")
    }
}

val profile = EmployeeProfile(
    name = "Jordan Lee",
    title = "Mobile Developer",
    phoneNumber = null,
    department = Department("Engineering")
)

displayProfile(profile)
