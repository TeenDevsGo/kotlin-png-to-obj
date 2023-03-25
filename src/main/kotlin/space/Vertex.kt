package space

class Vertex internal constructor(private val x: Double, private val y: Double, private val z: Double) {
    var id = 0

     val markup: String
        // Line to be written in obj file
        get() = "v $x $y $z"
}