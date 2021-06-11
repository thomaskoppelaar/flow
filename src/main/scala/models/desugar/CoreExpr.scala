package CoreExpr
sealed abstract class CoreExpr
case class CellC(e : CoreExpr, n : CoreExpr) extends CoreExpr
case class NumC(n : Int) extends CoreExpr
case class NilC() extends CoreExpr
case class MarkerC(e : CoreExpr) extends CoreExpr
case class PlusC(l: CoreExpr, r: CoreExpr) extends CoreExpr
case class MinC(l: CoreExpr, r: CoreExpr) extends CoreExpr
case class MultC(l: CoreExpr, r: CoreExpr) extends CoreExpr
case class DivC(l: CoreExpr, r: CoreExpr) extends CoreExpr