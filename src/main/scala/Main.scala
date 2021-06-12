import ReaderAndParser._
import StringExpr._
import ExtExpr._
import CoreExpr._
import Value._

object Main extends App {


    // String that contains the program
    val program: String = """{* "a" 5}"""
    println("Input Program: " + program)

    // Read input code, transform into string expressions
    val read_program: StringExpr = Reader.read(program)
    println("String Exp: " + read_program.toString())

    val parsed_program: ExtExpr = Parser.parse(read_program)
    println("Parsed: " + parsed_program.toString())

    val desugared_program: CoreExpr = Desugarer.desugar(parsed_program)
    println("Desugared: " + desugared_program.toString())

    val interped_program: Value = Interpreter.interp(desugared_program)
    println("Interpreted: " + interped_program.toString())

}