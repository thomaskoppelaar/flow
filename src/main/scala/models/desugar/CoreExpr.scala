package CoreExpr
sealed abstract class CoreExpr
case class CellC(e : CoreExpr, n : CoreExpr) extends CoreExpr
case class NumC(n : Int) extends CoreExpr
case class NilC() extends CoreExpr
case class MarkerC(e : CoreExpr) extends CoreExpr
