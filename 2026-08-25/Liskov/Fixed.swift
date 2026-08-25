protocol Shape {
    func area() -> Double
}

protocol Resizable {
    var width: Double { get set }
    var height: Double { get set }
}

struct Rectangle: Shape, Resizable {
		var width: Double
		var height: Double

		func area() -> Double { 
            width * height 
        }
}

struct Square: Shape {
	var side: Double
    func area() -> Double { 
        side * side
    }
}
