package ExtExpr

sealed abstract class ExtExpr
case class CellExt(e: ExtExpr, n: ExtExpr) extends ExtExpr
case class MarkerExt(n: ExtExpr) extends ExtExpr
case class NumExt(n : Int) extends ExtExpr
case class NilExt() extends ExtExpr
case class StringExt(c: String) extends ExtExpr
case class BoolExt(e : Boolean) extends ExtExpr
case class BinOpExt(c: String, l: ExtExpr, r: ExtExpr) extends ExtExpr
case class UnOpExt(c: String, e: ExtExpr) extends ExtExpr
case class FdExt(id: String, b: ExtExpr) extends ExtExpr
case class AppExt(f: ExtExpr, p: ExtExpr) extends ExtExpr
case class IdExt(c: String) extends ExtExpr
case class IfExt(c: ExtExpr, t: ExtExpr, f: ExtExpr) extends ExtExpr
case class IoExt(c: ExtExpr) extends ExtExpr