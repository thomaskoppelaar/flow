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
        
        case _ => throw new InterpException("Unknown Core syntax!")

    }
}