package coloringbots

/**
 * Created by IntelliJ IDEA.
 * User: Andrew F. Podkolzin
 * Date: 18.12.14
 * Time: 17:28
 * Since: 
 *
 */

case class Coord(x: Int, y: Int) {
  override def toString = s"{$x,$y}"
  override def hashCode(): Int = x.hashCode() * 1000 + y.hashCode()
  override def equals(obj: scala.Any): Boolean = obj.isInstanceOf[Coord] && x == obj.asInstanceOf[Coord].x && y == obj.asInstanceOf[Coord].y
}

trait Cell {
  def coord: Coord
  def field: Field
  def get: Option[Bot]
  def neighbours: Set[Cell]
  def up: Option[Cell]
  def down: Option[Cell]
  def left: Option[Cell]
  def right: Option[Cell]
}

trait Field {
  def size: Coord
  def get(coord: Coord): Option[Cell]
  def cells: Array[Array[Cell]]
}

trait Bot{
  var field: Field
  def color: String
  def action: Option[Cell]
  def notify(cell: Cell): Unit
}

