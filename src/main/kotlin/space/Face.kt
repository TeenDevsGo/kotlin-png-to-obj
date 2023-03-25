package space

class Face(x: Double, y: Double, z: Double, material: Material) {
    var vertices = arrayOfNulls<Vertex>(4)
    var material: Material

    // Construct from pixels
    init {
        vertices[0] = Vertex(x - H, y - H, z)
        vertices[1] = Vertex(x + H, y - H, z)
        vertices[2] = Vertex(x - H, y + H, z)
        vertices[3] = Vertex(x + H, y + H, z)
        this.material = material
    }

    val markup: String
        // Line to be written in obj file
        get() = "f " + vertices[1]!!.id + " " + vertices[0]!!.id + " " + vertices[2]!!.id + " " + vertices[3]!!.id

    companion object {
        private const val H = 0.5
    }
}