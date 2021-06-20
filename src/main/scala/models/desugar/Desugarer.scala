import ExtExpr._
import CoreExpr._

class DesugarException(m : String) extends Exception(m)



package object Desugarer {

    def desugar(x: ExtExpr): CoreExpr = x match {
        
        // Data types
        case NilExt() => NilC()
        case StringExt(c) => StringC(c)
        case NumExt(n) => NumC(n)
        case IdExt(c) => IdC(c)
        case BoolExt(e) => BoolC(e)

        // Program control
        case CellExt(e, n) => CellC(desugar(e) match {
            case f@FuncC(id, b) => AppC(f, null)
            case body => AppC(FuncC("_", body), null)
        }, desugar(n))
        case MarkerExt(n) => MarkerC(desugar(n))

        // Binary Operators
        case BinOpExt(c, l, r) => c match {
            case "+" => PlusC(desugar(l), desugar(r))
            case "-" => MinC(desugar(l), desugar(r))
            case "*" => MultC(desugar(l), desugar(r))
            case "/" => DivC(desugar(l), desugar(r))

            case "=" => EqC(desugar(l), desugar(r))
            case "<" => LtC(desugar(l), desugar(r))
            case ">" => IfC(EqC(desugar(l), desugar(r)), BoolC(false), IfC(LtC(desugar(l), desugar(r)), BoolC(false), BoolC(true)))
            case "<=" => IfC(EqC(desugar(l), desugar(r)), BoolC(true), IfC(LtC(desugar(l), desugar(r)), BoolC(true), BoolC(false)))
            case ">=" => IfC(EqC(desugar(l), desugar(r)), BoolC(true), IfC(LtC(desugar(l), desugar(r)), BoolC(false), BoolC(true)))

            case "and" => IfC(desugar(l), IfC(desugar(r), BoolC(true), BoolC(false)), BoolC(false))
            // For now, both sides of the equation should be evaluated in order to catch 'errors' like `or true "c"`
            case "or" => IfC(desugar(l), IfC(desugar(r), BoolC(true), BoolC(true)), IfC(desugar(r), BoolC(true), BoolC(false)))
            
            case _ => throw new DesugarException("Unknown symbol for binary operator:" + c)
        }

        case UnOpExt(c, e) => c match {
            case "not" => IfC(desugar(e), BoolC(false), BoolC(true))
        }

        case IfExt(c, t, f) => IfC(desugar(c), desugar(t), desugar(f))
        
        case FdExt(id, b) => FuncC(id, desugar(b))
        case AppExt(f, p) => AppC(desugar(f), desugar(p))

        case IoExt(null) => IoC(null)
        case IoExt(c) => IoC(desugar(c))
        
        case err => throw new DesugarException("Unknown abstract syntax: " + err.toString())
    }

}