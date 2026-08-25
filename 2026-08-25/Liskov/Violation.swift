class Rectangle {
		var width: Double
		var height: Double
		init(width: Double, height: Double) { 
            self.width = width; 
            self.height = height; 
        }
		func area() -> Double { width * height }
}

class Square: Rectangle {
		override var width: Double {
				didSet { height = width }
		}
}

func testArea(_ shape: Rectangle) {
		shape.height = 4; 
        shape.width = 5;
		assert(shape.area() == 20)							
        // FAILS for Square: area() returns 25
}
