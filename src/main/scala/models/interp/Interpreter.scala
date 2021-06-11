import CoreExpr._
import Value._

class InterpException(m : String) extends Exception(m)



package object Interpreter {

    /**
      * Interpreter method. Transforms CoreExpressions 
      *
      * @param x The expression to parse.
      * @param p The value gained from the previous expression.
      * @return a Value object.
      */
    def interp(x : CoreExpr, p: Value = NilV()): Value = x match {
        case CellC(e : CoreExpr, n : CoreExpr) => interp(n, interp(e, p))
        case NumC(n : Int) => NumV(n)
        case NilC() => p
        case MarkerC(e : CoreExpr) => interp(e, p)
        case PlusC(l, r) => (interp(l, p), interp(r, p)) match {
            case (NumV(x), NumV(y)) => NumV(x + y)
            case _ => throw new InterpException("PlusC err; 2 numbers required")
        }
        case MinC(l, r) => (interp(l, p), interp(r, p)) match {
            case (NumV(x), NumV(y)) => NumV(x - y)
            case _ => throw new InterpException("MinC err; 2 numbers required")
        }
        case MultC(l, r) => (interp(l, p), interp(r, p)) match {
            case (NumV(x), NumV(y)) => NumV(x * y)
            case _ => throw new InterpException("MultC err; 2 numbers required")
        }
        case DivC(l, r) => (interp(l, p), interp(r, p)) match {
            case (NumV(x), NumV(y)) if y != 0 => NumV(x / y)
            case _ => throw new InterpException("DivC err; 2 numbers required or division by 0")
        }

        
        case _ => throw new InterpException("Unknown Core syntax!")

    }
}