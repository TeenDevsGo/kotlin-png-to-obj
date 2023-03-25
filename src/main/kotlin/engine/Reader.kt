package engine

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


class Reader {
    fun run(path: String?): Array<Array<Color>> {

        // Get image
        var image: BufferedImage? = null
        try {
            image = ImageIO.read(path?.let { File(it) })
        } catch (e: IOException) {
            e.printStackTrace()
        }

        assert(image != null)

        val w = image!!.width
        val h = image.height

        // Check if image is legal size
        if (w > 255 || h > 255 || h < 1 || w < 1) {
            System.err.println("Image resolution is too high. Width and height must be in range [1 - 255]")
            System.exit(0)
        }
        val pixels = Array(w) {
            arrayOfNulls<Color>(
                h
            )
        }

        // Get pixels
        for (i in 0 until w) {
            for (j in 0 until h) {
                pixels[i][j] = Color(image.getRGB(i, j), true)
            }
        }
        return pixels
    }
}