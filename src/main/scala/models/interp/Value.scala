package Value
import CoreExpr._
sealed abstract class Value
case class NumV(x: Int) extends Value
case class NilV() extends Value
case class StringV(x: String) extends Value
case class FuncV(id: String, body: CoreExpr) extends Value
case class BoolV(x: Boolean) extends Value
case class Bind(id: String, x: Value)