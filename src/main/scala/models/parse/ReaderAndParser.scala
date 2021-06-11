import StringExpr._
import ExtExpr._
import scala.util.parsing.combinator._


class ParseException(m : String) extends Exception(m)

package ReaderAndParser {

    // Parser. Takes in string expressions and turns them into abstract syntax objects.

    package object Parser {

        def parse(i : StringExpr, n  : ExtExpr = NilExt()): ExtExpr = i match {

            // List of String expressions - iterated from right to left.
            // `b` is already parsed, and treated as the `next` element for the left element, `a`. 
            case SProgram(x) => x.foldRight(n)(
                (a, b) => parse(a, b)
            )

            // Symbols
            case SSym(x) => x match {
                
                // Default flowmarker
                case "=>" => MarkerExt(n)
                case _ => throw new ParseException("Unknown symbol or symbol not allowed in this location: " + x)
            }

            // Cell
            case SCell(x) => CellExt(cellparse(x), n)

            


            // In case of no known expression
            case err => throw new ParseException("Unknown symbol found during parsing: " + err)
        }

        def cellparse(i: StringExpr) : ExtExpr = i match {
            // Empty cell, aka {}
            case SList(x) if x.length == 0 => NilExt()
            case SList(x) => x.head match {
                case SSym(c) => c match {
                    case "+" | "-" | "*" | "/" if x.length == 3 => BinOpExt(c, cellparse(x(1)), cellparse(x(2)))
                    case _ => throw new ParseException("Unknown symbol at the start of list: " + c)
                }

                // Only one item in the list, aka a cell like {2}
                case a if x.length == 1 => cellparse(a)
                case _ => throw new ParseException("Unknown symbol in list: " + x.head)
            }
            
            case SNum(x) => NumExt(x)
            
            case err => throw new ParseException("Unknown symbol found during cell parsing: " + err.toString())
            
        }
    }

    package object Reader extends JavaTokenParsers {


        def read(text: String): StringExpr = {

            val result = parseAll(sexpr,text)

            result match {
                case Success(r,_) => r
                case Failure(msg,n) => 
                    sys.error(msg+" (input left: \""+n.source.toString.drop(n.offset)+"\")")
                case Error(msg, n) =>
                    sys.error(msg+" (input left: \""+n.source.toString.drop(n.offset)+"\")")
            }
        }

        
        // String Expression is either a number, a symbol, a list or a cell
        def sexpr  : Parser[StringExpr] = (num | symbol | sprogram | cell)

        def sprogram : Parser[StringExpr] = (cell | symbol).+ ^^ SProgram

        // An SList is a combination of cells and symbols
        def slist  : Parser[StringExpr] = "(" ~> (num | symbol | slist).* <~ ")"         ^^ SList

        // A symbol is any set of characters that don't match whitespace or {}
        def symbol : Parser[StringExpr] = not(wholeNumber) ~> "[^{}()\\s]+".r      ^^ SSym

        // A Cell is surrounded by {} and does not contain cells itself.
        def cell   : Parser[StringExpr] = "{" ~> (num | symbol | slist).* <~ "}" ^^ {s => SCell(SList(s)) }

        // A number is any whole number.
        def num    : Parser[StringExpr] = wholeNumber                            ^^ {s => SNum(s.toInt)}

        

    }

}