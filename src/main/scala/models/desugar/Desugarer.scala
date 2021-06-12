import ExtExpr._
import CoreExpr._

class DesugarException(m : String) extends Exception(m)



package object Desugarer {

    def desugar(x : ExtExpr)  : CoreExpr = x match {

        // Data types
        case NilExt() => NilC()
        case StringExt(c) => StringC(c)
        case NumExt(n : Int) => NumC(n)

        // Program control
        case CellExt(e: ExtExpr, n: ExtExpr) => CellC(desugar(e), desugar(n))
        case MarkerExt(n: ExtExpr) => MarkerC(desugar(n))

        // Binary Operators
        case BinOpExt(c, l, r) => c match {
            case "+" => PlusC(desugar(l), desugar(r))
            case "-" => MinC(desugar(l), desugar(r))
            case "*" => MultC(desugar(l), desugar(r))
            case "/" => DivC(desugar(l), desugar(r))
            case _ => throw new DesugarException("Unknown symbol for binary operator:" + c)
        }
        
        
        case _ => throw new DesugarException("Unknown abstract syntax!")
    }

}