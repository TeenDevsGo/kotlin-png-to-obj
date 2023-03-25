package engine

import space.Face
import space.Material
import java.awt.Color
import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.io.UnsupportedEncodingException


class Writer {
    var path: String? = null
    lateinit var data: Array<Array<Color>>
    private val faces = ArrayList<Face>()
    private val vertices = ArrayList<String>()
    private val materials = ArrayList<Material>()

    // Generates path for a new file
    private fun filepath(extension: String): String {
        return path?.let { File(it).parent } + "/" + filename() + extension
    }

    // Get file name without extension
    private fun filename(): String? {
        return path?.let { File(it).name.replaceFirst("[.][^.]+$".toRegex(), "") }
    }

    // File header comments
    private fun setHeaders(writer: PrintWriter?) {
        for (line in HEADERS) {
            writer!!.println("# $line")
        }
        writer!!.println("")
    }

    // Write .obj file
    fun generateObj() {
        // Prepare writer
        var writer: PrintWriter? = null
        try {
            writer = PrintWriter(filepath(".obj"), "UTF-8")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        assert(writer != null)
        setHeaders(writer)

        // Get each pixel
        println("1/4 Creating model..")
        for (i in data.indices) {
            for (j in data[i].indices) {

                // Only process pixels with no transparency
                if (data[i][j].alpha == 255) {

                    // Make material from pixel color
                    var material: Material? = null
                    var duplicate = false

                    // Check if material exists
                    for (m in materials) {
                        if (m.name == materialName(data[i][j])) {
                            material = m
                            duplicate = true
                            break
                        }
                    }

                    // Create material if not exists
                    if (!duplicate) {
                        val newMaterial = Material(materialName(data[i][j]), data[i][j])
                        materials.add(newMaterial)
                        material = newMaterial
                    }

                    // Create faces
                    faces.add(Face(i.toDouble(), j.toDouble(), 0.0, material!!))

                    // Print preview ASCII art
                    //System.out.print("10");
                } //else System.out.print("  ");
            } //System.out.println();
        }

        // Declare material file
        writer!!.println("# Materials")
        writer.println("mtllib " + filename() + ".mtl")
        writer.println("")

        // Write vertices
        println("2/4 Writing vertices. This may take some time..")
        writer.println("# Vertices")
        for (face in faces) {
            for (vertex in face.vertices) {
                // Add and write vertice if not already written
                val markup: String = vertex!!.markup
                if (!vertices.contains(markup)) {
                    vertices.add(markup)
                    writer.println(markup)
                }
                // Give id to each vertex
                vertex.id = vertices.indexOf(markup) + 1
            }
        }

        // Write faces
        println("3/4 Writing faces..")
        var currentMaterial = ""
        writer.println("")
        writer.println("# Faces")
        for (face in faces) {
            if (currentMaterial != face.material.name) {
                writer.println("usemtl " + face.material.name)
                currentMaterial = face.material.name
            }
            writer.println(face.markup)
        }

        // Save and close
        writer.close()
        println("Output: " + filepath(".obj"))
    }

    // Write .mtl file
    fun generateMtl() {
        // Prepare writer
        var writer: PrintWriter? = null
        try {
            writer = PrintWriter(filepath(".mtl"), "UTF-8")
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        assert(writer != null)
        setHeaders(writer)

        // Define and write materials
        println("4/4 Writing materials..")
        for (material in materials) {
            writer!!.println("newmtl " + material.name) // Declare
            writer.println("illum 1") // Illumination
            writer.println("Kd " + material.diffuse) // Diffuse
            writer.println("Ka 0.00 0.00 0.00") // Ambient
            writer.println("Ks 0.00 0.00 0.00") // Specular
            writer.println("Tf 1.00 1.00 1.00")
            writer.println("") // Space
        }

        // Save and close
        writer!!.close()
        println("Output: " + filepath(".mtl"))
    }

    // Create material name from Color object
    private fun materialName(color: Color): String {
        val r = Integer.toHexString(color.red)
        val g = Integer.toHexString(color.green)
        val b = Integer.toHexString(color.blue)
        return "mat_" + (if (r.length == 1) "0$r" else r) +
                (if (g.length == 1) "0$g" else g) +
                if (b.length == 1) "0$b" else b
    }

    companion object {
        // Header comments for result files
        private val HEADERS = arrayOf(
            "This file is generated using .PNG to .OBJ converter",
            "Source: https://github.com/jonivesto/java-png-to-obj",
            "Website: https://www.jonivesto.com"
        )
    }
}