import ReaderAndParser._
import StringExpr._
import ExtExpr._
import CoreExpr._
import Value._

object Main extends App {


    // String that contains the program
    val program: String = "{- 3 2}"

    // Read input code, transform into string expressions
    val read_program: StringExpr = Reader.read(program)

    val parsed_program: ExtExpr = Parser.parse(read_program)

    val desugared_program: CoreExpr = Desugarer.desugar(parsed_program)

    val interped_program: Value = Interpreter.interp(desugared_program)

    println("Input Program: " + program)
    println("String Exp: " + read_program.toString())
    println("Parsed: " + parsed_program.toString())
    println("Desugared: " + desugared_program.toString())
    println("Interpreted: " + interped_program.toString())

}