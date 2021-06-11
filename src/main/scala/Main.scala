import ReaderAndParser._
import StringExpr._
import ExtExpr._
import CoreExpr._
import Value._

object Main extends App {


    // String that contains the program
    val program: String = "{1} => {2}=>{3} => {4}"

    // Read input code, transform into string expressions
    val read_program: StringExpr = Reader.read(program)

    val parsed_program: ExtExpr = Parser.parse(read_program)

    val desugared_program: CoreExpr = Desugarer.desugar(parsed_program)

    val interped_program: Value = Interpreter.interp(desugared_program)

    println(program)
    println(read_program.toString())
    println(parsed_program.toString())
    println(desugared_program.toString())
    println(interped_program.toString())

}