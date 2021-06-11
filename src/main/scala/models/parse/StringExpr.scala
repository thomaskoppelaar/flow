package StringExpr

sealed abstract class StringExpr
case class SSym(x : String) extends StringExpr
case class SNum(x : Int) extends StringExpr
case class SList(x : List[StringExpr]) extends StringExpr
case class SCell(x : StringExpr) extends StringExpr
case class SProgram(x : List[StringExpr]) extends StringExpr