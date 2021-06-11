import ExtExpr._
import CoreExpr._

class DesugarException(m : String) extends Exception(m)



package object Desugarer {

    def desugar(x : ExtExpr)  : CoreExpr = x match {

        case CellExt(e: ExtExpr, n: ExtExpr) => CellC(desugar(e), desugar(n))
        case NumExt(n : Int) => NumC(n)
        case MarkerExt(n: ExtExpr) => MarkerC(desugar(n))
        case NilExt() => NilC()
        case _ => throw new DesugarException("Unknown abstract syntax!")
    }

}