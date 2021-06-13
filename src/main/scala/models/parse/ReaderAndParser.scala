import StringExpr._
import ExtExpr._
import scala.util.parsing.combinator._


class ParseException(m : String) extends Exception(m)

package ReaderAndParser {

    // Parser. Takes in string expressions and turns them into abstract syntax objects.
    package object Parser {

        
        /**
          * Parse method used for program flow, cells, and flow markers.
          * Separated from parsing cell contents to avoid hassle with lists.
          * @param i The element to parse.
          * @param n The element that is next in the program flow.
          * @return An ExtExpr.
          */
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
                case _ => throw new ParseException("Parse: Unknown symbol or symbol not allowed in this location: " + x)
            }

            // Cell
            case SCell(x) => CellExt(cellparse(Left(x)), n)

            // In case of no known expression
            case err => throw new ParseException("Parse: Unknown symbol found during parsing: " + err)
        }

        /**
          * Method for parsing elements in a cell. 
          *
          * @param i The element to parse.
          * @return the parsed expression.
          */
        def cellparse(i: Either[List[StringExpr], StringExpr]) : ExtExpr = i match {
            // Single expression to parse
            case Left(a :: Nil) => cellparse(Right(a))
            
            // Empty cell, aka {}
            case Left(Nil) => NilExt()

            case Left(SSym(c) :: SSym(":") :: rest) => FdExt(c, cellparse(Left(rest)))

            case Left(SSym(c) :: rest) => c match {
                    case "+" | "-" | "*" | "/" if rest.length == 2 => BinOpExt(c, cellparse(Right(rest(0))), cellparse(Right(rest(1))))
                    
                    case _ => throw new ParseException("CellParse: Unknown symbol at the start of cell list: " + c)
            }

            // Number
            case Right(SNum(x)) => NumExt(x)


            case Right(SSym(x)) => x match {
                case "nil" => NilExt()

                // String syntax
                case a if a.startsWith("\"") && a.endsWith("\"") => StringExt(a.substring(1, a.length()-1))
                
                // todo: reserved words list
                case _ => IdExt(x)
                // case _ => throw new ParseException("CellParse: Unknown symbol or symbol not allowed in this location: " + x)
            }

           
            
            case err => throw new ParseException("CellParse: Unknown symbol found during cell parsing: " + err.toString())
            
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

        def sprogram : Parser[StringExpr] = (cell | symbol).+                    ^^ SProgram

        // An SList is a combination of cells and symbols
        def slist  : Parser[StringExpr] = "(" ~> (num | symbol | slist).* <~ ")" ^^ SList

        // A symbol is any set of characters that don't match whitespace or {}
        def symbol : Parser[StringExpr] = not(wholeNumber) ~> ":|[^:{}()\\s]+".r    ^^ SSym

        // A Cell is surrounded by {} and does not contain cells itself.
        def cell   : Parser[StringExpr] = "{" ~> (num | symbol | slist).* <~ "}" ^^ {s => SCell(s) }

        // A number is any whole number.
        def num    : Parser[StringExpr] = wholeNumber                            ^^ {s => SNum(s.toInt)}

        

    }

}