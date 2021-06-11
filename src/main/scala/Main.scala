import ReaderAndParser._
import StringExpr._
import ExtEpr.ExtExpr

object Main extends App {


    // String that contains the program
    val program: String = "{1} => {2}=>{3}"

    // Read input code, transform into string expressions
    val read_program: StringExpr = Reader.read(program)

    val parsed_program: ExtExpr = Parser.parse(read_program)

    println(program)
    println(read_program.toString())
    println(parsed_program.toString())

}