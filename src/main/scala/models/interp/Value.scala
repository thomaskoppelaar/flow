package Value
sealed abstract class Value
case class NumV(x: Int) extends Value
case class NilV() extends Value
case class StringV(x : String) extends Value