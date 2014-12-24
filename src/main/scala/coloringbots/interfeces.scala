package coloringbots

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:28
 * Since: 
 *
 */
//todo rename to Bases, Bot for examle

case class Coord(x: Int, y: Int) {
  override def toString = s"{$x,$y}"
  override def hashCode(): Int = x.hashCode() * 1000 + y.hashCode()
  override def equals(obj: scala.Any): Boolean = obj.isInstanceOf[Coord] && x == obj.asInstanceOf[Coord].x && y == obj.asInstanceOf[Coord].y
}

trait Cell {
  def coord: Coord
  def field: Field
  def who: Option[Bot]
  def neighbours: Set[Cell]
  def up: Option[Cell]
  def down: Option[Cell]
  def left: Option[Cell]
  def right: Option[Cell]
  def isEmpty: Boolean
}

trait Field {
  def size: Coord
  def get(coord: Coord): Option[Cell]
  def cells: Array[Array[Cell]]
}

class Turn(val bot: Bot, val cell: Cell){
  def validate: Boolean = canPaint
  def canPaint = isBlank || canFill || canOver

  protected def isBlank: Boolean = cell.isEmpty && cell.neighbours.forall(_.who.isEmpty)
  protected def canFill: Boolean = cell.isEmpty && cell.neighbours.count( _.who.exists(_ == bot)) > 1
  protected def canOver: Boolean = cell.neighbours.count( _.who.exists(_ == bot)) > 2

}

trait Bot{
  var field: Field
  def color: String
  def nextTurn: Turn
  def notify(cell: Cell): Unit

  protected def ->(cell: Cell): Turn = new Turn(this, cell)
}

