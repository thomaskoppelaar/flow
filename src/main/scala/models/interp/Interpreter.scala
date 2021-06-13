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
        // Data values
        case NumC(n : Int) => NumV(n)
        case StringC(c) => StringV(c)
        case NilC() => NilV()
        case ValC(v) => v

        case IdC(c) => throw new InterpException("Tried to interpret ID but no matching value exists: " + c)
        // Program control
        // CellC: interp() the current expression with the previous element,
        // And use that result as the "previous" element for the next CoreExpr.
        case CellC(e : CoreExpr, n : CoreExpr) => n match {
            case MarkerC(_) => interp(n, interp(e, p))
            case NilC() => interp(e, p)
            case _ => throw new InterpException("Cell encountered next object that was not a marker or Null: " + n.toString())
            
        }

        // MarkerC: Pass through data
        // No checks done to see as to what element the data is being passed onto
        case MarkerC(e : CoreExpr) => interp(e, p)

        case FuncC(c, b) => interp(subst(c, p, b), p)

        // Arithmetic operations
        case PlusC(l, r) => (interp(l, p), interp(r, p)) match {
            case (NumV(x), NumV(y)) => NumV(x + y)
            case (StringV(x), StringV(y)) => StringV(x.concat(y))
            case _ => throw new InterpException("PlusC err; 2 numbers or strings required")
        }
        case MinC(l, r) => (interp(l, p), interp(r, p)) match {
            case (NumV(x), NumV(y)) => NumV(x - y)
            case _ => throw new InterpException("MinC err; 2 numbers required")
        }
        case MultC(l, r) => (interp(l, p), interp(r, p)) match {
            case (NumV(x), NumV(y)) => NumV(x * y)
            case (StringV(x), NumV(y)) => StringV(x.repeat(y))
            case _ => throw new InterpException("MultC err; 2 numbers or string and number required")
        }
        case DivC(l, r) => (interp(l, p), interp(r, p)) match {
            case (NumV(x), NumV(y)) if y != 0 => NumV(x / y)
            case _ => throw new InterpException("DivC err; 2 numbers required or division by 0")
        }

        
        case _ => throw new InterpException("Unknown Core syntax!")

    }

    def subst(f : String, t: Value, e : CoreExpr) : CoreExpr = e match {
        case IdC(c) if f == c => ValC(t)
        case PlusC(l, r) => PlusC(subst(f, t, l), subst(f, t, r))
        case MinC(l, r) => MinC(subst(f, t, l), subst(f, t, r))
        case MultC(l, r) => MultC(subst(f, t, l), subst(f, t, r))
        case DivC(l, r) => DivC(subst(f, t, l), subst(f, t, r))

        case _ => e

    }
}