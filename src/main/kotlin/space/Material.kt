package space

import java.awt.Color


class Material(var name: String, private val color: Color) {

    val diffuse: String
        // Return string to be written in .mtl file
        get() = (roundTwoPlaces(color.red.toDouble() / 255).toString() + " "
                + roundTwoPlaces(color.green.toDouble() / 255) + " "
                + roundTwoPlaces(color.blue.toDouble() / 255))

    companion object {
        // Custom round method
        private fun roundTwoPlaces(value: Double): Double {
            val scale = Math.pow(10.0, 2.0)
            return Math.round(value * scale) / scale
        }
    }
}