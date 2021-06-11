package ExtExpr

sealed abstract class ExtExpr
case class CellExt(e: ExtExpr, n: ExtExpr) extends ExtExpr
case class NumExt(n : Int) extends ExtExpr
case class MarkerExt(n: ExtExpr) extends ExtExpr
case class NilExt() extends ExtExpr