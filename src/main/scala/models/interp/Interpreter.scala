import CoreExpr._
import Value._

class InterpException(m : String) extends Exception(m)



package object Interpreter {

    /**
      * Interpreter method. Transforms CoreExpressions into Values
      *
      * @param x The expression to parse.
      * @param p The value gained from the previous expression.
      * @return a Value object.
      */
    def interp(curr: CoreExpr, prev: Value = null): Value = curr match {
        // Data values
        case NumC(n) => NumV(n)
        case StringC(c) => StringV(c)
        case NilC() => NilV()
        case ValC(v) => v
        case BoolC(e) => BoolV(e)

        case IdC(c) => throw new InterpException("Tried to interpret ID but no matching value exists: " + c)

        // IO Operation - decide whether or not to ask for input or print
        case IoC(c) => prev match {
            // In the case of no previous arrow, ask for input
            case null => StringV(scala.io.StdIn.readLine("Input: "))

            // Else, we need to print something
            case _ => c match {
                // If null was passed, it means we encountered something like =>{}
                // In which case we print whatever was passed into the cell
                case null => println(prev.toString()); prev

                // Otherwise, print whatever was passed in the IoC object
                case _ => println(c.toString()); interp(c, prev)
            }
        }

        // Program control
        // CellC: interp() the current expression with the previous element,
        // And use that result as the "previous" element for the next CoreExpr.
        case CellC(e, next) => next match {
            case MarkerC(_) => interp(next, interp(e, prev))
            case NilC() => interp(e, prev)
            case _ => throw new InterpException("Cell encountered next object that was not a marker or Null: " + next.toString())
            
        }

        // MarkerC: Pass through data
        // No checks done to see as to what element the data is being passed onto
        case MarkerC(e) => interp(e, prev)

        // Todo: Functions are always applied, which makes it impossible to send along a function as a return type
        case FuncC(c, b) => interp(subst(c, prev, b), prev)

        // Arithmetic operations
        case PlusC(l, r) => (interp(l, prev), interp(r, prev)) match {
            case (NumV(x), NumV(y)) => NumV(x + y)
            case (StringV(x), StringV(y)) => StringV(x.concat(y))
            case _ => throw new InterpException("PlusC err; 2 numbers or strings required")
        }
        case MinC(l, r) => (interp(l, prev), interp(r, prev)) match {
            case (NumV(x), NumV(y)) => NumV(x - y)
            case _ => throw new InterpException("MinC err; 2 numbers required")
        }
        case MultC(l, r) => (interp(l, prev), interp(r, prev)) match {
            case (NumV(x), NumV(y)) => NumV(x * y)
            case (StringV(x), NumV(y)) => StringV(x.repeat(y))
            case _ => throw new InterpException("MultC err; 2 numbers or string and number required")
        }
        case DivC(l, r) => (interp(l, prev), interp(r, prev)) match {
            case (NumV(x), NumV(y)) if y != 0 => NumV(x / y)
            case _ => throw new InterpException("DivC err; 2 numbers required or division by 0")
        }

        // Conditional operators
        case IfC(c, t, f) => interp(c, prev) match {
            case BoolV(false) => interp(f, prev)
            case BoolV(true) => interp(t, prev)

            case _ => throw new InterpException("Conditional error; condition was not of type boolean or number")
        }
        case EqC(l, r) => (interp(l, prev), interp(r, prev)) match {
            case (NumV(x), NumV(y)) => BoolV(x == y)
            case _ => throw new InterpException("EqC err; 2 numbers required")
        }
        case LtC(l, r) => (interp(l, prev), interp(r, prev)) match {
            case (NumV(x), NumV(y)) => BoolV(x < y)
            case _ => throw new InterpException("LtC err; 2 numbers required")
        }

        
        case _ => throw new InterpException("Unknown Core syntax!")

    }

    def subst(from : String, to: Value, e : CoreExpr) : CoreExpr = e match {
        case IdC(c) if from == c => ValC(to)
        case PlusC(l, r) => PlusC(subst(from, to, l), subst(from, to, r))
        case MinC(l, r) => MinC(subst(from, to, l), subst(from, to, r))
        case MultC(l, r) => MultC(subst(from, to, l), subst(from, to, r))
        case DivC(l, r) => DivC(subst(from, to, l), subst(from, to, r))
        case EqC(l, r) => EqC(subst(from, to, l), subst(from, to, r))
        case LtC(l, r) => LtC(subst(from, to, l), subst(from, to, r))
        case IfC(c, t, f) => IfC(subst(from, to, c), subst(from, to, t), subst(from, to, f))

        case _ => e

    }
}