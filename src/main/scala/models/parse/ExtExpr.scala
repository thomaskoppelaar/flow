package ExtExpr

sealed abstract class ExtExpr
case class CellExt(e: ExtExpr, n: ExtExpr) extends ExtExpr
case class MarkerExt(n: ExtExpr) extends ExtExpr
case class NumExt(n : Int) extends ExtExpr
case class NilExt() extends ExtExpr
case class StringExt(c: String) extends ExtExpr
case class BinOpExt(c: String, l: ExtExpr, r: ExtExpr) extends ExtExpr
case class FdExt(c: String, b: ExtExpr) extends ExtExpr
case class IdExt(c: String) extends ExtExpr