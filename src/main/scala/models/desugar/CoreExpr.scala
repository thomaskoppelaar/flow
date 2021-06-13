package CoreExpr
import Value._

sealed abstract class CoreExpr
case class CellC(e : CoreExpr, n : CoreExpr) extends CoreExpr
case class NumC(n : Int) extends CoreExpr
case class NilC() extends CoreExpr
case class IdC(c : String) extends CoreExpr
case class MarkerC(e : CoreExpr) extends CoreExpr
case class PlusC(l: CoreExpr, r: CoreExpr) extends CoreExpr
case class MinC(l: CoreExpr, r: CoreExpr) extends CoreExpr
case class MultC(l: CoreExpr, r: CoreExpr) extends CoreExpr
case class DivC(l: CoreExpr, r: CoreExpr) extends CoreExpr
case class StringC(c: String) extends CoreExpr
case class FuncC(c: String, b: CoreExpr) extends CoreExpr
case class ValC(v : Value) extends CoreExpr