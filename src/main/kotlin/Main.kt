import engine.Reader
import engine.Writer

class Main {

    private fun main(path: String){
        val reader = Reader()
        val writer = Writer()

        //init writer
        writer.data = reader.run(path)
        writer.path = path

        //generate files
        writer.generateObj()
        writer.generateMtl()
    }


}